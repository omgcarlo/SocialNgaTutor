package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.Server;

import org.json.JSONObject;

/**
 * Created by carlo on 1/26/16.
 */
public class PostService extends IntentService {

    private static final String TAG = "PostService";
    public static final String ACTION = PostService.class.getCanonicalName();

    public PostService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        Post sv = new Post();
        String res_txt = ""; //JSON response
        String description = i.getStringExtra("description");
        String tags = i.getStringExtra("tags");
        String type = i.getStringExtra("type");
        String ownerId = i.getStringExtra("ownerId");

        try {
            res_txt = sv.postFeed(description,type,ownerId,tags);
            //  CONVERT JSON RESPONSE TO STRINGS
            Log.d("JSONRES",res_txt);
            JSONObject reader = new JSONObject(res_txt);
            JSONObject data = reader.getJSONObject("Post");
            if (!res_txt.equals("") && data.getBoolean("Success")) {
                Intent i2 = new Intent(ACTION);
                //i2.putExtra("response", res_txt);
                i2.putExtra("Success",true);
                sendBroadcast(i2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
