package com.incc.softwareproject.socialngatutor.Server;

import java.net.URLEncoder;

/**
 * Created by carlo on 08/02/2016.
 */
public class Comment extends Server {
    String res_txt;
    //====================COMMENT===========================================
    public String postComment(String comment,String userId,String postId){
        String uri = getBaseUrl() + getCommentUrl() ;
        res_txt = "";
        try {
            String data = URLEncoder.encode("action", "UTF-8")
                    + "=" + URLEncoder.encode("new", "UTF-8");
            data += "&" +URLEncoder.encode("comment", "UTF-8")
                    + "=" + URLEncoder.encode(comment, "UTF-8");
            data += "&" +URLEncoder.encode("userId", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");
            data += "&" +URLEncoder.encode("postId", "UTF-8")
                    + "=" + URLEncoder.encode(postId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String getComments(String postId){
        String uri = getBaseUrl() + getCommentUrl() +"?action=getcomments";
        res_txt = "";
        try {
            String data = URLEncoder.encode("postId", "UTF-8")
                    + "=" + URLEncoder.encode(postId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String approveComment(String commentId){
        String uri = getBaseUrl() + getCommentUrl() +"?action=approve";
        res_txt = "";
        try {
            String data = URLEncoder.encode("commentId", "UTF-8")
                    + "=" + URLEncoder.encode(commentId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String disapproveComment(String commentId){
        String uri = getBaseUrl() + getCommentUrl() +"?action=disapprove";
        res_txt = "";
        try {
            String data = URLEncoder.encode("commentId", "UTF-8")
                    + "=" + URLEncoder.encode(commentId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String getCommentDetails(String commentId){
        String uri = getBaseUrl() + getCommentUrl() +"?action=getComment";
        res_txt = "";
        try {
            String data = URLEncoder.encode("commentId", "UTF-8")
                    + "=" + URLEncoder.encode(commentId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
    public String deleteComment(String commentId){
        String uri = getBaseUrl() + getCommentUrl() +"?action=deleteComment";
        res_txt = "";
        try {
            String data = URLEncoder.encode("commentId", "UTF-8")
                    + "=" + URLEncoder.encode(commentId, "UTF-8");
            res_txt = postFunction(uri,data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res_txt;
    }
}
