package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 26/02/2016.
 */
public class Request extends Server {
    public String newRequest(String userId,String remarks){
        String res_txt = "";
        String uri = getBaseUrl() + getRequestUrl() + "?action=request";
        try {
            String data = URLEncoder.encode("userId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");

            data += "&" + URLEncoder.encode("remarks", "UTF-8")
                    + "=" + URLEncoder.encode(remarks, "UTF-8");
            //post function here
            res_txt =  postFunction(uri,data);

            // res_txt = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;

    }
    public String getRequestStatus(String userId){
        String res_txt = "";
        String uri = getBaseUrl() + getRequestUrl() + "?action=checkStatus";
        try {
            String data = URLEncoder.encode("userId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");

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
