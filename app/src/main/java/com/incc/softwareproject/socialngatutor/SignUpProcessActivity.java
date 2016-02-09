package com.incc.softwareproject.socialngatutor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpProcessActivity extends AppCompatActivity {
    TextView loadingtxt;
    Button toLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_process);
        loadingtxt = (TextView) findViewById(R.id.s_loading_txt);
        toLoginBtn = (Button) findViewById(R.id.toLoginBtn);
        toLoginBtn.setVisibility(View.INVISIBLE);
        new SigupProcess().execute();
    }
    public void toLoginBtn(View v){
        startActivity(new Intent(this,MainActivity.class));
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
            User svConn = new User();
            // Return result
            return svConn.signup(schoolId,username,password,birthdate,email,programId,fullname);

        }
        @Override
        protected void onPostExecute(String result) {
            //Log.d("sql",result);
            try {
                JSONObject reader = new JSONObject(result);
                JSONObject data = reader.getJSONObject("User");
                if(data.getBoolean("Success")){
                    loadingtxt.setText("YEY!");

                }
                else{
                    loadingtxt.setText("YOY!");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            toLoginBtn.setVisibility(View.VISIBLE);
        }
    }
}


