package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 11/02/2016.
 */
public class Notification extends Server {
    public String getNotif(String ownerId){
        String uri = getBaseUrl() + getNotificationUrl() + "?action=get";
        String res_txt = "";
        try {
            String data = URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(ownerId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
}
