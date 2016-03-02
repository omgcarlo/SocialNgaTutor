package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.incc.softwareproject.socialngatutor.Server.Comment;

import org.json.JSONObject;

/**
 * Created by Carlo on 1/29/2016.
 */
public class CommentService extends IntentService {
    public static final String TAG = "CommentService";
    public static final String ACTION = CommentService.class.getCanonicalName();
    private SharedPreferences spreferences;
    public CommentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        Comment sv = new Comment();
        String res_txt = "";
        spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        //  INIT PARAMS FOR SERVER
        String comment = i.getStringExtra("comment");
        String userId = spreferences.getString("SchoolId", "Wala");
        String postId = i.getStringExtra("postId");
        try {
            res_txt = sv.postComment(comment, userId, postId);
            //Log.e(TAG,res_txt);
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
}

