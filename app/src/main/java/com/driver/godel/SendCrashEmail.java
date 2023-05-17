package com.driver.godel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineActivities.GodelActivity;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.response.CrashReport.CrashEmailResponse;
import com.google.android.material.snackbar.Snackbar;


import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Ajay2.Sharma on 24-Oct-17.
 */

public class SendCrashEmail extends GodelActivity {
    String crashReason = "undefined error type";
    String snackbarMessage = "";
    private ConstraintLayout clParent;
    boolean isEmailSend = false;
    ProgressDialog progressDialog;
    WebRequest webRequest;
    SharedPreferences prefs;
    String driverId;
    SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_email);
        prefs = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);

//        prefs = getSharedPreferences(Activity_UserSessionManager.PREFER_NAME, MODE_PRIVATE);
        driverId = prefs.getString(SharedValues.DRIVER_ID, null);

        webRequest = WebRequest.getSingleton(this);
        clParent = (ConstraintLayout) findViewById(R.id.cl_parent);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crashReason = extras.getString("error");
        }

        Log.d("crashReason ",""+crashReason);
        sendEmail(crashReason + " driverId: " + driverId);
    }

    private void sendEmail(String reason) {
        if (isNetworkAvailable()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Sending issue report to developers...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;


    String country_code=CountryCodeCheck.countrycheck(SendCrashEmail.this);
                if(country_code!=null&&country_code.trim().length()>0){
                    Call<CrashEmailResponse> callEmail = webRequest.apiInterface.SendCrashEmail(country_code,Global.USER_AGENT,Accept,USER_TOKEN, driverId,"DRIVER APP CRASH " + reason);
                    getSignupResponse(callEmail);
                        }else{
                        if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        }
                        }


        } else {
            snackbarMessage = "No internet connection";
            Snackbar(clParent);
        }
    }

    private void getSignupResponse(Call<CrashEmailResponse> profileCall) {
        profileCall.enqueue(new Callback<CrashEmailResponse>() {
            @Override
            public void onResponse(Call<CrashEmailResponse> call, retrofit2.Response<CrashEmailResponse> response) {
                CrashEmailResponse crashResponse = response.body();
                if (crashResponse != null) {
                    if (crashResponse.getResponse() == 1) {
                        String msg = "";
                        msg = crashResponse.getData().getMessage();
                        isEmailSend = true;
                        Toast.makeText(SendCrashEmail.this, "" + msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        snackbarMessage = "Failed to send email";
                        Snackbar(clParent);
                    }
                } else {
                    onBackPressed();
                }
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CrashEmailResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    snackbarMessage = "Connection timeout";
                    Snackbar(clParent);
                }
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
            }
        });
    }

    public void Snackbar(final View layout) {
        Snackbar mySnackbar = Snackbar.make(layout, snackbarMessage, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);
        mySnackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    sendEmail(crashReason);
                } else {
                    Snackbar(layout);
                }
            }
        });
        mySnackbar.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(" There was an error processing the request. Please try again.");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onQuitPressed();
            }
        });

       /* builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean is_available = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                if (is_available) {
                Intent intent = new Intent(SendCrashEmail.this, MyPackagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SendCrashEmail.this);
//                    builder.setCancelable(false);
//                    builder.setMessage("Location enabled required ");
//                    builder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent openLocationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(openLocationSettings);
//                        }
//                    });
//                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            onQuitPressed();
//                        }
//                    });
//                    builder.show();
//                }
            }
        });*/
        builder.show();
    }

    public void onQuitPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEmailSend == true) {
            onBackPressed();
        }
    }

}