package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    String uri = "http://192.168.1.6/socialtutor/server/user.php";

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

    private void balhin() {
        Intent i = new Intent(this, AfterLoginActivity.class);
        startActivity(i);
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
                tv.setText(result);
                loading1.setVisibility(View.INVISIBLE);
                balhin();
            }
        }
    }
}
