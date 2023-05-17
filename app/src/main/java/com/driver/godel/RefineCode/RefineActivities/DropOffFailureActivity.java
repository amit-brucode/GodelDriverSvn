package com.driver.godel.RefineCode.RefineActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.DropOffFailureResponse.MainRes;
import com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus;
import com.driver.godel.response.setActivePackage.ActivePackageResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DropOffFailureActivity extends GodelActivity implements View.OnClickListener {
    private EditText etFailureReason;
    private ImageView ivFailureImage, ivBack;
    private Button captureImageBtn, btnSubmit;
    private final int REQUEST_CAMERA = 1888;
    private String imageBase64 = "";
    private TextView tvTitle;
    String language_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_off_failure_new);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

            if(language_type.equals("1")&&language_type!=null){
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }else if(language_type.equals("2")&&language_type!=null){
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

        }

        Global global = new Global(DropOffFailureActivity.this);
        global.setCurrencySymbol();

        //Initialization
        etFailureReason = (EditText) findViewById(R.id.et_failure_reason);
        ivFailureImage = (ImageView) findViewById(R.id.iv_failure_image);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        captureImageBtn = (Button) findViewById(R.id.btn_capture_image);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.dropofffailuretext));
        btnSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        captureImageBtn.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
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
//                    else {
//                        String locale;
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                        } else {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                        }
//
////                            Log.d(TAG,"locale :"+ locale );
//
//                        if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
////                                Log.d("check_type","language_type1---  "+language_type );
//                            Locale myLocale = new Locale("sw");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//
//                        } else {
////                           language_type="2";
////                        Log.d("check_type","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                            Locale myLocale = new Locale("en");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }
//
//                        Log.d("check_type", "language_typelllll--  " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                    }
//
//                } else {
//                    if(language_type.equals("1")&&language_type!=null){
//                        Locale myLocale = new Locale("sw");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }else if(language_type.equals("2")&&language_type!=null){
//                        Locale myLocale = new Locale("en");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }else {
//                        String locale;
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                        } else {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                        }
//
////                            Log.d(TAG,"locale :"+ locale );
//
//                        if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
////                                Log.d("check_type","language_type1---  "+language_type );
//                            Locale myLocale = new Locale("sw");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//
//                        } else {
////                           language_type="2";
////                        Log.d("check_type","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                            Locale myLocale = new Locale("en");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }
//
//                        Log.d("check_type", "language_typelllll--  " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                    }
//                }
//
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (etFailureReason == null || etFailureReason.getText().toString().trim().length() == 0) {
                    etFailureReason.setError(getResources().getString(R.string.reasonrequirederror));
                } else if (imageBase64.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.wouldyouliketoproceed));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isNetworkAvailable()) {
                                dropoffFailure(etFailureReason.getText().toString(), imageBase64);

                            } else {
                                Snackbar(ivBack);
                            }

                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.notext), null);
                    builder.show();
                } else {
                    if (isNetworkAvailable()) {
                        dropoffFailure(etFailureReason.getText().toString(), imageBase64);
                    } else {
                        Snackbar(ivBack);
                    }

                }
                break;

            case R.id.btn_capture_image:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Update Bitmap to Base64
                generateBase64(bm);
            }
        }
    }

    private void generateBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        if (imageBase64 != null && imageBase64.length() > 0) {
            ivFailureImage.setVisibility(View.VISIBLE);
            ivFailureImage.setImageBitmap(bitmap);
        }
    }

    ProgressDialog dropoffFailureProgress;

    private void dropoffFailure(String comment, String encodedImage) {
        if (!(DropOffFailureActivity.this.isFinishing())) {
            if (dropoffFailureProgress != null && dropoffFailureProgress.isShowing()) {
                dropoffFailureProgress.dismiss();
            }
        }
        if (dropoffFailureProgress == null) {
            dropoffFailureProgress = new ProgressDialog(DropOffFailureActivity.this);
        }
        dropoffFailureProgress.setMessage(getResources().getString(R.string.proceesingdialog) + "...");
        dropoffFailureProgress.setCancelable(false);
        dropoffFailureProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(DropOffFailureActivity.this.isFinishing())) {
                if (dropoffFailureProgress != null && dropoffFailureProgress.isShowing()) {
                    dropoffFailureProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(DropOffFailureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {


                Call<MainRes> dropOffFailure = webRequest.apiInterface.packageFailure(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, comment, encodedImage, driverId);
                dropOffFailure.enqueue(new Callback<MainRes>() {
                    @Override
                    public void onResponse(Call<MainRes> call, Response<MainRes> response) {
                        if (response.body() != null) {
                            MainRes failureResponse = response.body();
                            if (failureResponse.getResponse().equalsIgnoreCase("1")) {
                                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(DropOffFailureActivity.this);
                                confirmationDialog.setCancelable(false);
                                confirmationDialog.setMessage(getResources().getString(R.string.wouldyouliketoreplace));
                                confirmationDialog.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                passIntent();

                                        setActivePackageData(driverId, packageCode, Global.driverLatitude, Global.driverLongitude, "1");
                                        // setActivePackage(driverId, packageCode, Global.driverLatitude, Global.driverLongitude, "1");
//                                setReassignPackage(driverId, packageCode, Global.driverLatitude, Global.driverLongitude, "1");

                                    }
                                });
                                confirmationDialog.setNegativeButton(getResources().getString(R.string.replacetext), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.remove(SharedValues.PACKAGE_CODE);
                                        editor.remove(SharedValues.PACKAGE_ID);
                                        editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                                        editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                                        editor.commit();

                                        Intent intent = new Intent(getApplicationContext(), MyPackagesActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("TabPosition", "1");
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                            } else if(failureResponse.getData().getMessage()!=null){
                                if (failureResponse.getData().getMessage().trim().equals("Unauthenticated")) {
                                    Global.signInIntent(DropOffFailureActivity.this);
                                }
                            } else {
                                Toast.makeText(DropOffFailureActivity.this, "" + failureResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DropOffFailureActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (dropoffFailureProgress != null && dropoffFailureProgress.isShowing()) {
                                dropoffFailureProgress.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainRes> call, Throwable t) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (dropoffFailureProgress != null && dropoffFailureProgress.isShowing()) {
                                dropoffFailureProgress.dismiss();
                            }
                        }
                        t.printStackTrace();
                        Toast.makeText(DropOffFailureActivity.this, "fail to connect ", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                if (dropoffFailureProgress != null && dropoffFailureProgress.isShowing()) {
                    dropoffFailureProgress.dismiss();
                }
            }
        }

    }

    private void setActivePackageData(final String driverId, final String packageCode, final String currentLat, final String currentLng, final String type) {
        if (activePackageCall != null) {
            activePackageCall.cancel();
        }
        if (!(DropOffFailureActivity.this.isFinishing())) {
            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                ActivePckgProgress.dismiss();
            }
        }
        if (ActivePckgProgress == null) {
            ActivePckgProgress = new ProgressDialog(this);
        }
        ActivePckgProgress.setCancelable(false);
        ActivePckgProgress.setMessage(getResources().getString(R.string.loadingtext) + "...");
        ActivePckgProgress.show();

        final String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        final String Accept = Global.ACCEPT;
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(DropOffFailureActivity.this.isFinishing())) {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {
            String country_code = CountryCodeCheck.countrycheck(DropOffFailureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                String FCM_token = preferences.getString(SharedValues.FCM_TOKEN, "0");
                Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> updatePackageStatusCall = webRequest.apiInterface.updateFailurePackageStatus(country_code, Global.USER_AGENT, Global.ACCEPT, USER_TOKEN, driverId,
                        packageId, driverId, "5", FCM_token, "0", " ");
                updatePackageStatusCall.enqueue(new Callback<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus>() {
                    @Override
                    public void onResponse(Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> call, Response<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> response) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                ActivePckgProgress.dismiss();
                            }
                        }
                        if (response.body() != null) {
                            com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus updatePackageStatus = response.body();
                            if (updatePackageStatus.getResponse() == 1) {


                                String country_code = CountryCodeCheck.countrycheck(DropOffFailureActivity.this);
                                if (country_code != null && country_code.trim().length() > 0) {


                                    activePackageCall = webRequest.apiInterface.setActivePackage(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, packageCode, currentLat, currentLng, type);
                                    activePackageCall.enqueue(new Callback<ActivePackageResponse>() {
                                        @Override
                                        public void onResponse(Call<ActivePackageResponse> call, Response<ActivePackageResponse> response) {
                                            if (!(DropOffFailureActivity.this.isFinishing())) {
                                                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                                    ActivePckgProgress.dismiss();
                                                }
                                            }

                                            if (response.body() != null) {
                                                ActivePackageResponse activePackageResponse = response.body();
                                                //Check response
                                                if (activePackageResponse.getResponse() == 1) {
                                                    passIntent();
                                                }else if(activePackageResponse.getData().getMessage()!=null){
                                                    if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                                        Global.signInIntent(DropOffFailureActivity.this);
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(DropOffFailureActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ActivePackageResponse> call, Throwable t) {
                                            if (!(DropOffFailureActivity.this.isFinishing())) {
                                                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                                    ActivePckgProgress.dismiss();
                                                }
                                            }
                                        }
                                    });


                                } else {
                                    if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                        ActivePckgProgress.dismiss();
                                    }
                                }
                            } else if(updatePackageStatus.getData().getMessage()!=null){
                                if (updatePackageStatus.getData().getMessage().trim().equals("Unauthenticated")) {
                                    Global.signInIntent(DropOffFailureActivity.this);
                                }
                            } else {
                                Toast.makeText(DropOffFailureActivity.this, "" + updatePackageStatus.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DropOffFailureActivity.this, "null response", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> call, Throwable t) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                ActivePckgProgress.dismiss();
                            }
                        }
                        t.printStackTrace();
                    }
                });


            } else {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }

        }

    }

    ProgressDialog ActivePckgProgress;
    Call<ActivePackageResponse> activePackageCall;
    Call<com.driver.godel.response.ownResign.MainRes> reassignPackageCall;

    private void setReassignPackage(String driverId, String packageCode, String currentLat, String currentLng, String type) {
        if (reassignPackageCall != null) {
            reassignPackageCall.cancel();
        }
        if (!(DropOffFailureActivity.this.isFinishing())) {
            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                ActivePckgProgress.dismiss();
            }
        }
        if (ActivePckgProgress == null) {
            ActivePckgProgress = new ProgressDialog(this);
        }
        ActivePckgProgress.setCancelable(false);
        ActivePckgProgress.setMessage(getResources().getString(R.string.loadingtext) + "...");
        ActivePckgProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                ActivePckgProgress.dismiss();
            }
            Global.signInIntent(this);
        } else {


            String country_code = CountryCodeCheck.countrycheck(DropOffFailureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                reassignPackageCall = webRequest.apiInterface.ownResign(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, currentLat, currentLng, type);


                reassignPackageCall.enqueue(new Callback<com.driver.godel.response.ownResign.MainRes>() {
                    @Override
                    public void onResponse(Call<com.driver.godel.response.ownResign.MainRes> call, Response<com.driver.godel.response.ownResign.MainRes> response) {
                        if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                            ActivePckgProgress.dismiss();
                        }
                        if (response.body() != null) {
                            com.driver.godel.response.ownResign.MainRes activePackageResponse = response.body();
                            //Check response
                            if (activePackageResponse.getResponse().equalsIgnoreCase("1")) {
                                passIntent();
                            }else if(activePackageResponse.getData().getMessage()!=null){
                                if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(DropOffFailureActivity.this);
                                }
                            }
                            else {
                                Toast.makeText(DropOffFailureActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.driver.godel.response.ownResign.MainRes> call, Throwable t) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                ActivePckgProgress.dismiss();
                            }
                        }
                    }
                });
                ;
            } else {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }
        }
    }


    private void setActivePackage(String driverId, String packageCode, String currentLat, String currentLng, String type) {
        if (activePackageCall != null) {
            activePackageCall.cancel();
        }
        if (!(DropOffFailureActivity.this.isFinishing())) {
            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                ActivePckgProgress.dismiss();
            }
        }
        if (ActivePckgProgress == null) {
            ActivePckgProgress = new ProgressDialog(this);
        }
        ActivePckgProgress.setCancelable(false);
        ActivePckgProgress.setMessage(getResources().getString(R.string.loadingtext) + "...");
        ActivePckgProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(DropOffFailureActivity.this.isFinishing())) {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(DropOffFailureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                activePackageCall = webRequest.apiInterface.setActivePackage(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, packageCode, currentLat, currentLng, type);
                activePackageCall.enqueue(new Callback<ActivePackageResponse>() {
                    @Override
                    public void onResponse(Call<ActivePackageResponse> call, Response<ActivePackageResponse> response) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                ActivePckgProgress.dismiss();
                            }
                        }

                        if (response.body() != null) {
                            ActivePackageResponse activePackageResponse = response.body();
                            //Check response
                            if (activePackageResponse.getResponse() == 1) {
                                passIntent();
                            } else if(activePackageResponse.getData().getMessage()!=null){
                                if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(DropOffFailureActivity.this);
                                }
                            }
                             else {
                                Toast.makeText(DropOffFailureActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ActivePackageResponse> call, Throwable t) {
                        if (!(DropOffFailureActivity.this.isFinishing())) {
                            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                                ActivePckgProgress.dismiss();
                            }
                        }
                    }
                });

            } else {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }
        }

    }


    private void passIntent() {
        //Pass an Intent
        Intent intent = new Intent(getApplicationContext(), MyPackagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void Snackbar(final View layout) {
        final Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.NoInternet, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);
        mySnackbar.setAction(getResources().getString(R.string.retrytext), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    mySnackbar.dismiss();
                } else {
                    Snackbar(layout);
                }
            }
        });
        mySnackbar.show();
    }
}

