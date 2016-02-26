package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.ReportService;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class ReportActivity extends AppCompatActivity {
    private TextView report_tv_fullname;
    private TextView report_tv_username;
    private TextView report_tv_description;
    private TextView report_tv_datetime;
    private SimpleDraweeView report_userpp;
    private TextView report_tv_file_description;
    private String referenceTable;
    private String postId;
    private String reporterId;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        broadcastReceiver = new MyBroadcastReceiver();
        postId = getIntent().getStringExtra("PostId");
        reporterId = getIntent().getStringExtra("ReporterId");
        referenceTable = getIntent().getStringExtra("ReferenceTable");

        report_userpp = (SimpleDraweeView) findViewById(R.id.report_card_ppicture);
        report_tv_description = (TextView) findViewById(R.id.report_card_post_details);
        report_tv_datetime = (TextView) findViewById(R.id.report_card_datetime);
        report_tv_fullname = (TextView) findViewById(R.id.report_card_fullname);
        report_tv_username = (TextView) findViewById(R.id.report_card_username);
        report_tv_file_description = (TextView) findViewById(R.id.report_card_file_name);

        new getPostDetails().execute(postId,reporterId);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_report) {
            //PostService / PROCESS
            Intent intent = new Intent(this, ReportService.class);
            intent.putExtra("PostId", postId);
            intent.putExtra("table", referenceTable);
            //Log.e("Report",referenceTable);
            startService(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void initPost(String result) {
        //  GENERATE SINGLE POST VIEW
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Post");
            report_tv_fullname.setText(data.getString("full_name"));
            report_tv_username.setText(data.getString("username"));
            report_tv_description.setText(data.getString("description"));
            report_tv_datetime.setText(data.getString("datetime"));
            Uri uri = Uri.parse(data.getString("pic_url"));
            report_userpp.setImageURI(uri);
            if("nofile".equals(data.getString("file_url"))){
                findViewById(R.id.report_card_post_file).setVisibility(View.GONE);
            }
            else{
                //fileUrl = jsonobject.getString("file_url"));
                findViewById(R.id.report_card_post_file).setVisibility(View.VISIBLE);
                report_tv_file_description.setText(data.getString("file_description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(ReportService.ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
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
}
