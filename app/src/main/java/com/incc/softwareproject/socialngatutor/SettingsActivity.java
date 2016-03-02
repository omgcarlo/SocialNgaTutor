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
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Request;
import com.incc.softwareproject.socialngatutor.Server.Search;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;
import com.incc.softwareproject.socialngatutor.adapters.SearchRecyclerAdapter;
import com.incc.softwareproject.socialngatutor.services.FollowService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences spreferences;
    private String schoolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();


        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");

        new getRequestStatus().execute(schoolId);
    }
    public void onViewAbout(View view) {
        Intent i = new Intent(SettingsActivity.this, AboutActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.animate3, R.animator.animate2);

    }
    public void onViewEditProfile(View view) {
        Intent i = new Intent(this, EditProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.animate3, R.animator.animate2);
    }
    public void onViewRequest(View view) {
        Intent i = new Intent(this, RequestActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.animate3, R.animator.animate2);
    }
    private class getRequestStatus extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            Request sv = new Request();
            return sv.getRequestStatus(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("SetttingsAct",s);
            try{
                JSONObject reader = new JSONObject(s);
                JSONObject data = reader.getJSONObject("Request");
                if (data.getString("Status").equals("Pending") ){
                    findViewById(R.id.setting_request_container).setEnabled(false);
                    findViewById(R.id.setting_request).setEnabled(false);
                    findViewById(R.id.setting_request_sub).setEnabled(false);
                    ((TextView) findViewById(R.id.setting_request)).setText("Requesting...");
                }
                if (data.getString("Status").equals("Tutor")){
                    findViewById(R.id.setting_request_container).setEnabled(false);
                    findViewById(R.id.setting_request).setEnabled(false);
                    findViewById(R.id.setting_request_sub).setEnabled(false);
                    ((TextView) findViewById(R.id.setting_request)).setText("You are already a Tutor or a Teacher");
                }
            }catch (Exception e){

            }
        }
    }


}