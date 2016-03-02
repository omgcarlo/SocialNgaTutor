package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.tokenizer.SpaceTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EditPostActivity extends AppCompatActivity {
    private String postId;
    private TextView tv_fullname;
    private TextView tv_username;
    private MultiAutoCompleteTextView mtv_description;
    private TextView tv_datetiem;
    private SimpleDraweeView userpp;

    private String fileUrl;
    private CardView file;
    private TextView fileName;
    //  SHARE
    private String share_postId;
    private LinearLayout share_container;
    private SimpleDraweeView share_profilePicture;
    private TextView share_fullname;
    private TextView share_username;
    private TextView share_post_description;
    private CardView share_file_container;
    private TextView share_file_name;
    private String shareFileUrl;
    String schoolId;
    SharedPreferences spreferences;
    private static int PICKFILE_RESULT_CODE;
    protected String filePath;
    private List<String> usernames = new ArrayList<>();
    private String tags;
    String desc;
    private boolean filestatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filestatus = false; //  true if the user deletes the file else false;
        tv_fullname = (TextView) findViewById(R.id.edit_view_fullname);
        tv_username = (TextView) findViewById(R.id.edit_view_username);
        mtv_description = (MultiAutoCompleteTextView) findViewById(R.id.edit_post_details);
        tv_datetiem = (TextView) findViewById(R.id.edit_view_datetime);
        userpp = (SimpleDraweeView) findViewById(R.id.edit_view_ppicture);
        file = (CardView) findViewById(R.id.edit_post_file);
        fileName = (TextView) findViewById(R.id.edit_file_name);


        share_container = (LinearLayout) findViewById(R.id.edit_share_card_container);
        share_profilePicture = (SimpleDraweeView) findViewById(R.id.edit_share_card_ppicture);
        share_fullname = (TextView) findViewById(R.id.edit_share_card_fullname);
        share_username = (TextView) findViewById(R.id.edit_share_card_username);
        share_post_description = (TextView) findViewById(R.id.edit_share_card_post_details);
        share_file_container = (CardView) findViewById(R.id.edit_share_card_post_file);
        share_file_name = (TextView) findViewById(R.id.edit_share_card_file_name);

        postId = getIntent().getStringExtra("PostId");
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");

        new getUsers().execute(schoolId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, usernames);
        mtv_description.setAdapter(adapter);
        mtv_description.setTokenizer(new SpaceTokenizer());

        String postId = getIntent().getStringExtra("PostId");
        new getPostDetails().execute(postId, schoolId);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_post, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {

            if (share_postId != null && !share_postId.equals("")) {
                desc = "::share:" + share_postId + "::" + mtv_description.getText().toString();
            }
            else {
                desc = mtv_description.getText().toString();
            }
            //String postId,String description, String type,String ownerId,String tags
            String type = "Post";

            new updatePost().execute(postId,desc,type,schoolId,tags);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPost(String result) {
        //  GENERATE SINGLE POST VIEW
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Post");
            tv_fullname.setText(data.getString("full_name"));
            getSupportActionBar().setTitle(data.getString("full_name"));

            tv_username.setText(data.getString("username"));
            mtv_description.setText(data.getString("description"));
            tv_datetiem.setText(data.getString("datetime"));
            tags = data.getString("tags");
            Uri uri = Uri.parse(data.getString("pic_url"));
            userpp.setImageURI(uri);
            if (!"nofile".equals(data.getString("file_url"))) {
                file.setVisibility(View.VISIBLE);
                fileUrl = data.getString("file_url");
                fileName.setText(data.getString("file_description"));

            }
            // share
            if (!data.isNull("share_postId")) {
                share_container.setVisibility(View.VISIBLE);
                share_postId = data.getString("share_postId");
                share_profilePicture.setImageURI(Uri.parse(data.getString("share_pic_url")));
                share_fullname.setText(data.getString("share_full_name"));
                share_username.setText(data.getString("share_username"));
                //share_userType.add(jsonobject.getString("share_userType"));
                share_post_description.setText(data.getString("share_description"));
                if (!data.isNull("share_file_url")) {
                    share_file_container.setVisibility(View.VISIBLE);
                    shareFileUrl = data.getString("share_file_url");
                    share_file_name.setText(data.getString("share_file_description"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  DETAILS INCLUDES THE POST COMPONENTS AND COMMENTS
    private class getPostDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Post sv = new Post();
            String res = sv.getPost(params[0], params[1]);
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            initPost(s);
        }
    }

    public void changeFile(View v) {
        PICKFILE_RESULT_CODE = 1;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }
    public void deleteShare(View v){
        share_container.setVisibility(View.GONE);
        share_postId = "";
    }
    public void deleteFile(View v){
        file.setVisibility(View.GONE);
        filePath = null;
        filestatus = true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (resultCode == RESULT_OK) {
            if (PICKFILE_RESULT_CODE == 1) {
                filePath = data.getData().getPath();
                Log.e("PostActivity", filePath);
                fileName.setText(data.getData().getPath());
                filestatus = false;
                file.setVisibility(View.VISIBLE);
            }
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

    private void initPeople(String s) {
        try {
            JSONObject reader = new JSONObject(s);
            JSONArray data = reader.getJSONArray("User");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
                usernames.add("@" + jsonobject.getString("username"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class updatePost extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Post sv = new Post();
            return sv.updatePost(params[0], params[1], params[2], params[3], params[4],filestatus);
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("")){
                Log.e("UpdatePost",s);
                try{
                    JSONObject reader = new JSONObject(s);
                    JSONObject data = reader.getJSONObject("Post");
                    if(data.getBoolean("Success") && filePath != null){
                        new UploadFile().execute();
                    }
                    else{
                        if(data.getBoolean("Success")){
                            Toast.makeText(EditPostActivity.this, "You have edited your post", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(),AfterLoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    protected class UploadFile extends AsyncTask<Integer, Void, Integer >{

        @Override
        protected Integer doInBackground(Integer... params) {
            return uploadFile(filePath,desc,"Post",schoolId,tags);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 200){
                Toast.makeText(EditPostActivity.this, "File Upload and Post Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),AfterLoginActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(EditPostActivity.this, "Something happen", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public int uploadFile(String sourceFileUri, String description, String type, String ownerId, String tags) {
        Server sv = new Server();
        int serverResponseCode = 0;
        String data = "";
        try {
            data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("upload_file", "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                    + "=" + URLEncoder.encode(description, "UTF-8");

            data += "&" + URLEncoder.encode("type", "UTF-8")
                    + "=" + URLEncoder.encode(type, "UTF-8");

            data += "&" + URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(ownerId, "UTF-8");

            data += "&" + URLEncoder.encode("tags", "UTF-8")
                    + "=" + URLEncoder.encode(tags, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String upLoadServerUri = sv.getBaseUrl() + sv.getPostUrl() + "?" + data;

        String fileName = sourceFileUri;
        Log.e("TAG", fileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                Log.e("Buffer", bytesRead + ";" + buffer);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditPostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            //Log.e("PostAct:res", upLoadServerUri + sb.toString());

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
            Toast.makeText(this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.getMessage());
        }
        return serverResponseCode;
    }


}
