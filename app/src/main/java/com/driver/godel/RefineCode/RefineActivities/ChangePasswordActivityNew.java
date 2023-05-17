package com.driver.godel.RefineCode.RefineActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.response.changePasswordResp.ChangePasswordResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivityNew extends GodelActivity implements View.OnClickListener {
    private CardView cvOldPassword, cvChangePassword;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnVerify, btnUpdatePassword;
    private TextView tvTextLabel;

    //Toolbar
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ivBack;

    //Session Manage
    private String driverId, password;

    //Web Services
    private Call<ChangePasswordResponse> changePasswordResponseCall;
    private ChangePasswordResponse passwordResponse;
    private ProgressDialog ChangePswdProgress;
    String language_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Global global = new Global(ChangePasswordActivityNew.this);
        global.setCurrencySymbol();
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
        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvOldPassword = (CardView) findViewById(R.id.cv_old_password);
        etOldPassword = (EditText) findViewById(R.id.et_old_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        cvChangePassword = (CardView) findViewById(R.id.cv_new_confirm_psswd);
        btnVerify = (Button) findViewById(R.id.btn_verify);
        btnUpdatePassword = (Button) findViewById(R.id.btn_update_password);
        tvTextLabel = (TextView) findViewById(R.id.tv_text_label);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        setSupportActionBar(toolbar);
        tvTitle.setText(getResources().getString(R.string.changepasswordsmall));

        //Show/Hide Visibility
        ivBack.setVisibility(View.VISIBLE);

        //Set On Click Listener
        ivBack.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        btnUpdatePassword.setOnClickListener(this);

        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        password = preferences.getString(SharedValues.DRIVER_PASSWORD, "");
        //Get Driver Details

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //Back Press
                onBackPressed();
                break;

            case R.id.btn_verify:
                String oldPassword = etOldPassword.getText().toString();
                if (oldPassword.isEmpty()) {
                    etOldPassword.setError(getResources().getString(R.string.oldpasswordrequirerror));
                    etOldPassword.requestFocus();
                } else if (etOldPassword.getText().toString().trim().length() < 6) {
                    etOldPassword.setError(getResources().getString(R.string.mincharactersrequired));
                    etOldPassword.requestFocus();
                } else if (!password.matches(oldPassword)) {
                    etOldPassword.setError(getResources().getString(R.string.wrongpassworderror));
                    etOldPassword.requestFocus();
                } else {
                    //Hide Visibilities
                    cvOldPassword.setVisibility(View.GONE);
                    btnVerify.setVisibility(View.GONE);
                    //Change Text
                    tvTextLabel.setText(getResources().getString(R.string.enternewpasswordtext));
                    //Show Visibilities
                    cvChangePassword.setVisibility(View.VISIBLE);
                    btnUpdatePassword.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_update_password:
                password = preferences.getString(SharedValues.DRIVER_PASSWORD, "");
                if (etNewPassword.getText().toString().isEmpty()) {
                    etNewPassword.setError(getResources().getString(R.string.newpassworderror));
                    etNewPassword.requestFocus();
                } else if (etNewPassword.getText().toString().length() < 6) {
                    etNewPassword.setError(getResources().getString(R.string.mincharactersrequired));
                    etNewPassword.requestFocus();
                } else if (password.equalsIgnoreCase(etNewPassword.getText().toString())) {
                    etNewPassword.setError(getResources().getString(R.string.newpassworddifferent));
                    etNewPassword.requestFocus();
                } else if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setError(getResources().getString(R.string.confirmpasswordrequirerror));
                    etConfirmPassword.requestFocus();
                } else if (etConfirmPassword.getText().toString().length() < 6) {
                    etConfirmPassword.setError(getResources().getString(R.string.mincharactersrequired));
                    etConfirmPassword.requestFocus();
                } else if (!etConfirmPassword.getText().toString().matches(etNewPassword.getText().toString())) {
                    etConfirmPassword.setError(getResources().getString(R.string.confirmpasswordincorrecterror));
                    etConfirmPassword.requestFocus();
                } else {
                    String password = etConfirmPassword.getText().toString();
                    //Api Hit CHange Password
                    changePasswordApi(password);
                }
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");


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
//
//                }
//
//            }
//        }
    }

    //Change Passwor API HIT
    private void changePasswordApi(final String password) {
        if (changePasswordResponseCall != null) {
            changePasswordResponseCall.cancel();
        }
        if (ChangePswdProgress != null && ChangePswdProgress.isShowing()) {
            ChangePswdProgress.dismiss();
        }
        if (ChangePswdProgress == null) {
            ChangePswdProgress = new ProgressDialog(this);
        }
        ChangePswdProgress.setMessage(getResources().getString(R.string.resettingpassworddialog)+"...");
        ChangePswdProgress.setCancelable(false);
        ChangePswdProgress.show();


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;



    String country_code=CountryCodeCheck.countrycheck(ChangePasswordActivityNew.this);
                if(country_code!=null&&country_code.trim().length()>0){


        changePasswordResponseCall = webRequest.apiInterface.changePasswordApi(country_code,Global.USER_AGENT, Accept, USER_TOKEN,driverId, driverId, password);
        changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    passwordResponse = response.body();
                    if (passwordResponse.getResponse() == 1) {
                        if (password != null && password.length() > 0) {
                            editor.putString(SharedValues.DRIVER_PASSWORD, password);
                        }
                        showBuilder(passwordResponse.getData().getMessage());
                    }else if(passwordResponse.getData().getMessage()!=null){
                        if (passwordResponse.getData().getMessage().equals("Unauthenticated")) {
                            Global.signInIntent(ChangePasswordActivityNew.this);
                        }
                    }
                    else {
                        Toast.makeText(ChangePasswordActivityNew.this, "" + passwordResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                      if(!(ChangePasswordActivityNew.this.isFinishing())) {
                          if (ChangePswdProgress != null && ChangePswdProgress.isShowing()) {
                              ChangePswdProgress.dismiss();
                          }
                      }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                if(!(ChangePasswordActivityNew.this.isFinishing())) {
                    if (ChangePswdProgress != null && ChangePswdProgress.isShowing()) {
                        ChangePswdProgress.dismiss();
                    }
                }
                t.printStackTrace();
            }
        });


                }else{
                    if (ChangePswdProgress != null && ChangePswdProgress.isShowing()) {
                        ChangePswdProgress.dismiss();
                    }
                }
    }

    //Alert Dialog
    public void showBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.passwordchangeddialog));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Back to Login
                onBackPressed();
            }
        });
        builder.show();
    }
}
