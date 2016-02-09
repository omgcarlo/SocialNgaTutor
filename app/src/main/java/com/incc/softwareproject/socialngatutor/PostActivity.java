package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.services.PostService;

public class PostActivity extends AppCompatActivity {
    private SharedPreferences spreferences;
    private BroadcastReceiver broadcastReceiver;
    boolean vis = false;
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

        broadcastReceiver = new MyBroadcastReceiver();
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
            String description = ((EditText) findViewById(R.id.pt_desc)).getText().toString();
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
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("Success",false)){
                Toast.makeText(context, "Successfully Posted Something", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
