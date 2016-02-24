package com.incc.softwareproject.socialngatutor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.Search;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.SearchRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.FollowService;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.tokenizer.SpaceTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    private TextView share_tv_fullname;
    private TextView share_tv_username;
    private TextView share_tv_description;
    private TextView share_tv_datetime;
    private SimpleDraweeView share_userpp;
    private TextView share_tv_file_description;

    protected List<String> username = new ArrayList<>();
    private String postId;
    private String ownerId;
    MultiAutoCompleteTextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.share_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setTitle(R.string.title_share_activity);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        postId = getIntent().getStringExtra("PostId");
        ownerId = getIntent().getStringExtra("OwnerId");  // school Id

        share_tv_fullname = (TextView) findViewById(R.id.share_card_fullname);
        share_tv_datetime = (TextView) findViewById(R.id.share_card_datetime);
        share_tv_description = (TextView) findViewById(R.id.share_card_post_details);
        share_tv_username =  (TextView) findViewById(R.id.share_card_username);
        share_userpp = (SimpleDraweeView) findViewById(R.id.share_card_ppicture);
        share_tv_file_description = (TextView) findViewById(R.id.share_card_file_name);

        new getUsers().execute(ownerId);
        new getPostDetails().execute(postId,ownerId);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, username);
        description = (MultiAutoCompleteTextView) findViewById(R.id.share_description);
        description.setAdapter(adapter);
        description.setTokenizer(new SpaceTokenizer());
    }

    private void initPost(String result) {
        //  GENERATE SINGLE POST VIEW
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Post");
            share_tv_fullname.setText(data.getString("full_name"));
            share_tv_username.setText(data.getString("username"));
            share_tv_description.setText(data.getString("description"));
            share_tv_datetime.setText(data.getString("datetime"));
            Uri uri = Uri.parse(data.getString("pic_url"));
            share_userpp.setImageURI(uri);
            if("nofile".equals(data.getString("file_url"))){
                findViewById(R.id.share_card_post_file).setVisibility(View.GONE);
            }
            else{
                //fileUrl = jsonobject.getString("file_url"));
                findViewById(R.id.share_card_post_file).setVisibility(View.VISIBLE);
                share_tv_file_description.setText(data.getString("file_description"));
            }
        } catch (Exception e) {
        }
        // FINISH THE LOADING
        findViewById(R.id.share_progressbar).setVisibility(View.GONE);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            String tags = ((MultiAutoCompleteTextView) findViewById(R.id.share_tags)).getText().toString();
            new sharePost()
                    .execute(postId,description.getText().toString(),"Post",ownerId,tags);
        }
        return super.onOptionsItemSelected(item);
    }
    private void initPeople(String s) {
        try {
            JSONObject reader = new JSONObject(s);
            JSONArray data = reader.getJSONArray("User");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                username.add("@" + jsonobject.getString("username"));
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
    private class sharePost extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            Post sv = new Post();
            return sv.sharePost(params[0],params[1],params[2],params[3],params[4]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("JSONRES",s);

            JSONObject data = null;
            try {
                JSONObject reader = new JSONObject(s);
                data = reader.getJSONObject("Post");
                if (!s.equals("") && data.getBoolean("Success")) {
                    finish();
                    overridePendingTransition(R.animator.animate2, R.animator.animate3);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}