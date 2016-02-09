package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.Server.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Carlo on 1/29/2016.
 */
public class FollowService extends IntentService {
    public static final String TAG = "FollowService";
    public static final String ACTION = FollowService.class.getCanonicalName();
    private SharedPreferences spreferences;
    public FollowService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        User sv = new User();
        String res_txt = "";
        spreferences = getSharedPreferences("ShareData",MODE_PRIVATE);
        //  INIT PARAMS FOR SERVER
        String imongId = spreferences.getString("SchoolId", "wala");
        String iyangId = i.getStringExtra("iyangId");
        if(i.getStringExtra("follow").equals("follow")){
            //  FOLLOW NEW USER
            try {
                res_txt = sv.addFollowing(imongId,iyangId);
                //Log.d("Follow",res_txt);
                JSONObject reader = new JSONObject(res_txt);
                JSONObject data = reader.getJSONObject("Follow");
                if (!res_txt.equals("") && !data.getBoolean("Success")) {
                    Intent i2 = new Intent(ACTION);
                    //i2.putExtra("response", res_txt);
                    i2.putExtra("Success",true);
                    sendBroadcast(i2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            //  UNFOLLOW USER
            try {
                res_txt = sv.unfollow(imongId,iyangId);
                Log.d("FollowService",res_txt);
                JSONObject reader = new JSONObject(res_txt);
                JSONObject data = reader.getJSONObject("Follow");
                if (!res_txt.equals("") && !data.getBoolean("Success")) {
                    Intent i2 = new Intent(ACTION);
                    //i2.putExtra("response", res_txt);
                    i2.putExtra("Success",true);
                    sendBroadcast(i2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

