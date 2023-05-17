package com.driver.godel.RefineCode.RefineFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;


/**
 * Created by Ajay2.Sharma on 7/13/2018.
 */

public class GodelDriverFragment extends Fragment {
    WebRequest webRequest;
    String driverId;
    String packageCode ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        webRequest = WebRequest.getSingleton(getActivity());
        preferences = getActivity().getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
