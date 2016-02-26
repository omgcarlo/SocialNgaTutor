package com.incc.softwareproject.socialngatutor.Server;

import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by carlo on 08/02/2016.
 */
public class Search extends Server {
    private String res_txt;
    //=================== SEARCH====================
    public String searchPeople(String queries,String imongId,String action){
        String uri = getBaseUrl() + getSearchUrl() + "?";

        //Log.e("uri", uri);
        res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode(action, "UTF-8");
            data += "&" + URLEncoder.encode("queries", "UTF-8")
                    + "=" + URLEncoder.encode(queries, "UTF-8");
            data += "&" + URLEncoder.encode("imo", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            uri += data;
            res_txt =  getFunction(uri, action);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
    public String dicoverTopics(String action,String queries,String imongId,String courseNo){
        String uri = getBaseUrl() + getSearchUrl() + "?";
        //Log.e("uri", uri);
        res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode(action, "UTF-8");
            data += "&" + URLEncoder.encode("queries", "UTF-8")
                    + "=" + URLEncoder.encode(queries, "UTF-8");
            data += "&" + URLEncoder.encode("imongId", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            data += "&" + URLEncoder.encode("courseNo", "UTF-8")
                    + "=" + URLEncoder.encode(courseNo, "UTF-8");
            uri += data;
            res_txt =  getFunction(uri, action);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
    public String dicoverNotes(String action,String queries,String imongId,String courseNo){
        String uri = getBaseUrl() + getSearchUrl() + "?";
        //Log.e("uri", uri);
        res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode(action, "UTF-8");
            data += "&" + URLEncoder.encode("queries", "UTF-8")
                    + "=" + URLEncoder.encode(queries, "UTF-8");
            data += "&" + URLEncoder.encode("imo", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            data += "&" + URLEncoder.encode("courseNo", "UTF-8")
                    + "=" + URLEncoder.encode(courseNo, "UTF-8");
            uri += data;
            res_txt =  getFunction(uri, action);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }

}
