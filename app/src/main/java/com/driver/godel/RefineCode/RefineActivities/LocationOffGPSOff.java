package com.driver.godel.RefineCode.RefineActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;


public class LocationOffGPSOff extends GodelActivity {


    Button btnTurnOn, btnExit;
    TextView tvLocationOff, tvLocationText;
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_off);
        tvLocationOff = (TextView) findViewById(R.id.tv_location_off);
        tvLocationText = (TextView) findViewById(R.id.tv_location_text);
        btnTurnOn = (Button) findViewById(R.id.btn_turn_on);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSIntent();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    if (checkLocationOnOff()) {
                        finish();
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {

            }
        }
    }

    private void GPSIntent() {
        Intent openLocationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(openLocationSettings);
        finish();
    }

    public void onQuitPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    @Override
    public void onBackPressed() {
        if(checkLocationOnOff()) {
            finish();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(LocationOffGPSOff.this);
            dialog.setCancelable(false);
            dialog.setMessage("Really want to exit app");
            dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
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

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        if (checkLocationOnOff()) {
            String id = preferences.getString(SharedValues.DRIVER_ID, "noId");
            if (id.trim().equals("noId")) {
                Intent intent = new Intent(LocationOffGPSOff.this, SignIn.class);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        }
    }

    public boolean checkLocationOnOff() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
