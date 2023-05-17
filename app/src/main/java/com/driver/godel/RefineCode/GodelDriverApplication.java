package com.driver.godel.RefineCode;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
//import android.support.v7.app.AppCompatDelegate;
//
//import com.crashlytics.android.Crashlytics;
import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
//import io.fabric.sdk.android.Fabric;

/*import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;*/

/**
 * Created by QSYS\simarjot.singh on 11/5/17.
 */

public class GodelDriverApplication extends MultiDexApplication
{
    private Tracker mTracker;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    synchronized public Tracker getDefaultTracker()
    {
        if (mTracker == null)
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(this);
//        FirebaseCrashlytics.getInstance().log("Owner not set correctly. Transaction not saved");
        FirebaseCrashlytics.getInstance().recordException(new ExceptionHandler(this));
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

       /* if (LeakCanary.isInAnalyzerProcess(this))
        {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }
}
