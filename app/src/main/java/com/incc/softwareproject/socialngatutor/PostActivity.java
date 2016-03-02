package com.incc.softwareproject.socialngatutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.mentions.models.Person;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.tokenizer.SpaceTokenizer;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.impl.BasicSuggestionsListBuilder;
import com.linkedin.android.spyglass.suggestions.interfaces.OnSuggestionsVisibilityChangeListener;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsListBuilder;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.RichEditorView;

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

public class PostActivity extends AppCompatActivity implements QueryTokenReceiver {
    private SharedPreferences spreferences;
    private BroadcastReceiver broadcastReceiver;

    boolean vis = false;    //  TAG INPUT VISIBILITY
    private List<String> username = new ArrayList<>();
    private String schoolId;

    // MENTION
    private Person.PersonLoader people;
    private RichEditorView editor;
    private SuggestionsResult lastPersonSuggestions;

    private static final String PERSON_BUCKET = "people-database";

    String filePath;
    String description;
    String tags;
    String type;

    String ownerId;
    private final static int PICKFILE_RESULT_CODE = 1;

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
                android.R.layout.simple_dropdown_item_1line, username);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.pt_desc);
        textView.setAdapter(adapter);
        textView.setTokenizer(new SpaceTokenizer());

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


    public void showTagInput(View v) {
        if (vis) {
            findViewById(R.id.pt_tag).setVisibility(View.GONE);
            vis = false;
        } else {
            findViewById(R.id.pt_tag).setVisibility(View.VISIBLE);
            vis = true;
        }
    }

    public void openFileChooser(View v) {
        int PICKFILE_RESULT_CODE=1;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (resultCode == RESULT_OK) {
            if(PICKFILE_RESULT_CODE == 1) {
                filePath = data.getData().getPath();
                Log.e("PostActivity", filePath);
                findViewById(R.id.upload_layout).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.post_choose_file)).setText(data.getData().getPath());
            }
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
        if (id == R.id.action_post) {
            description = ((MultiAutoCompleteTextView) findViewById(R.id.pt_desc)).getText().toString();
            tags = ((EditText) findViewById(R.id.pt_tag)).getText().toString();
            type = "Post";
            spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
            ownerId = spreferences.getString("SchoolId", "wala");
            //post process
            Intent intent = new Intent(this, PostService.class);
            intent.putExtra("description", description);
            intent.putExtra("tags", tags);
            intent.putExtra("type", type);
            intent.putExtra("ownerId", ownerId);
            intent.putExtra("filepath", filePath);
            startService(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<String> onQueryReceived(@NonNull final QueryToken queryToken) {
        final List<String> buckets = new ArrayList<>();
        final SuggestionsResultListener listener = editor;
        final Handler handler = new Handler(Looper.getMainLooper());

        // Fetch people if necessary
        buckets.add(PERSON_BUCKET);
        new Runnable() {
            @Override
            public void run() {
                List<Person> suggestions = people.getSuggestions(queryToken);
                lastPersonSuggestions = new SuggestionsResult(queryToken, suggestions);
                listener.onReceiveSuggestionsResult(lastPersonSuggestions, PERSON_BUCKET);
            }
        };
        return buckets;
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

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("Success", false)) {
                Toast.makeText(context, "Successfully Posted Something", Toast.LENGTH_SHORT).show();
                if(filePath != null && !filePath.equals("")){
                    new UploadFile().execute();
                }
                else{
                    finish();
                }

            }
        }
    }
    protected class UploadFile extends AsyncTask<Integer, Void, Integer >{

        @Override
        protected Integer doInBackground(Integer... params) {
            return uploadFile(filePath,description,type,ownerId,tags);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 200){
                Toast.makeText(PostActivity.this, "File Upload and Post Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),AfterLoginActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(PostActivity.this, "Something happen", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public int uploadFile(String sourceFileUri,String description,String type,String ownerId,String tags) {
        Server sv = new Server();
        int serverResponseCode = 0;
        String data = "";
        try{
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
        } catch(Exception e){
            e.printStackTrace();
        }

        String upLoadServerUri = sv.getBaseUrl() + sv.getPostUrl() +"?"+ data;

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
                Log.e("Buffer",bytesRead + ";" + buffer);
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
                        Toast.makeText(PostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
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
        return serverResponseCode;    }


}
