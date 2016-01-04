package com.incc.softwareproject.socialngatutor.connection;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by carlo on 04/01/2016.
 */
public class LoginProcess extends AsyncTask<String,Void,String> {
    HttpURLConnection urlConnection;


    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();

    }
    @Override
    protected void onPostExecute(String result) {

    }
}
