package com.incc.softwareproject.socialngatutor;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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
    private CardView file;
    private TextView fileName;
    //  SHARE
    private String share_postId;
    private LinearLayout share_container;
    private SimpleDraweeView share_profilePicture;
    private TextView share_fullname;
    private TextView share_username;
    private TextView share_post_description;
    private CardView share_file_container;
    private TextView share_file_name;
    private String shareFileUrl;

    private List<String> commentId = new ArrayList<>();
    private List<String> fullname = new ArrayList<>();
    private List<String> username= new ArrayList<>();
    private List<String> userType= new ArrayList<>();
    private List<String> userId= new ArrayList<>();
    private List<Boolean> isApproved = new ArrayList<>();
    private List<String> pic_url = new ArrayList<>();
    private List<String> comment= new ArrayList<>();
    private List<String> datetime = new ArrayList<>();
    private List<Boolean> owned = new ArrayList<>();

    RecyclerView recyclerView;
    String schoolId;
    SharedPreferences spreferences;
    private String fileUrl;
    private boolean own;
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

        findViewById(R.id.post_view_loading).setVisibility(View.VISIBLE);
        postId = getIntent().getStringExtra("PostId");
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");

        tv_fullname = (TextView) findViewById(R.id.post_view_fullname);
        tv_username = (TextView) findViewById(R.id.post_view_username);
        tv_description = (TextView) findViewById(R.id.post_view_post_details);
        tv_datetiem = (TextView) findViewById(R.id.post_view_datetime);
        userpp = (SimpleDraweeView) findViewById(R.id.post_view_ppicture);

        file = (CardView) findViewById(R.id.view_post_file);
        fileName = (TextView) findViewById(R.id.view_file_name);

        share_container = (LinearLayout) findViewById(R.id.view_share_card_container);
        share_profilePicture = (SimpleDraweeView) findViewById(R.id.view_share_card_ppicture);
        share_fullname = (TextView) findViewById(R.id.view_share_card_fullname);
        share_username = (TextView) findViewById(R.id.view_share_card_username);
        share_post_description = (TextView) findViewById(R.id.view_share_card_post_details);
        share_file_container = (CardView) findViewById(R.id.view_share_card_post_file);
        share_file_name = (TextView) findViewById(R.id.view_share_card_file_name);

        recyclerView = (RecyclerView) findViewById(R.id.post_view_recyclerView);

        //Log.e("POSTVIEW",postId + " " + schoolId);
        new getPostDetails()
                .execute(postId,schoolId);
        new getComments()
                .execute(postId);
    }

    private void initPost(String result) {
        //  GENERATE SINGLE POST VIEW
        try {
            //Log.e("PostView",result + "asdasd");
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Post");
            tv_fullname.setText(data.getString("full_name"));
            getSupportActionBar().setTitle(data.getString("full_name"));

            tv_username.setText(data.getString("username"));
            tv_description.setText(data.getString("description"));
            tv_datetiem.setText(data.getString("datetime"));
            Uri uri = Uri.parse(data.getString("pic_url"));
            userpp.setImageURI(uri);
            if (!"nofile".equals(data.getString("file_url"))) {
                file.setVisibility(View.VISIBLE);
                fileUrl = data.getString("file_url");
                fileName.setText(data.getString("file_description"));
            }
            // share
            if (!data.isNull("share_postId")) {
                share_container.setVisibility(View.VISIBLE);
                share_postId = data.getString("share_postId");
                share_profilePicture.setImageURI(Uri.parse(data.getString("share_pic_url")));
                share_fullname.setText(data.getString("share_full_name"));
                share_username.setText(data.getString("share_username"));
                //share_userType.add(jsonobject.getString("share_userType"));
                share_post_description.setText(data.getString("share_description"));
                if (!data.isNull("share_file_url")) {
                    share_file_container.setVisibility(View.VISIBLE);
                    shareFileUrl = data.getString("share_file_url");
                    share_file_name.setText(data.getString("share_file_description"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tv_fullname.setText("Something is wrong...");
            getSupportActionBar().setTitle("Post not found");
            tv_username.setText("");
            tv_description.setText("");
            tv_datetiem.setText("");
            userpp.setVisibility(View.GONE);
        }
        // FINISH THE LOADING
        findViewById(R.id.post_view_loading).setVisibility(View.GONE);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentRecyclerAdapter crecyclerAdapter = new CommentRecyclerAdapter(commentId,fullname,username,userId,isApproved,comment
                ,userType,pic_url,datetime,owned);
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
                commentId.add(jsonobject.getString("commentId"));
                comment.add(jsonobject.getString("comment"));
                isApproved.add(jsonobject.getBoolean("isApproved"));
                datetime.add(jsonobject.getString("datetime"));
                owned.add(own);

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
