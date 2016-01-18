package com.incc.softwareproject.socialngatutor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.connection.Server;

public class SignUpProcessActivity extends AppCompatActivity {
    TextView loadingtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_process);
        loadingtxt = (TextView) findViewById(R.id.s_loading_txt);
        new SigupProcess().execute();
    }

    private class SigupProcess extends AsyncTask<String, Integer, String> {
        String schoolId,fullname,username,password,email,birthdate,programId;

        @Override
        protected String doInBackground(String... params) {
            /**
             * why init data here?
             * para mafeel sa user ang loading
             * ug choy man ang loading hahaha
             */
            schoolId = getIntent().getExtras().getString("schoolId");
            fullname = getIntent().getExtras().getString("fullname");
            username = getIntent().getExtras().getString("username");
            password = getIntent().getExtras().getString("password");
            email = getIntent().getExtras().getString("email");
            birthdate = getIntent().getExtras().getString("birthdate");
            programId = "1";
            Server svConn = new Server();
            // Return result
            return svConn.signup(schoolId,fullname,password,birthdate,email,programId,fullname);

        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("sql",result);
            loadingtxt.setText(result);
        }
    }
}


