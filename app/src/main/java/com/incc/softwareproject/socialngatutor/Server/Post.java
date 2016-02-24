package com.incc.softwareproject.socialngatutor.Server;

import android.content.Context;
import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by carlo on 08/02/2016.
 */
public class Post extends Server {
    private String res_txt;

    //================== FEEDS/POST ===========================
    public String postFeed(String description, String type,String ownerId,String tags) {
        String uri = getBaseUrl() + getPostUrl();
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("new", "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                    + "=" + URLEncoder.encode(description, "UTF-8");

            data += "&" + URLEncoder.encode("type", "UTF-8")
                    + "=" + URLEncoder.encode(type, "UTF-8");

            data += "&" + URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(ownerId, "UTF-8");

            data += "&" + URLEncoder.encode("tags", "UTF-8")
                    + "=" + URLEncoder.encode(tags, "UTF-8");

            //post function here
            res_txt =  postFunction(uri,data);

            // res_txt = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }

    public String getFeeds(String ownerId){
        String uri = getBaseUrl() + getPostUrl() + "?action=feed";
        res_txt = "";
        try {
            String data = URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(ownerId, "UTF-8");
            res_txt =  postFunction(uri, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;

    }
    public String getPost(String postId,String userId){
        String uri = getBaseUrl() + getPostUrl() + "?action=getPost";
        try{
            String data = URLEncoder.encode("postId", "UTF-8")
                    + "=" + URLEncoder.encode(postId, "UTF-8");
            data += "&" + URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");
            res_txt =  postFunction(uri, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String upvote(String userId,String postId){
        //Log.e("POSTSERVER",userId + " " + postId);
        String uri = getBaseUrl() + getPostUrl() + "?action=upvote";
        try{
            String data = URLEncoder.encode("postId", "UTF-8")
                    + "=" + URLEncoder.encode(postId, "UTF-8");

            data += "&" + URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");
            res_txt =  postFunction(uri, data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String sharePost(String postId,String description, String type,String ownerId,String tags){
        String uri = getBaseUrl() + getPostUrl() + "?share=" + postId;
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("new", "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                    + "=" + URLEncoder.encode(description, "UTF-8");

            data += "&" + URLEncoder.encode("type", "UTF-8")
                    + "=" + URLEncoder.encode(type, "UTF-8");

            data += "&" + URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(ownerId, "UTF-8");

            data += "&" + URLEncoder.encode("tags", "UTF-8")
                    + "=" + URLEncoder.encode(tags, "UTF-8");

            //post function here
            res_txt =  postFunction(uri,data);

            // res_txt = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;

    }


}
