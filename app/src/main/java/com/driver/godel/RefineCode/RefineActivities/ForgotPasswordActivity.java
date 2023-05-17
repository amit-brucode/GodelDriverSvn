package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.response.ForgotPaswdResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends GodelActivity implements View.OnClickListener {
    private EditText etEmail, etverifyCode, etPassword, etConfirmPassword;
    private Button btnReset, btnSubmit, btnSubmit1, btnLogin;
    private TextView tvTitle, tvVerifyText, tvBackLogin, tvForgotPassword;
    private ImageView ivBack;
    private Toolbar toolbar;
    private String email, verifyCode, password;
    String language_type;
    private Call<ForgotPaswdResponse> forgotPaswdResponseCall;
    private ForgotPaswdResponse forgotPaswdResponse;

    private ProgressDialog resetPswdLinkProgress, resetPasswordProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Global global = new Global(ForgotPasswordActivity.this);
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
        setSupportActionBar(toolbar);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        etEmail = (EditText) findViewById(R.id.et_email);
        etverifyCode = (EditText) findViewById(R.id.et_verify_code);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit1 = (Button) findViewById(R.id.btn_submit1);
        tvVerifyText = (TextView) findViewById(R.id.tv_verify_text);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvBackLogin = (TextView) findViewById(R.id.tv_back_login);
        tvForgotPassword = (TextView) findViewById(R.id.tv_already_member);

        //Set Forgot Password Text
        String forgotText = getResources().getString(R.string.forgotyourtext) + " " + "<b>"+getResources().getString(R.string.passwordquestion) +"</b>";
        tvForgotPassword.setText(Html.fromHtml(forgotText));

        //Set Text
        String backLoginText = getResources().getString(R.string.backtextlogin) + " " + "<b>"+getResources().getString(R.string.loginquestion)+"</b>";
        tvBackLogin.setText(Html.fromHtml(backLoginText));

        //Set Font
        etPassword.setTypeface(Typeface.DEFAULT);
        etConfirmPassword.setTypeface(Typeface.DEFAULT);

        //Set Title
        tvTitle.setText(getResources().getString(R.string.forget_password));

        //SetOnClickListener
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnSubmit1.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvBackLogin.setOnClickListener(this);

        etverifyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    hideKeyboard(v);
                }
            }
        });
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
            case R.id.iv_back:

                //Back Press
                onBackPressed();
                break;

            case R.id.btn_reset:

                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError(getResources().getString(R.string.err_msg_email));
                } else if (!isValidEmail(etEmail.getText().toString())) {
                    etEmail.setError(getResources().getString(R.string.invalidemail));
                } else {
                    email = etEmail.getText().toString();
                    btnReset.setEnabled(false);
                    btnReset.setFocusable(false);

                    //API HIT LOGIN
                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                    String Accept = Global.ACCEPT;
                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");

                    String country_code = CountryCodeCheck.countrycheck(ForgotPasswordActivity.this);

                    if (country_code != null && country_code.trim().length() > 0) {
                        forgotPaswdResponseCall = webRequest.apiInterface.resetPasswd(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, email);
                        resetPasswordLink(forgotPaswdResponseCall);
                        hideSoftKeyboard();
                    } else {
                        if (resetPswdLinkProgress != null && resetPswdLinkProgress.isShowing()) {
                            resetPswdLinkProgress.dismiss();
                        }
                        btnReset.setEnabled(true);
                        btnReset.setFocusable(true);
                    }


                    //Hide KeyBoard

                }
                break;

            case R.id.btn_submit:

                if (etverifyCode.getText().toString().isEmpty()) {
                    etverifyCode.setError(getResources().getString(R.string.enterverificationcode));
                } else {
                    verifyCode = etverifyCode.getText().toString();
                    //Show//Hide
                    tvVerifyText.setVisibility(View.GONE);
                    etverifyCode.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);
                    etPassword.setSelection(0);
                    etPassword.setVisibility(View.VISIBLE);
                    etConfirmPassword.setVisibility(View.VISIBLE);
                    etPassword.requestFocus();
                    btnSubmit1.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_submit1:

                if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.enternewpasswordtext));
                } else if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setError(getResources().getString(R.string.enterconfirmpassworderror));
                } else if (!etConfirmPassword.getText().toString().matches(etPassword.getText().toString())) {
                    etConfirmPassword.setError(getResources().getString(R.string.passwordshouldmatcherror));
                } else {
                    password = etPassword.getText().toString();
                    //API HIT LOGIN
                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                    String Accept = Global.ACCEPT;
                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");


                    String country_code = CountryCodeCheck.countrycheck(ForgotPasswordActivity.this);
                    if (country_code != null && country_code.trim().length() > 0) {
                        forgotPaswdResponseCall = webRequest.apiInterface.forgotPasswd(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, password, verifyCode);
                        forgotPasswd(forgotPaswdResponseCall);
                        //Hide KeyBoard
                        hideSoftKeyboard();
                    } else {
                        if (resetPasswordProgress != null && resetPasswordProgress.isShowing()) {
                            resetPasswordProgress.dismiss();
                        }

                    }


                }
                break;

            case R.id.btn_login:
                //Pass Intent to Login Screen
                Intent loginIntent = new Intent(this, SignIn.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
                break;

            case R.id.tv_back_login:
                onBackPressed();
                break;
        }
    }

    //Reset Password Link
    private void resetPasswordLink(Call<ForgotPaswdResponse> resetPasswd) {
        if (!(ForgotPasswordActivity.this.isFinishing())) {
            if (resetPswdLinkProgress != null && resetPswdLinkProgress.isShowing()) {
                resetPswdLinkProgress.dismiss();
            }
        }
        if (resetPswdLinkProgress == null) {
            resetPswdLinkProgress = new ProgressDialog(this);
        }
        resetPswdLinkProgress.setMessage(getResources().getString(R.string.sendingresetlinktextt) + "...");
        resetPswdLinkProgress.setCancelable(false);
        resetPswdLinkProgress.show();

        resetPasswd.enqueue(new Callback<ForgotPaswdResponse>() {
            @Override
            public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
                if (response.isSuccessful()) {
                    forgotPaswdResponse = response.body();

                    if (forgotPaswdResponse.getResponse() == 1) {
                        //Show Alert Builder
                        showBuilder(getResources().getString(R.string.changepasswordemailsentemail));

                    } else if (forgotPaswdResponse.getResponse() == 0) {
                        Toast.makeText(ForgotPasswordActivity.this, "" + forgotPaswdResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //showToastMessage(ForgotPasswordActivity.this,"Please Try Again");
                    }
                }
                if (!(ForgotPasswordActivity.this.isFinishing())) {
                    if (resetPswdLinkProgress != null && resetPswdLinkProgress.isShowing()) {
                        resetPswdLinkProgress.dismiss();
                    }
                }
                btnReset.setEnabled(true);
                btnReset.setFocusable(true);
            }

            @Override
            public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                if (!(ForgotPasswordActivity.this.isFinishing())) {
                    if (resetPswdLinkProgress != null && resetPswdLinkProgress.isShowing()) {
                        resetPswdLinkProgress.dismiss();
                    }
                }
                t.printStackTrace();
                btnReset.setEnabled(true);
                btnReset.setFocusable(true);
            }
        });
    }

    //Forgot Password
    private void forgotPasswd(Call<ForgotPaswdResponse> forgotPasswd) {
        if (!(ForgotPasswordActivity.this.isFinishing())) {
            if (resetPasswordProgress != null && resetPasswordProgress.isShowing()) {
                resetPasswordProgress.dismiss();
            }
        }
        if (resetPasswordProgress == null) {
            resetPasswordProgress = new ProgressDialog(this);
        }
        resetPasswordProgress.setMessage(getResources().getString(R.string.resetpasswordtext) + "...");
        resetPasswordProgress.setCancelable(false);
        resetPasswordProgress.show();

        forgotPasswd.enqueue(new Callback<ForgotPaswdResponse>() {
            @Override
            public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
                if (response.isSuccessful()) {
                    forgotPaswdResponse = response.body();
                    tvVerifyText.setText(getResources().getString(R.string.passwordchangedlogin));
                    tvVerifyText.setTextColor(Color.BLACK);
                    tvVerifyText.setVisibility(View.VISIBLE);
                    //Show Login Button
                    etPassword.setVisibility(View.GONE);
                    etConfirmPassword.setVisibility(View.GONE);
                    btnSubmit1.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);

                    if (forgotPaswdResponse.getResponse() == 1) {
                        Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.passwordresetsuccessfully), Toast.LENGTH_SHORT).show();
                    }
                }
                if (!(ForgotPasswordActivity.this.isFinishing())) {
                    if (resetPasswordProgress != null && resetPasswordProgress.isShowing()) {
                        resetPasswordProgress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                if (!(ForgotPasswordActivity.this.isFinishing())) {
                    if (resetPasswordProgress != null && resetPasswordProgress.isShowing()) {
                        resetPasswordProgress.dismiss();
                    }
                }
                t.printStackTrace();
            }
        });
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //Alert Dialog
    public void showBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
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
