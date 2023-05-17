package com.driver.godel.RefineCode.RefineActivities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;


/**
 * Created by Ajay2.Sharma on 27-Feb-18.
 */

public class CheckPermissions extends GodelActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int REQUEST_CODE_PERMISSION_BAACKGROUND = 3;

    private String[] storagePermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.POST_NOTIFICATIONS
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    private int count = 0;
    private boolean isSendToSettings = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpermissions);
        if (ActivityCompat.checkSelfPermission(this, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[4]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[5]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[6]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[7]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[8]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, storagePermissions[9]) != PackageManager.PERMISSION_GRANTED
                ) {
            checkPermissions();
        } else {
            startActivity();
        }
    }

    private void checkPermissions() {
        if (count <= 2) {
            try {
                if (ActivityCompat.checkSelfPermission(this, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[2]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[3]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[4]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[5]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[6]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[7]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[8]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, storagePermissions[9]) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_CODE_PERMISSION);

                    count++;

                } else {
                    startActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {

            startActivity();
        }
//        else {
//
//            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//            alert.setCancelable(false);
//            alert.setMessage(getResources().getString(R.string.passwordrequiredfurther));
//            alert.setPositiveButton(getResources().getString(R.string.allowpemissionstext), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    isSendToSettings = true;
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivityForResult(intent, 101);
//
//                }
//            });
//            alert.setNegativeButton(getResources().getString(R.string.exitext), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    isSendToSettings = false;
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
//                    startActivity(intent);
//                    finish();
//                    System.exit(0);
//                }
//            });
//            alert.show();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //If permission is granted
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[5] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[6] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[7] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[8] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else if (grantResults[9] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    return;
                } else {
                    startActivity();
                }
            } else {
                checkPermissions();
            }
        }
        else if(requestCode == REQUEST_CODE_PERMISSION_BAACKGROUND) {
            startActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSendToSettings) {
            if (ActivityCompat.checkSelfPermission(this, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[2]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[3]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[4]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[5]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[6]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[7]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[8]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, storagePermissions[9]) != PackageManager.PERMISSION_GRANTED
                    ) {
                checkPermissions();
            } else {
                startActivity();
            }
            isSendToSettings = false;
        }
    }

    private void startActivity() {
        if (checkLocationOnOff()) {

            String id = preferences.getString(SharedValues.DRIVER_ID, "");
            if (id.trim().length() == 0 || id.trim().equalsIgnoreCase("")) {
                Intent intent = new Intent().setClass(CheckPermissions.this, SignIn.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent().setClass(CheckPermissions.this, MyPackagesActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent i = new Intent().setClass(CheckPermissions.this, LocationOff.class);
            startActivity(i);
            finish();
        }
    }

    public boolean checkLocationOnOff() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
