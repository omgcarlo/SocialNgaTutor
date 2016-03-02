package com.incc.softwareproject.socialngatutor;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class ViewAllPostActivity extends AppCompatActivity {
    SharedPreferences spreferences;
    List<String> userId1 = new ArrayList<>();
    List<String> username = new ArrayList<>();
    List<String> fullname = new ArrayList<>();
    List<String> post = new ArrayList<>();
    List<String> postId = new ArrayList<>();
    List<String> datetime = new ArrayList<>();
    List<String> pp_url = new ArrayList<>();
    List<Boolean> owned = new ArrayList<>();
    List<Boolean> isUpvoted = new ArrayList<>();
    List<Boolean> isShared = new ArrayList<>();
    List<String> upvotes = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    List<String> shares = new ArrayList<>();
    List<String> fileUrl = new ArrayList<>();
    List<String> fileName = new ArrayList<>();
    //share
    private List<String> share_postId = new ArrayList<>();
    private List<String> share_pic_url = new ArrayList<>();
    private List<String> share_fullname = new ArrayList<>();
    private List<String> share_username = new ArrayList<>();
    private List<String> share_post_description = new ArrayList<>();
    private List<String> share_file_url = new ArrayList<>();
    private List<String> share_file_name = new ArrayList<>();
    private List<String> share_userType = new ArrayList<>();
    private String userId;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.profile_post_recyclerView);
        new initPost().execute(getIntent().getStringExtra("PostId"));

    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(fullname,username,post,postId,
                userId1,datetime,pp_url,owned,isUpvoted,
                isShared,upvotes, comments,shares,fileUrl,fileName,
                share_postId, share_userType , share_pic_url,
                share_fullname,  share_username, share_post_description,
                share_file_url, share_file_name);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(recyclerAdapter));
    }
    public void clear(){
        username.clear();
        fullname.clear();
        postId.clear();
        userId1.clear();
        post.clear();
        datetime.clear();
        pp_url.clear();
        owned.clear();
        isUpvoted.clear();
        isShared.clear();
        upvotes.clear();
        comments.clear();
        shares.clear();
        fileName.clear();
        fileUrl.clear();
        share_pic_url.clear();
        share_fullname.clear();
        share_username.clear();
        share_post_description.clear();
        share_file_url.clear();
        share_file_name.clear();
    }
    private class initPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //Log.d("id",SCHOOL_ID);
            Post svConn = new Post();
            return svConn.getOwnPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("")){
                // Log.d("ress",result);
                convertJSON(result);
            }
            else{
                Log.d("TAB1", "error");
            }
        }
    }
    protected void convertJSON(String result){
        //Log.e("profile res:",result);
        try {
            clear();
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Post");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                //Log.d("des",jsonobject.getString("description"));  //TESTING PURPOSE ONLY
                username.add(jsonobject.getString("username"));
                post.add(jsonobject.getString("description"));
                fullname.add(jsonobject.getString("full_name"));
                postId.add(jsonobject.getString("postId"));
                userId1.add(jsonobject.getString("schoolId"));
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
                    share_postId.add("");
                    share_pic_url.add("");
                    share_fullname.add("");
                    share_username.add("");
                    share_post_description.add("");
                    share_file_url.add("");
                    share_file_name .add("");
                    share_userType.add("");
                }
                else{
                    share_postId.add(jsonobject.getString("share_postId"));
                    share_pic_url.add(jsonobject.getString("share_pic_url"));
                    share_fullname.add(jsonobject.getString("share_full_name"));
                    share_username.add(jsonobject.getString("share_username"));
                    share_userType.add(jsonobject.getString("share_userType"));
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
            setupRecyclerView(recyclerView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
