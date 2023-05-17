package com.driver.godel.RefineCode.RefineActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.COPResponse.COPDriverResp;
import com.driver.godel.RefineCode.RefineWebServices.CancelPackage.cancelPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.RecurringDetails;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
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

import static android.view.View.FOCUS_UP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class GetPackageDetailActivity extends GodelActivity implements View.OnClickListener {

    private String packageCode, packageFinalPrice, IsDropToWarehouse;
    private TextView tvPickupAddress, tvDropOffAddress, tvPackagePickupTime, tvPackageDeliveryTime, tvPackageStatus, tvRecurringPickupType, tvRecurringPickupDetails;
    private TextView tvRecurringDeliveryDetails, tvPackageLength, tvPackageWidth, tvPackageDepth, tvPackageWeight, tvPackageQuantity, tvPackageValue, tvPackageType, tvPackageCode, tvPackageId;
    private TextView tvPickupAddressNote, tvDropoffAddressNote, tvPaymentAmt, tvPaymentMethod, tvPaymentStatus, tvTitle, tvUserUniqueId, tvUserName,tv_email_label, tvEmail, tvPhone, tvPckgAlreadyComplete;
    private Context context;
    private WebRequest webRequest;
    private RelativeLayout rlBookingTiming, rlRecurringDetails, rlAddressNotes, rlUserDetails;
    private Button btnPickupComplete, btnDropOffComplete,btn_drop_off_first_round_complete, btnDropOffFailure, btnCashReceived,btn_del_cash_received;
    private Call<GetPackageResponse> getPackageResponseCall;
    private ProgressDialog COPProgressDialog, PckgPrgsDlg;
    private final int REQUEST_CAMERA = 1888;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog updatePackageStatusProgress;
    private ImageView ivBack;
    boolean isOpen = false;
    private String ScannedPackageCode = "";
    private TextView tvEstimatedFareValue;
    private Button btnCancelPackage;
    private String packageID = "";
    public String TAG = "GETPACKAGEDETAILACTIVITY_LOG";
    public String isCashOnPickup = "";
    public String is_round_trip="",is_first_round_completed="";
    String language_type;
    private View view_contact1;
    View view_booking23;
    TextView tv_bookingType_label,tv_bookingType_status;
    private LinearLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail_new);

        Global global = new Global(GetPackageDetailActivity.this);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            packageCode = extras.getString("PackageCode");
        }
        webRequest = WebRequest.getSingleton(this);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        context = this;

        //=========== Initialise TextViews ===========//
        view_booking23 = (View) findViewById(R.id.view_booking23);
        tv_bookingType_label = (TextView) findViewById(R.id.tv_bookingType_label);
        tv_bookingType_status = (TextView) findViewById(R.id.tv_bookingType_status);
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        tvDropOffAddress = (TextView) findViewById(R.id.tv_drop_off_address);
        tvPackagePickupTime = (TextView) findViewById(R.id.tv_package_pickup_time);
        tvPackageDeliveryTime = (TextView) findViewById(R.id.tv_package_delivery_time);
        tvPackageStatus = (TextView) findViewById(R.id.tv_package_status);
        tvRecurringPickupType = (TextView) findViewById(R.id.tv_pickup_type);
        tvRecurringPickupDetails = (TextView) findViewById(R.id.tv_pickup_details);
        tvRecurringDeliveryDetails = (TextView) findViewById(R.id.tv_delivery_details);
        tvPackageLength = (TextView) findViewById(R.id.tv_package_length);
        tvPackageWidth = (TextView) findViewById(R.id.tv_package_width);
        tvPackageDepth = (TextView) findViewById(R.id.tv_package_height);
        tvPackageWeight = (TextView) findViewById(R.id.tv_package_weight);
        tvPackageQuantity = (TextView) findViewById(R.id.tv_package_quantity);
        tvPackageValue = (TextView) findViewById(R.id.tv_package_value);
        tvPackageType = (TextView) findViewById(R.id.tv_package_type);
        tvPickupAddressNote = (TextView) findViewById(R.id.tv_pickup_address_note);
        tvDropoffAddressNote = (TextView) findViewById(R.id.tv_dropoff_address_note);
        tvPaymentAmt = (TextView) findViewById(R.id.tv_payment_amt);
        tvPaymentMethod = (TextView) findViewById(R.id.tv_payment_method);
        tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
        tvUserUniqueId = (TextView) findViewById(R.id.tv_user_unique_id_);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tv_email_label = (TextView) findViewById(R.id.tv_email_label);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPckgAlreadyComplete = (TextView) findViewById(R.id.already_cmplt);
        tvPackageId = (TextView) findViewById(R.id.tv_package_id);
        tvPackageCode = (TextView) findViewById(R.id.tv_package_code);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvEstimatedFareValue = (TextView) findViewById(R.id.tv_estimated_fare_value);

        btnPickupComplete = (Button) findViewById(R.id.btn_pickup_complete);
        btnDropOffFailure = (Button) findViewById(R.id.btn_drop_off_failure);
        btnDropOffComplete = (Button) findViewById(R.id.btn_drop_off);
        btn_drop_off_first_round_complete = (Button) findViewById(R.id.btn_drop_off_first_round_complete);
        btnCashReceived = (Button) findViewById(R.id.btn_cash_received);
        btn_del_cash_received = (Button) findViewById(R.id.btn_del_cash_received);
        btnCancelPackage = (Button) findViewById(R.id.btn_cancel_package);
        view_contact1 = (View) findViewById(R.id.view_contact1);

        rlBookingTiming = (RelativeLayout) findViewById(R.id.rl_booking_timing);
        rlRecurringDetails = (RelativeLayout) findViewById(R.id.rl_recurring_details);
        rlAddressNotes = (RelativeLayout) findViewById(R.id.rl_address_notes);
        rlUserDetails = (RelativeLayout) findViewById(R.id.rl_user_details);

        ivBack = (ImageView) findViewById(R.id.iv_back);

        ll_det_visi_length = (LinearLayout) findViewById(R.id.ll_det_visi_length);
        ll_det_visi_width = (LinearLayout) findViewById(R.id.ll_det_visi_width);
        ll_det_visi_depth = (LinearLayout) findViewById(R.id.ll_det_visi_depth);
        ll_det_visi_weight = (LinearLayout) findViewById(R.id.ll_det_visi_weight);
        ll_det_visi_quan = (LinearLayout) findViewById(R.id.ll_det_visi_quan);
        ll_det_visi_type = (LinearLayout) findViewById(R.id.ll_det_visi_type);
        ll_det_visi_value = (LinearLayout) findViewById(R.id.ll_det_visi_value);
        if(preferences.getString(Global.DISPLAY_PACKAGE_LENGTH, "").equals("0")) {
            ll_det_visi_length.setVisibility(GONE);
        }else {
            ll_det_visi_length.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_WIDTH, "").equals("0")) {
            ll_det_visi_width.setVisibility(GONE);
        }else {
            ll_det_visi_width.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_DEPTH, "").equals("0")) {
            ll_det_visi_depth.setVisibility(GONE);
        }else {
            ll_det_visi_depth.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_WEIGHT, "").equals("0")) {
            ll_det_visi_weight.setVisibility(GONE);
        }else {
            ll_det_visi_weight.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_QUANTITY, "").equals("0")) {
            ll_det_visi_quan.setVisibility(GONE);
        }else {
            ll_det_visi_quan.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_TYPE, "").equals("0")) {
            ll_det_visi_type.setVisibility(GONE);
        }else {
            ll_det_visi_type.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_VALUE, "").equals("0")) {
            ll_det_visi_value.setVisibility(GONE);
        }else {
            ll_det_visi_value.setVisibility(VISIBLE);
        }

        tvTitle.setText(getResources().getString(R.string.packagedetailstext));
        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
            btn_del_cash_received.setVisibility(GONE);
        }else{
            btn_del_cash_received.setVisibility(GONE);
        }

            btnPickupComplete.setVisibility(VISIBLE);
        btnDropOffFailure.setVisibility(GONE);
        btnDropOffComplete.setVisibility(GONE);
        btn_drop_off_first_round_complete.setVisibility(GONE);
        tvPckgAlreadyComplete.setVisibility(GONE);
        btnCashReceived.setVisibility(GONE);
//        btn_del_cash_received.setVisibility(GONE);

        btnCashReceived.setOnClickListener(this);
        btn_del_cash_received.setOnClickListener(this);
        btnPickupComplete.setOnClickListener(this);
        btnDropOffFailure.setOnClickListener(this);
        btnDropOffComplete.setOnClickListener(this);
        btn_drop_off_first_round_complete.setOnClickListener(this);
        btnCancelPackage.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // intent can contain anydata
            if (isOpen) {


                getPackageDetail(packageCode);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isOpen = true;
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
//
//                }
//
//            }
//        }

        LocalBroadcastManager.getInstance(context).registerReceiver(onNotice, new IntentFilter(Global.ONGOING_BROADCAST));
        getPackageDetail(packageCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpen = false;
        LocalBroadcastManager.getInstance(context).unregisterReceiver(onNotice);
    }

    private void getPackageDetail(final String pacakgeCode) {
        if (getPackageResponseCall != null) {
            getPackageResponseCall.cancel();
        }
        if (!(GetPackageDetailActivity.this.isFinishing())) {
            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                PckgPrgsDlg.dismiss();
            }
        }
        if (PckgPrgsDlg == null) {
            PckgPrgsDlg = new ProgressDialog(context);
        }
        PckgPrgsDlg.setCancelable(false);
        PckgPrgsDlg.setMessage(getResources().getString(R.string.gettingpackagedetailsdialog) + "...");
        PckgPrgsDlg.show();

        if (pacakgeCode != null && pacakgeCode.length() > 0) {
            if (webRequest == null) {
                webRequest = WebRequest.getSingleton(this);
            }
            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if (!(GetPackageDetailActivity.this.isFinishing())) {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }
                Global.signInIntent(context);
            } else {
                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {


                    getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode);
                    Log.d(TAG,"------Global.USER_AGENT-------"+Global.USER_AGENT);
                    Log.d(TAG,"------Accept-------"+Accept);
                    Log.d(TAG,"------USER_TOKEN-------"+USER_TOKEN);
                    Log.d(TAG,"------driverId-------"+driverId);
                    Log.d(TAG,"------pacakgeCode-------"+pacakgeCode);

                    getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
                        @Override
                        public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                            if (response.isSuccessful()) {
                                GetPackageResponse packageResponse = response.body();
                                Log.d(TAG,"------response.body()-------"+response.body());
                                if (packageResponse != null) {
                                    if (packageResponse.getResponse() == 1) {
                                        String length = "", width = "", depth = "", weight = "", quantity = "", EstValue = "", AddressNote = "";
                                        String bookingType = "", pickupTime = "", deliveryTime = "",delivery_type = "", isWarehousePickup = "", isWarehouseDropoff = "", deliverySession = "";
                                        String packageStatus = "", pickupAddress = "", pickupLat = "", pickupLng = "";
                                        String dropoffAddress = "", dropoffLat = "", dropoffLng = "", currentDriverId = "";
                                        String userID = "", userName = "", userEmail = "", userNumber = "",countryCode="";
                                        String  paymentStatus = "", packageType = "", EstimatePrice = "",userType="";

                                        length = packageResponse.getData().getPackage_details().getPackage_length();
                                        width = packageResponse.getData().getPackage_details().getPackage_width();
                                        depth = packageResponse.getData().getPackage_details().getPackage_depth();
                                        weight = packageResponse.getData().getPackage_details().getPackage_weight();
                                        quantity = packageResponse.getData().getPackage_details().getPackage_quantity();
                                        EstValue = packageResponse.getData().getPackage_details().getPackage_est_value();
                                        packageCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        packageID = packageResponse.getData().getPackage_details().getPackage_id();

                                        Log.d("", "");
                                        packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
                                        isCashOnPickup = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                                        paymentStatus = packageResponse.getData().getPackage_details().getPayment_status();
                                        packageType = packageResponse.getData().getPackage_details().getPackage_type();
                                        packageFinalPrice = packageResponse.getData().getPackage_details().getPending_amount();
                                        is_round_trip = packageResponse.getData().getPackage_details().getIs_round_trip();
                                        is_first_round_completed = packageResponse.getData().getPackage_details().getIs_first_round_completed();



                                        Log.d(TAG,"------------is_round_trip------------"+is_round_trip);
                                        Log.d(TAG,"------------is_first_round_completed------------"+is_first_round_completed);
//                                        packageFinalPrice = packageResponse.getData().getPackage_details().getFinal_price();
                                        EstimatePrice = packageResponse.getData().getPackage_details().getEstimate_price();

                                        AddressNote = packageResponse.getData().getLocation_details().getAddress_note();

                                        delivery_type = packageResponse.getData().getBooking_details().getDelivery_type();
                                        bookingType = packageResponse.getData().getBooking_details().getBooking_type();
                                        pickupTime = packageResponse.getData().getBooking_details().getBooking_pickup_datetime();
                                        deliveryTime = packageResponse.getData().getBooking_details().getBooking_delivery_datetime();
                                        deliverySession = packageResponse.getData().getBooking_details().getDelivery_session();

                                        isWarehousePickup = packageResponse.getData().getIs_warehouse_pickup();
                                        isWarehouseDropoff = packageResponse.getData().getIs_warehouse_dropoff();
                                        IsDropToWarehouse = isWarehouseDropoff;
                                        currentDriverId = packageResponse.getData().getCurrent_driver_id();

                                        userID = packageResponse.getData().getUser_details().getUser_unique_id();
                                        userName = packageResponse.getData().getUser_details().getName();
                                        userEmail = packageResponse.getData().getUser_details().getUser_email();
                                        userNumber = packageResponse.getData().getUser_details().getUser_phone();
                                        countryCode = packageResponse.getData().getUser_details().getCountry_code();
                                        userType=packageResponse.getData().getUser_details().getType();


                                        boolean isValidPackage = true, isDriverIdMatched = false;
                                        String message = "";

                                        if (packageStatus != null && packageStatus.length() > 0) {
                                            switch (Integer.parseInt(packageStatus)) {
//                                                case (-1):
//                                                    btnPickupComplete.setVisibility(GONE);
//                                                    btnDropOffComplete.setVisibility(GONE);
//                                                    btn_del_cash_received.setVisibility(GONE);
//                                                    btnDropOffFailure.setVisibility(GONE);
//                                                    btnCashReceived.setVisibility(GONE);
//                                                    tvPckgAlreadyComplete.setVisibility(GONE);
//                                                    btnCancelPackage.setVisibility(GONE);
//                                                    break;

                                                case 0:
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
//                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                                        if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(VISIBLE);
                                                            btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        }

                                                        else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                        } else {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                        }
                                                    }else{
                                                       if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(VISIBLE);
                                                        btnPickupComplete.setVisibility(GONE);
                                                           btn_del_cash_received.setVisibility(GONE);
                                                        String value = Global.formatValue(packageFinalPrice);
                                                        btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                    }
//                                                    else if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
////                                                        btn_del_cash_received.setVisibility(VISIBLE);
//
//
//                                                        if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                        btnCashReceived.setVisibility(GONE);
//                                                        btnPickupComplete.setVisibility(VISIBLE);}
//                                                        }
                                                        else{
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                    }
                                                    }

                                                    break;
                                                case 1:
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
//                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                                      if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(VISIBLE);
                                                        btnPickupComplete.setVisibility(GONE);
                                                          btn_del_cash_received.setVisibility(GONE);
                                                        String value = Global.formatValue(packageFinalPrice);
                                                        btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                       } else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                       btnCashReceived.setVisibility(GONE);
                                                       btnPickupComplete.setVisibility(VISIBLE);
                                                          btn_del_cash_received.setVisibility(GONE);
                                                       }else {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                        }
                                                    }else{
                                                    if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(VISIBLE);
                                                        btnPickupComplete.setVisibility(GONE);
                                                        btn_del_cash_received.setVisibility(GONE);
                                                        String value = Global.formatValue(packageFinalPrice);
                                                        btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                    }
//                                                    else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                    btnCashReceived.setVisibility(GONE);
//                                                    btnPickupComplete.setVisibility(VISIBLE);
//                                                    }
                                                    else {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                    }}
                                                    break;
                                                case 2:
                                                    btnPickupComplete.setVisibility(VISIBLE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(VISIBLE);
                                                            btnPickupComplete.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        } else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                        } else {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                        }
                                                    }else {
                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(VISIBLE);
                                                            btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        }
//                                                        else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                            btnCashReceived.setVisibility(GONE);
//                                                            btnPickupComplete.setVisibility(VISIBLE);
//                                                        }
                                                        else {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                        }
                                                    }
                                                    break;
                                                case 3:
                                                    btnPickupComplete.setVisibility(VISIBLE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
//                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(VISIBLE);
                                                        btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                        String value = Global.formatValue(packageFinalPrice);
                                                        btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                    } else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                    }else {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                    }}else{
                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(VISIBLE);
                                                            btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        }else {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                        }
                                                    }
                                                    break;
                                                case 4:
                                                    btnPickupComplete.setVisibility(VISIBLE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
//                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(VISIBLE);
                                                        btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                        String value = Global.formatValue(packageFinalPrice);
                                                        btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                    } else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                    }else {
                                                        btnCashReceived.setVisibility(GONE);
                                                        btnPickupComplete.setVisibility(VISIBLE);
                                                    }}else{
                                                        if (isCashOnPickup != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus != null && paymentStatus.equalsIgnoreCase("0")) {
                                                            btnCashReceived.setVisibility(VISIBLE);
                                                            btnPickupComplete.setVisibility(GONE);
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btnCashReceived.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        }else {
                                                            btnCashReceived.setVisibility(GONE);
                                                            btnPickupComplete.setVisibility(VISIBLE);
                                                        }

                                                    }
                                                    break;
                                                case 5:
                                                    btnPickupComplete.setVisibility(GONE);

                                                    if(is_round_trip.equals("0")) {
                                                        btnDropOffComplete.setVisibility(VISIBLE);
                                                        btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    }else{

                                                        if(is_first_round_completed.equals("0")) {
                                                            btnDropOffComplete.setVisibility(GONE);
                                                            btn_drop_off_first_round_complete.setVisibility(VISIBLE);
                                                        }else{
                                                            btnDropOffComplete.setVisibility(VISIBLE);
                                                            btn_drop_off_first_round_complete.setVisibility(GONE);
                                                        }
                                                    }
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)||
                                                            preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)||
                                                            preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                        if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
                                                            btn_del_cash_received.setVisibility(GONE);
                                                            String value = Global.formatValue(packageFinalPrice);
                                                            btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
                                                        }else if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("1") && paymentStatus.equalsIgnoreCase("0"))
                                                            {
                                                                btn_del_cash_received.setVisibility(GONE);
                                                            }

                                                        }else {
                                                        btn_del_cash_received.setVisibility(GONE);
                                                    }
                                                btnDropOffFailure.setVisibility(VISIBLE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    break;
                                                case 6:
                                                    btnPickupComplete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
//                                                    btnDropOffComplete.setVisibility(VISIBLE);

                                                    if(is_round_trip.equals("0")) {
                                                        btnDropOffComplete.setVisibility(VISIBLE);
                                                        btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    }else{

                                                        if(is_first_round_completed.equals("0")) {
                                                            btnDropOffComplete.setVisibility(GONE);
                                                            btn_drop_off_first_round_complete.setVisibility(VISIBLE);
                                                        }else{
                                                            btnDropOffComplete.setVisibility(VISIBLE);
                                                            btn_drop_off_first_round_complete.setVisibility(GONE);
                                                        }
                                                    }

//                                                    if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                        btn_del_cash_received.setVisibility(VISIBLE);
//                                                        String value = Global.formatValue(packageFinalPrice);
//                                                        btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
//                                                    }
                                                    btnDropOffFailure.setVisibility(VISIBLE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    break;
                                                case 7:
                                                    editor.remove(SharedValues.PACKAGE_CODE);
                                                    editor.remove(SharedValues.PACKAGE_ID);
                                                    editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                                                    editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                                                    editor.commit();

                                                    btnPickupComplete.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(VISIBLE);
                                                    btnCancelPackage.setVisibility(GONE);
                                                    break;
                                                case 8:
                                                    btnPickupComplete.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(GONE);
                                                    break;
                                                case 9:
                                                    btnPickupComplete.setVisibility(GONE);
//                                                    btnDropOffComplete.setVisibility(VISIBLE);

                                                    if(is_round_trip.equals("0")) {
                                                        btnDropOffComplete.setVisibility(VISIBLE);
                                                        btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    }else{

                                                        if(is_first_round_completed.equals("0")) {
                                                            btnDropOffComplete.setVisibility(GONE);
                                                            btn_drop_off_first_round_complete.setVisibility(VISIBLE);
                                                        }else{
                                                            btnDropOffComplete.setVisibility(VISIBLE);
                                                            btn_drop_off_first_round_complete.setVisibility(GONE);
                                                        }
                                                    }

//                                                    if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                        btn_del_cash_received.setVisibility(VISIBLE);
//                                                        String value = Global.formatValue(packageFinalPrice);
//                                                        btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
//                                                    }
                                                    btnDropOffFailure.setVisibility(VISIBLE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    break;
                                                case 10:
                                                    btnPickupComplete.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(GONE);
                                                    break;
                                                case 11:
                                                    btnPickupComplete.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    break;
                                                case 12:
                                                    btnPickupComplete.setVisibility(GONE);
//                                                    btnDropOffComplete.setVisibility(VISIBLE);

                                                    if(is_round_trip.equals("0")) {
                                                        btnDropOffComplete.setVisibility(VISIBLE);
                                                        btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    }else{

                                                        if(is_first_round_completed.equals("0")) {
                                                            btnDropOffComplete.setVisibility(GONE);
                                                            btn_drop_off_first_round_complete.setVisibility(VISIBLE);
                                                        }else{
                                                            btnDropOffComplete.setVisibility(VISIBLE);
                                                            btn_drop_off_first_round_complete.setVisibility(GONE);
                                                        }
                                                    }


//                                                    if (isCashOnPickup != null && paymentStatus != null && isCashOnPickup.equalsIgnoreCase("2") && paymentStatus.equalsIgnoreCase("0")) {
//                                                        btn_del_cash_received.setVisibility(VISIBLE);
//                                                        String value = Global.formatValue(packageFinalPrice);
//                                                        btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value);
//                                                    }
                                                    btnDropOffFailure.setVisibility(VISIBLE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(VISIBLE);
                                                    break;
                                                case 13:
                                                    btnPickupComplete.setVisibility(GONE);
                                                    btnDropOffComplete.setVisibility(GONE);
                                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                                    btn_del_cash_received.setVisibility(GONE);
                                                    btnDropOffFailure.setVisibility(GONE);
                                                    btnCashReceived.setVisibility(GONE);
                                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                                    btnCancelPackage.setVisibility(GONE);
                                                    break;
                                            }
                                        }

                                        if (length != null && length.length() > 0) {
                                            tvPackageLength.setText(Global.getPackageCm(length));
                                        }
                                        if (width != null && width.length() > 0) {
                                            tvPackageWidth.setText(Global.getPackageCm(width));
                                        }
                                        if (depth != null && depth.length() > 0) {
                                            tvPackageDepth.setText(Global.getPackageCm(depth));
                                        }
                                        if (weight != null && weight.length() > 0) {
//                                            tvPackageWeight.setText(Global.getPackageKg(weight));
                                            tvPackageWeight.setText(Global.getPackageKg(weight));
                                        }
                                        if (quantity != null && quantity.length() > 0) {
                                            tvPackageQuantity.setText(Global.getPackageItems(quantity));
                                        }

                                        if (EstValue != null && EstValue.length() > 0) {
                                            String value = Global.formatValue(EstValue);
                                            String currency = "<font color='#939393'>"+Global.CurrencySymbol+"</font>";
                                            tvPackageValue.setText(Html.fromHtml(value + " " + currency));
                                        }

                                        if (packageCode != null && packageCode.length() > 0) {
                                            tvPackageId.setText(getResources().getString(R.string.bookingidcolon) + " " + packageCode);
                                            tvPackageCode.setText(getResources().getString(R.string.bookingidcolon) + " " + packageCode);
                                        }
                                        if (packageResponse.getData().getUser_details() == null) {
                                            rlUserDetails.setVisibility(GONE);
                                        } else {
                                            rlUserDetails.setVisibility(VISIBLE);
                                            if (userID != null && userID.length() > 0) {
                                                tvUserUniqueId.setText(userID);
                                            }
                                            if (userName != null && userName.length() > 0) {
                                                tvUserName.setText(userName);
                                            }
                                            if (userEmail != null && userEmail.length() > 0) {
                                                if(userEmail.equals("nomail@nomail.com")) {
                                                    tvEmail.setVisibility(GONE);
                                                    tv_email_label.setVisibility(GONE);
                                                    view_contact1.setVisibility(GONE);
//            tv_Email.setHeight(0);
                                                }else{
                                                tvEmail.setText(userEmail);}
                                            }
                                            if (userNumber != null && userNumber.length() > 0) {
                                                if(userType.equals("1") || userType.equals("0")){
                                                    if(userNumber.startsWith("+255")){
                                                        Log.d("User_TYPE","-----1---255----user type-----"+userType);
                                                       tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    }else if(userNumber.startsWith("+254")){
                                                        Log.d("User_TYPE","-----1---254----user type-----"+userType);
                                                       tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    }else if(userNumber.startsWith("+256")){
                                                        Log.d("User_TYPE","---1--256---user type-----"+userType);
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    }else if(userNumber.startsWith("+800")){
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    }else if(userNumber.startsWith("+91")){
                                                        Log.d("User_TYPE","----1--91--user type-----"+userType);
                                                        tvPhone.setText(Global.formatValueNumber2(userNumber));
                                                    }else if(userNumber.startsWith("+44")){
                                                        tvPhone.setText(Global.formatValueNumber2(userNumber));
                                                    }else {
                                                        tvPhone.setText(userNumber);
                                                    }
                                                }else if(userType.equals("2")){
                                                    Log.d("User_TYPE","-----2---user type-----"+userType);
//                                                    tvPhone.setText("+255"+" "+userNumber);
                                                    tvPhone.setText(countryCode+" "+userNumber);
                                                }else if(userType.equals("4")){
                                                    Log.d("User_TYPE","-----4---user type-----"+userType);
//                                                    tvPhone.setText("+255"+" "+userNumber);
                                                    tvPhone.setText(countryCode+" "+userNumber);
                                                }else{
                                                    tvPhone.setText(userNumber);
                                                }
//                                                tvPhone.setText(Global.formatValueNumber(userNumber));
                                            }
                                        }
                                        // add address notes
                                        if (AddressNote != null && AddressNote.trim().length() > 0) {
                                            rlAddressNotes.setVisibility(VISIBLE);
                                            String def[] = AddressNote.split(Global.AddressNoteSeprator);
                                            for (int i = 0; i < def.length; i++) {
                                                if (def[i] != null || def[i].length() > 0) {
                                                    if (i == 0) {
                                                        if (def[i].replaceAll(Global.AddressNoteSeprator, "").length() > 0) {
                                                            tvPickupAddressNote.setText(def[i]);
                                                            tvPickupAddressNote.setTextColor(getResources().getColor(R.color.blackColor));
                                                        } else {
                                                            tvPickupAddressNote.setText(getResources().getString(R.string.nopickupaddressnote));
                                                        }
                                                    }
                                                    if (i == 1) {
                                                        if (def[i].replaceAll(Global.AddressNoteSeprator, "").length() > 0) {
                                                            tvDropoffAddressNote.setText(def[i].replaceAll(Global.AddressNoteSeprator, ""));
                                                            tvDropoffAddressNote.setTextColor(getResources().getColor(R.color.blackColor));
                                                        } else {
                                                            tvDropoffAddressNote.setText(getResources().getString(R.string.nodeliveryaddressnote));
                                                        }
                                                    }
                                                } else {
                                                    if (i == 0) {
                                                        tvPickupAddressNote.setText(getResources().getString(R.string.nopickupaddressnote));
                                                    }
                                                    if (i == 1) {
                                                        tvDropoffAddressNote.setText(getResources().getString(R.string.nodeliveryaddressnote));
                                                    }
                                                }
                                            }
                                        } else {
                                            rlAddressNotes.setVisibility(GONE);
                                        }
                                        //set booking timings
                                        if (bookingType != null && bookingType.trim().length() > 0) {
                                            if (bookingType.equalsIgnoreCase("2")) {
                                                RecurringDetails recurringDetails = packageResponse.getData().getRecurring_details();
                                                //Check Recurring Details
                                                if (recurringDetails != null) {
                                                    //Hide Pickup
                                                    rlBookingTiming.setVisibility(GONE);
                                                    //Show Recurring Details
                                                    rlRecurringDetails.setVisibility(VISIBLE);
                                                    //Check Recurring Status for Daily Basis
                                                    if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("0")) {
                                                        //Set Recurring Type
                                                        if(is_round_trip.equals("1")) {
                                                        tvRecurringPickupType.setText(getResources().getString(R.string.dailytext)+" (Round Trip)");}
                                                        else {
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.dailytext));
                                                        }
                                                        //Get Recurring Day Time
                                                        String recurringDayTime = packageResponse.getData().getRecurring_details().getRecurring_day_time();
                                                        //Check Recurring Day Time
                                                        if (recurringDayTime != null) {
                                                            //Set Recurring Timing
                                                            tvRecurringPickupDetails.setText(recurringDayTime);
                                                        }
                                                    }
                                                    //Check Recurring Status for Weekly Basis
                                                    else if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("1")) {
                                                        //Set Recurring Type
                                                        if(is_round_trip.equals("1")) {
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.weeklytext)+" (Round Trip)");
                                                        }else {
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.weeklytext));
                                                        }
                                                        //Get Recurring Week Day
                                                        int recurringWeekDay = Integer.parseInt(packageResponse.getData().getRecurring_details().getRecurring_day());
                                                        //Get Recurring Day Time
                                                        String recurringDayTime = packageResponse.getData().getRecurring_details().getRecurring_day_time();
                                                        //Check Recurring Day Time and Recurring Day
                                                        String dayOfWeek = Global.getPickupDay(recurringWeekDay);
                                                        if (recurringDayTime != null) {
                                                            //Set Recurring Timing and DayOfWeek
                                                            tvRecurringPickupDetails.setText(dayOfWeek + " " + recurringDayTime);
                                                        }
                                                    }
                                                    //Check Recurring Status for Monthly Basis
                                                    else if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("2")) {
                                                        //Set Recurring Type
                                                        if(is_round_trip.equals("1")) {
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.monthlytext)+" (Round Trip)");
                                                        }else {
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.monthlytext));
                                                        }
                                                        //Get Recurring Week
                                                        String recurringWeek = packageResponse.getData().getRecurring_details().getRecurring_week();
                                                        //Check Recurring Week and Day Time
                                                        if (recurringWeek != null) {
                                                            //Set Recurring Week and Day Time
                                                            tvRecurringPickupDetails.setText(recurringWeek);
                                                        }
                                                    }
                                                 if(preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)){
    if (deliverySession != null) {
        //Check Delivery Session for Morning or Evening
        if (deliverySession.equalsIgnoreCase("0")) {
            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
        } else if (deliverySession.equalsIgnoreCase("1")) {
            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
        }
    }else{

    }
                                                    }else{
                                                    if(delivery_type!=null) {
                                                        if (delivery_type.equals("0")) {
                                                            tvRecurringDeliveryDetails.setText("Instant");
                                                        } else if (delivery_type.equals("1")) {
                                                            tvRecurringDeliveryDetails.setText("Schedule");
                                                        } else if (delivery_type.equals("2")) {
                                                            tvRecurringDeliveryDetails.setText("Standard");
                                                        } else if (delivery_type.equals("3")) {
                                                            tvRecurringDeliveryDetails.setText("Express");
                                                        } else  {
                                                            tvRecurringDeliveryDetails.setText("Overnight");
                                                        }
                                                    }else {
                                                        if (deliverySession != null) {
                                                            //Check Delivery Session for Morning or Evening
                                                            if (deliverySession.equalsIgnoreCase("0")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                                                            } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                                                            }
                                                        }
                                                    }}
                                                }
                                                else {

                                                    //Check Recuurence Status for Delivery
                                                    if (deliverySession != null) {
                                                        //Check Delivery Session for Morning or Evening
                                                        if (deliverySession.equalsIgnoreCase("0")) {
                                                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                                                        } else if (deliverySession.equalsIgnoreCase("1")) {
                                                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                                                        }
                                                    }

                                                }
                                                    //Check Recuurence Status for Delivery
//                                                    if (deliverySession != null) {
//                                                        //Check Delivery Session for Morning or Evening
//                                                        if (deliverySession.equalsIgnoreCase("0")) {
//                                                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
//                                                        } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
//                                                        }
//                                                    }
//                                                }
                                            } else {
                                                //Hide Recurring Details
                                                rlRecurringDetails.setVisibility(GONE);
                                                //Show Pickup
                                                rlBookingTiming.setVisibility(VISIBLE);
                                                //Set Pickup Time
                                                if (pickupTime != null) {

                                                    if (bookingType != null && bookingType.equalsIgnoreCase("0")) {
                                                        String packagePickupTime = Global.getConvertedDateTime(pickupTime);
                                                        tvPackagePickupTime.setText(packagePickupTime.replaceAll("00:00:00", ""));
                                                    } else {
                                                        String packagePickupTime = Global.getConvertedPickupDateTime(pickupTime);
                                                        tvPackagePickupTime.setText(packagePickupTime.replaceAll("00:00:00", ""));
                                                    }


                                                }
                                                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                                                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                    if(delivery_type!=null) {
                                                        if (delivery_type.equals("0")) {
                                                            tvPackageDeliveryTime.setText("Instant");
                                                        } else if (delivery_type.equals("1")) {
                                                            tvPackageDeliveryTime.setText("Schedule");
                                                        } else if (delivery_type.equals("2")) {
                                                            tvPackageDeliveryTime.setText("Standard");
                                                        } else if (delivery_type.equals("3")) {
                                                            tvPackageDeliveryTime.setText("Express");
                                                        } else  {
                                                            tvPackageDeliveryTime.setText("Overnight");
                                                        }
                                                    }else {

                                                        if (deliveryTime != null) {
                                                            if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
                                                                tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                            } else {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                                                if (deliverySession != null) {
                                                                    //Check Delivery Session for Morning or Evening
                                                                    if (deliverySession.equalsIgnoreCase("0")) {
                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
                                                                    } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
                                                                    }
                                                                } else {
                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    //Set DropOff Time
                                                    if (deliveryTime != null) {

                                                        Log.d("Tagg", "-----lese--------11122--");
                                                        if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                            Log.d("Tagg", "-----lese--------11122-warehouse-");
                                                            String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
//                                                            if(is_round_trip.equals("0")) {
                                                            tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                            }else{
//                                                                tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
//                                                            }
                                                        } else {


                                                            String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                                            Log.d("Tagg", "-----lese----------" + packageDeliveryTime);
                                                            if (deliverySession != null) {
                                                                //Check Delivery Session for Morning or Evening
                                                                if (deliverySession.equalsIgnoreCase("0")) {
//                                                                    if(is_round_trip.equals("0")) {
                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
//                                                                    }else {
//                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning) (Round Trip)");
//                                                                    }
                                                                } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                                    if (is_round_trip.equals("0")) {
                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
//                                                                    } else {
//                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening) (Round Trip)");
//                                                                    }
                                                                }
                                                            } else {
                                                                Log.d("Tagg", "-----lese-----ewe-----" + packageDeliveryTime);
//                                                                if(is_round_trip.equals("0")) {
                                                                tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                                }else {
//                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
//                                                                }
                                                            }
                                                        }

                                                        if (is_round_trip.equals("1")) {
                                                            view_booking23.setVisibility(VISIBLE);
                                                            tv_bookingType_label.setVisibility(VISIBLE);
                                                            tv_bookingType_status.setVisibility(VISIBLE);
                                                            String deliveryType = packageResponse.getData().getBooking_details().getDelivery_type();
                                                            if (deliveryType != null) {
                                                                if (deliveryType.equals("0")) {
                                                                    tv_bookingType_status.setText("Instant (Round Trip)");
                                                                } else if (deliveryType.equals("1")) {
                                                                    tv_bookingType_status.setText("Schedule (Round Trip)");
                                                                } else if (deliveryType.equals("2")) {
                                                                    tv_bookingType_status.setText("Standard (Round Trip)");
                                                                } else if (deliveryType.equals("3")) {
                                                                    tv_bookingType_status.setText("Express (Round Trip)");
                                                                } else {
                                                                    tv_bookingType_status.setText("Overnight (Round Trip)");
                                                                }
                                                            } else {
                                                                view_booking23.setVisibility(GONE);
                                                                tv_bookingType_label.setVisibility(GONE);
                                                                tv_bookingType_status.setVisibility(GONE);
                                                            }
                                                        } else {
                                                            view_booking23.setVisibility(GONE);
                                                            tv_bookingType_label.setVisibility(GONE);
                                                            tv_bookingType_status.setVisibility(GONE);
                                                        }
                                                    }
                                                }
                                                String pckstatus = Global.getStatus(Integer.parseInt(packageStatus));
                                                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                ||preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                        ||preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                                 if(packageStatus.equals("5")){
                                                     if(is_first_round_completed.equals("1")){
                                                         tvPackageStatus.setText("Return In Transit");
                                                     }else {
                                                         tvPackageStatus.setText("In Transit");
                                                     }
                                                 }else{
                                                     if(pckstatus.equalsIgnoreCase("Accepted")) {
                                                         if(is_first_round_completed.equals("0")){
                                                         tvPackageStatus.setText(pckstatus);
                                                         }else{
                                                         tvPackageStatus.setText("Return Package Accepted");
                                                         }
                                                     }else {
                                                         tvPackageStatus.setText(pckstatus);
                                                     }
                                                 }
                                                }else{
                                                    if(pckstatus.equalsIgnoreCase("Accepted")) {
                                                        if(is_first_round_completed.equals("0")){
                                                            tvPackageStatus.setText(pckstatus);
                                                        }else{
                                                            tvPackageStatus.setText("Return Package Accepted");
                                                        }
                                                    }else {
                                                        tvPackageStatus.setText(pckstatus);
                                                    }
                                                }
                                            }
                                        }

                                        if (packageType != null && packageType.length() > 0 && packageType.trim().equalsIgnoreCase("1")) {
                                            tvPackageType.setText("Document");
                                        } else {
                                            tvPackageType.setText("other");
                                        }

                                        //set pickup address
                                        if (isWarehousePickup != null) {
                                            if (isWarehousePickup.equalsIgnoreCase("true")) {
                                                pickupAddress = packageResponse.getData().getWarehouse_pickup_address().get(0).getAddress();
                                                pickupLat = packageResponse.getData().getWarehouse_pickup_address().get(0).getLatitude();
                                                pickupLng = packageResponse.getData().getWarehouse_pickup_address().get(0).getLongtitude();
                                                //Set WareHouse Pickup Address
                                                if (pickupAddress != null) {
                                                    tvPickupAddress.setText(pickupAddress);
                                                }
                                            } else {
                                                if (packageResponse.getData().getLocation_details() != null) {
                                                    if(packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")) {
                                                        pickupLat = packageResponse.getData().getLocation_details().getPickup_location_lat();
                                                        pickupLng = packageResponse.getData().getLocation_details().getPickup_location_lng();
                                                        String pickupHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                                        String pickupStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                                        String pickupLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                                        String exactpickupAddress = packageResponse.getData().getLocation_details().getPickup_location();
                                                        pickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, exactpickupAddress);

                                                        //Set User Pickup Location
                                                        if (pickupAddress != null) {
                                                            tvPickupAddress.setText(pickupAddress);
                                                        }
                                                    }else {
                                                        pickupLat = packageResponse.getData().getLocation_details().getRound_pickup_location_lat();
                                                        pickupLng = packageResponse.getData().getLocation_details().getRound_pickup_location_lng();
                                                        String pickupHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                                        String pickupStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                                        String pickupLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                                        String exactpickupAddress = packageResponse.getData().getLocation_details().getRound_pickup_location();
                                                        pickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, exactpickupAddress);

                                                        //Set User Pickup Location
                                                        if (pickupAddress != null) {
                                                            tvPickupAddress.setText(pickupAddress);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        //set dropoff address
                                        if (isWarehouseDropoff != null) {
                                            if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                dropoffAddress = packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress();
                                                dropoffLat = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLatitude();
                                                dropoffLng = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLongtitude();
                                                //Set WareHouse Address
                                                if (dropoffAddress != null) {
                                                    tvDropOffAddress.setText(dropoffAddress);
                                                }
                                            } else {
                                                if (packageResponse.getData().getLocation_details() != null) {
                                                    if(packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")) {
                                                        dropoffLat = packageResponse.getData().getLocation_details().getDropoff_location_lat();
                                                        dropoffLng = packageResponse.getData().getLocation_details().getDropoff_location_lng();
                                                        String dropoffHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                                        String dropoffStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                                        String dropoffLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                                        String exactDropOffAddress = packageResponse.getData().getLocation_details().getDropoff_location();
                                                        dropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, exactDropOffAddress);
                                                        //Set DropOff LOcation
                                                        if (dropoffAddress != null) {
                                                            tvDropOffAddress.setText(dropoffAddress);
                                                        }
                                                    }else{
                                                        dropoffLat = packageResponse.getData().getLocation_details().getRound_dropoff_location_lat();
                                                        dropoffLng = packageResponse.getData().getLocation_details().getRound_dropoff_location_lng();
                                                        String dropoffHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                                        String dropoffStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                                        String dropoffLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                                        String exactDropOffAddress = packageResponse.getData().getLocation_details().getRound_dropoff_location();
                                                        dropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, exactDropOffAddress);
                                                        //Set DropOff LOcation
                                                        if (dropoffAddress != null) {
                                                            tvDropOffAddress.setText(dropoffAddress);
                                                        }
                                                    }
                                                }
                                            }
                                        }
//                                        if(packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("1")) {
////                                            if (alternatePickupAndDropoffLocations.equals("1")) {
//                                            if (isWarehouseDropoff != null) {
//                                                if (isWarehouseDropoff.equalsIgnoreCase("true")) {
//                                                    String roundPickup = tvDropOffAddress.getText().toString();
//                                                    String roundDropOff = tvPickupAddress.getText().toString();
//                                                    tvDropOffAddress.setText(roundDropOff);
//                                                    tvPickupAddress.setText(roundPickup);
//                                                }
//                                            } else {
//                                                String roundPickup = tvDropOffAddress.getText().toString();
//                                                String roundDropOff = tvPickupAddress.getText().toString();
//                                                tvDropOffAddress.setText(roundDropOff);
//                                                tvPickupAddress.setText(roundPickup);
//                                            }
//                                        }

                                        if (EstimatePrice != null) {
                                            tvEstimatedFareValue.setText(Global.formatValue("" + EstimatePrice) + " " + Global.CurrencySymbol);
                                        } else {
                                            tvEstimatedFareValue.setVisibility(GONE);
//                                    tvEstimatedFare.setVisibility(GONE);
                                        }

                                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                            }
                                        }
                                    }else if(packageResponse.getResponse() == 0){
                                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                            }

                                            btnPickupComplete.setVisibility(GONE);
                                            btnDropOffComplete.setVisibility(GONE);
                                            btn_drop_off_first_round_complete.setVisibility(GONE);
                                            btn_del_cash_received.setVisibility(GONE);
                                            btnDropOffFailure.setVisibility(GONE);
                                            btnCashReceived.setVisibility(GONE);
                                            tvPckgAlreadyComplete.setVisibility(GONE);
                                            btnCancelPackage.setVisibility(GONE);
                                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                            alert.setCancelable(false);
                                            alert.setMessage("This package has been deleted by admin");
                                            alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    onBackPressed();
                                                }
                                            });
                                            alert.show();
                                        }

                                    } else if(packageResponse.getData().getMessage()!=null){
                                        if (packageResponse.getData().getMessage().equals("Unauthenticated")) {
                                            Global.signInIntent(GetPackageDetailActivity.this);
                                        }
                                    } else {
                                        Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                            }
                                        }
                                    }
                                } else {
                                    if (!(GetPackageDetailActivity.this.isFinishing())) {
                                        if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                            PckgPrgsDlg.dismiss();
                                        }
                                    }
                                }
                            } else {
                                if (!(GetPackageDetailActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                            try {
                                if (!(GetPackageDetailActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                                t.printStackTrace();
                            } catch (Exception e) {
                            }
                        }
                    });


                } else {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }


            }


        }
    }

    private void COP_Driver(String DriverId, String PackageCode) {
        if (!(GetPackageDetailActivity.this.isFinishing())) {
            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                COPProgressDialog.dismiss();
            }
        }
        if (COPProgressDialog == null) {
            COPProgressDialog = new ProgressDialog(this);
        }
        COPProgressDialog.setMessage(getResources().getString(R.string.proceesingdialog) + "...");
        COPProgressDialog.setCancelable(false);
        COPProgressDialog.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
//        driverId = preferences.getString(SharedValues.DRIVER_ID, "");

        Log.d("CHECK_LOGS_BOPOK", "Global.USER_AGENT " + Global.USER_AGENT);
        Log.d("CHECK_LOGS_BOPOK", "Accept " + Accept);
        Log.d("CHECK_LOGS_BOPOK", "USER_TOKEN " + USER_TOKEN);
        Log.d("CHECK_LOGS_BOPOK", "driverId " + driverId);
        Log.d("CHECK_LOGS_BOPOK", "PackageCode " + packageCode);

        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(GetPackageDetailActivity.this.isFinishing())) {
                if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                    COPProgressDialog.dismiss();
                }
            }
            Global.signInIntent(context);
        } else {
            String country_code = CountryCodeCheck.countrycheck(context);
            if (country_code != null && country_code.trim().length() > 0) {

                Call<COPDriverResp> cashOnPickupCall = webRequest.apiInterface.CashOnPickup(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, driverId);

                cashOnPickupCall.enqueue(new Callback<COPDriverResp>() {
                    @Override
                    public void onResponse(Call<COPDriverResp> call, Response<COPDriverResp> response) {
                        if (response.body() != null) {
                            COPDriverResp copResp = response.body();
                            if (copResp.getResponse() == 1) {
                                btnPickupComplete.setVisibility(VISIBLE);
                                btnCashReceived.setVisibility(GONE);
                            } else if (copResp.getResponse() == 2) {
                                btnPickupComplete.setVisibility(VISIBLE);
                                btnCashReceived.setVisibility(GONE);
                                String message = copResp.getData().getMessage();
                                AlertDialog.Builder alert = new AlertDialog.Builder(GetPackageDetailActivity.this);
                                alert.setCancelable(false);
                                alert.setMessage(message);
                                alert.setPositiveButton(getResources().getString(R.string.oktext), null).show();
                            } else if(copResp.getData().getMessage()!=null){
                                if (copResp.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(GetPackageDetailActivity.this);
                                }
                                } else {
                                String message = copResp.getData().getMessage();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Pass an Intent
                            Toast.makeText(GetPackageDetailActivity.this, getResources().getString(R.string.pleaseretrytext), Toast.LENGTH_SHORT).show();
                        }
                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                                COPProgressDialog.dismiss();
                            }
                        }
                        btnCashReceived.setEnabled(true);
                        btnCashReceived.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<COPDriverResp> call, Throwable t) {
                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                                COPProgressDialog.dismiss();
                            }
                        }
                        Toast.makeText(GetPackageDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();

                        btnCashReceived.setEnabled(true);
                        btnCashReceived.setClickable(true);
                    }
                });

            } else {
                if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                    COPProgressDialog.dismiss();
                }
            }
        }


    }
private void COP_Driver2(String DriverId, String PackageCode) {

    Log.d("ttttttttttttt", "entered");

        if (!(GetPackageDetailActivity.this.isFinishing())) {
            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                COPProgressDialog.dismiss();
            }
        }
        if (COPProgressDialog == null) {
            COPProgressDialog = new ProgressDialog(this);
        }
        COPProgressDialog.setMessage(getResources().getString(R.string.proceesingdialog) + "...");
        COPProgressDialog.setCancelable(false);
        COPProgressDialog.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");

        String Accept = Global.ACCEPT;
//        driverId = preferences.getString(SharedValues.DRIVER_ID, "");

        Log.d("ttttttttttttt", "Global.USER_AGENT " + Global.USER_AGENT);
        Log.d("ttttttttttttt", "Accept " + Accept);
        Log.d("ttttttttttttt", "USER_TOKEN " + USER_TOKEN);
        Log.d("ttttttttttttt", "driverId " + driverId);
        Log.d("ttttttttttttt", "PackageCode " + packageCode);

        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(GetPackageDetailActivity.this.isFinishing())) {
                if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                    Log.d("ttttttttttttt", "usertokenNULLL");
                    COPProgressDialog.dismiss();
                }
            }
            Global.signInIntent(context);
        } else {
            String country_code = CountryCodeCheck.countrycheck(context);
            if (country_code != null && country_code.trim().length() > 0) {

                Call<COPDriverResp> cashOnPickupCall2 = webRequest.apiInterface.CashOnPickup2(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode, driverId);

                cashOnPickupCall2.enqueue(new Callback<COPDriverResp>() {
                    @Override
                    public void onResponse(Call<COPDriverResp> call, Response<COPDriverResp> response) {
                        if (response.body() != null) {
                            COPDriverResp copResp = response.body();
                            Log.d("ttttttttttttt","response.body()"+response.body());
                            if (copResp.getResponse() == 1) {
                                btnPickupComplete.setVisibility(VISIBLE);
                                btnCashReceived.setVisibility(GONE);
                                btn_del_cash_received.setVisibility(GONE);
                            } else if (copResp.getResponse() == 2) {
                                btnPickupComplete.setVisibility(VISIBLE);
                                btnCashReceived.setVisibility(GONE);
                                String message = copResp.getData().getMessage();
                                AlertDialog.Builder alert = new AlertDialog.Builder(GetPackageDetailActivity.this);
                                alert.setCancelable(false);
                                alert.setMessage(message);
                                alert.setPositiveButton(getResources().getString(R.string.oktext), null).show();
                            } else if(copResp.getData().getMessage()!=null){
                                if (copResp.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(GetPackageDetailActivity.this);
                                }
                                } else {
                                String message = copResp.getData().getMessage();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Pass an Intent
                            Toast.makeText(GetPackageDetailActivity.this, getResources().getString(R.string.pleaseretrytext), Toast.LENGTH_SHORT).show();
                        }
                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                                COPProgressDialog.dismiss();
                            }
                        }
                        btnCashReceived.setEnabled(true);
                        btnCashReceived.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<COPDriverResp> call, Throwable t) {
                        if (!(GetPackageDetailActivity.this.isFinishing())) {
                            if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                                COPProgressDialog.dismiss();
                            }
                        }
                        Toast.makeText(GetPackageDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();

                        btnCashReceived.setEnabled(true);
                        btnCashReceived.setClickable(true);
                    }
                });

            } else {
                if (COPProgressDialog != null && COPProgressDialog.isShowing()) {
                    COPProgressDialog.dismiss();
                }
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cash_received:
                btnCashReceived.setEnabled(false);
                btnCashReceived.setClickable(false);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setCancelable(false);
                String value = Global.formatValue(packageFinalPrice);
                alert.setMessage("Have you collected " + Global.CurrencySymbol + " " + value + " ?");
                alert.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (isNetworkAvailable()) {
                            COP_Driver(driverId, ScannedPackageCode);
                        } else {
                            btnCashReceived.setEnabled(true);
                            btnCashReceived.setClickable(true);
                            Snackbar(ivBack);

                        }

                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.notext), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnCashReceived.setEnabled(true);
                        btnCashReceived.setClickable(true);
                    }
                });
                alert.show();
                break;

          case R.id.btn_del_cash_received:
              btn_del_cash_received.setEnabled(false);
              btn_del_cash_received.setClickable(false);
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setCancelable(false);
                String value1 = Global.formatValue(packageFinalPrice);
                alert1.setMessage("Have you collected " + Global.CurrencySymbol + " " + value1 + " ?");
                alert1.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (isNetworkAvailable()) {
                            COP_Driver2(driverId, ScannedPackageCode);
                             Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                             i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                            i1.putExtra("is_round_trip", "" + is_round_trip);
                            i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                            i1.putExtra("is_drop_off_complete", "1" );
                             startActivity(i1);
                        } else {
                            btn_del_cash_received.setEnabled(true);
                            btn_del_cash_received.setClickable(true);
                            Snackbar(ivBack);

                        }

                    }
                });

                alert1.setNegativeButton(getResources().getString(R.string.notext), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_del_cash_received.setEnabled(true);
                        btn_del_cash_received.setClickable(true);
                    }
                });
                alert1.show();
                break;

            case R.id.btn_pickup_complete:
//                btnPickupComplete.setEnabled(false);
//                btnPickupComplete.setClickable(false);
                if (isNetworkAvailable()) {
                    showMessageDialog();
                } else {
                    Snackbar(ivBack);
                }

                break;

            case R.id.btn_drop_off_failure:
                Intent i = new Intent().setClass(GetPackageDetailActivity.this, DropOffFailureActivity.class);
                startActivity(i);
                break;

            case R.id.btn_drop_off:
//                String isCashOnPickup = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                    if (isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("2") ) {

                        String value2 = Global.formatValue(packageFinalPrice);

                        Log.d(TAG,"---------value2-----------"+value2);
                        if(value2.equals("0")) {
                            Log.d(TAG,"---------value2---inequal zero--------"+value2);
                            btn_del_cash_received.setVisibility(GONE);
                            Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                            i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                            i1.putExtra("is_round_trip", "" + is_round_trip);
                            i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                            i1.putExtra("is_drop_off_complete", "1" );
                            startActivity(i1);
                        }else{
                            Log.d(TAG,"---------value2---inelse-------"+value2);
                        btnDropOffComplete.setVisibility(GONE);
                        btn_drop_off_first_round_complete.setVisibility(GONE);
                 btn_del_cash_received.setVisibility(VISIBLE);

                  btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " + Global.CurrencySymbol + " " + value2);}}
                  else if(isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("1")){
                        btn_del_cash_received.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "1" );
                        startActivity(i1);
                    }else if(isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("0")){
                        btn_del_cash_received.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "1" );
                        startActivity(i1);
                    }else{
                        btn_del_cash_received.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "1" );
                        startActivity(i1);
                    }
                }
                else {
                    btn_del_cash_received.setVisibility(GONE);
                    Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                    i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                    i1.putExtra("is_round_trip", "" + is_round_trip);
                    i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                    i1.putExtra("is_drop_off_complete", "1" );
                    startActivity(i1);
                }
                break;


             case R.id.btn_drop_off_first_round_complete:
//                String isCashOnPickup = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                    if (isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("2") ) {

                        String value2 = Global.formatValue(packageFinalPrice);
//
//                        Log.d(TAG,"---------value2-----------"+value2);
                        if(value2.equals("0")) {
//                            Log.d(TAG,"---------value2---inequal zero--------"+value2);
                            btn_del_cash_received.setVisibility(GONE);
                            Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                            i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "0" );
                            startActivity(i1);
                        }else{
//                            Log.d(TAG,"---------value2---inelse-------"+value2);
                        btnDropOffComplete.setVisibility(GONE);
                        btn_drop_off_first_round_complete.setVisibility(GONE);
                        btn_del_cash_received.setVisibility(VISIBLE);

                        btn_del_cash_received.setText(getResources().getString(R.string.collectcashtext) + " " +
                                Global.CurrencySymbol + " " + value2);}


                    } else if(isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("1")){
                           btn_del_cash_received.setVisibility(GONE);
                        btn_drop_off_first_round_complete.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "0" );
                        startActivity(i1);
                    }else if(isCashOnPickup != null  && isCashOnPickup.equalsIgnoreCase("0")){
                        btn_del_cash_received.setVisibility(GONE);
                        btn_drop_off_first_round_complete.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "0" );
                        startActivity(i1);
                    }else{
                        btn_del_cash_received.setVisibility(GONE);
                        btn_drop_off_first_round_complete.setVisibility(GONE);
                        Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                        i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                        i1.putExtra("is_round_trip", "" + is_round_trip);
                        i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                        i1.putExtra("is_drop_off_complete", "0" );
                        startActivity(i1);
                    }
                }
                else {
                    btn_del_cash_received.setVisibility(GONE);
                    btn_drop_off_first_round_complete.setVisibility(GONE);
                    Intent i1 = new Intent().setClass(GetPackageDetailActivity.this, SignatureActivity.class);
                    i1.putExtra("IsWareHouseDropoff", "" + IsDropToWarehouse);
                    i1.putExtra("is_round_trip", "" + is_round_trip);
                    i1.putExtra("is_first_round_completed", "" + is_first_round_completed);
                    i1.putExtra("is_drop_off_complete", "0" );
                    startActivity(i1);
                }
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_cancel_package:
                final Dialog cancelDialog = new Dialog(this);
                cancelDialog.setCancelable(false);
                cancelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                cancelDialog.setContentView(R.layout.cancel_package);
                final EditText etReason = (EditText) cancelDialog.findViewById(R.id.et_reason);
                Button btn_cancel = (Button) cancelDialog.findViewById(R.id.btn_cancel);
                Button btn_ok = (Button) cancelDialog.findViewById(R.id.btn_ok);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDialog.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "ok click");
                        if (etReason == null || etReason.length() <= 0 || etReason.getText().toString().trim().equalsIgnoreCase("")) {
                            etReason.setError("Please enter the reason");
                            etReason.requestFocus(FOCUS_UP);
                            Log.d(TAG, "Reason Required");
                        } else {
                            cancelPackage(packageID, etReason.getText().toString().trim());
                            cancelDialog.dismiss();
                            Log.d(TAG, "Cancel Package");
                        }

                    }
                });
                cancelDialog.show();


                break;
        }
    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("");
        builder.setMessage(getResources().getString(R.string.wanttocaptureproofimage));
        builder.setCancelable(false);
        //Replace Button
        builder.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
                //Open Camera Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        //Yes Button
        builder.setNegativeButton(getResources().getString(R.string.notext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
                if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                    updatePackageStatusProgress.dismiss();
                }

                if (updatePackageStatusProgress == null) {
                    updatePackageStatusProgress = new ProgressDialog(GetPackageDetailActivity.this);
                }
                updatePackageStatusProgress.setCancelable(false);
                updatePackageStatusProgress.setMessage(getResources().getString(R.string.updatingpackagedialog) + "...");
                updatePackageStatusProgress.show();
                updatePackageStatusApi("", Global.STATUS_PICKUP_COMPLETE);
            }
        });
        builder.show();
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
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.e("ImageBase64", "" + encodedImage);

        if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
            updatePackageStatusProgress.dismiss();
        }

        if (updatePackageStatusProgress == null) {
            updatePackageStatusProgress = new ProgressDialog(this);
        }

        updatePackageStatusProgress.setCancelable(false);
        updatePackageStatusProgress.setMessage(getResources().getString(R.string.updatingpackagedialog) + "...");
        updatePackageStatusProgress.show();
        //API Hit Update Package
        updatePackageStatusApi(encodedImage, Global.STATUS_PICKUP_COMPLETE);
    }

    private void updatePackageStatusApi(String base64Image, String PackageStatus) {
        String FCM_token = preferences.getString(SharedValues.FCM_TOKEN, "0");
        String deviceToken = preferences.getString(SharedValues.FCM_TOKEN, "0");

        final String details = "Pickup complete api failure issue         Parameters: PackageId:" + packageId + " driverId:" + driverId + " deviceToken:" + deviceToken + " base64Image " + base64Image;
        Log.d("PACKAGE_DETAILS_LOG", "details " + details);
        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                updatePackageStatusProgress.dismiss();
            }
            Global.signInIntent(context);
        } else {

            if (isNetworkAvailable()) {
                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {

                    Call<UpdatePackageStatus> updatePackageStatusCall = webRequest.apiInterface.updatePackageStatus(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId,
                            packageId, driverId, PackageStatus, FCM_token, "0", base64Image.trim());
                    updatePackageStatusCall.enqueue(new Callback<UpdatePackageStatus>() {
                        @Override
                        public void onResponse(Call<UpdatePackageStatus> call, Response<UpdatePackageStatus> response) {
                            btnPickupComplete.setEnabled(true);
                            btnPickupComplete.setClickable(true);
                            if (response.body() != null) {
                                UpdatePackageStatus updatePackageStatus = response.body();
                                if (updatePackageStatus.getResponse() == 1) {
                                    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(GetPackageDetailActivity.this);
                                    confirmationDialog.setCancelable(false);
                                    confirmationDialog.setMessage(getResources().getString(R.string.wouldyouliketoreplace));
                                    confirmationDialog.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            setActivePackage(driverId, packageCode, Global.driverLatitude, Global.driverLongitude, "0");
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
                                }else if (updatePackageStatus.getResponse() == 0){

                                    if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                                        updatePackageStatusProgress.dismiss();
                                    }

                                    btnPickupComplete.setVisibility(GONE);
                                    btnDropOffComplete.setVisibility(GONE);
                                    btn_drop_off_first_round_complete.setVisibility(GONE);
                                    btn_del_cash_received.setVisibility(GONE);
                                    btnDropOffFailure.setVisibility(GONE);
                                    btnCashReceived.setVisibility(GONE);
                                    tvPckgAlreadyComplete.setVisibility(GONE);
                                    btnCancelPackage.setVisibility(GONE);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                    alert.setCancelable(false);
                                    alert.setMessage("This package has been deleted by admin");
                                    alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
                                        }
                                    });
                                    alert.show();
                            }else if(updatePackageStatus.getData().getMessage()!=null){
                                    if (updatePackageStatus.getData().getMessage().trim().equals("Unauthenticated")) {
                                        Global.signInIntent(GetPackageDetailActivity.this);
                                    }
                                } else {
                                    Toast.makeText(context, "" + updatePackageStatus.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "null response", Toast.LENGTH_SHORT).show();
                            }

                            if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                                updatePackageStatusProgress.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdatePackageStatus> call, Throwable t) {
                            btnPickupComplete.setEnabled(true);
                            btnPickupComplete.setClickable(true);
                            if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                                updatePackageStatusProgress.dismiss();
                            }
                            Toast.makeText(context, "Package picked Failed", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });

                } else {
                    if (updatePackageStatusProgress != null && updatePackageStatusProgress.isShowing()) {
                        updatePackageStatusProgress.dismiss();
                    }
                }
            } else {
                Snackbar(ivBack);
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

    ProgressDialog ActivePckgProgress;
    Call<ActivePackageResponse> activePackageCall;

    private void setActivePackage(String driverId, String packageCode, String currentLat, String currentLng, String type) {
        if (activePackageCall != null) {
            activePackageCall.cancel();
        }
        if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
            ActivePckgProgress.dismiss();
        }
        if (ActivePckgProgress == null) {
            ActivePckgProgress = new ProgressDialog(this);
        }
        ActivePckgProgress.setCancelable(false);
        ActivePckgProgress.setMessage(getResources().getString(R.string.loadingtext) + "...");
        ActivePckgProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                ActivePckgProgress.dismiss();
            }
            Global.signInIntent(this);
        } else {


            String country_code = CountryCodeCheck.countrycheck(context);
            if (country_code != null && country_code.trim().length() > 0) {

                activePackageCall = webRequest.apiInterface.setActivePackage(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, packageCode, currentLat, currentLng, type);
                activePackageCall.enqueue(new Callback<ActivePackageResponse>() {
                    @Override
                    public void onResponse(Call<ActivePackageResponse> call, Response<ActivePackageResponse> response) {
                        if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                            ActivePckgProgress.dismiss();
                        }
                        if (response.body() != null) {
                            ActivePackageResponse activePackageResponse = response.body();
                            //Check response
                            if (activePackageResponse.getResponse() == 1) {
                                passIntent();
                            }else if(activePackageResponse.getData().getMessage()!=null){
                                if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(GetPackageDetailActivity.this);
                                }
                            }

                            else {
                                Toast.makeText(GetPackageDetailActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ActivePackageResponse> call, Throwable t) {
                        if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                            ActivePckgProgress.dismiss();
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

    ProgressDialog progressDialog;
    Call<cancelPackageResponse> requestDriver;

    private void cancelPackage(String PackageId, String reason) {
        if (isNetworkAvailable()) {
            if (requestDriver != null) {
                requestDriver.cancel();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
            }

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getResources().getString(R.string.cancelrequestdialog) + "...");
            progressDialog.show();

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");

            Log.d("CHECK_RESPONSE", "PackageId " + PackageId);

            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Global.signInIntent(this);
            } else {


                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {
                    requestDriver = webRequest.apiInterface.cancelPackage(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, PackageId, reason);
                    Log.d(TAG,"--------country_code------"+country_code);
                    Log.d(TAG,"--------Global.USER_AGENT------"+Global.USER_AGENT);
                    Log.d(TAG,"--------Accept------"+Accept);
                    Log.d(TAG,"--------USER_TOKEN------"+USER_TOKEN);
                    Log.d(TAG,"--------driverId------"+driverId);

                    CancelBookingResponse(requestDriver);
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }


            }

        } else {
            Snackbar(ivBack);
        }
    }

    //    ============ cancel booking API hit response===============//
    private void CancelBookingResponse(Call<cancelPackageResponse> profileCall) {
        profileCall.enqueue(new Callback<cancelPackageResponse>() {
            @Override
            public void onResponse(Call<cancelPackageResponse> call, retrofit2.Response<cancelPackageResponse> response) {
                cancelPackageResponse signupRes = response.body();
                Log.d(TAG,"--------response.body()------"+response.body());
                int hitResp = signupRes.getResponse();
                Log.d(TAG,"--------hitResp------"+hitResp);
                if (hitResp == 1) {
                    editor.remove(SharedValues.PACKAGE_CODE);
                    editor.remove(SharedValues.PACKAGE_ID);
                    editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
                    editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
                    editor.commit();

                    Toast.makeText(GetPackageDetailActivity.this, "" + signupRes.getData().getMessage(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(GetPackageDetailActivity.this, MyPackagesActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }else if (signupRes.getData().getMessage()!=null) {
                    if (signupRes.getData().getMessage().trim().equals("Unauthenticated")) {
                        Global.signInIntent(GetPackageDetailActivity.this);
                    }
                }
                else {
                    Toast.makeText(GetPackageDetailActivity.this, "" + signupRes.getData().getMessage(), Toast.LENGTH_LONG).show();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<cancelPackageResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
}