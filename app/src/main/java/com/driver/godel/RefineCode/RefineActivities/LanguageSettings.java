package com.driver.godel.RefineCode.RefineActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;

import java.util.Locale;

public class LanguageSettings extends AppCompatActivity {
    private Toolbar toolbar;
    Context context;
    TextView tvTitle;
    ImageView ivBack;
    RadioGroup rg_language_type;
    RadioButton ll_swahili,ll_english;
    SharedPreferences preferences;
//    RadioButton ll_default;
    SharedPreferences.Editor editor;
//    BroadcastReceiver receiver;
    private static boolean is_Active = false;
    String type;
    public static String notificationContent, notificationMessage;
    public static boolean isServiceError = false;
    String language_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);
//        editor = preferences.edit();
        context = LanguageSettings.this;
        //Initialization
        ivBack = (ImageView) findViewById(R.id.iv_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
//        ll_default = (RadioButton) findViewById(R.id.ll_default);
        ll_swahili = (RadioButton) findViewById(R.id.ll_swahili);
        ll_english = (RadioButton) findViewById(R.id.ll_english);
        rg_language_type = (RadioGroup) findViewById(R.id.rg_language_type);


        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();

        setSupportActionBar(toolbar);
        tvTitle.setText(getResources().getString(R.string.languageSettingstext));

        ivBack.setVisibility(View.VISIBLE);
        //Set On Click Listener
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
        Log.d("Check_logsaaa","language_type-----"+language_type);
        if(language_type==null){
//        String locale1;
//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            locale1 = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
////        } else {
////            locale1 = String.valueOf(Resources.getSystem().getConfiguration().locale);
////        }
//        if (locale1.equals("hi_") || locale1.equals("hi_IN")) {
//            ll_swahili.setChecked(true);
//            Locale locale = new Locale("sw");
//            Locale.setDefault(locale);
//
//            Resources resources = context.getResources();
//
//            Configuration configuration = resources.getConfiguration();
//            configuration.locale = locale;
//
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        } else {
//            ll_english.setChecked(true);
//            Locale locale = new Locale("en");
//            Locale.setDefault(locale);
//
//            Resources resources = context.getResources();
//
//            Configuration configuration = resources.getConfiguration();
//            configuration.locale = locale;
//
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        }
    }else {

        if(language_type.equals("1")){
            ll_swahili.setChecked(true);
//            Log.d("Mypackage_log","language_type--in language_setting  "+language_type );
//            Locale locale = new Locale("sw");
//            Locale.setDefault(locale);
//
//            Resources resources = context.getResources();
//
//            Configuration configuration = resources.getConfiguration();
//            configuration.locale = locale;
//
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        if(language_type.equals("2")){
            ll_english.setChecked(true);
//            Log.d("Mypackage_log","language_type--in language_setting  "+language_type );
//            Locale locale = new Locale("en");
//            Locale.setDefault(locale);
//
//            Resources resources = context.getResources();
//
//            Configuration configuration = resources.getConfiguration();
//            configuration.locale = locale;
//
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }}

        rg_language_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.ll_swahili) {
                    ll_swahili.setChecked(true);
                    Log.d("Mypackage_log","language_type--in language_setting click_Listener "+language_type );
                    language_type="1";
                    editor.putString(SharedValues.LANGUAGE_SETTINGS,language_type);
                    editor.commit();



                    Locale locale = new Locale("sw");
                Locale.setDefault(locale);

                Resources resources = context.getResources();

                Configuration configuration = resources.getConfiguration();
                configuration.locale = locale;

                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//                LocalBroadcastManager.getInstance(context).registerReceiver(serviceErrorBroadcaste, new IntentFilter(Global.SERVICE_ERROR_BROADCAST));

//                editor.putString(SharedValues.LANGUAGE_SWAHILI,"sw");
                Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                }


                if (checkedId == R.id.ll_english) {
                    ll_english.setChecked(true);
                    Log.d("Mypackage_log","language_type--in language_setting click_Listener  "+language_type );
                    language_type="2";
                    editor.putString(SharedValues.LANGUAGE_SETTINGS,language_type);
                    editor.commit();


                        Locale locale = new Locale("en");
                        Locale.setDefault(locale);

                        Resources resources = context.getResources();

                        Configuration configuration = resources.getConfiguration();
                        configuration.locale = locale;

                        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

//                    editor.putString(SharedValues.LANGUAGE_SETTINGS, type);
//                    editor.commit();
//                    LocalBroadcastManager.getInstance(context).registerReceiver(serviceErrorBroadcaste, new IntentFilter(Global.SERVICE_ERROR_BROADCAST));

//                editor.putString(SharedValues.LANGUAGE_SWAHILI,"sw");
                        Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();


//                    editor.putString(SharedValues.LANGUAGE_SETTINGS, type);
//                    editor.commit();
//                LocalBroadcastManager.getInstance(context).registerReceiver(serviceErrorBroadcaste, new IntentFilter(Global.SERVICE_ERROR_BROADCAST));
                }
            }
        });


//        if(preferences.getString(SharedValues.LANGUAGE_SETTINGS,"")==null){
//            String locale;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//            }else{
//                locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//            }
//
////            Log.d(TAG,"locale :"+ locale );
//
//            if (locale.equals("sw_") || locale.equals("sw_TZ")){
//
////                Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
//                Locale myLocale = new Locale("sw");
//                Resources res = getResources();
//                DisplayMetrics dm = res.getDisplayMetrics();
//                Configuration conf = res.getConfiguration();
//                conf.locale = myLocale;
//                res.updateConfiguration(conf, dm);
//                ll_swahili.setChecked(true);
//
//            }else{
//
////                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                Locale myLocale = new Locale("en");
//                Resources res = getResources();
//                DisplayMetrics dm = res.getDisplayMetrics();
//                Configuration conf = res.getConfiguration();
//                conf.locale = myLocale;
//                res.updateConfiguration(conf, dm);
//                ll_english.setChecked(true);
//            }
//
//        }else{ if (preferences.getString(SharedValues.LANGUAGE_SETTINGS, "").equals("1")) {
//                ll_swahili.setChecked(true);
//            }
//            if (preferences.getString(SharedValues.LANGUAGE_SETTINGS, "").equals("2")) {
//                ll_english.setChecked(true);
//            }
//        }

//            public void onClick(View view) {
////                editor.putString(SharedValues.LANGUAGE_SWAHILI,"Swahili");
////                editor.commit();
////                Locale locale = new Locale("sw");
////                Locale.setDefault(locale);
////                Configuration config = new Configuration();
////                config.locale = locale;
////                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//

//                Locale locale = new Locale("sw");
//                Locale.setDefault(locale);
//
//                Resources resources = context.getResources();
//
//                Configuration configuration = resources.getConfiguration();
//                configuration.locale = locale;
//
//                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
////                LocalBroadcastManager.getInstance(context).registerReceiver(serviceErrorBroadcaste, new IntentFilter(Global.SERVICE_ERROR_BROADCAST));
//
////                editor.putString(SharedValues.LANGUAGE_SWAHILI,"sw");
//                Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
////                editor.commit();
//
//            }
//        });
//        ll_default.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                editor.putString(SharedValues.LANGUAGE_DEFAULT,"Default");
////                editor.commit();
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//
//                    String locale;
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                    }else{
//                        locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                    }
//
////                    Log.d(TAG,"locale :"+ locale );
//
//                    if (locale.equals("sw_") || locale.equals("sw_TZ")){
//
////                        Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
//                        Locale myLocale = new Locale("sw");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                        editor.putString(SharedValues.LANGUAGE_SWAHILI,"sw");
//                        editor.commit();
//                        Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);
//
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//
//                    }else{
//
////                        Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                        Locale myLocale = new Locale("en");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                        editor.putString(SharedValues.LANGUAGE_ENGLISH,"en");
//                        editor.commit();
//
//                        Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);
//
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//
//
////
//
//                } else {
////                    Log.d(TAG, "locale uganda :");
//                    Locale myLocale = new Locale("en");
//                    Resources res = getResources();
//                    DisplayMetrics dm = res.getDisplayMetrics();
//                    Configuration conf = res.getConfiguration();
//                    conf.locale = myLocale;
//                    res.updateConfiguration(conf, dm);
//                    editor.putString(SharedValues.LANGUAGE_ENGLISH,"en");
//                    editor.commit();
//                    Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        });
//        ll_english.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                editor.putString(SharedValues.LANGUAGE_ENGLISH,"English");
////                editor.commit();
////                Locale locale = new Locale("en");
////                Locale.setDefault(locale);
////                Configuration config = new Configuration();
////                config.locale = locale;
////                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
////
//                Locale locale = new Locale("en");
//                Locale.setDefault(locale);
//
//                Resources resources = context.getResources();
//
//                Configuration configuration = resources.getConfiguration();
//                configuration.locale = locale;
//
//                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//                editor.putString(SharedValues.LANGUAGE_ENGLISH,"en");
//                editor.commit();
//                Intent intent = new Intent(LanguageSettings.this, MyPackagesActivity.class);
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//
//            }
//        });
    }

//    private BroadcastReceiver serviceErrorBroadcaste = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (is_Active == true && notificationContent != null && notificationMessage != null) {
//                showErrorDialog();
//            }
//        }
//    };
//    private void showErrorDialog() {
//        isServiceError = false;
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setMessage(notificationMessage);
//        alert.setTitle(notificationContent);
//        alert.setCancelable(false);
//        alert.setPositiveButton(getResources().getString(R.string.closeapptext), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAndRemoveTask();
//                } else {
//                    finishAffinity();
//                    System.exit(0);
//                }
//            }
//        });
//        alert.show();
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
////        LanguageSettings.this.unregisterReceiver(receiver);
//        LocalBroadcastManager.getInstance(context).unregisterReceiver(serviceErrorBroadcaste);
//    }
   @Override
    protected void onResume() {
        super.onResume();
//        language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
       String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
       String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2,"");
       if(language_type.equals(language_type2)){
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
           }}else{ editor.putString(SharedValues.LANGUAGE_Type2, language_type);
           editor.commit();
           Intent intent = getIntent();
           finish();
           startActivity(intent);


       }
//        if(language_type.equals("1")){
//                    Locale myLocale = new Locale("sw");
//                    Resources res = getResources();
//                    DisplayMetrics dm = res.getDisplayMetrics();
//                    Configuration conf = res.getConfiguration();
//                    conf.locale = myLocale;
//                    res.updateConfiguration(conf, dm);
//                }else if(language_type.equals("2")){
//                    Locale myLocale = new Locale("en");
//                    Resources res = getResources();
//                    DisplayMetrics dm = res.getDisplayMetrics();
//                    Configuration conf = res.getConfiguration();
//                    conf.locale = myLocale;
//                    res.updateConfiguration(conf, dm);
//                }
    }
}