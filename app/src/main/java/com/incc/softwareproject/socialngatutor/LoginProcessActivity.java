package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.connection.Server;

import org.json.JSONObject;

public class LoginProcessActivity extends AppCompatActivity {
    SharedPreferences preferenceData;
    TextView tv;
    ProgressBar loading1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_process);
        tv = (TextView) findViewById(R.id.res);

        //Toast.makeText(this,uri,Toast.LENGTH_SHORT).show();

        // Animate Loading
        loading1 = (ProgressBar) findViewById(R.id.loading_login);
        loading1.setVisibility(View.VISIBLE);

        preferenceData = getSharedPreferences("ShareData", MODE_PRIVATE);

        new ConnectProccess().execute(getIntent().getExtras().getString("username"), getIntent().getExtras().getString("password"));
    }

    private void balhin(String res) {
        try {
            /**
             * Read and Convert JSON response
             */
            Toast.makeText(this, res, Toast.LENGTH_LONG).show();      //para detect sa error

            String schoolId = "";
            JSONObject reader = new JSONObject(res);
            JSONObject data = reader.getJSONObject("User");
            //tv.setText(fullname);

            //Toast.makeText(this, schoolId, Toast.LENGTH_LONG).show();
            if (!data.getBoolean("Success")) {
                //Toast.makeText(this,"SAYOP", Toast.LENGTH_LONG).show();
                animateRetry();
                tv.setText("Wrong Username or Password");
            } else {

                tv.setText("Last na jud ni...");
                schoolId = data.getString("SchoolId");
                String full_name = data.getString("Name");
                String username = getIntent().getExtras().getString("username");

                Intent i = new Intent(this, AfterLoginActivity.class);
                /* i.putExtra("SchoolId", schoolId);
                i.putExtra("FullName",full_name);
                i.putExtra("Username", username);*/

                //store data
                SharedPreferences.Editor editor = preferenceData.edit();
                editor.putString("SchoolId", schoolId);
                editor.putString("FullName", full_name);
                editor.putString("Username", username);
                editor.apply();
                //transfer
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateRetry() {
        loading1.setVisibility(View.INVISIBLE);
        (findViewById(R.id.retrybtn)).setVisibility(View.VISIBLE);
    }

    public void retryBtn(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private class ConnectProccess extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            setProgress(0);
            Server svConn = new Server();
            setProgress(1);
            return svConn.login(params[0], params[1]);

        }

        protected void onProgressUpdate(Integer... progress) {
            switch (progress[0]) {
                case 0:
                    tv.setText("Connecting...");
                    break;
                case 1:
                    tv.setText("Fetching Data...");
                    break;
                default:
                    tv.setText("Finalizing");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                loading1.setVisibility(View.INVISIBLE);

                balhin(result);
                finish();
            } else {
                //animate
                animateRetry();
                tv.setText("Cannot Connect to Server or Database");
            }
        }
    }
}
