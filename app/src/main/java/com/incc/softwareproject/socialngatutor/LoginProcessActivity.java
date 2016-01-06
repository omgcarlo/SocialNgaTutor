package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginProcessActivity extends AppCompatActivity {
    TextView tv;
    ProgressBar loading1;
    HttpURLConnection urlConnection;
    String uri = "http://192.168.1.18/socialtutor/server/user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_process);
        tv = (TextView) findViewById(R.id.res);
        uri += "?action=login&username=" +
                getIntent().getExtras().getString("username") +
                "&password=" + getIntent().getExtras().getString("password");
        //Toast.makeText(this,uri,Toast.LENGTH_SHORT).show();
        loading1 = (ProgressBar) findViewById(R.id.loading_login);
        loading1.setVisibility(View.VISIBLE);
        new ConnectProccess().execute(uri);
    }

    private void balhin(String res) {
        try {

            String schoolId = "";
            JSONObject reader = new JSONObject(res);
            JSONObject data = reader.getJSONObject("User");
            schoolId = data.getString("SchoolId");

            //tv.setText(fullname);
            //Toast.makeText(this, fullname, Toast.LENGTH_LONG).show();

            Intent i = new Intent(this, AfterLoginActivity.class);
            i.putExtra("SchoolId", schoolId);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ConnectProccess extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                publishProgress(1);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();

        }

        protected void onProgressUpdate(Integer... progress) {
            switch (progress[0]) {
                case 0:
                    tv.setText("Connecting...");
                    break;
                case 2:
                    tv.setText("Fetching Data...");
                    break;
                default:
                    tv.setText("Finalizing");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                loading1.setVisibility(View.INVISIBLE);
                balhin(result);
                finish();
            } else {
                Button retry = (Button) findViewById(R.id.retrybtn);
                retry.setVisibility(View.VISIBLE);
                retry.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    }

                });
            }


        }
    }
}
