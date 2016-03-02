package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.Server.Report;
import com.incc.softwareproject.socialngatutor.Server.Request;

import org.json.JSONObject;

/**
 * Created by carlo on 26/02/2016.
 */
public class RequestService extends IntentService{
    public static final String TAG = "RequestService";
    public static final String ACTION = RequestService.class.getCanonicalName();
    public RequestService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent i) {
        Request sv = new Request();
        String res_txt = "";
        SharedPreferences spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        //  INIT PARAMS FOR SERVER
        String userId = spreferences.getString("SchoolId", "Wala");
        String remarks = i.getStringExtra("Remarks");
        try {
            res_txt = sv.newRequest(userId, remarks);
            // Log.e(TAG,postId + " " + userId + " " + table);
             Log.e(TAG, res_txt);
            JSONObject reader = new JSONObject(res_txt);
            JSONObject data = reader.getJSONObject("Request");
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
