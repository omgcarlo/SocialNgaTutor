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
import android.widget.EditText;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Comment;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.adapters.CommentRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.PostService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private String icomment;    // inputted comment to be inserted in the database
    private String postId;
    private BroadcastReceiver broadcastReceiver;

    private List<String> fullname = new ArrayList<>();
    private List<String> username= new ArrayList<>();
    private List<String> userType= new ArrayList<>();
    private List<String> userId= new ArrayList<>();
    private List<Boolean> isApproved = new ArrayList<>();

    private List<String> comment= new ArrayList<>();
    private List<String> pic_url = new ArrayList<>();

    RecyclerView recyclerView;
    private String s_fullname;
    SharedPreferences spreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postId = getIntent().getStringExtra("PostId");
        broadcastReceiver = new MyBroadcastReceiver();
        recyclerView = (RecyclerView) findViewById(R.id.c_recyclerView);
        //  GET USERNAME
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        s_fullname = spreferences.getString("FullName", "Wala");
        new InitComment().execute(postId);
    }
    public void commentClose(View v){
        finish();
    }
    public void postComment(View v){
        //Toast.makeText(CommentActivity.this, "wewewewewew", Toast.LENGTH_SHORT).show();
        icomment = ((EditText) findViewById(R.id.c_icomment)).getText().toString();

         findViewById(R.id.c_icomment).setEnabled(false);
        //PostService / PROCESS
        Intent intent = new Intent(this, CommentService.class);
        intent.putExtra("comment", icomment);
        intent.putExtra("postId", postId);
        startService(intent);

    }
    private void clrData(){
        fullname.clear();
        username.clear();
        userId.clear();
        isApproved.clear();
        comment.clear();
        userType.clear();
        pic_url.clear();
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentRecyclerAdapter crecyclerAdapter = new CommentRecyclerAdapter(fullname,username,userId,isApproved,comment,userType,pic_url);
        recyclerView.setAdapter(crecyclerAdapter);
    }
    protected void animateComment(){
        findViewById(R.id.c_icomment).setEnabled(true);
        ((EditText) findViewById(R.id.c_icomment)).setText("");
        this.fullname.add(s_fullname);
        // COMMENT
        this.comment.add(icomment);
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
                comment.add(jsonobject.getString("comment"));
                isApproved.add(jsonobject.getBoolean("isApproved"));
                pic_url.add(jsonobject.getString("pic_url"));
            }
            setupRecyclerView(recyclerView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(PostService.ACTION));
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
                Log.d("ress", result);
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
                Toast.makeText(context, "Successfully Commented Something", Toast.LENGTH_SHORT).show();
                animateComment();
                finish();
            }
        }
    }
}
