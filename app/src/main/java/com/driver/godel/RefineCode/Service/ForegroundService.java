package com.driver.godel.RefineCode.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {

        super.onCreate();
//        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MyPackagesActivity.class);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Godel Driver")
//                .setContentText(input)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        Log.d("ForegroundService", "------------onCreate------------");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
//        createNotificationChannel();
//        Intent notificationIntent = new Intent(this, MyPackagesActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Godel Driver")
////                .setContentText(input)
//                .setSmallIcon(R.mipmap.app_icon)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(1, notification);
        Log.d("ForegroundService", "------------onStartCommand------------");
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d("ForegroundService", "------------onDestroy------------");
        super.onDestroy();
//        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ForegroundService", "------------onBind------------");
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
