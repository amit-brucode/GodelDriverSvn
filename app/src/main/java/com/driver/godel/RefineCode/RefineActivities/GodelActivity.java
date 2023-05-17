package com.driver.godel.RefineCode.RefineActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;

/**
 * Created by Ajay2.Sharma on 7/31/2018.
 */

public class GodelActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    WebRequest webRequest;
    String driverId = "", driverName = "", driverUniqueId = "", packageCode = "", packageId = "", driverImage ,driverType="",language_type="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        webRequest = WebRequest.getSingleton(this);
        driverType=preferences.getString(SharedValues.DRIVER_TYPE,"");
        packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
        packageId = preferences.getString(SharedValues.PACKAGE_ID, "");
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        driverName = preferences.getString(SharedValues.DRIVER_NAME, "");

        driverImage = preferences.getString(SharedValues.DRIVER_IMAGE, "");

        driverUniqueId = preferences.getString(SharedValues.DRIVER_UNIQUES_CODE, "");
        language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
