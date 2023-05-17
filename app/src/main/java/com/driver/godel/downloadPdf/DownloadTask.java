package com.driver.godel.downloadPdf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import androidx.core.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.SendCrashEmail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private String folderName = "Godel_Driver_PDF";
    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;

    public DownloadTask(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {
        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    sendNotification(downloadFileName);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                }

                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {
                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                InputStream is = c.getInputStream();//Get InputStream for connection
                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();
            } catch (Exception e) {
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
            }
            return null;
        }
    }

    private void sendNotification(String messageBody) {
        File apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + folderName + messageBody);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Intent intent1 = new Intent();
            intent1.setAction(android.content.Intent.ACTION_VIEW);
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.setDataAndType(Uri.fromFile(apkStorage), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"));
            context.startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Nothing found to open file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SendCrashEmail.class);
            intent.putExtra("error", e.toString());
            context.startActivity(intent);
        }

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkStorage), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "download_pdf";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        NotificationCompat.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle("Downloading Complete")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle("Downloading Complete")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        // Create Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mChannel != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        // Build Notification with Notification Manager
        if (notificationBuilder != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }


}

