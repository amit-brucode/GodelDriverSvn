package com.driver.godel.RefineCode.RefineActivities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.Service.ForegroundService;
import com.driver.godel.response.GetFareResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends GodelActivity {
    private int version;
    private Float finalVersion;
    Context con;
    final String TAG = "SPLASH_LOG";
    String language_type;
//    private final WeakReference<Splash> mainActivityWeakRef;
//
//    public Splash(WeakReference<Splash> mainActivityWeakRef) {
//        this.mainActivityWeakRef = mainActivityWeakRef;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        con = Splash.this;


        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Godel Driver ");
        ContextCompat.startForegroundService(this, serviceIntent);


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        Log.d(TAG,"TOKEN="+USER_TOKEN);
        String Accept = Global.ACCEPT;
        String mUserId = preferences.getString(SharedValues.DRIVER_ID, "");
        Intent intent1 = getIntent();
        String code = "";
        code = intent1.getStringExtra("CODES");

        if (mUserId != null) {
            if (mUserId.trim().length() > 0) {

                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
                        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
                             language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
                            if(language_type==""){
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
                                }  }
                            oncreatecall();
                        }
                        else  if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)) {
                             language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
                            if(language_type==""){
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
                                }  }
                            oncreatecall();
                        } else {
                            language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

                            if(language_type==null){
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
                             }  }
                            oncreatecall();
                        }
                    } else {
                        Global.logoutuser(Splash.this);
//                        Intent intent = new Intent(Splash.this, Selected_Country.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    }
                } else {
                    Global.logoutuser(Splash.this);
//                    Intent intent = new Intent(Splash.this, Selected_Country.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }

            } else {
                if (code != null) {
                    Log.d(TAG, "intent" + "null");
                    if (code.trim().equals("1")) {

//
//                        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//                            Locale myLocale = new Locale("sw");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }

                        oncreatecall();
                    } else {
//                        Global.logoutuser(Splash.this);
                        Intent intent = new Intent(Splash.this, Selected_Country.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                } else {
//                    oncreatecall();
//                    Global.logoutuser(Splash.this);
                    Intent intent = new Intent(Splash.this, Selected_Country.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }


    }


    @Override
    protected void onDestroy() {

        con=Splash.this;
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
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


        }
    }

    public void oncreatecall() {

        Global global = new Global(Splash.this);
        global.setCurrencySymbol();

//        Intent intent = new Intent(Splash.this, UpdateDriverLocationServiceNew.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            con.startForegroundService(intent);
//        }else {
//            startService(intent);
//        }

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
            finalVersion = Float.parseFloat(String.valueOf(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (isNetworkAvailable()) {
            GlobalDataAPIHit();
        } else {
            Intent();
        }
    }


    public void GlobalDataAPIHit() {

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        //Hit API GET Fare


        String country_code = CountryCodeCheck.countrycheck(Splash.this);
        Log.d(TAG, "Code" + country_code);
        if (country_code != null && country_code.trim().length() > 0) {
            Call<GetFareResponse> getFareResponseCall = webRequest.apiInterface.getFare(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId);
            getFareEstimate(getFareResponseCall);
        }


    }
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
    //Get Fare Estimate
    private void getFareEstimate(final Call<GetFareResponse> getFareResponseCall) {
        getFareResponseCall.enqueue(new Callback<GetFareResponse>() {
            @Override
            public void onResponse(Call<GetFareResponse> call, Response<GetFareResponse> response) {
                //godelDriverActivity.dismissProgressDialog();
                if (response.isSuccessful()) {
                    GetFareResponse getFareResponse = response.body();
                    Log.d(TAG,"===============response.bodrrkf0----------"+response.body());

                    if (getFareResponse.getResponse() == 1) {

                        float latestAppVersion = Float.parseFloat(getFareResponse.getData().getAndroid_driver_app_version());



                        Log.e("latestAppVersion", String.valueOf(latestAppVersion));


                        String displayPackageLength = getFareResponse.getData().getDisplay_package_length();
                        String displayPackageWidth = getFareResponse.getData().getDisplay_package_width();
                        String displayPackageDepth = getFareResponse.getData().getDisplay_package_depth();
                        String displayPackageWeight = getFareResponse.getData().getDisplay_package_weight();
                        String displayPackageQuantity = getFareResponse.getData().getDisplay_package_quantity();
                        String displayPackageValue = getFareResponse.getData().getDisplay_package_value();
                        String displayPackageType = getFareResponse.getData().getDisplay_package_type();
                        String displayBriefDescriptionOfGoods = getFareResponse.getData().getDisplay_brief_description_of_goods();
                        String displayVat = getFareResponse.getData().getDisplay_VAT();
                        String displayUcc = getFareResponse.getData().getDisplay_UCC();
                        String displayInsurance = getFareResponse.getData().getDisplay_insurance();
                        String displayLoader= getFareResponse.getData().getDisplay_loader();
                        String displayInsuranceCover= getFareResponse.getData().getDisplay_insurance_cover_price();

                        editor=preferences.edit();
                        if(!(displayPackageLength==null)||displayPackageLength.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_LENGTH,""+displayPackageLength.trim());
                            Log.d("Add_package_displaytag","displayPackageLength-----splash----"+displayPackageLength);
                        }

                        if(!(displayPackageWidth==null)||displayPackageWidth.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_WIDTH,""+displayPackageWidth.trim());
                            Log.d("Add_package_displaytag","displayPackageWidth----splash-----"+displayPackageWidth);
                        }

                        if(!(displayPackageDepth==null)||displayPackageDepth.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_DEPTH,""+displayPackageDepth.trim());
                            Log.d("Add_package_displaytag","displayPackageDepth-----splash----"+displayPackageDepth);
                        }

                        if(!(displayPackageWeight==null)||displayPackageWeight.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_WEIGHT,""+displayPackageWeight.trim());
                            Log.d("Add_package_displaytag","displayPackageWeight----splash-----"+displayPackageWeight);
                        }


                        if(!(displayPackageQuantity==null)||displayPackageQuantity.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_QUANTITY,""+displayPackageQuantity.trim());
                            Log.d("Add_package_displaytag","displayPackageQuantity---splash------"+displayPackageQuantity);
                        }

                        if(!(displayPackageValue==null)||displayPackageValue.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_VALUE,""+displayPackageValue.trim());
                            Log.d("Add_package_displaytag","displayPackageValue-----splash----"+displayPackageValue);
                        }

                        if(!(displayPackageType==null)||displayPackageType.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_TYPE,""+displayPackageType.trim());
                            Log.d("Add_package_displaytag","displayPackageType----splash-----"+displayPackageType);
                        }

                        if(!(displayBriefDescriptionOfGoods==null)||displayBriefDescriptionOfGoods.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_BRIEF_DESCRIPTION_OF_GOODS,""+displayBriefDescriptionOfGoods.trim());
                            Log.d("Add_package_displaytag","displayBriefDescriptionOfGoods---splash------"+displayBriefDescriptionOfGoods);
                        }

                        if(!(displayVat==null)||displayVat.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_VAT,""+displayVat.trim());
                            Log.d("Add_package_displaytag","displayVat----splash-----"+displayVat);
                        }

                        if(!(displayUcc==null)||displayUcc.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_UCC,""+displayUcc.trim());
                            Log.d("Add_package_displaytag","displayUcc----splash-----"+displayUcc);
                        }

                        if(!(displayInsurance==null)||displayInsurance.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_INSURANCE,""+displayInsurance.trim());
                            Log.d("Add_package_displaytag","displayInsurance-----splash----"+displayInsurance);
                        }

                        if(!(displayLoader==null)||displayLoader.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_LOADER,""+displayLoader.trim());
                            Log.d("Add_package_displaytag","displayLoader-----splash----"+displayLoader);
                        }

                        if(!(displayInsuranceCover==null)||displayInsuranceCover.length()<=0){
                            editor.putString(Global.DISPLAY_PACKAGE_INSURANCE_COVER,""+displayInsuranceCover.trim());
                            Log.d("Add_package_displaytag","displayInsuranceCover-----splash----"+displayInsuranceCover);
                        }
                        editor.commit();


                        if (finalVersion < latestAppVersion) {
//                            con=null;
//                            con=Splash.this;
                            if (con!= null && isRunning(Splash.this) ) {
                                Log.d(TAG, "-----try------in Splash dialog----mainActivityWeakRef---------" );
                            AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                            builder.setCancelable(false);
                            builder.setTitle("Update available");
                            builder.setMessage("A new updated version is available for Godel Driver app. Would you like to update ?");
                            builder.setCancelable(false);
                            //ok Button
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        Log.d(TAG, "-----try------in Splash dialog-------------" );
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.driver.godel")));
                                    } catch (android.content.ActivityNotFoundException anfe) {
//                                        Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getMessage() );
//                                        Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getLocalizedMessage() );
//                                        Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getCause() );
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id==com.driver.godel")));
                                    }
                                    /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.driver.godel"));
                                    startActivity(intent);*/
                                    finish();
                                }
                            });
                          /*  //Cancel Button
                            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent();
                                    finish();
                                }
                            });*/

                            builder.show();
                        }else{
                                Log.d(TAG, "-----try----not--in Splash dialog----mainActivityWeakRef---------" );
                                con = Splash.this;

                                try {
                                    Log.d(TAG, "-----try------in Splash dialog-------------" );
                                    GlobalDataAPIHit();
                                } catch (android.content.ActivityNotFoundException anfe) {
//                                    Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getMessage() );
//                                    Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getLocalizedMessage() );
//                                    Log.d(TAG, "----catch-------in Splash dialog-------------" +anfe.getCause() );
                                    }
                            }
                        } else {
                            Intent();
                            finish();
                        }

                    } else {
                        Intent();
                        finish();
                    }
                } else {
                    Intent();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetFareResponse> call, Throwable t) {
                Log.d("SPlash_Log", "fail " + t);
                Intent();
                finish();
                t.printStackTrace();
            }
        });
    }



    private void Intent() {
        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
//            editor.clear().commit();
            editor.remove(SharedValues.PREF_NAME);
            editor.remove(SharedValues.PACKAGE_CODE);
            editor.remove(SharedValues.PACKAGE_ID);
            editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
            editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
            editor.remove(SharedValues.FCM_TOKEN);
            editor.remove(SharedValues.DRIVER_ID);
            editor.remove(SharedValues.DRIVER_UNIQUES_CODE);
            editor.remove(SharedValues.DRIVER_NAME);
            editor.remove(SharedValues.DRIVER_EMAIL);
            editor.remove(SharedValues.DRIVER_PHONE);
            editor.remove(SharedValues.DRIVER_IMAGE);
            editor.remove(SharedValues.DRIVER_NUMBER_CODE);
            editor.remove(SharedValues.DRIVER_PASSWORD);
            editor.remove(SharedValues.KEY_DRIVER_LOCATION_INTERVAL);
            editor.remove(SharedValues.DRIVER_STATUS);
            editor.remove(SharedValues.DRIVER_AUTH_TOKEN);


        }


        Intent i = new Intent().setClass(Splash.this, CheckPermissions.class);
        startActivity(i);
        finish();
    }
}
