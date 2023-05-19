package com.driver.godel;

/**
 * Created by Simarjot.Singh on 02-Jun-16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler extends Throwable implements Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final String LINE_SEPARATOR = "\n";
    SharedPreferences preferences;

    public ExceptionHandler(Context context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        preferences = myContext.getSharedPreferences(SharedValues.PREF_NAME, myContext.MODE_PRIVATE);
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());
        String error = errorReport.toString();

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Country: ");
        errorReport.append(preferences.getString(SharedValues.SELECTED_COUNTRY, ""));
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);

        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(" app version: " + Global.APK_VERSION);
        errorReport.append(" app version code : " + Global.VersionCode);
        Intent intent = new Intent(myContext, SendCrashEmail.class);
        intent.putExtra("error", errorReport.toString());
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(-1);
    }
}