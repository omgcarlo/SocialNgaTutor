package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.FollowService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class ProfileActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    String schoolId;    //IMONG ID
    String userId;      //IYANG ID

    private Button follow_btn;
    private Button following_btn;
    private BroadcastReceiver broadcastReceiver;

    SharedPreferences spreferences;
    List<String> username = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);

       /* android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        */
        final Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("full_name"));

        recyclerView = (RecyclerView) findViewById(R.id.profile_post_recyclerView);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Profile");

        dynamicToolbarColor();
        toolbarTextApperance();

        follow_btn = (Button) findViewById(R.id.profile_btnFollow);
        following_btn = (Button) findViewById(R.id.profile_btnFollowing);

        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");

        if(getIntent().getExtras() != null && getIntent().getExtras().getString("UserId") != null) {
            userId = getIntent().getExtras().getString("UserId");

        }
        else{
            userId = schoolId;

        }

        broadcastReceiver = new MyBroadcastReceiver();
        new InitProfileInfo().execute(schoolId,userId);
    }

    private void dynamicToolbarColor() {
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.color_primary_red));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.color_primary_red_dark));
    }

    private void toolbarTextApperance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    public void getPost(View v){
        Intent i = new Intent(this,ViewAllPostActivity.class);
        i.putExtra("PostId",userId);
        startActivity(i);
    }

    private void initData(String result){

        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("User");
            ((TextView) findViewById(R.id.profile_fullname)).setText(data.getString("full_name"));
            ((TextView) findViewById(R.id.profile_username)).setText("@" + data.getString("username"));
            ((TextView) findViewById(R.id.profile_followers)).setText(data.getInt("followers") + "");
            ((TextView) findViewById(R.id.profile_followings)).setText(data.getInt("followings") + "");
            ((TextView) findViewById(R.id.profile_posts)).setText(data.getInt("posts") + "");
            if(data.getString("userType").equals("T")){
                //  show indicator if teacher = T
                findViewById(R.id.profile_indicator)
                        .setVisibility(View.VISIBLE);
            }
            if(data.getString("bio") == null || !data.getString("bio").equals("null") || data.isNull("bio"))
                ((TextView) findViewById(R.id.profile_bio)).setText("");
            else
                ((TextView) findViewById(R.id.profile_bio)).setText(data.getString("bio"));
            //  IMAGE URL parsing
            Uri uri = Uri.parse(data.getString("pic_url"));
            ((SimpleDraweeView) findViewById(R.id.profile_userPP)).setImageURI(uri);

            if (data.isNull("isFollowed") || schoolId == userId || data.get("isFollowed") == null) {
                follow_btn.setVisibility(View.GONE);
            } else {
                    if (data.getBoolean("isFollowed")) {
                        follow_btn.setVisibility(View.GONE);
                        following_btn.setVisibility(View.VISIBLE);
                    } else {
                        follow_btn.setVisibility(View.VISIBLE);
                        following_btn.setVisibility(View.GONE);
                    }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void followBtns(View v) {
        //follow process
        Intent intent = new Intent(this, FollowService.class);
        if (v.getId() == R.id.profile_btnFollow) {
            Log.d("Button", "follow");
            intent.putExtra("follow", "follow");
            //ANIMATE FOLLOWING BUTTON
            follow_btn.setVisibility(View.GONE);
            following_btn.setVisibility(View.VISIBLE);

        } else {
            intent.putExtra("follow", "unfollow");
            //ANIMATE FOLLOW BUTTON
            follow_btn.setVisibility(View.VISIBLE);
            following_btn.setVisibility(View.GONE);
        }
        //pass iyang id
        intent.putExtra("iyangId", userId.toString());
        startService(intent);
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

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("Success", false)) {
                Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class InitProfileInfo extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            User sv = new User();
            return sv.getProfileInfo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            initData(s);
        }
    }

}
