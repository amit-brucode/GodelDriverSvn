package com.driver.godel.RefineCode.Service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

//import androidx.annotation.NonNull;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.response.CrashReport.CrashEmailResponse;
import com.driver.godel.response.ForgotPaswdResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ajay2.Sharma on 04-Sep-17.
 */

public class UpdateDriverLocationServiceNew extends JobIntentService implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient googleApiClient;
    Location location;
    double latitude, longitude;
    public static String driverId = null;
    static WebRequest webRequest;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    //Database
    NotificationManager notificationmanager;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000; // = 5 seconds

    int locationUpdateInterval = 5;
    final static String TAG = "SERVICE_UPDATE_DRIVER1";
    static Context con;
    public static final String CHANNEL_ID = "GodelDriverForegroundServiceChannel";

    CountDownTimer countdown;
    Call<ForgotPaswdResponse> packageTrackCall;
    int errorCount = 0;
//    private final IBinder mBinder =new LocalBinder();

//    public class LocalBinder extends Binder{
//        UpdateDriverLocationServiceNew getService(){
//            return UpdateDriverLocationServiceNew.this;
//        }
//
//    }


    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MyPackagesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Foreground Service")
                .setContentTitle("Godel Driver")
                .setSmallIcon(R.drawable.app_icon_)
                .setContentIntent(pendingIntent)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground(1, notification);

//        if (intent == null) {
//            Log.e("SeviceNull", "onStartCommand: ");
//
//            return START_REDELIVER_INTENT;
//        }
//        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        con = this;
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        driverId = preferences.getString(SharedValues.DRIVER_ID, null);
        locationUpdateInterval = Integer.parseInt(preferences.getString(SharedValues.KEY_DRIVER_LOCATION_INTERVAL, "5"));
        editor = preferences.edit();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(con);
        mSettingsClient = LocationServices.getSettingsClient(con);
        connectWithFusedClient();
        webRequest = new WebRequest(this);
        ConnectClient();
//        trackPackageStatus();
//        Notification("Connecting to server...", "", -1);
    }

    public void connectWithFusedClient() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(con);
            mSettingsClient = LocationServices.getSettingsClient(con);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "-----task.getResult()----" + task.getResult());
                        location = task.getResult();
                    }
                }
            });
        }catch (Exception e){
            Log.d(TAG,"-----task.getResult()---ee-"+e);
        }
    }


    Notification notification;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"-----onStartCommand-------"+intent);
//        notificationmanager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//        createNotificationChannel();
//        Intent notificationIntent = new Intent(this, MyPackagesActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
////                .setContentTitle("Foreground Service")
//                .setContentTitle("Godel Driver")
//                .setSmallIcon(R.drawable.app_icon_)
//                .setContentIntent(pendingIntent)
//                .build();


//        if (intent == null) {
//            Log.e("SeviceNull", "onStartCommand: ");
//
//            return START_REDELIVER_INTENT;
//        }
        return START_NOT_STICKY;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "GodelDriverForegroundServiceChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Global.driverLatitude = "" + location.getLatitude();
            Global.driverLongitude = "" + location.getLongitude();
        }else{
            connectWithFusedClient();
            ConnectClient();
        }
        Log.d(TAG,"-----location---onConnected-----"+location);

        trackPackageStatus();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"-----location---onConnectionSuspended-----");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

        Log.d(TAG,"-----location---onConnectionFailed-----"+connectionResult);
        ConnectClient();
        trackPackageStatus();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Global.driverLatitude = "" + latitude;
        Global.driverLongitude = "" + longitude;
        Log.d(TAG,"-----location---onLocationChanged-----"+location);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        Notification("Driver offline", "Please reopen app", 1);
        Intent locationService = new Intent(this, UpdateDriverLocationServiceNew.class);
        notificationmanager= (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(locationService);
        }else{
        startService(locationService);
        }
    }

    //Track Package Method
    private void trackPackageStatus() {
        if (countdown != null) {
            countdown.cancel();
            countdown = null;
        }

        long seconds = locationUpdateInterval * 1000;
        countdown = new  CountDownTimer(seconds, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "on Tick");
            }

            public void onFinish() {
                driverId = preferences.getString(SharedValues.DRIVER_ID, null);
                String status = preferences.getString(SharedValues.DRIVER_STATUS, "0");

                boolean isGoogleAPIConnected = false, isLocationReceived = false, isDriverOnline = false, isGPSEnabled = false, isInternetEnabled = false;
                LocationManager manager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean gpsAvailable = manager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
                String message = "Update Driver Location API Error :- Driver id: " + driverId + " APK VERSION: " + Global.APK_VERSION;
                if (isNetworkAvailable()) {
                    isInternetEnabled = true;
                    if (gpsAvailable) {
                        isGPSEnabled = true;
                        if (googleApiClient != null && googleApiClient.isConnected()) {
                            isGoogleAPIConnected = true;
                            if (Integer.parseInt(status) == 0 && driverId != null) {
                                isDriverOnline = true;
                                if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                if (googleApiClient != null && googleApiClient.isConnected()) {
                                    locationRequest = new LocationRequest();
                                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    locationRequest.setInterval(UPDATE_INTERVAL);
                                    locationRequest.setFastestInterval(FASTEST_INTERVAL);
                                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location1) {
                                            Log.d(TAG, "-----location---1   location-----" + location1);
                                            location=location1;
                                        }
                                    });
                                }
                                Log.d(TAG,"-----location---1-----"+location);
                                if(location==null){

//                                    trackPackageStatus();
                                    connectWithFusedClient();
                                    ConnectClient();
                                    locationRequest = new LocationRequest();
                                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    locationRequest.setInterval(UPDATE_INTERVAL);
                                    locationRequest.setFastestInterval(FASTEST_INTERVAL);

                                    if (ActivityCompat.checkSelfPermission(con,
                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                            &&  ActivityCompat.checkSelfPermission(con,
                                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        Toast.makeText(con, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
                                    }
                                    if (googleApiClient != null && googleApiClient.isConnected()) {
                                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                                            @Override
                                            public void onLocationChanged(Location location1) {
                                                Log.d(TAG, "-----location---1   location-----" + location1);
                                                location=location1;
                                            }
                                        });
                                    }

                                }
                                Log.d(TAG,"-----location---2-----"+location);

                                if (location != null) {
                                    isLocationReceived = true;
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    float speed = location.getSpeed();
//                                    Toast.makeText(UpdateDriverLocationService.this, "Speed: " + speed, Toast.LENGTH_SHORT).show();
                                    if (packageTrackCall != null) {
                                        packageTrackCall.cancel();
                                    }
//                                    Log.d(TAG,"-----locationhhfb  fk df----1----"+location);
//                                    Log.d(TAG,"-----location.getLatitude()----"+location.getLatitude());
//                                    Log.d(TAG,"-----location.getLongitude()----"+location.getLongitude());
//                                    Log.d(TAG,"-----location.getLongitude()----"+(latitude>0.0));
//                                    Log.d(TAG,"-----location.getLongitude()----"+(String.valueOf(latitude).length()>0));
//                                    Log.d(TAG,"-----((latitude > 0.0) && (longitude > 0.0))----"+(String.valueOf(longitude).length()>0));
                                    if (String.valueOf(latitude).length()>0 || String.valueOf(longitude).length()>0) {
                                        if (packageTrackCall != null) {
                                            packageTrackCall.cancel();
                                        }
                                        errorCount = 0;
                                        Log.d(TAG,"-----googleApiClient----1----"+googleApiClient);
                                        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                                        String Accept = Global.ACCEPT;
                                        driverId = preferences.getString(SharedValues.DRIVER_ID, "");


                                        String country_code = CountryCodeCheck.countrycheck(UpdateDriverLocationServiceNew.this);
                                        Log.d("Tagg","----1111---in api----------"+country_code);
                                        if (country_code != null && country_code.trim().length() > 0) {

                                            packageTrackCall = webRequest.apiInterface.trackDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                                            Log.d("Tagg","-------in api----------"+country_code);
                                            trackPackage(packageTrackCall);

                                        }


                                    } else {
                                        ConnectClient();
                                    }
                                    location = null;
                                } else {
                                    isLocationReceived = false;
                                    ConnectClient();
                                    message = message + ", Location: null";
                                }
                            } else {
                                Log.d(TAG, "offline");
                                isDriverOnline = false;
                                if (driverId != null) {
                                    message = message + ", isDriverOnline:  false";
                                }
                                trackPackageStatus();
                            }

                        }
                        else {
                            isGoogleAPIConnected = false;
                            ConnectClient();
                            message = message + ", Google client connected: false";
                        }

                    } else {
                        isGPSEnabled = false;
                        message = message + ", GPS Enabled:  false";
                    }
                } else {
                    isInternetEnabled = false;
                    trackPackageStatus();
                }

                String notificationMessage = "", notificationContent = "";
                if (driverId != null) {
                    notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationmanager.cancelAll();
                    if (isInternetEnabled == false) {
//                        notificationContent = "Unable to connect";
//                        notificationMessage = "Please connect with internet";
//                        Notification(notificationContent, notificationMessage, 1);
                    } else if (status.trim().equals("1")) {
                        notificationContent = "You are offline";
                        notificationMessage = "";
                        Notification(notificationContent, notificationMessage, 1);
                    } else if (isGPSEnabled == false || isLocationReceived == false || isGoogleAPIConnected == false) {
                        if (!isGPSEnabled) {
                            notificationContent = "GPS Required";
                            notificationMessage = "Please turn on GPS and restart app";
                        } else if (!isGoogleAPIConnected) {
                            notificationContent = "Unable to detect location";
                            notificationMessage = "Check location services, GPS and reopen app";
                        } else
                        if (!isLocationReceived) {
                            notificationContent = "Unable to detect location";
                            notificationMessage = "Check location services, GPS and reopen app";
                        }
//                        Notification(notificationContent, notificationMessage, 1);

                        if (errorCount == 3) {
                            sendEmail(message);
                            MyPackagesActivity.isServiceError = true;
                            MyPackagesActivity.notificationContent = notificationContent;
                            MyPackagesActivity.notificationMessage = notificationMessage;
                            Intent broadcast = new Intent(Global.SERVICE_ERROR_BROADCAST);
                            LocalBroadcastManager.getInstance(con).sendBroadcast(broadcast);
                        }

                        Log.d(TAG, "message " + message);

                        errorCount = errorCount + 1;
                        trackPackageStatus();
                    }
                } else {

//                    notificationContent = "Login Required";
//                    notificationMessage = "Please Login with driver";
//                    Notification(notificationContent, notificationMessage);
                }
            }
        }.start();
    }


    private void Notification(String content, String message, int status) {

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "download_pdf";
//        CharSequence name = "Notification_Settings";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            noti = new Notification.Builder(UpdateDriverLocationServiceNew.this)
                    .setContentTitle(content)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setChannelId(CHANNEL_ID)
                    .setOngoing(true) // Again, THIS is the important line
                    .setAutoCancel(false).build();
        } else {
            noti = new Notification.Builder(UpdateDriverLocationServiceNew.this)
                    .setContentTitle(content)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setOngoing(true) // Again, THIS is the important line
                    .setAutoCancel(false).build();
        }
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mChannel != null) {
                notificationmanager.createNotificationChannel(mChannel);
            }
        }
        notificationmanager.notify(0, noti);
//        Global.driverStatus = status;
        MyPackagesActivity.changeStatus(status);
    }

    public void ConnectClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        googleApiClient.connect();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        trackPackageStatus();
    }

    //Track Package API
    private void trackPackage(Call<ForgotPaswdResponse> trackPackage) {

        Log.d(TAG,"------in trackPackage-----------");
        trackPackage.enqueue(new Callback<ForgotPaswdResponse>() {
            @Override
            public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
//                Toast.makeText(UpdateDriverLocationServiceNew.this, "api hitted "+latitude +" <---> "+longitude, Toast.LENGTH_SHORT).show();
//                Log.d("Tagg","------------response-----of trackPackage-----"+response);
                trackPackageStatus();


//                Notification("Connected to godel server", "", 0);
            }

            @Override
            public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                trackPackageStatus();
                Log.d("Tagg","------------ t----------"+ t);
                Notification("Connection failure", "Unable to connect server", 1);
            }
        });
    }

    private void sendEmail(String reason) {
        if (isNetworkAvailable()) {
            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");


            String country_code = CountryCodeCheck.countrycheck(UpdateDriverLocationServiceNew.this);
            if (country_code != null && country_code.trim().length() > 0) {

                Call<CrashEmailResponse> callEmail = webRequest.apiInterface.SendInformationEmail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, "DRIVER APP LOCATION INFORMATION:- " + reason);
                getSignupResponse(callEmail);
            }

        }
    }

    private void getSignupResponse(Call<CrashEmailResponse> profileCall) {
        profileCall.enqueue(new Callback<CrashEmailResponse>() {
            @Override
            public void onResponse(Call<CrashEmailResponse> call, Response<CrashEmailResponse> response) {
                CrashEmailResponse crashResponse = response.body();
            }

            @Override
            public void onFailure(Call<CrashEmailResponse> call, Throwable t) {

            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
