package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 06/02/2016.
 */
public class Events extends Server {
    public String getEvents(String date){
        String uri = getBaseUrl() + getEventUrl() + "?action=getEvent";
        String res_txt = "";
        try {
            String data = URLEncoder.encode("edate", "UTF-8")
                    + "=" + URLEncoder.encode(date, "UTF-8");   //action
            res_txt =  postFunction(uri, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
    public String getUpcommentEvents(String date){
        String uri = getBaseUrl() + getEventUrl() + "?action=getUpcomingEvents";
        String res_txt = "";
        try {
            String data = URLEncoder.encode("edate", "UTF-8")
                    + "=" + URLEncoder.encode(date, "UTF-8");   //action
            res_txt =  postFunction(uri, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
}
