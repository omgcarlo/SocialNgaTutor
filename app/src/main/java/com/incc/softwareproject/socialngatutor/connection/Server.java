package com.incc.softwareproject.socialngatutor.connection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by carlo on 1/8/16.
 */
public class Server {
    private final static String BASE_URL = "http://192.168.1.5/SocialTutor/server/";
    private final static String USER_URL = "user.php";
    private final static String PROGRAM_URL = "program.php";

    public String login(String username, String password) {
        BufferedReader reader = null;
        String uri = BASE_URL + USER_URL;
        String res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("login", "UTF-8");

            data += "&" + URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

            // Defined URL  where to send data
            URL url = new URL(uri);

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

            res_txt = sb.toString();
            // res_txt = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }

    public String signup(String... params) {
        String fields[] = {"schoolId", "username", "password", "birthdate", "email", "programId", "full_name"};
        BufferedReader reader = null;
        String uri = BASE_URL + USER_URL;
        String res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("signup", "UTF-8");   //action

            for (int i = 0; i < fields.length; i++) {
                data += "&" + URLEncoder.encode(fields[i], "UTF-8")
                        + "=" + URLEncoder.encode(params[i], "UTF-8");
                Log.d("data:",params.length + "");
            }
            // Defined URL  where to send data
            URL url = new URL(uri);

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

            res_txt = sb.toString();
            //res_txt = data;
            Log.d("result:",res_txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
}
