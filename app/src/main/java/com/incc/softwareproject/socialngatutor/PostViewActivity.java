package com.incc.softwareproject.socialngatutor;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.Comment;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.adapters.CommentRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostViewActivity extends AppCompatActivity {

    private String postId;
    private TextView tv_fullname;
    private TextView tv_username;
    private TextView tv_description;
    private TextView tv_datetiem;
    private SimpleDraweeView userpp;

    private List<String> fullname = new ArrayList<>();
    private List<String> username= new ArrayList<>();
    private List<String> userType= new ArrayList<>();
    private List<String> userId= new ArrayList<>();
    private List<Boolean> isApproved = new ArrayList<>();
    private List<String> pic_url = new ArrayList<>();
    private List<String> comment= new ArrayList<>();

    RecyclerView recyclerView;
    String schoolId;
    SharedPreferences spreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.post_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);



        tv_fullname = (TextView) findViewById(R.id.post_view_fullname);
        tv_username = (TextView) findViewById(R.id.post_view_username);
        tv_description = (TextView) findViewById(R.id.post_view_post_details);
        tv_datetiem = (TextView) findViewById(R.id.post_view_datetime);
        userpp = (SimpleDraweeView) findViewById(R.id.post_view_ppicture);
        recyclerView = (RecyclerView) findViewById(R.id.post_view_recyclerView);

        findViewById(R.id.post_view_loading).setVisibility(View.VISIBLE);
        postId = getIntent().getStringExtra("PostId");
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");

        new getPostDetails()
                .execute(postId,schoolId);
        new getComments()
                .execute(postId);
    }

    private void initPost(String result) {
        //  GENERATE SINGLE POST VIEW
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Post");
            tv_fullname.setText(data.getString("full_name"));
            getSupportActionBar().setTitle(data.getString("full_name"));

            tv_username.setText(data.getString("username"));
            tv_description.setText(data.getString("description"));
            tv_datetiem.setText(data.getString("datetime"));
            Uri uri = Uri.parse(data.getString("pic_url"));
            userpp.setImageURI(uri);
        } catch (Exception e) {
        }
        // FINISH THE LOADING
        findViewById(R.id.post_view_loading).setVisibility(View.GONE);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentRecyclerAdapter crecyclerAdapter = new CommentRecyclerAdapter(fullname,username,userId,isApproved,comment,userType,pic_url);
        recyclerView.setAdapter(crecyclerAdapter);
    }

    protected void initComment(String result){

        try {
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Comment");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                Log.e("name", jsonobject.getString("Username"));
                //  USER
                username.add(jsonobject.getString("Username"));
                fullname.add(jsonobject.getString("Name"));
                userId.add(jsonobject.getString("schoolId"));
                userType.add(jsonobject.getString("UserType"));
                pic_url.add(jsonobject.getString("pic_url"));
                // COMMENT
                comment.add(jsonobject.getString("comment"));
                isApproved.add(jsonobject.getBoolean("isApproved"));

            }
            setupRecyclerView(recyclerView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //  DETAILS INCLUDES THE POST COMPONENTS AND COMMENTS
    private class getPostDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Post sv = new Post();
            String res = sv.getPost(params[0],params[1]);
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            initPost(s);
        }
    }

    //  GET COMMENT FROM POST
    private class getComments extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Comment sv = new Comment();
            String res = sv.getComments(params[0]);
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            initComment(s);
        }
    }

}
