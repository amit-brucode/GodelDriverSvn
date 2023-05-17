/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.driver.godel.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String message, bookingId, title, pckgStatus;
    //Shared Prefs
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int vibration = 0;
    Vibrator vibrator;
    final String TAG = "NOTIFICATIONS_TAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get("is_inactive") != null) {
                if (remoteMessage.getData().get("is_inactive").toString().equalsIgnoreCase("1")) {
                    SharedPreferences preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    Intent broadcast = new Intent(Global.INACTIVE_BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
                    Log.d(TAG, "notifiacatoincall" + "inactivate");
                    Log.d(TAG, "------remoteMessage.getData()-----111---" + remoteMessage.getData());


                } else {
                    Log.d(TAG, "notifiacatoincall" + "sendbroadcast1");
                    senBroadcast();
                    Log.d(TAG, "------remoteMessage.getData()----222----" + remoteMessage.getData());
                }
            } else {
                Log.d(TAG, "notifiacatoincall" + "sendbroadcast2");
                senBroadcast();
                Log.d(TAG, "------remoteMessage.getData()---3333-----" + remoteMessage.getData());
            }
        } else {
            Log.d(TAG, "notifiacatoincall" + "sendbroadcast3");
            Log.d(TAG, "------remoteMessage.getData()---4444-----" + remoteMessage.getData());
            senBroadcast();
        }

        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            //Get Message and booking Id;
            title = object.getString("title");
            if(title.equals("Package status updated by admin")){
                vibration=1;
            }else {
                vibration=0;
            }
            message = object.getString("message");
            bookingId = object.getString("booking_id");
            pckgStatus = object.getString("package_status");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Notification(title);
    }


    public void Notification(String messageBody) {
        Log.d(TAG, "notifiacatoincall" + messageBody);
        prefs = getSharedPreferences(SharedValues.PREF_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent i = new Intent("send");
        i.putExtra("BookingId", bookingId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        Intent intent = new Intent(this, MyPackagesActivity.class);
        intent.putExtra("BookingId", bookingId);
        intent.putExtra("Status", "notification");

        Log.d(TAG, "notifiacatoincall" + "sendbroadcast3");
        Log.d(TAG, "----bookingId----" + bookingId);
        Log.d(TAG, "====Status--1---" + "notification");
        Log.d(TAG, "====Status---2--" + "Status");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }


//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "download_pdf";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel =null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

//        if(vibration==1){
//            Toast.makeText(this, "Vibration 11", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "-----vibration--111---" + vibration);
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//            } else {
//                //deprecated in API 26
//                vibrator.vibrate(500);
//            }
//        }else{
//            Toast.makeText(this, "Vibration 00", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "-----vibration--000---" + vibration);
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            if (vibrator != null && vibrator.hasVibrator()) {
//                Log.d(TAG,"Vibration"+ "  Stopped: ");
//
//                vibrator.cancel();
//            } else {
//                Log.d(TAG ,"Vibration   "+ "null: ");
//            }
//        }
        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(vibration==1) {
                Log.d(TAG, "====vibration---1111--))))OOOOOO)))" + vibration);
                vibration=0;
                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("Godel Driver")
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[]{1L, 2L, 3L})
                        .setContentIntent(pendingIntent)
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true);

            }else{
                Log.d(TAG, "====vibration---00000-- )))))OOOOO)))))" + vibration);
                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("Godel Driver")
                        .setContentText(messageBody)
                        .setSound(null)
                        .setVibrate(null)
                        .setContentIntent(pendingIntent)
                        .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true);

            }
        }else{
            if(vibration==1) {
                Log.d(TAG, "====vibration---11111-- )))))not O)))))" + vibration);
                vibration=0;
                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("Godel Driver")
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[]{1L, 2L, 3L})
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            }else{
                Log.d(TAG, "====vibration---00000-- )))))not O))))" + vibration);
                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("Godel Driver")
                        .setContentText(messageBody)
                        .setSound(null)
                        .setVibrate(null)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            }
        }

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(mChannel!=null) {
                notificationmanager.createNotificationChannel(mChannel);
            }
        }
        // Build Notification with Notification Manager
        if(builder!=null) {
            notificationmanager.notify(0, builder.build());
        }
        }


    private void senBroadcast() {
        MyPackagesActivity.isNotificationAvailable = 1;
        Intent OngoingBroadcast = new Intent(Global.ONGOING_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(OngoingBroadcast);

        Intent pickupBroadcast = new Intent(Global.PICKUP_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pickupBroadcast);

        Intent deliveryBroadcast = new Intent(Global.DELIVERY_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(deliveryBroadcast);
    }
}
