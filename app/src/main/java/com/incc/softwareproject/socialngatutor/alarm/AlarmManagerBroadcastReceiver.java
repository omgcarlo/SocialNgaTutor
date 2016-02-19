package com.incc.softwareproject.socialngatutor.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.AfterLoginActivity;
import com.incc.softwareproject.socialngatutor.R;

import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by secac on 2/17/2016.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    final public static String ONE_TIME = "onetime";
    public static final String TAG = "AlarmManagerBroadcastReceiver";
    String title;
    String content;
    private Context context;
    private com.incc.softwareproject.socialngatutor.Server.Notification notif;
    SharedPreferences sData;
    String SCHOOL_ID;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AMB","NI RECEIVE BAI");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();
        /*
        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
        */
        this.context = context;
        //Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        sData = context.getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SCHOOL_ID = sData.getString("SchoolId", "wala");

        Log.e(TAG, SCHOOL_ID);
        new getData().execute(SCHOOL_ID);

        //Release the lock
        wl.release();
    }
    private class getData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            notif = new com.incc.softwareproject.socialngatutor.Server.Notification();
            return notif.getNotif(SCHOOL_ID);
        }

        @Override
        protected void onPostExecute(String s) {
            showNotification(s);
        }
    }
    public void showNotification(String result){
        Log.e("AMBRESULT",result);
        try {
            JSONObject reader = new JSONObject(result);
            JSONObject data = reader.getJSONObject("Notification");
            title = data.getString("from_username");
            content = data.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Notification notification;
        Intent notificationIntent = new Intent(context,AfterLoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
        //notification.setLatestEventInfo(AlarmManagerActivity.this, "na","wewewew", pendingIntent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logov1)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .build();
        notificationManager.notify(9999, notification);
        CancelAlarm(context);
    }
    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}