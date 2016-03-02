package com.incc.softwareproject.socialngatutor.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.incc.softwareproject.socialngatutor.AfterLoginActivity;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    Bitmap myBitmap;
    String reference;
    String refId;
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        this.context = context;
        //Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        sData = context.getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SCHOOL_ID = sData.getString("SchoolId", "wala");
        if(SCHOOL_ID.equals("wala")){
            CancelAlarm(context);
        }
        Log.e("Activity Listener is","Listening");
        new getData().execute(SCHOOL_ID);

        //Release the lock
        wl.release();
    }
    private class getData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            notif = new com.incc.softwareproject.socialngatutor.Server.Notification();
            String s = notif.getNotif(params[0]);
            String desc;
            Log.e(TAG,s);
            if(!s.equals("")){
                try {
                    JSONObject reader = new JSONObject(s);
                    JSONObject data = reader.getJSONObject("Notification");
                    reference = data.getString("Notification");
                    refId = data.getString("referenceId");
                    title = data.getString("from_full_name");
                    if(data.getString("description").equals("tagged"))
                        desc = "mentioned you on a ";
                    else
                        desc = "commented on your";
                    //content = "@" + data.getString("from_username") + " "+ desc + " " + data.getString("Notification") ;
                    content =  desc + " " + data.getString("Notification") ;
                    URL url = new URL(data.getString("pic_url"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(in);
                    notif.updateNotif(data.getString("notificationId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            showNotification(s);
        }
    }
    public void showNotification(String result){
        //Log.e("AMBRESULT",result);
        if(!result.equals("")){

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            @SuppressWarnings("deprecation")

            Notification notification;
            Intent notificationIntent = null;

            notificationIntent = new Intent(context, PostViewActivity.class);
            notificationIntent.putExtra("PostId", refId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            notification = builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logov_small_icon)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setLargeIcon(myBitmap)
                    .setContentText(content)
                    .setVibrate(new long[] { 500, 500 })
                    .build();
            notificationManager.notify(9999, notification);

        }

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
