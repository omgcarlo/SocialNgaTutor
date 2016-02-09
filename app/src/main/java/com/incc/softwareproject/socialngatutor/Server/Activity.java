package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 08/02/2016.
 */
public class Activity extends Server {
    private String res_txt ;
    public String getActivities(String imongId){
        String uri = getBaseUrl() + getActivityUrl() +"?action=getActivities";
        res_txt = "";
        try {
            String data = URLEncoder.encode("ownerId", "UTF-8")
                    + "=" + URLEncoder.encode(imongId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
}
