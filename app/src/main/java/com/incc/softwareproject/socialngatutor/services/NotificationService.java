package com.incc.softwareproject.socialngatutor.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by carlo on 12/02/2016.
 */
public class NotificationService extends Service {
    public static final String TAG = "NotificationService";
    public static final String ACTION = NotificationService.class.getCanonicalName();
    private static final int NOTIFICATION = 1;
    private NotificationManager nM;
    com.incc.softwareproject.socialngatutor.Server.Notification notif;


    @Override
    public void onCreate() {
        nM= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notif = new com.incc.softwareproject.socialngatutor.Server.Notification();
        Log.e(TAG, notif.getNotif(intent.getStringExtra("UserId")));
        showNotification(notif.getNotif(intent.getStringExtra("UserId")));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(String result){
        String title = "",content = "";
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Notification");
            title = data.getString("from_username");
            content = data.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(R.drawable.logov1)  // the status icon
                .setContentTitle(title)  // the label of the entry
                .setContentText(content)  // the contents of the entry
                .build();   //   CHILL RA NA SIYA
        nM.notify(NOTIFICATION, notification);

    }




}
