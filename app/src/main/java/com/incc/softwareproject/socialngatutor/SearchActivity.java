package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Search;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.SearchRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.FollowService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SEARCH";
    List<String> username = new ArrayList<>();
    List<String> fullname = new ArrayList<>();
    List<String> userId = new ArrayList<>();
    List<Boolean> isFollowed = new ArrayList<>();
    List<String> datetime = new ArrayList<>();
    List<String> post = new ArrayList<>();
    List<String> postId = new ArrayList<>();
    List<String> pp_url = new ArrayList<>();
    List<Boolean> owned = new ArrayList<>();
    private List<Boolean> isUpvoted  = new ArrayList<>();
    private List<Boolean> isShared = new ArrayList<>();
    private List<String> upvotes = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> shares = new ArrayList<>();
    private List<String> fileUrl = new ArrayList<>();
    private List<String> fileName = new ArrayList<>();
    //share
    private List<String> share_postId = new ArrayList<>();
    private List<String> share_pic_url = new ArrayList<>();
    private List<String> share_fullname = new ArrayList<>();
    private List<String> share_username = new ArrayList<>();
    private List<String> share_post_description = new ArrayList<>();
    private List<String> share_file_url = new ArrayList<>();
    private List<String> share_file_name = new ArrayList<>();
    private List<String> share_userType = new ArrayList<>();

        RecyclerView recyclerViewPeople;
        RecyclerView recyclerViewTopics;
        private BroadcastReceiver broadcastReceiver;
        private SharedPreferences spreferences;
        private String schoolId;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar();

            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("Queries"));
            //
            recyclerViewPeople = (RecyclerView) findViewById(R.id.search_p_recyclerView);
            recyclerViewTopics = (RecyclerView) findViewById(R.id.search_p_recyclerView);
            //Clean dem
            username.clear();
            fullname.clear();
            userId.clear();
            //INIT YOUR id
            spreferences = getSharedPreferences("ShareData",MODE_PRIVATE);
            schoolId = spreferences.getString("SchoolId", "wala");

            new initResults().execute(getIntent().getStringExtra("Queries"),schoolId,getIntent().getStringExtra("Action"));

            broadcastReceiver = new MyBroadcastReceiver();

        }
        @Override
        protected void onResume() {
            super.onResume();
            registerReceiver(broadcastReceiver, new IntentFilter(FollowService.ACTION));
        }

        @Override
        protected void onPause() {
            super.onPause();
            unregisterReceiver(broadcastReceiver);
        }

        private class initResults extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                Search sv = new Search();
                return sv.searchPeople(params[0],params[1],params[2]);  //QUERIES FROM THE OTHER SIDE
            }

            @Override
            protected void onPostExecute(String result) {
                //Log.i(TAG,result);
                if(getIntent().getStringExtra("Action").equals("people")){
                    //PEOPLE
                    initViewPeople(result);
                }
                else if(getIntent().getStringExtra("Action").equals("topics")){
                    //POST
                    initViewPost(result);
                }
                else{
                    // DISCOVER
                }

            }


        }
    public void clear(){
        username.clear();
        fullname.clear();
        postId.clear();
        userId.clear();
        post.clear();
        datetime.clear();
        pp_url.clear();
        owned.clear();
        share_pic_url.clear();
        share_fullname.clear();
        share_username.clear();
        share_post_description.clear();
        share_file_url.clear();
        share_file_name.clear();
    }
    private void initViewPeople(String result) {
        clear();
        try {
            Log.d(TAG, result);
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("User");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                username.add(jsonobject.getString("username"));
                fullname.add(jsonobject.getString("full_name"));
                userId.add(jsonobject.getString("schoolId"));
                isFollowed.add(jsonobject.getBoolean("isFollowed"));
                pp_url.add(jsonobject.getString("pic_url"));

            }
            setupRecyclerView_search(recyclerViewPeople);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initViewPost(String result){
        clear();
        try {
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Post");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                //Log.d("des",jsonobject.getString("description"));  //TESTING PURPOSE ONLY
                username.add(jsonobject.getString("username"));
                post.add(jsonobject.getString("description"));
                fullname.add(jsonobject.getString("full_name"));
                postId.add(jsonobject.getString("postId"));
                userId.add(jsonobject.getString("schoolId"));
                datetime.add(jsonobject.getString("datetime"));
                pp_url.add(jsonobject.getString("pic_url"));
                owned.add(jsonobject.getBoolean("isOwned"));
                isUpvoted.add(jsonobject.getBoolean("isUpvoted"));
                isShared.add(jsonobject.getBoolean("isShared"));
                upvotes.add(jsonobject.getString("upvotes"));
                shares.add(jsonobject.getString("shares"));
                comments.add(jsonobject.getString("comments"));
                if("nofile".equals(jsonobject.getString("file_url"))){
                    fileUrl.add("");
                    fileName.add("");
                }
                else{
                    fileUrl.add(jsonobject.getString("file_url"));
                    fileName.add(jsonobject.getString("file_description"));
                }
                // share
                if(jsonobject.isNull("share_postId")){
                    Log.e("SearchActivitiy:" , "null bai");
                    share_postId.add("");
                    share_pic_url.add("");
                    share_fullname.add("");
                    share_username.add("");
                    share_post_description.add("");
                    share_file_url.add("");
                    share_file_name .add("");
                }
                else{
                    share_postId.add(jsonobject.getString("share_postId"));
                    share_pic_url.add(jsonobject.getString("share_pic_url"));
                    share_fullname.add(jsonobject.getString("share_full_name"));
                    share_username.add(jsonobject.getString("share_username"));
                    share_post_description.add(jsonobject.getString("share_description"));
                    if(jsonobject.isNull("share_file_url")){
                        share_file_url.add("");
                        share_file_name .add("");
                    }
                    else {
                        share_file_url.add(jsonobject.getString("share_file_url"));
                        share_file_name.add(jsonobject.getString("share_file_description"));
                    }
                }
            }
            setupRecyclerView_topics(recyclerViewTopics);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setupRecyclerView_search(RecyclerView srecycler) {
        srecycler.setLayoutManager(new LinearLayoutManager(this));
        SearchRecyclerAdapter sRecyclerAdapter = new SearchRecyclerAdapter(fullname,username,userId,isFollowed,pp_url);
        srecycler.setAdapter(sRecyclerAdapter);
    }
    private void setupRecyclerView_topics(RecyclerView precycler) {
        precycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(fullname,username,post,postId,userId,datetime,pp_url,owned,isUpvoted,
                isShared,upvotes, comments,shares,fileUrl,fileName,share_postId,
                share_userType , share_pic_url,
                 share_fullname,  share_username, share_post_description,
                 share_file_url, share_file_name);
        precycler.setAdapter(recyclerAdapter);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                Toast.makeText(context, "Successfully Follow", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
