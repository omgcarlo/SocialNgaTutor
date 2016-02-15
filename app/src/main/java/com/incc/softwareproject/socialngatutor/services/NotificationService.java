package com.incc.softwareproject.socialngatutor.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by carlo on 12/02/2016.
 */
public class NotificationService extends Service {
    public static final String TAG = "NotificationService";
    public static final String ACTION = NotificationService.class.getCanonicalName();
    private static final int NOTIFICATION = 0;
    private NotificationManager nM;
    private Context context;
    com.incc.softwareproject.socialngatutor.Server.Notification notif;

    @Override
    public void onCreate() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        context = getBaseContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notif = new com.incc.softwareproject.socialngatutor.Server.Notification();
        Log.e(TAG, notif.getNotif(intent.getStringExtra("UserId")));
        showNotification(notif.getNotif(intent.getStringExtra("UserId")));
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(String result) {
        String title = "Testing", content = "Testing ni siya";
        /** try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Notification");
            title = data.getString("from_username");
            content = data.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        } */

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .build();

        nM.notify(NOTIFICATION, notification);

    }


}
