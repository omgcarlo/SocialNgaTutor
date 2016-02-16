package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlo on 1/29/2016.
 */
public class UpvoteService extends IntentService {
    public static final String TAG = "UpvoteService";
    public static final String ACTION = UpvoteService.class.getCanonicalName();
    public UpvoteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        Post sv = new Post();
        String res_txt;
        String userId =  i.getStringExtra("userId");
        String postId = i.getStringExtra("postId");
        //Log.e(TAG,postId + " " +userId );
            try {
                res_txt = sv.upvote(userId,postId);
                JSONObject reader = new JSONObject(res_txt);
                JSONObject data = reader.getJSONObject("Post");
                if (!res_txt.equals("") && !data.getBoolean("Success")) {
                    Intent i2 = new Intent(ACTION);
                    i2.putExtra("Success",true);
                    sendBroadcast(i2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

}


