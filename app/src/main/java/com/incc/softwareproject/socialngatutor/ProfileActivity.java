package com.incc.softwareproject.socialngatutor;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.services.FollowService;

public class ProfileActivity extends AppCompatActivity {
    String schoolId;    //IMONG ID
    String userId;      //IYANG ID

    private Button follow_btn;
    private Button following_btn;
    private BroadcastReceiver broadcastReceiver;

    private SharedPreferences spreferences;

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
        follow_btn = (Button) findViewById(R.id.profile_btnFollow);
        following_btn = (Button) findViewById(R.id.profile_btnFollowing);

        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);

        schoolId = spreferences.getString("SchoolId", "Wala");
        userId = getIntent().getExtras().getString("UserId");
        broadcastReceiver = new MyBroadcastReceiver();
        initData();
    }

    public void initData() {
        //schoolId = spreferences.getString("SchoolId","wala");
        //Log.d("TAG",getIntent().getExtras().getString("FullName"));
        ((TextView) findViewById(R.id.profile_fullname)).setText(getIntent().getExtras().getString("FullName"));
        ((TextView) findViewById(R.id.profile_username)).setText("@" + getIntent().getExtras().getString("Username"));
        //Log.d("sername", spreferences.getString("Username", "Wala"));
        //Log.d("sername2",getIntent().getExtras().getString("Username"));

        if(getIntent().getExtras().getString("UserId").equals(spreferences.getString("SchoolId", "Wala"))){
            follow_btn.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.profile_username)).setText(getIntent().getExtras().getString("Username"));
        }else{
            if (getIntent().hasExtra("isFollowed")) {
                if (getIntent().getStringExtra("isFollowed").equals("true")) {
                    follow_btn.setVisibility(View.GONE);
                    following_btn.setVisibility(View.VISIBLE);
                } else {
                    follow_btn.setVisibility(View.VISIBLE);
                    following_btn.setVisibility(View.GONE);
                }
            }
        }

//hannah was here
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
}
