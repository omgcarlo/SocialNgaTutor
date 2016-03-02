package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.services.RequestService;

import org.json.JSONObject;

/**
 * Created by carlo on 2/19/16.
 */
public class RequestActivity  extends AppCompatActivity{
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.request_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Request");
        broadcastReceiver = new MyBroadcastReceiver();
        SharedPreferences spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        String userId = spreferences.getString("SchoolId", "Wala");

        new initProfileInfo().execute(userId,"");
    }
    public void requestBtn(View v){
        EditText remarks = (EditText) findViewById(R.id.request_remarks);
        Intent i = new Intent(this,RequestService.class);
        i.putExtra("Remarks",remarks.getText().toString());
        startService(i);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(RequestService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    public void initData(String result){
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("User");
            ((TextView) findViewById(R.id.request_fullname)).setText(data.getString("full_name"));
            ((TextView) findViewById(R.id.request_username)).setText("@" + data.getString("username"));
           // ((TextView) findViewById(R.id.profile_followers)).setText(data.getInt("followers") + "");
           // ((TextView) findViewById(R.id.profile_followings)).setText(data.getInt("followings") + "");
            //((TextView) findViewById(R.id.profile_posts)).setText(data.getInt("posts") + "");
            //  IMAGE URL parsing
            Uri uri = Uri.parse(data.getString("pic_url"));
            ((SimpleDraweeView) findViewById(R.id.request_userpp)).setImageURI(uri);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                Toast.makeText(context, "Thank You for reporting", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private class initProfileInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            User sv = new User();
            return sv.getProfileInfo(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            initData(s);
        }
    }
}