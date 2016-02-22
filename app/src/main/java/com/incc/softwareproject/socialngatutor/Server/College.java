package com.incc.softwareproject.socialngatutor.Server;

/**
 * Created by carlo on 21/02/2016.
 */
public class College extends Server {
    private String res_txt;
    public String getCollege(){
        String uri = getBaseUrl() + getCollegeUrl() + "?action=fetch";
        //Log.e("uri", uri);
        res_txt = "";
        try {
            res_txt =  getFunction(uri, "fetch");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
    public String getCourse(String code){
        String uri = getBaseUrl() + getCollegeUrl() + "?action=getCourse&college=" + code;
        //Log.e("uri", uri);
        res_txt = "";
        try {
            res_txt =  getFunction(uri, "fetch");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return response on activity
        return res_txt;
    }
}
