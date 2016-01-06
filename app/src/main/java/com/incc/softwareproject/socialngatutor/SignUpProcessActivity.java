package com.incc.softwareproject.socialngatutor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignUpProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_process);

    }

    private class SigupProcess extends AsyncTask<String, Integer, String> {
        String fullname;
        String username;
        String password;
        String email;
        String birthdate;

        @Override
        protected String doInBackground(String... params) {
            fullname = getIntent().getExtras().getString("fullname");
            username = getIntent().getExtras().getString("username");
            password = getIntent().getExtras().getString("password");
            email = getIntent().getExtras().getString("email");
            birthdate = getIntent().getExtras().getString("birthdate");


            // Create data variable for sent values to server

            String data = null;
            try {
                data = URLEncoder.encode("fullname", "UTF-8")
                        + "=" + URLEncoder.encode(fullname, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                        + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String result = "";
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL("http://androidexample.com/media/webservice/httppost.php");

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                result = sb.toString();
            } catch (Exception ex) {

            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            // Return result
            return result;

        }
    }
}


