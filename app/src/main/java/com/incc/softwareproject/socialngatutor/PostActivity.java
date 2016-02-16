package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.services.PostService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private SharedPreferences spreferences;
    private BroadcastReceiver broadcastReceiver;
    boolean vis = false;
    private List<String> username = new ArrayList<>();
    private String schoolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");
        new getUsers().execute(schoolId);
        broadcastReceiver = new MyBroadcastReceiver();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,username);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.pt_desc);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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


    public void showTagInput(View v){

        if(vis) {
            findViewById(R.id.pt_tag).setVisibility(View.GONE);
            vis = false;
        }
        else {
            findViewById(R.id.pt_tag).setVisibility(View.VISIBLE);
            vis = true;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_post){
            String description = ((MultiAutoCompleteTextView) findViewById(R.id.pt_desc)).getText().toString();
            String tags = ((EditText) findViewById(R.id.pt_tag)).getText().toString();
            String type = "Post";
            spreferences = getSharedPreferences("ShareData",MODE_PRIVATE);
            String ownerId = spreferences.getString("SchoolId","wala");
            //post process
            Intent intent = new Intent(this, PostService.class);
            intent.putExtra("description", description);
            intent.putExtra("tags",tags);
            intent.putExtra("type",type);
            intent.putExtra("ownerId", ownerId);
            startService(intent);
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
    private class getUsers extends AsyncTask<String,Void,String>{

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
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                Toast.makeText(context, "Successfully Posted Something", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),AfterLoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

}
