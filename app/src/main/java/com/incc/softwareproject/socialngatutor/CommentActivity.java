package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Comment;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.adapters.CommentRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.DeleteService;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.tokenizer.SpaceTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CommentActivity extends AppCompatActivity {
    private String icomment;    // inputted comment to be inserted in the database
    private String postId;
    //private String pp_url;
    private BroadcastReceiver broadcastReceiver;

    private List<String> commentId = new ArrayList<>();
    private List<String> fullname = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> userType = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private List<Boolean> isApproved = new ArrayList<>();
    private List<String> datetime = new ArrayList<>();
    private List<String> comment= new ArrayList<>();
    private List<String> pic_url = new ArrayList<>();
    private List<Boolean> owned = new ArrayList<>();

    RecyclerView recyclerView;
    SharedPreferences spreferences;
    private boolean own;
    private List<String> usernames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postId = getIntent().getStringExtra("PostId");
        own  = getIntent().getBooleanExtra("isOwned",false);
        broadcastReceiver = new MyBroadcastReceiver();
        recyclerView = (RecyclerView) findViewById(R.id.c_recyclerView);
        //  GET USERNAME
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        String schoolId = spreferences.getString("SchoolId", "Wala");
        new InitComment().execute(postId);
        new getUsers().execute(schoolId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, usernames);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.c_icomment);
        textView.setAdapter(adapter);
        textView.setTokenizer(new SpaceTokenizer());
    }
    public void commentClose(View v){
        finish();
    }
    public void postComment(View v){
        //Toast.makeText(CommentActivity.this, "wewewewewew", Toast.LENGTH_SHORT).show();
        icomment = ((MultiAutoCompleteTextView) findViewById(R.id.c_icomment)).getText().toString();

         findViewById(R.id.c_icomment).setEnabled(false);
        //PostService / PROCESS
        Intent intent = new Intent(this, CommentService.class);
        intent.putExtra("comment", icomment);
        intent.putExtra("postId", postId);
        startService(intent);

    }
    private void clrData(){
        commentId.clear();
        fullname.clear();
        username.clear();
        userId.clear();
        isApproved.clear();
        comment.clear();
        userType.clear();
        pic_url.clear();
    }
    private void initPeople(String s) {
        try {
            JSONObject reader = new JSONObject(s);
            JSONArray data = reader.getJSONArray("User");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                usernames.add("@" + jsonobject.getString("username"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class getUsers extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            User sv = new User();
            return sv.getFollowingPeople(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            initPeople(s);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentRecyclerAdapter crecyclerAdapter = new CommentRecyclerAdapter(commentId,fullname,username,
                userId,isApproved,comment,userType,pic_url,datetime,owned);
        recyclerView.setAdapter(crecyclerAdapter);
    }
    protected void animateComment(){
        findViewById(R.id.c_icomment).setEnabled(true);
        ((EditText) findViewById(R.id.c_icomment)).setText("");
        //clrData();
        new InitComment().execute(postId);
        setupRecyclerView(recyclerView);
    }
    protected void convertJSON(String result){
        clrData();
        try {
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Comment");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                //  USER
                username.add(jsonobject.getString("Username"));
                fullname.add(jsonobject.getString("Name"));
                userId.add(jsonobject.getString("schoolId"));
                userType.add(jsonobject.getString("UserType"));
                // COMMENT
                commentId.add(jsonobject.getString("commentId"));
                comment.add(jsonobject.getString("comment"));
                isApproved.add(jsonobject.getBoolean("isApproved"));
                pic_url.add(jsonobject.getString("pic_url"));
                datetime.add(jsonobject.getString("datetime"));
                owned.add(own);
            }
            setupRecyclerView(recyclerView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(CommentService.ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(DeleteService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    private class InitComment extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            Comment sv = new Comment();
            return sv.getComments(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("")){
                //Log.d("ress", result);
                convertJSON(result);
            }
            else{
                Log.d("res","error");
            }
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                //Toast.makeText(context, "Successfully Commented Something", Toast.LENGTH_SHORT).show();
                animateComment();
                //finish();
            }
            else{
                Toast.makeText(context, "Opss...something is not right", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
