package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.incc.softwareproject.socialngatutor.Server.Comment;
import com.incc.softwareproject.socialngatutor.Server.Report;

import org.json.JSONObject;

/**
 * Created by Carlo on 1/29/2016.
 */
public class ReportService extends IntentService {
    public static final String TAG = "ReportService";
    public static final String ACTION = ReportService.class.getCanonicalName();

    public ReportService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        Report sv = new Report();
        String res_txt = "";
        SharedPreferences spreferences = getSharedPreferences("ShareData", MODE_PRIVATE);
        //  INIT PARAMS FOR SERVER
        String postId = i.getStringExtra("PostId");
        String userId = spreferences.getString("SchoolId", "Wala");
        String table = i.getStringExtra("table");
        try {
            res_txt = sv.sendReport(userId, postId, table);
           // Log.e(TAG,postId + " " + userId + " " + table);
           // Log.e(TAG, res_txt);
            JSONObject reader = new JSONObject(res_txt);
            JSONObject data = reader.getJSONObject("Report");
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

