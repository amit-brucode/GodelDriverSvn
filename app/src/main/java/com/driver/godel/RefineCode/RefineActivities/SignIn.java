package com.driver.godel.RefineCode.RefineActivities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.response.LoginResp.Data;
import com.driver.godel.response.LoginResp.LoginResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ajay2.Sharma on 8/2/2018.
 */

public class SignIn extends GodelActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private String FCMToken, deviceName = "", deviceAPIVersion = "", uuid;
    private Call<LoginResponse> loginResponseCal;
    private BroadcastReceiver broadcastReceiver;
    private ProgressDialog signInProgress;
    private TextView tvForgetPassword,tvwantuser;
    private GoogleApiClient googleApiClient;
    private Location location;
    ScrollView scrollView;
    static String country_code;
    RadioButton rbDriver, rbAgent;
    RadioGroup rgGroup;
    String type = "0";
    String language_type;
    Context con;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_new);
        con=SignIn.this;

        Global global = new Global(SignIn.this);
        global.setCurrencySymbol();
        tvwantuser=(TextView)findViewById(R.id.tv_want_user);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        rbDriver = (RadioButton) findViewById(R.id.rb_driver);
        rbAgent = (RadioButton) findViewById(R.id.rb_agent);
        rgGroup = (RadioGroup) findViewById(R.id.rg_group);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
        if(!language_type.equals(null)){
            if(language_type.equals("1")){
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }else if(language_type.equals("2")){
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
        }}
        FCMToken = FirebaseInstanceId.getInstance().getToken();
        uuid = Settings.Secure.getString(SignIn.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            deviceName = "Device " + Build.BRAND + " Model " + Build.MODEL;
            deviceAPIVersion = "SDK " + Build.VERSION.SDK + " VERSION  " + Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    if (!checkLocationOnOff()) {
                        Intent i = new Intent().setClass(SignIn.this, LocationOff.class);
                        startActivity(i);
                    }
                }
            }
        };
        this.registerReceiver(broadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

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
        if (location != null) {
            Global.driverLatitude = "" + location.getLatitude();
            Global.driverLongitude = "" + location.getLongitude();

        }

        Log.d("CHECK_LOGIN", "type =  " + type);

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_driver:
//                        boolean b = Global.DRIVER_TYPE.toString() == 0;
                        type = "0";
                        Log.d("CHECK_LOGIN", "type =  " + type);
                        break;
                    case R.id.rb_agent:
                        type = "1";
                        Log.d("CHECK_LOGIN", "type =  " + type);
                        break;
                }
            }
        });


        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)||
                        preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                    tvwantuser.setVisibility(View.VISIBLE);
                    rgGroup.setVisibility(View.VISIBLE);

                } else {
                    rgGroup.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ConnectClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        ConnectClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        Global.driverLatitude = "" + location.getLatitude();
        Global.driverLongitude = "" + location.getLongitude();
    }

    private void ConnectClient() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Global.driverLatitude = "" + location.getLatitude();
            Global.driverLongitude = "" + location.getLongitude();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
                    if(language_type== ""){
                        String locale;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
                        } else {
                            locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
                        }

//                            Log.d(TAG,"locale :"+ locale );

                        if (locale.equals("sw_") || locale.equals("sw_TZ")) {
//                        language_type="1";

//                                Log.d("check_type","language_type1---  "+language_type );
                            Locale myLocale = new Locale("sw");
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);

                        } else {
//                           language_type="2";
//                        Log.d("check_type","language_type2--  "+language_type );
//                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
                            Locale myLocale = new Locale("en");
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);
                        }

                    }else{
                        String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
                        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type1----"+language_type );
                        String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2,"");
                        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type2----"+language_type2 );
                        if(language_type.equals(language_type2)){
                            Log.d("MYPACKAGE_LOG","language_type_onResume--equal1--"+language_type );
                            if(language_type.equals("1")){
                                Locale myLocale = new Locale("sw");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                            }else if(language_type.equals("2")){
                                Log.d("MYPACKAGE_LOG","language_type--onResume_equals=2----"+language_type );
                                Locale myLocale = new Locale("en");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                            }}else{
                            Log.d("MYPACKAGE_LOG","language_type_on else----"+language_type +language_type2);
                            editor.putString(SharedValues.LANGUAGE_Type2, language_type);
                            editor.commit();
                            Log.d("MYPACKAGE_LOG","editor--commit 1 & 2---"+language_type +language_type2);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);


                        }  }

//                } else {
//                    language_type="2";
//                   if(language_type.equals(null)){
//                       String locale;
//
//                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                           locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                       } else {
//                           locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                       }
//
////                            Log.d(TAG,"locale :"+ locale );
//
//                       if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
////                                Log.d("check_type","language_type1---  "+language_type );
//                           Locale myLocale = new Locale("sw");
//                           Resources res = getResources();
//                           DisplayMetrics dm = res.getDisplayMetrics();
//                           Configuration conf = res.getConfiguration();
//                           conf.locale = myLocale;
//                           res.updateConfiguration(conf, dm);
//
//                       } else {
////                           language_type="2";
////                        Log.d("check_type","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                           Locale myLocale = new Locale("en");
//                           Resources res = getResources();
//                           DisplayMetrics dm = res.getDisplayMetrics();
//                           Configuration conf = res.getConfiguration();
//                           conf.locale = myLocale;
//                           res.updateConfiguration(conf, dm);
//                       }
//
//                   }else{
//                    if(language_type.equals("1")){
//                        Locale myLocale = new Locale("sw");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }else if(language_type.equals("2")){
//                        Locale myLocale = new Locale("en");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }}
//
//
//                }
//
//            }
//        }

        Log.d("CHECK_LOGIN", "coun " + "onresumecall");
        if (!checkLocationOnOff()) {
            Intent i = new Intent().setClass(SignIn.this, LocationOff.class);
            startActivity(i);

//            country_code=CountryCodeCheck.countrycheck(SignIn.this);
        }
        SharedPreferences preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        String selectedcountry = preferences.getString(SharedValues.SELECTED_COUNTRY, "");
        Log.d("CHECK_LOGIN", "coun " + selectedcountry);
        if (selectedcountry != null) {
            if (selectedcountry.trim().length() > 0) {
                if (preferences.getString(SharedValues.URL_API, "").trim().length() > 0) {
                    country_code = preferences.getString(SharedValues.URL_API, "");
                }
//                if (selectedcountry.equals(SharedValues.SELECTED_COUNTRY_UGANDA)) {
//                    countrycode = SharedValues.UGANDA_URL;
//                } else if (selectedcountry.equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//                    countrycode = SharedValues.TANZANIA_URL;
//                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                if (isNetworkAvailable()) {
                    if (!validateEmail()) {
                        return;
                    } else if (!validatePassword()) {
                        return;
                    } else {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (SignIn.this.getCurrentFocus() != null) {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                        String email = etPhone.getText().toString().trim();
                        String password = etPassword.getText().toString();
                        if (FCMToken == null) {
                            FCMToken = FirebaseInstanceId.getInstance().getToken();
                        }
                        if (deviceName == null || deviceName.trim().length() == 0) {
                            try {
                                deviceName = "Device " + Build.BRAND + " Model " + Build.MODEL;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (deviceAPIVersion == null && deviceAPIVersion.length() == 0) {
                            try {
                                deviceName = "Device " + Build.BRAND + " Model " + Build.MODEL;
                                deviceAPIVersion = "SDK " + Build.VERSION.SDK + " VERSION  " + Build.VERSION.RELEASE;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (uuid == null && uuid.length() == 0) {
                            uuid = Settings.Secure.getString(SignIn.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        }
                        if (loginResponseCal != null) {
                            loginResponseCal.cancel();
                        }
                        if (!(SignIn.this.isFinishing())) {
                            if (signInProgress != null && signInProgress.isShowing()) {
                                signInProgress.dismiss();
                            }
                        }
                        if (signInProgress == null) {
                            signInProgress = new ProgressDialog(this);
                        }
                        signInProgress.setCancelable(false);
                        signInProgress.setMessage(getResources().getString(R.string.signindialog) + "...");
                        signInProgress.show();

                        Log.d("CHECK_LOGIN", "USER_AGENT " + Global.USER_AGENT);
                        Log.d("CHECK_LOGIN", "email  " + email);
                        Log.d("CHECK_LOGIN", "etPassword  " + etPassword.getText().toString().trim());
                        Log.d("CHECK_LOGIN", "Global.driverLatitude  " + Global.driverLatitude);
                        Log.d("CHECK_LOGIN", "Global.driverLongitude  " + Global.driverLongitude);
                        Log.d("CHECK_LOGIN", "uuid  " + uuid);
                        Log.d("CHECK_LOGIN", "FCMToken  " + FCMToken);
                        Log.d("CHECK_LOGIN", "deviceName  " + deviceName);
                        Log.d("CHECK_LOGIN", "deviceAPIVersion  " + deviceAPIVersion);
                        Log.d("CHECK_LOGIN", "Global.APK_VERSION  " + Global.APK_VERSION);
                        Log.d("CHECK_LOGIN", "Global.driver_type  " + type);

                        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                        String Accept = Global.ACCEPT;
                        driverId = preferences.getString(SharedValues.DRIVER_ID, "");

                        Log.d("CHECK_LOGIN", "Global.DRIVER_ID  " + driverId);
                        Log.d("CHECK_LOGIN", "Code" + country_code);

                        if (country_code != null && country_code.trim().length() > 0) {
                            //API HIT LOGIN
                            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
                                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {

                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)||
                                            preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                        loginResponseCal = webRequest.apiInterface.driverAgentLogin(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, email, etPassword.getText().toString().trim(), Global.driverLatitude, Global.driverLongitude, uuid, FCMToken, deviceName, deviceAPIVersion, Global.APK_VERSION, type);
                                        getLoginResponse(loginResponseCal);
//                                        Log.d("CHECK_LOGIN", "Global.driver_type  " + type);
                                        Log.d("CHECK_LOGIN", "Global.driver_id  " + driverId);
                                        Log.d("CHECK_LOGIN", "country code  " + country_code);
                                        Log.d("CHECK_LOGIN", "Accept  " + Accept);
                                        Log.d("CHECK_LOGIN", "USER_AGENT " + Global.USER_AGENT);
                                        Log.d("CHECK_LOGIN", "USER_TOKEN " + USER_TOKEN);
                                        Log.d("CHECK_LOGIN", "email  " + email);
                                        Log.d("CHECK_LOGIN", "etPassword  " + etPassword.getText().toString().trim());
                                        Log.d("CHECK_LOGIN", "Global.driverLatitude  " + Global.driverLatitude);
                                        Log.d("CHECK_LOGIN", "Global.driverLongitude  " + Global.driverLongitude);
                                        Log.d("CHECK_LOGIN", "uuid  " + uuid);
                                        Log.d("CHECK_LOGIN", "FCMToken  " + FCMToken);
                                        Log.d("CHECK_LOGIN", "deviceName  " + deviceName);
                                        Log.d("CHECK_LOGIN", "deviceAPIVersion  " + deviceAPIVersion);
                                        Log.d("CHECK_LOGIN", "Global.APK_VERSION  " + Global.APK_VERSION);
                                        Log.d("CHECK_LOGIN", "Global.driver_type  " + type);
                                    } else {
                                        loginResponseCal = webRequest.apiInterface.loginDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, email, etPassword.getText().toString().trim(), Global.driverLatitude, Global.driverLongitude, uuid, FCMToken, deviceName, deviceAPIVersion, Global.APK_VERSION);
                                        getLoginResponse(loginResponseCal);
//                                        Log.d("CHECK_LOGIN", "code1" + country_code);
//                                        Log.d("CHECK_LOGIN", "Global.driver_type no type " + type);
                                    }
                                }
                            }


                        } else {
                            Log.d("CHECK_LOGIN", "code2" + country_code);
                            if (signInProgress != null && signInProgress.isShowing()) {
                                signInProgress.dismiss();

                            }
                        }


                    }
                } else {
                    Snackbar(scrollView);
                    //  Toast.makeText(this, "Internet is not working please check.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.tv_forget_password:
                Intent intent = new Intent().setClass(SignIn.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getLoginResponse(Call<LoginResponse> loginResp) {
        loginResp.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("RESLOG", " this is Response" + response.body());

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getResponse() == 1) {
                        if (!(SignIn.this.isFinishing())) {
                            if (signInProgress != null && signInProgress.isShowing()) {
                                signInProgress.dismiss();
                            }
                        }
                        String password = etPassword.getText().toString().trim();
                        if (password != null && password.length() > 0) {
                            editor.putString(SharedValues.DRIVER_PASSWORD, password);
                            editor.commit();
                        }
                        savedToShared(loginResponse);
                    } else {
                        if (!(SignIn.this.isFinishing())) {
                            if (signInProgress != null && signInProgress.isShowing()) {
                                signInProgress.dismiss();
                            }   //Show Error Message
                        }
                        Toast.makeText(SignIn.this, "" + loginResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("CHECK_LOGIN", "Fail:- " + t);

                if (!(SignIn.this.isFinishing())) {
                    if (signInProgress != null && signInProgress.isShowing()) {
                        signInProgress.dismiss();
                    }
                }
                Toast.makeText(SignIn.this, "Something is wrong please try again.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void savedToShared(LoginResponse loginResponse) {
        Data jsonObject = loginResponse.getData();
        String driverId = jsonObject.getId();
        String driverUniqueId = jsonObject.getDriver_unique_id();
        String driverName = jsonObject.getDriver_name();
        String driverEmail = jsonObject.getDriver_email();
        String vehicleID = jsonObject.getVehicle_id_fk();
        String driverStatus = jsonObject.getDriver_status();
        String driverNotify = jsonObject.getDriver_app_notification();
        String driverImage = jsonObject.getDriver_image();
        String driverPhone = jsonObject.getDriver_phone();
        String vehicleNo = jsonObject.getVehicle();
        String vehicleName = jsonObject.getVechicle_name();
        String vehicleColor = jsonObject.getVechicle_color();
        String latitude = jsonObject.getDriver_lat();
        String longitude = jsonObject.getDriver_lng();
        String code = jsonObject.getCode();
        String accessToken = jsonObject.getAccess_token();
        String driverType= jsonObject.getDriver_type();

        if (driverId != null && driverId.length() > 0) {
            editor.putString(SharedValues.DRIVER_ID, driverId);
        }
        if (driverName != null && driverName.length() > 0) {
            editor.putString(SharedValues.DRIVER_NAME, driverName);
        }
        if (driverEmail != null && driverEmail.length() > 0) {
            editor.putString(SharedValues.DRIVER_EMAIL, driverEmail);
        }
        if (driverPhone != null && driverPhone.length() > 0) {
            editor.putString(SharedValues.DRIVER_PHONE, driverPhone);
        }
        if (driverUniqueId != null && driverUniqueId.length() > 0) {
            editor.putString(SharedValues.DRIVER_UNIQUES_CODE, driverUniqueId);
        }
        if (driverImage != null && driverImage.length() > 0) {
            editor.putString(SharedValues.DRIVER_IMAGE, driverImage);
        }
        if (code != null && code.length() > 0) {
            editor.putString(SharedValues.DRIVER_NUMBER_CODE, code);
        }
        if (accessToken != null && accessToken.length() > 0) {
            editor.putString(SharedValues.DRIVER_AUTH_TOKEN, "Bearer " + accessToken);
        }
        if (driverType != null && driverId.length() > 0) {
            editor.putString(SharedValues.DRIVER_TYPE, driverType);
        }
        editor.commit();

//        Intent service = new Intent(SignIn.this, UpdateDriverLocationServiceNew.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            if(isAppForground(con)==true) {
//                con.startForegroundService(service);
////            }else{
////                startService(service);
////            }
//
//        }else {
//            startService(service);
//        }

        Intent mapIntent = new Intent(SignIn.this, MyPackagesActivity.class);
        startActivity(mapIntent);
        finish();
    }
    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }

    private boolean validateEmail() {
        String email = etPhone.getText().toString().trim();
        if (email.isEmpty()) {
            etPhone.setError(getString(R.string.err_msg_email));
            etPhone.requestFocus();
            return false;
        } else if (!isValidEmail(email)) {
            etPhone.setError(getString(R.string.invalidemail));
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignIn.this, Selected_Country.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty()) {
            Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error_black_24dp);
            customErrorDrawable.setBounds(0, 0, 10, customErrorDrawable.getIntrinsicHeight());
            etPassword.setError(getString(R.string.err_msg_password), customErrorDrawable);
            etPassword.requestFocus();
            return false;
//        } else if (etPassword.getText().toString().trim().length() < 6) {
//            Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error_black_24dp);
//            customErrorDrawable.setBounds(0, 0, 10, customErrorDrawable.getIntrinsicHeight());
//            etPassword.setError(getString(R.string.err_msg_password_min_length), customErrorDrawable);
//            etPassword.requestFocus();
//            return false;
        } else {
            //inputLayoutPaswd.setErrorEnabled(false);
        }
        return true;
    }

    public boolean checkLocationOnOff() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public void Snackbar(final View layout) {
        final Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.NoInternet, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);
        mySnackbar.setAction(getResources().getString(R.string.retrytext), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    mySnackbar.dismiss();
                } else {
                    Snackbar(layout);
                }
            }
        });
        mySnackbar.show();
    }
}
