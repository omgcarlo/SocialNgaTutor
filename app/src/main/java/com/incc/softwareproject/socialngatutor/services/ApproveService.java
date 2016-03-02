package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.content.Intent;
import com.incc.softwareproject.socialngatutor.Server.Comment;
import org.json.JSONObject;

/**
 * Created by carlo on 28/02/2016.
 */
public class ApproveService extends IntentService {
    public static final String TAG = "ApproveService";
    public static final String ACTION = ApproveService.class.getCanonicalName();

    public ApproveService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        Comment sv = new Comment();
        String res_txt = "";
        //  INIT PARAMS FOR SERVER
        String commentId = i.getStringExtra("CommentId");
        String action = i.getStringExtra("Action");
        if(action.equals("approve")){
            try {
                res_txt = sv.approveComment(commentId);
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
        else{
            try {
                res_txt = sv.disapproveComment(commentId);
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
}
