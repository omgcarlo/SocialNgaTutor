package com.incc.softwareproject.socialngatutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;
import com.incc.softwareproject.socialngatutor.upload.PictureUpload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUpProcessActivity extends AppCompatActivity {
    TextView loadingtxt;
    Button toLoginBtn;
    ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_process);
        loadingtxt = (TextView) findViewById(R.id.s_loading_txt);
        toLoginBtn = (Button) findViewById(R.id.toLoginBtn);
        toLoginBtn.setVisibility(View.INVISIBLE);
        new SigupProcess().execute();
    }

    public void toLoginBtn(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
    //  UPLOAD IMAGE
    public int uploadFile(String sourceFileUri) {
        Server sv = new Server();
        int serverResponseCode = 0;
        String upLoadServerUri = sv.getBaseUrl() + sv.getUploadpicUrl();
        String fileName = sourceFileUri;
        Log.e("TAG",fileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 5 * 1024 * 1024;
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
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
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
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            if(serverResponseCode == 200){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUpProcessActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
            Toast.makeText(SignUpProcessActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(SignUpProcessActivity.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.getMessage());
        }
        return serverResponseCode;
    }
    private class SigupProcess extends AsyncTask<String, Integer, String> {
        String schoolId, fullname, username, password, email, birthdate, programId;

        @Override
        protected String doInBackground(String... params) {
            /**
             * why init data here?
             * para mafeel sa user ang loading
             * ug choy man ang loading hahaha
             */
            setProgress(0);
            schoolId = getIntent().getExtras().getString("schoolId");
            fullname = getIntent().getExtras().getString("fullname");
            username = getIntent().getExtras().getString("username");
            password = getIntent().getExtras().getString("password");
            email = getIntent().getExtras().getString("email");
            birthdate = getIntent().getExtras().getString("birthdate");
            programId = "1";
            setProgress(1);
            User svConn = new User();

            if (getIntent().getStringExtra("imagepath") != null) {
                setProgress(2);
                int res = uploadFile(getIntent().getStringExtra("imagepath"));    //  IMAGE NI
            }
            setProgress(3);
            // Return result
            return svConn.signup(schoolId, username, password, birthdate, email, programId, fullname);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]) {
                case 0:
                    loadingtxt.setText("Accessing the Server...");
                    break;
                case 1:
                    loadingtxt.setText("Adding data to database...");
                    break;
                case 2:
                    loadingtxt.setText("Uploading image...");
                    break;
                default:
                    loadingtxt.setText("All done!");
                    break;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("SIGNUPPROCCESS", result);
            try {
                JSONObject reader = new JSONObject(result);
                JSONObject data = reader.getJSONObject("User");
                if (data.getBoolean("Success")) {
                    loadingtxt.setText("YEY!");

                } else {
                    loadingtxt.setText("YOY!");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            toLoginBtn.setVisibility(View.VISIBLE);
        }
    }
}


