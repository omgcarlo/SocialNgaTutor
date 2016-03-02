package com.incc.softwareproject.socialngatutor.Server;

import android.util.Log;

import java.io.BufferedReader;
import java.net.URLEncoder;

/**
 * Created by carlo on 08/02/2016.
 */
public class User extends Server {
    private String res_txt = "";
    //================== LOGIN & SIGNUP =====================
    public String login(String username, String password) {
        String uri = getBaseUrl() + getUserUrl();
        try {
            // String data = URLEncoder.encode("action", "UTF-8")
            //+ "=" + URLEncoder.encode("login", "UTF-8");
            uri += "?action=login";

            String data = "&" + URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

            //function here
            res_txt =  postFunction(uri,data);

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
        String uri = getBaseUrl() + getUserUrl()+ "?action=signup";
        res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("signup", "UTF-8");   //action

            for (int i = 0; i < fields.length; i++) {
                data += "&" + URLEncoder.encode(fields[i], "UTF-8")
                        + "=" + URLEncoder.encode(params[i], "UTF-8");

            }
            Log.e("data:", data+ "");
            res_txt =  postFunction(uri,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }

    //================= FOLLOWING AND FOLLOWER ==============================
    public String addFollowing(String imongId,String iyangId){
        String uri = getBaseUrl() + getUserUrl() + "?action=addfollowing";
        res_txt = "";
        try {
            String data = URLEncoder.encode("imo", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            data += "&" +URLEncoder.encode("iya", "UTF-8")
                    + "=" + URLEncoder.encode(iyangId, "UTF-8");
            res_txt = postFunction(uri, data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }

    public String unfollow(String imongId,String iyangId){
        String uri = getBaseUrl() + getUserUrl()  + "?action=unfollow";
        res_txt = "";
        try {
            String data = URLEncoder.encode("imo", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            data += "&" +URLEncoder.encode("iya", "UTF-8")
                    + "=" + URLEncoder.encode(iyangId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String getProfileInfo(String imongId,String iyangId){
        String uri = getBaseUrl() + getUserUrl()  + "?action=getUserCredentials";
        res_txt = "";
        try {
            String data = URLEncoder.encode("imo", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            if(!iyangId.equals("")) {
                data += "&" + URLEncoder.encode("iya", "UTF-8")
                        + "=" + URLEncoder.encode(iyangId, "UTF-8");
            }
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String getFollowingPeople(String userId){

        String uri = getBaseUrl() + getUserUrl()  + "?action=getFollowingPeople";
        res_txt = "";
        try {
            String data = URLEncoder.encode("userId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
}
