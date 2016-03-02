package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.Server.Comment;
import com.incc.softwareproject.socialngatutor.Server.Post;

import org.json.JSONObject;

/**
 * Created by carlo on 28/02/2016.
 */
public class DeleteService extends IntentService {
    public static final String TAG = "DeleteService";
    public static final String ACTION = DeleteService.class.getCanonicalName();

    public DeleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        String res_txt = "";
        String action = i.getStringExtra("Action");
        String id = "";
        if(action.equals("comment")){
            Comment sv = new Comment();
            id = i.getStringExtra("CommentId");
            try {
                res_txt = sv.deleteComment(id);
                Log.e(TAG, res_txt);
                JSONObject reader = new JSONObject(res_txt);
                JSONObject data = reader.getJSONObject("Comment");
                if (data.getBoolean("Success")) {
                    Intent i2 = new Intent(ACTION);
                    i2.putExtra("Success", true);
                    sendBroadcast(i2);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            //post
            Post sv = new Post();
            id = i.getStringExtra("PostId");
            try {
                res_txt = sv.deletePost(id);
                Log.e(TAG,res_txt);
                JSONObject reader = new JSONObject(res_txt);
                JSONObject data = reader.getJSONObject("Post");
                if (data.getBoolean("Success")) {
                    Intent i2 = new Intent(ACTION);
                    i2.putExtra("Success", true);
                    sendBroadcast(i2);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
