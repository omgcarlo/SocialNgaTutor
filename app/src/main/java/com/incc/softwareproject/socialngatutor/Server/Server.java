package com.incc.softwareproject.socialngatutor.Server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by carlo on 1/8/16.
 */
public class Server {
    private final static String BASE_URL = "http://192.168.1.7/STFinal/STServer/";
    private final static String USER_URL = "user.php";
    private final static String PROGRAM_URL = "program.php";
    private final static String POST_URL = "post.php";
    private final static String SEARCH_URL = "search.php";
    private final static String COMMENT_URL = "comment.php";
    private final static String EVENT_URL = "events.php";
    private final static String ACTIVITY_URL = "activity.php";
    private final static String NOTIFICATION_URL = "notification.php";
    private final static String UPLOADPIC_URL = "uploadpicture.php";

    public static String getUploadpicUrl() {
        return UPLOADPIC_URL;
    }

    public static String getNotificationUrl() {
        return NOTIFICATION_URL;
    }


    public static String getActivityUrl() {
        return ACTIVITY_URL;
    }


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getUserUrl() {
        return USER_URL;
    }

    public static String getProgramUrl() {
        return PROGRAM_URL;
    }

    public static String getPostUrl() {
        return POST_URL;
    }

    public static String getSearchUrl() {
        return SEARCH_URL;
    }

    public static String getCommentUrl() {
        return COMMENT_URL;
    }

    public static String getEventUrl() {
        return EVENT_URL;
    }

    private BufferedReader reader = null;
    private String res_txt = "";

    //====================ALWAYS GAMITON NGA FUNCTIONS=======================
    public String postFunction(String uri, String data) {

        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res_txt;
    }

    public String getFunction(String uri, String data) {
        String res_txt = "";
        try {
            URL obj = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("queries", data);

            int responseCode = con.getResponseCode();
            Log.d("Sending", uri);
            Log.d("Response Code : ", responseCode + "");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            res_txt = response.toString();
        } catch (Exception e) {

        }
        return res_txt;
    }
}
