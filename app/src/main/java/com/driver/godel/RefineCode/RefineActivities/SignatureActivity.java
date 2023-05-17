package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.packageDropWarehouse.PackageDropWareHouseResponse;
import com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus;

import com.driver.godel.response.BookingPaymentStatusRes.MainRes;
import com.driver.godel.response.MarkroundTrip.MarkroundTripResponse;
import com.driver.godel.response.fareApiResponse.FareResponse;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignatureActivity extends GodelActivity implements View.OnClickListener {
    private SignaturePad mSignaturePad;
    private Button btnClear, btnSave;
    private EditText etName;
    private String isWarehouseDropoff,is_round_trip,is_first_round_completed,is_drop_off_complete;
    private ImageView ivBack;
    private Call<PackageDropWareHouseResponse> dropWareHouseResponseCall;
    private ProgressDialog dropWarehouseProgress, updatePackageStatusProgress;
    private TextView tvTitle;
    private Boolean isSigned = false;
    private ProgressDialog DeliveryProgress, FairProgress, paymentProgress;
    private ImageView ivDelName;
    String language_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_new);
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

        Global global = new Global(SignatureActivity.this);
        global.setCurrencySymbol();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isWarehouseDropoff = bundle.getString("IsWareHouseDropoff");
            is_drop_off_complete = bundle.getString("is_drop_off_complete");
            is_first_round_completed = bundle.getString("is_first_round_completed");
            is_round_trip = bundle.getString("is_round_trip");
        }

        Log.d("Tagg","--------isWarehouseDropoff---------"+isWarehouseDropoff);
        Log.d("Tagg","--------is_drop_off_complete---------"+is_drop_off_complete);
        Log.d("Tagg","--------is_first_round_completed---------"+is_first_round_completed);
        Log.d("Tagg","--------is_round_trip---------"+is_round_trip);


        btnClear = (Button) findViewById(R.id.clear_button);
        btnSave = (Button) findViewById(R.id.save_button);
        etName = (EditText) findViewById(R.id.et_name);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivDelName = (ImageView) findViewById(R.id.iv_del_name);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched

                hideKeyboard();
                isSigned = true;
                btnClear.setVisibility(VISIBLE);
                btnSave.setVisibility(VISIBLE);
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed


                isSigned = true;
                btnClear.setVisibility(VISIBLE);
                btnSave.setVisibility(VISIBLE);
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                isSigned = false;
                btnClear.setVisibility(GONE);
                btnSave.setVisibility(GONE);
            }
        });

        btnClear.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivDelName.setOnClickListener(this);

        ivDelName.setVisibility(GONE);

        tvTitle.setText(getResources().getString(R.string.signaturetitle));
        etName.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName != null && etName.getText().toString().length() > 0) {
                    ivDelName.setVisibility(VISIBLE);
                } else {
                    ivDelName.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnClear.setVisibility(GONE);
        btnSave.setVisibility(GONE);

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
            case R.id.clear_button:
                mSignaturePad.clear();
                break;

            case R.id.save_button:
                Bitmap myBitmap = mSignaturePad.getSignatureBitmap();
                int count = 0;

                int[] pixels = new int[myBitmap.getWidth() * myBitmap.getHeight()];
                myBitmap.getPixels(pixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
                for (int i = 0; i < myBitmap.getWidth() * myBitmap.getHeight(); i++) {
                    if (pixels[i] == Color.BLACK) {
                        count++;
                    }

                }

                if (count > 600) {
                    Log.d("Signature_Log", "count=" + count);
                    if (etName.getText().toString().isEmpty()) {
                        etName.setError("Name required");
                    } else if (isSigned == false) {
                        Toast.makeText(this, "Signature required", Toast.LENGTH_LONG).show();
                    } else {
                        //Get Bitmap
                        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                        btnSave.setEnabled(false);
                        btnSave.setFocusable(false);
                        btnClear.setEnabled(false);
                        btnClear.setFocusable(false);
                        //Convert to Base64

                        if (isNetworkAvailable()) {
                            Bitmap mBitmap = addBorderToBitmap(signatureBitmap, 2, Color.BLACK);

                            if (mBitmap != null) {
//                            updateBase64(signatureBitmap);
                                updateBase64(mBitmap);
                            }
                        } else {
                            btnSave.setEnabled(true);
                            btnSave.setFocusable(true);
                            btnClear.setEnabled(true);
                            btnClear.setFocusable(true);
                            Snackbar(ivBack);
                        }


                    }
                } else {
                    Toast.makeText(this, "Please Sign it Properly", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_del_name:
                etName.setText("");
                ivDelName.setVisibility(GONE);
                break;
        }
    }

    public void updateBase64(Bitmap bitmap) {
        //Bitmap bitmap = Bitmap.createBitmap(bm);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String signatureImg = Base64.encodeToString(byteArray, Base64.DEFAULT);

        if (isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("false")) {

            Log.d("Tag","===========isWarehouseDropoff========111111===update package=="+isWarehouseDropoff);

//            if(is_drop_off_complete.equals("1")){
//                updatePackageAPI(signatureImg, Global.STATUS_DROP_COMPLETE);
//            }else{
                if(is_round_trip.equals("1")) {

                    if(is_first_round_completed.equals("0")) {
                        updatePackageAPIForFirstRound(signatureImg, Global.STATUS_DROP_COMPLETE);
                    }else {
                        updatePackageAPI(signatureImg, Global.STATUS_DROP_COMPLETE);
                    }
                }else{
                    updatePackageAPI(signatureImg, Global.STATUS_DROP_COMPLETE);
                }
//            }

        } else {

            Log.d("Tag","=============isWarehouseDropoff===2222222===drop warehouse====="+isWarehouseDropoff);
            packageDropWareHouse(signatureImg);


        }
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void packageDropWareHouse(String base64Image) {
        if (dropWareHouseResponseCall != null) {
            dropWareHouseResponseCall.cancel();
        }
        if (!(SignatureActivity.this.isFinishing())) {
            if (dropWarehouseProgress != null && dropWarehouseProgress.isShowing()) {
                dropWarehouseProgress.dismiss();
            }
        }
        if (dropWarehouseProgress == null) {
            dropWarehouseProgress = new ProgressDialog(this);
        }
        dropWarehouseProgress.setMessage("progress...");
        dropWarehouseProgress.setCancelable(false);
        dropWarehouseProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length(
        ) <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

            if (!(SignatureActivity.this.isFinishing())) {
                if (dropWarehouseProgress != null && dropWarehouseProgress.isShowing()) {
                    dropWarehouseProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(SignatureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                dropWareHouseResponseCall = webRequest.apiInterface.packageDropWarahouse(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, driverId, base64Image);
                dropWareHouseResponseCall.enqueue(new Callback<PackageDropWareHouseResponse>() {
                    @Override
                    public void onResponse(Call<PackageDropWareHouseResponse> call, Response<PackageDropWareHouseResponse> response) {
                        if (response.isSuccessful()) {
                            PackageDropWareHouseResponse dropWareHouseResponse = response.body();

                            if (dropWareHouseResponse.getResponse().equalsIgnoreCase("1")) {
                                Toast.makeText(SignatureActivity.this, "Package Drop to WareHouse", Toast.LENGTH_SHORT).show();
                                //Remove Package Code
                                editor.remove(SharedValues.PACKAGE_CODE);
                                editor.remove(SharedValues.PACKAGE_ID);
                                editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                                editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                                editor.commit();
                                //Pass Intent to HomeScreen
                                Intent intent = new Intent(SignatureActivity.this, MyPackagesActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else if (dropWareHouseResponse.getData().getMessage() != null) {
                                if (dropWareHouseResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(SignatureActivity.this);
                                }
                            } else {
                                Toast.makeText(SignatureActivity.this, "" + dropWareHouseResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            btnSave.setEnabled(true);
                            btnSave.setFocusable(true);
                            btnClear.setEnabled(true);
                            btnClear.setFocusable(true);
                        }

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (dropWarehouseProgress != null && dropWarehouseProgress.isShowing()) {
                                dropWarehouseProgress.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PackageDropWareHouseResponse> call, Throwable t) {
                        if (!(SignatureActivity.this.isFinishing())) {
                            if (dropWarehouseProgress != null && dropWarehouseProgress.isShowing()) {
                                dropWarehouseProgress.dismiss();
                            }
                        }
                        t.printStackTrace();

                        if (!(call.isCanceled())) {
                            btnSave.setEnabled(true);
                            btnSave.setFocusable(true);
                            btnClear.setEnabled(true);
                            btnClear.setFocusable(true);
                        }

                    }
                });

            } else {
                if (!(SignatureActivity.this.isFinishing())) {
                    if (dropWarehouseProgress != null && dropWarehouseProgress.isShowing()) {
                        dropWarehouseProgress.dismiss();
                    }
                }
            }

        }

    }

    Call<MarkroundTripResponse> updatePackageStatusCall2;

    private void updatePackageAPIForFirstRound(String base64Image, final String packageStatus) {
        if (updatePackageStatusCall2 != null) {
            updatePackageStatusCall2.cancel();
        }
        if (!(SignatureActivity.this.isFinishing())) {
            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                DeliveryProgress.dismiss();
            }
        }


        if (DeliveryProgress == null) {
            DeliveryProgress = new ProgressDialog(this);
        }
        DeliveryProgress.setCancelable(false);
        DeliveryProgress.setMessage("processing...");
        DeliveryProgress.show();
        String uuid = Settings.Secure.getString(SignatureActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

            if (!(SignatureActivity.this.isFinishing())) {
                if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                    DeliveryProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {


            String country_code = CountryCodeCheck.countrycheck(SignatureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                updatePackageStatusCall2 = webRequest.apiInterface.updatePackageDeliveredForFirstRound(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, driverId, base64Image, etName.getText().toString());
                updatePackageStatusCall2.enqueue(new Callback<MarkroundTripResponse>() {
                    @Override
                    public void onResponse(Call<MarkroundTripResponse> call, Response<MarkroundTripResponse> response) {

                        if (response.isSuccessful()) {
                            MarkroundTripResponse updatePackageStatus = response.body();
                            //Show Package Response
//                            if (updatePackageStatus.getResponse() == 1) {
                                //Check Package Status to Show Total Fare

                            Toast.makeText(SignatureActivity.this, "Return Trip Started..", Toast.LENGTH_LONG).show();
                                Log.d("Tagg","----updatePackageStatus-----"+updatePackageStatus);
                                Log.d("Tagg","----updatePackageStatus.getResponse()-----"+updatePackageStatus.getResponse());
                            MyPackagesActivity.isNotificationAvailable = 1;
                            Intent OngoingBroadcast = new Intent(Global.ONGOING_BROADCAST);
                            LocalBroadcastManager.getInstance(SignatureActivity.this).sendBroadcast(OngoingBroadcast);
                            Intent i = new Intent().setClass(SignatureActivity.this, MyPackagesActivity.class);

                            i.putExtra("is_alternate_pickupAndDropoff_locations","1");
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();

//                                if (packageStatus.equalsIgnoreCase("7")) {
//                                    //API HIT of Get Fare
//                                    getTotalFare();
//                                } else if (packageStatus.equalsIgnoreCase("10")) {
//                                    editor.remove(SharedValues.PACKAGE_CODE);
//                                    editor.remove(SharedValues.PACKAGE_ID);
//                                    editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
//                                    editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
//                                    editor.commit();
//                                    Intent intent = new Intent(SignatureActivity.this, MyPackagesActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            } else if (updatePackageStatus.getData().getMessage() != null) {
//                                if (updatePackageStatus.getData().getMessage().equals("Unauthenticated")) {
//                                    Global.signInIntent(SignatureActivity.this);
//                                }
//                            }
                        } else {
                            Toast.makeText(SignatureActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                                DeliveryProgress.dismiss();
                            }
                        }

                        btnSave.setEnabled(true);
                        btnSave.setFocusable(true);
                        btnClear.setEnabled(true);
                        btnClear.setFocusable(true);
                    }

                    @Override
                    public void onFailure(Call<MarkroundTripResponse> call, Throwable t) {

                        Log.d("Tagg","----------t----------"+t);
                        if (!(SignatureActivity.this.isFinishing())) {
                            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                                DeliveryProgress.dismiss();
                            }
                        }
                        if (!(call.isCanceled())) {
                            btnSave.setEnabled(true);
                            btnSave.setFocusable(true);
                            btnClear.setEnabled(true);
                            btnClear.setFocusable(true);
                        }
                    }
                });

            } else {
                if (!(SignatureActivity.this.isFinishing())) {
                    if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                        DeliveryProgress.dismiss();
                    }
                }
            }
        }

    }
    Call<UpdatePackageStatus> updatePackageStatusCall;
    private void updatePackageAPI(String base64Image, final String packageStatus) {
        if (updatePackageStatusCall != null) {
            updatePackageStatusCall.cancel();
        }
        if (!(SignatureActivity.this.isFinishing())) {
            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                DeliveryProgress.dismiss();
            }
        }


        if (DeliveryProgress == null) {
            DeliveryProgress = new ProgressDialog(this);
        }
        DeliveryProgress.setCancelable(false);
        DeliveryProgress.setMessage("processing...");
        DeliveryProgress.show();
        String uuid = Settings.Secure.getString(SignatureActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

            if (!(SignatureActivity.this.isFinishing())) {
                if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                    DeliveryProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {


            String country_code = CountryCodeCheck.countrycheck(SignatureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                updatePackageStatusCall = webRequest.apiInterface.updatePackageDelivered(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageId, driverId, packageStatus, uuid, "0", base64Image, etName.getText().toString());
                updatePackageStatusCall.enqueue(new Callback<UpdatePackageStatus>() {
                    @Override
                    public void onResponse(Call<UpdatePackageStatus> call, Response<UpdatePackageStatus> response) {

                        if (response.isSuccessful()) {
                            UpdatePackageStatus updatePackageStatus = response.body();
                            //Show Package Response
                            if (updatePackageStatus.getResponse() == 1) {
                                //Check Package Status to Show Total Fare
                                if (packageStatus.equalsIgnoreCase("7")) {
                                    //API HIT of Get Fare
                                    getTotalFare();
                                } else if (packageStatus.equalsIgnoreCase("10")) {
                                    editor.remove(SharedValues.PACKAGE_CODE);
                                    editor.remove(SharedValues.PACKAGE_ID);
                                    editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                                    editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                                    editor.commit();
                                    Intent intent = new Intent(SignatureActivity.this, MyPackagesActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if (updatePackageStatus.getData().getMessage() != null) {
                                if (updatePackageStatus.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(SignatureActivity.this);
                                }
                            }
                        } else {
                            Toast.makeText(SignatureActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                                DeliveryProgress.dismiss();
                            }
                        }

                        btnSave.setEnabled(true);
                        btnSave.setFocusable(true);
                        btnClear.setEnabled(true);
                        btnClear.setFocusable(true);
                    }

                    @Override
                    public void onFailure(Call<UpdatePackageStatus> call, Throwable t) {

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                                DeliveryProgress.dismiss();
                            }
                        }
                        if (!(call.isCanceled())) {
                            btnSave.setEnabled(true);
                            btnSave.setFocusable(true);
                            btnClear.setEnabled(true);
                            btnClear.setFocusable(true);
                        }
                    }
                });

            } else {
                if (!(SignatureActivity.this.isFinishing())) {
                    if (DeliveryProgress != null && DeliveryProgress.isShowing()) {
                        DeliveryProgress.dismiss();
                    }
                }
            }
        }

    }

    Call<FareResponse> fareResponseCall;

    private void getTotalFare() {

        if (fareResponseCall != null) {
            fareResponseCall.cancel();
        }

        if (!(SignatureActivity.this.isFinishing())) {
            if (FairProgress != null && FairProgress.isShowing()) {
                FairProgress.dismiss();
            }
        }else{
            FairProgress = new ProgressDialog(this);
            FairProgress.setCancelable(false);
            FairProgress.setMessage("processing...");
            FairProgress.show();

        }
        if (FairProgress == null) {
            FairProgress = new ProgressDialog(this);
            FairProgress.setCancelable(false);
            FairProgress.setMessage("processing...");
            FairProgress.show();
        }
//        FairProgress.setCancelable(false);
//        FairProgress.setMessage("processing...");
//        FairProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

            if (!(SignatureActivity.this.isFinishing())) {
                if (FairProgress != null && FairProgress.isShowing()) {
                    FairProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(SignatureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                fareResponseCall = webRequest.apiInterface.getFinalFare(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode);
                fareResponseCall.enqueue(new Callback<FareResponse>() {
                    @Override
                    public void onResponse(Call<FareResponse> call, Response<FareResponse> response) {
                        if (response.body() != null) {
                            FareResponse fareResponse = response.body();
                            //Check Response
                            if (fareResponse.getResponse() == 1) {
                                //Get Final Fare
                                String totalAmount = fareResponse.getData().getFinal_fare();
                                //Check Final Fare
                                if (totalAmount != null) {
                                    hitBookingPaymentStatus(packageCode, totalAmount);
                                }
                            } else if (fareResponse.getData().getMessage() != null) {
                                if (fareResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(SignatureActivity.this);
                                }
                            } else {
                                Toast.makeText(SignatureActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (FairProgress != null && FairProgress.isShowing()) {
                                FairProgress.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FareResponse> call, Throwable t) {

                        if (!(SignatureActivity.this.isFinishing())) {
                            if (FairProgress != null && FairProgress.isShowing()) {
                                FairProgress.dismiss();
                            }
                        }
                        t.printStackTrace();
                    }
                });

            } else {
                if (!(SignatureActivity.this.isFinishing())) {
                    if (FairProgress != null && FairProgress.isShowing()) {
                        FairProgress.dismiss();
                    }
                }
            }
        }


    }

    Call<MainRes> setBookingPaymentStatus;

    private void hitBookingPaymentStatus(String packageCode, final String totalAmount) {

        if (setBookingPaymentStatus != null) {
            setBookingPaymentStatus.cancel();
        }
        if (!(SignatureActivity.this.isFinishing())) {
            if (paymentProgress != null && paymentProgress.isShowing()) {
                paymentProgress.dismiss();
            }
        }
        if (paymentProgress == null) {
            paymentProgress = new ProgressDialog(this);
        }
        paymentProgress.setCancelable(false);
        paymentProgress.setMessage("processing...");
        paymentProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

            if (!(SignatureActivity.this.isFinishing())) {
                if (paymentProgress != null && paymentProgress.isShowing()) {
                    paymentProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(SignatureActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {
                setBookingPaymentStatus = webRequest.apiInterface.setBookingPaymentStatus(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode);
                setBookingstatus(setBookingPaymentStatus, totalAmount);
            } else {
                if (!(SignatureActivity.this.isFinishing())) {
                    if (paymentProgress != null && paymentProgress.isShowing()) {
                        paymentProgress.dismiss();
                    }
                }
            }

        }

    }

    private void setBookingstatus(Call<MainRes> call, final String totalAmount) {
        call.enqueue(new Callback<MainRes>() {
            @Override
            public void onResponse(Call<MainRes> call, Response<MainRes> response) {
                //godelDriverActivity.dismissProgressDialog();
                MainRes res = response.body();
                if (res != null) {
                    if (Integer.parseInt(res.getResponse()) == 1) {
                        //Show Dialog Fare
                        showFareDialog(totalAmount);
                    } else if (res.getData().getMessage() != null) {
                        if (res.getData().getMessage().trim().equals("Unauthenticated")) {
                            Global.signInIntent(SignatureActivity.this);
                        }
                    }
                } else {
                    Toast.makeText(SignatureActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                }

                if (!(SignatureActivity.this.isFinishing())) {
                    if (paymentProgress != null && paymentProgress.isShowing()) {
                        paymentProgress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainRes> call, Throwable t) {
                //godelDriverActivity.dismissProgressDialog();
                t.printStackTrace();
                if (!(SignatureActivity.this.isFinishing())) {
                    if (paymentProgress != null && paymentProgress.isShowing()) {
                        paymentProgress.dismiss();
                    }
                }
            }
        });
    }

    Dialog dialogFare;

    private void showFareDialog(String fareAmount) {
        dialogFare = new Dialog(this);
        dialogFare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFare.setContentView(R.layout.dialog_fare_amount);
        dialogFare.setCancelable(false);
        dialogFare.setCanceledOnTouchOutside(false);
        dialogFare.getWindow().setGravity(Gravity.BOTTOM);
        dialogFare.show();
        //Initialization
        TextView tvTotalFare = (TextView) dialogFare.findViewById(R.id.tv_fare);
        Button btnDone = (Button) dialogFare.findViewById(R.id.btn_done);
        //Set Text

        String Value = Global.formatValue(fareAmount);
        tvTotalFare.setText(Global.CurrencySymbol + " " + Value);

        //Set Button Click Listener
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove Package Code
                editor.remove(SharedValues.PACKAGE_CODE);
                editor.remove(SharedValues.PACKAGE_ID);
                editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                editor.commit();
                //Pass Intent to HomeScreen
                Intent intent = new Intent(SignatureActivity.this, MyPackagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
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


    public Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth * 2, // Width
                srcBitmap.getHeight() + borderWidth * 2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );


        Canvas canvas = new Canvas(dstBitmap);


        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);


        Rect rect = new Rect(
                borderWidth / 2,
                borderWidth / 2,
                canvas.getWidth() - borderWidth / 2,
                canvas.getHeight() - borderWidth / 2
        );


        canvas.drawRect(rect, paint);


        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);


        srcBitmap.recycle();


        return dstBitmap;
    }
}


