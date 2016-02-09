package com.incc.softwareproject.socialngatutor.Server;

import android.util.Log;

/**
 * Created by carlo on 08/02/2016.
 */
public class Search extends Server {
    private String res_txt;
    //=================== SEARCH====================
    public String searchPeople(String queries,String imongId,String action){
        String uri = getBaseUrl() + getSearchUrl() + "?action="+ action +"&queries=" + queries + "&imo=" + imongId;
        //Log.e("uri", uri);
        res_txt = "";
        try {

            res_txt =  getFunction(uri, queries);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }

}
