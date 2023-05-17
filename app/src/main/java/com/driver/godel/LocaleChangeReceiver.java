package com.driver.godel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;

import java.util.Locale;

/**
 * Created by Kawaldeep.Singh on 4/30/2019.
 */

public class LocaleChangeReceiver extends BroadcastReceiver {
SharedPreferences preferences;
SharedPreferences.Editor editor;
String type;
String TAG="Check_locale";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Localetttt", "Changed:" + String.valueOf(Resources.getSystem().getConfiguration().locale));
        preferences = context.getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        String locale;
//        Language = Locale.getDefault().getDisplayLanguage().toString();
//        Locale current = getResources().getConfiguration().locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
        } else {
            locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
        }
//        Configuration config = context.getResources().getConfiguration();
        Log.d(TAG, "locale :" + locale);

        if (locale.equals("sw_") || locale.equals("sw_TZ")) {
            type = "1";
//

            Log.d(TAG, "selected_languuage  " + "set_here_sw  " + locale);
            Locale myLocale = new Locale("sw");
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else {
            type = "2";

            Log.d(TAG, "selected_languuage tanzania " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
            Log.d(TAG, "selected_languuagehgiugtygu" + preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS, ""));
//                                    editor.commit();

            Log.d(TAG, "selected_languuage" + "set_here_english  " + locale);
            Locale myLocale = new Locale("en");
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        editor.putString(SharedValues.LANGUAGE_SETTINGS, type);
        editor.commit();
        Log.d("MYPACKAGE_LOG", "-----locale change----" + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));

//
//        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);



//        context.startActivity(i);
//        context.sendBroadcast(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
//        context.startActivity(context.getIntent());
//        context.finish();


    }

}
