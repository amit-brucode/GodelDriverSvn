package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.RecurringDetails;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.response.TransferPackageResponse.TransferPackageToDriver;
import com.driver.godel.response.getDriverDetails.GetProfileResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GetPackageDetailScanActivity extends GodelActivity implements View.OnClickListener {

    static Activity activitycon;
    private final int REQUEST_CAMERA = 1888;
    public String TAG = "GETPACKAGEDETAILACTIVITYSCAN_LOG";
    public String isCashOnPickup = "";
    boolean isOpen = false;
    String language_type;
    String onClickkk = "";
    String todriver = "";
    String Packagetobetranferred = "";
    private String packageCode,packageStatus, packageFinalPrice, IsDropToWarehouse,alternatePickupAndDropoffLocations="0";
    private TextView tvPickupAddress, tvDropOffAddress, tvPackagePickupTime, tvPackageDeliveryTime, tvPackageStatus, tvRecurringPickupType, tvRecurringPickupDetails;
    private TextView tvRecurringDeliveryDetails, tvPackageLength, tvPackageWidth, tvPackageDepth, tvPackageWeight, tvPackageQuantity, tvPackageValue, tvPackageType, tvPackageCode, tvPackageId;
    private TextView tvPickupAddressNote, tvDropoffAddressNote, tvPaymentAmt, tvPaymentMethod, tvPaymentStatus, tvTitle, tvUserUniqueId, tvUserName, tv_email_label, tvEmail, tvPhone, tvDriverUniqueId, tvDriverName, tvDriverEmail, tvDriverPhone;
    private LinearLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;
    public String is_round_trip="",is_first_round_completed="";
    private Context context;
    private WebRequest webRequest;
    private RelativeLayout rlBookingTiming, rlRecurringDetails, rlAddressNotes, rlUserDetails;
    private Button btntransfer, btnconfirmt;
    //    private Button btnPickupComplete, btnDropOffComplete, btnDropOffFailure, btnCashReceived,btn_del_cash_received;
    private Call<GetPackageResponse> getPackageResponseCall;
    private Call<TransferPackageToDriver> getTranferResponseCall;
    private ProgressDialog PckgPrgsDlg;
    private ProgressDialog driverProfileProgress;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView ivBack;
    private String ScannedPackageCode = "";
    private TextView tvEstimatedFareValue;
    private ImageView downarrow;
    private RelativeLayout driverDetails;
    private String packageID = "";
    private View view_contact1;
    private Call<GetProfileResponse> getProfileResponseCall;
    private GetProfileResponse getProfileResponse;
    View view_booking23;
    TextView tv_bookingType_label,tv_bookingType_status;
    // open scanner for scan package
    public static void openScanner(int type) {
        IntentIntegrator integrator = new IntentIntegrator(activitycon);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(activitycon.getString(R.string.scan_drivers_qrcode));
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

//    private BroadcastReceiver onNotice = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // intent can contain anydata
//            if (isOpen) {
//                getPackageDetail(packageCode);
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_package_detail_scan);

        activitycon = GetPackageDetailScanActivity.this;

        Global global = new Global(GetPackageDetailScanActivity.this);
        global.setCurrencySymbol();
        preferences = activitycon.getSharedPreferences(SharedValues.PREF_NAME, activitycon.MODE_PRIVATE);
        editor = preferences.edit();
        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");

        if (language_type.equals("1") && language_type != null) {
            Locale myLocale = new Locale("sw");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        } else if (language_type.equals("2") && language_type != null) {
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
            alternatePickupAndDropoffLocations = extras.getString("is_alternate_pickupAndDropoff_locations");
        }

        webRequest = WebRequest.getSingleton(this);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        context = this;

        //=========== Initialise TextViews ===========//
        view_booking23 = (View) findViewById(R.id.view_booking23);
        tv_bookingType_label = (TextView) findViewById(R.id.tv_bookingType_label);
        tv_bookingType_status = (TextView) findViewById(R.id.tv_bookingType_status);
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_addressscan);
        tvDropOffAddress = (TextView) findViewById(R.id.tv_drop_off_addressscan);
        tvPackagePickupTime = (TextView) findViewById(R.id.tv_package_pickup_timescan);
        tvPackageDeliveryTime = (TextView) findViewById(R.id.tv_package_delivery_timescan);
        tvPackageStatus = (TextView) findViewById(R.id.tv_package_statusscan);
        tvRecurringPickupType = (TextView) findViewById(R.id.tv_pickup_typescan);
        tvRecurringPickupDetails = (TextView) findViewById(R.id.tv_pickup_detailsscan);
        tvRecurringDeliveryDetails = (TextView) findViewById(R.id.tv_delivery_detailsscan);
        tvPackageLength = (TextView) findViewById(R.id.tv_package_lengthscan);
        tvPackageWidth = (TextView) findViewById(R.id.tv_package_widthscan);
        tvPackageDepth = (TextView) findViewById(R.id.tv_package_heightscan);
        tvPackageWeight = (TextView) findViewById(R.id.tv_package_weightscan);
        tvPackageQuantity = (TextView) findViewById(R.id.tv_package_quantityscan);
        tvPackageValue = (TextView) findViewById(R.id.tv_package_valuescan);
        tvPackageType = (TextView) findViewById(R.id.tv_package_typescan);
        tvPickupAddressNote = (TextView) findViewById(R.id.tv_pickup_address_notescan);
        tvDropoffAddressNote = (TextView) findViewById(R.id.tv_dropoff_address_notescan);
        tvPaymentAmt = (TextView) findViewById(R.id.tv_payment_amtscan);
        tvPaymentMethod = (TextView) findViewById(R.id.tv_payment_methodscan);
        tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_statusscan);
        tvUserUniqueId = (TextView) findViewById(R.id.tv_user_unique_id_scan);
        tvDriverUniqueId = (TextView) findViewById(R.id.tv_driver2_unique_id_scan);
        tvUserName = (TextView) findViewById(R.id.tv_user_namescan);
        tvDriverName = (TextView) findViewById(R.id.tv_driver2_namescan);
        tvEmail = (TextView) findViewById(R.id.tv_emailscan);
        tvDriverEmail = (TextView) findViewById(R.id.tv_emailscandriver2);
        tv_email_label = (TextView) findViewById(R.id.tv_email_labelscan);
        tvPhone = (TextView) findViewById(R.id.tv_phonescan);
        tvDriverPhone = (TextView) findViewById(R.id.tv_phonescandriver2);
        tvPackageId = (TextView) findViewById(R.id.tv_package_idscan);
        tvPackageCode = (TextView) findViewById(R.id.tv_package_codescan);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvEstimatedFareValue = (TextView) findViewById(R.id.tv_estimated_fare_valuescan);

        btntransfer = (Button) findViewById(R.id.btn_transfer);
        btnconfirmt = (Button) findViewById(R.id.btn_confirmt);
        downarrow = (ImageView) findViewById(R.id.iv_downn);
        driverDetails = (RelativeLayout) findViewById(R.id.rl_driver2_detailsscan);
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

        btntransfer.setVisibility(VISIBLE);
        downarrow.setVisibility(GONE);
        driverDetails.setVisibility(GONE);

//        btnDropOffFailure = (Button) findViewById(R.id.btn_drop_off_failurescan);
//        btnDropOffComplete = (Button) findViewById(R.id.btn_drop_offscan);
//        btnCashReceived = (Button) findViewById(R.id.btn_cash_receivedscan);
//        btn_del_cash_received = (Button) findViewById(R.id.btn_del_cash_receivedscan);
//        btnCancelPackage = (Button) findViewById(R.id.btn_cancel_packagescan);
        view_contact1 = (View) findViewById(R.id.view_contact1scan);

        rlBookingTiming = (RelativeLayout) findViewById(R.id.rl_booking_timingscan);
        rlRecurringDetails = (RelativeLayout) findViewById(R.id.rl_recurring_detailsscan);
        rlAddressNotes = (RelativeLayout) findViewById(R.id.rl_address_notesscan);
        rlUserDetails = (RelativeLayout) findViewById(R.id.rl_user_detailsscan);

        ivBack = (ImageView) findViewById(R.id.iv_back);

        tvTitle.setText(getResources().getString(R.string.packagedetailstext));

        btntransfer.setOnClickListener(this);
        btnconfirmt.setOnClickListener(this);

        ivBack.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpen = true;
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
        String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");
//        Log.d("MYPACKAGE_LOG", "language_type_onResume--language_type1----" + language_type);
        String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2, "");
//        Log.d("MYPACKAGE_LOG", "language_type_onResume--language_type2----" + language_type2);
        if (language_type.equals(language_type2)) {
//            Log.d("MYPACKAGE_LOG", "language_type_onResume--equal1--" + language_type);
            if (language_type.equals("1")) {
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            } else if (language_type.equals("2")) {
//                Log.d("MYPACKAGE_LOG", "language_type--onResume_equals=2----" + language_type);
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }
        } else {
//            Log.d("MYPACKAGE_LOG", "language_type_on else----" + language_type + language_type2);
            editor.putString(SharedValues.LANGUAGE_Type2, language_type);
            editor.commit();
//            Log.d("MYPACKAGE_LOG", "editor--commit 1 & 2---" + language_type + language_type2);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

//        LocalBroadcastManager.getInstance(context).registerReceiver(onNotice, new IntentFilter(Global.ONGOING_BROADCAST));

        if (isNetworkAvailable()) {
            getPackageDetail(packageCode);
        } else {
            Snackbar(ivBack);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpen = false;
//        LocalBroadcastManager.getInstance(context).unregisterReceiver(onNotice);
    }

    private void getPackageDetail(final String pacakgeCode) {
        if (getPackageResponseCall != null) {
            getPackageResponseCall.cancel();
        }
        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                PckgPrgsDlg.dismiss();
            }
        }
        if (PckgPrgsDlg == null) {
            PckgPrgsDlg = new ProgressDialog(context);
        }
        PckgPrgsDlg.setCancelable(false);
        PckgPrgsDlg.setMessage(getResources().getString(R.string.gettingdetailsdialog) + "...");
        PckgPrgsDlg.show();

        if (pacakgeCode != null && pacakgeCode.length() > 0) {
            if (webRequest == null) {
                webRequest = WebRequest.getSingleton(this);
            }
            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }
                Global.signInIntent(context);
            } else {
                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {

                    getPackageResponseCall = webRequest.apiInterface.ScangetPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode, driverId);
//                    Log.d(TAG, "------Global.USER_AGENT-------" + Global.USER_AGENT);
//                    Log.d(TAG, "------Accept-------" + Accept);
//                    Log.d(TAG, "------USER_TOKEN-------" + USER_TOKEN);
//                    Log.d(TAG, "------driverId-------" + driverId);
//                    Log.d(TAG, "------pacakgeCode-------" + pacakgeCode);

                    getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
                        @Override
                        public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                            if (response.isSuccessful()) {
                                GetPackageResponse packageResponse = response.body();
//                                Log.d(TAG, "------response.body()-------" + response.body());
                                if (packageResponse != null) {
                                    if (packageResponse.getResponse() == 1) {


                                        String length = "", width = "", depth = "", weight = "", quantity = "", EstValue = "", AddressNote = "";
                                        String bookingType = "", pickupTime = "", deliveryTime = "", delivery_type = "", isWarehousePickup = "", isWarehouseDropoff = "", deliverySession = "";
                                        String pickupAddress = "", pickupLat = "", pickupLng = "";
                                        String dropoffAddress = "", dropoffLat = "", dropoffLng = "", currentDriverId = "";
                                        String userID = "", userName = "", userEmail = "", userNumber = "", countryCode = "";
                                        String paymentStatus = "", packageType = "", EstimatePrice = "", userType = "";

                                        length = packageResponse.getData().getPackage_details().getPackage_length();
                                        width = packageResponse.getData().getPackage_details().getPackage_width();
                                        depth = packageResponse.getData().getPackage_details().getPackage_depth();
                                        weight = packageResponse.getData().getPackage_details().getPackage_weight();
                                        quantity = packageResponse.getData().getPackage_details().getPackage_quantity();
                                        EstValue = packageResponse.getData().getPackage_details().getPackage_est_value();
                                        packageCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        packageID = packageResponse.getData().getPackage_details().getPackage_id();
                                        is_round_trip = packageResponse.getData().getPackage_details().getIs_round_trip();
                                        is_first_round_completed = packageResponse.getData().getPackage_details().getIs_first_round_completed();

                                        Log.d("", "");
                                        packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
                                        isCashOnPickup = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                                        paymentStatus = packageResponse.getData().getPackage_details().getPayment_status();
                                        packageType = packageResponse.getData().getPackage_details().getPackage_type();
                                        packageFinalPrice = packageResponse.getData().getPackage_details().getPending_amount();
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
                                        userType = packageResponse.getData().getUser_details().getType();

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
                                            tvPackageWeight.setText(Global.getPackageKg(weight));
//                                            tvPackageWeight.setText(Global.formatValue(weight));
                                        }
                                        if (quantity != null && quantity.length() > 0) {
                                            tvPackageQuantity.setText(Global.getPackageItems(quantity));
                                        }

                                        if (EstValue != null && EstValue.length() > 0) {
                                            String value = Global.formatValue(EstValue);
                                            String currency = "<font color='#939393'>" + Global.CurrencySymbol + "</font>";
                                            tvPackageValue.setText(Html.fromHtml(value + " " + currency));
                                        }

                                        if (packageCode != null && packageCode.length() > 0) {
                                            tvPackageId.setText(getResources().getString(R.string.bookingidcolon) + " " + packageCode);
                                            tvPackageCode.setText(getResources().getString(R.string.bookingidcolon) + " " + packageCode);
                                            Packagetobetranferred = packageCode;
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
                                                if (userEmail.equals("nomail@nomail.com")) {
                                                    tvEmail.setVisibility(GONE);
                                                    tv_email_label.setVisibility(GONE);
                                                    view_contact1.setVisibility(GONE);
//            tv_Email.setHeight(0);
                                                } else {
                                                    tvEmail.setText(userEmail);
                                                }
                                            }
                                            if (userNumber != null && userNumber.length() > 0) {
                                                if (userType.equals("1") || userType.equals("0")) {
                                                    if (userNumber.startsWith("+255")) {
//                                                        Log.d("User_TYPE", "-----1---255----user type-----" + userType);
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    } else if (userNumber.startsWith("+254")) {
//                                                        Log.d("User_TYPE", "-----1---255----user type-----" + userType);
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    } else if (userNumber.startsWith("+256")) {
//                                                        Log.d("User_TYPE", "---1--256---user type-----" + userType);
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    } else if (userNumber.startsWith("+800")) {
                                                        tvPhone.setText(Global.formatValueNumber(userNumber));
                                                    } else if (userNumber.startsWith("+91")) {
//                                                        Log.d("User_TYPE", "----1--91--user type-----" + userType);
                                                        tvPhone.setText(Global.formatValueNumber2(userNumber));
                                                    } else if (userNumber.startsWith("+44")) {
                                                        tvPhone.setText(Global.formatValueNumber2(userNumber));
                                                    } else {
                                                        tvPhone.setText(userNumber);
                                                    }
                                                } else if (userType.equals("2")) {
//                                                    Log.d("User_TYPE", "-----2---user type-----" + userType);
//                                                    tvPhone.setText("+255"+" "+userNumber);
                                                    tvPhone.setText(countryCode + " " + userNumber);
                                                } else if (userType.equals("4")) {
//                                                    Log.d("User_TYPE", "-----4---user type-----" + userType);
//                                                    tvPhone.setText("+255"+" "+userNumber);
                                                    tvPhone.setText(countryCode + " " + userNumber);
                                                } else {
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
                                                        if(is_round_trip.equals("1")){
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.dailytext)+" (Round Trip)");
                                                        }else {
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
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.weeklytext) + " (Round Trip)");
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
                                                            tvRecurringPickupType.setText(getResources().getString(R.string.monthlytext) + " (Round Trip)");
                                                        }else{
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
                                                    if(preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)) {
                                                        if (deliverySession != null) {
                                                            //Check Delivery Session for Morning or Evening
                                                            if (deliverySession.equalsIgnoreCase("0")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                                                            } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                                                            }
                                                        } else {

                                                        }
                                                    }else{

                                                    if (delivery_type != null) {
                                                        if (delivery_type.equals("0")) {
                                                            tvRecurringDeliveryDetails.setText("Instant");
                                                        } else if (delivery_type.equals("1")) {
                                                            tvRecurringDeliveryDetails.setText("Schedule");
                                                        } else if (delivery_type.equals("2")) {
                                                            tvRecurringDeliveryDetails.setText("Standard");
                                                        } else if (delivery_type.equals("3")) {
                                                            tvRecurringDeliveryDetails.setText("Express");
                                                        } else {
                                                            tvRecurringDeliveryDetails.setText("Overnight");
                                                        }
                                                    } else {
                                                        if (deliverySession != null) {
                                                            //Check Delivery Session for Morning or Evening
                                                            if (deliverySession.equalsIgnoreCase("0")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                                                            } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                                                            }
                                                        }
                                                    }
                                                }
                                                } else {

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
//                                                ||preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                        ||
                                                        preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                    if (delivery_type != null) {
                                                        if (delivery_type.equals("0")) {
                                                            tvPackageDeliveryTime.setText("Instant");
                                                        } else if (delivery_type.equals("1")) {
                                                            tvPackageDeliveryTime.setText("Schedule");
                                                        } else if (delivery_type.equals("2")) {
                                                            tvPackageDeliveryTime.setText("Standard");
                                                        } else if (delivery_type.equals("3")) {
                                                            tvPackageDeliveryTime.setText("Express");
                                                        } else {
                                                            tvPackageDeliveryTime.setText("Overnight");
                                                        }
                                                    } else {

                                                        if (deliveryTime != null) {
                                                            if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);

//                                                                if(is_round_trip.equals("0")) {
                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                                }else{
//                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
//                                                                }
                                                            } else {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                                                if (deliverySession != null) {
                                                                    //Check Delivery Session for Morning or Evening
                                                                    if (deliverySession.equalsIgnoreCase("0")) {
//                                                                        if(is_round_trip.equals("0")) {
                                                                            tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
//                                                                        }else {
//                                                                            tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning) (Round Trip)");
//                                                                        }
                                                                    } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                                        if (is_round_trip.equals("0")) {
                                                                            tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
//                                                                        } else {
//                                                                            tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening) (Round Trip)");
//                                                                        }
                                                                    }
                                                                } else {

                                                                    if(is_round_trip.equals("0")) {
                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                                    }else {
                                                                        tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    //Set DropOff Time
                                                    if (deliveryTime != null) {
                                                        if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                            String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
//                                                            if(is_round_trip.equals("0")) {
                                                                tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                            }else{
//                                                                tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
//                                                            }
                                                        } else {
                                                            String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
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
//                                                                if(is_round_trip.equals("0")) {
                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                                }else {
//                                                                    tvPackageDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "")+" (Round Trip)");
//                                                                }

                                                            }
                                                        }
                                                        if(is_round_trip.equals("1")){
                                                            view_booking23.setVisibility(VISIBLE);
                                                            tv_bookingType_label.setVisibility(VISIBLE);
                                                            tv_bookingType_status.setVisibility(VISIBLE);
                                                            String deliveryType=packageResponse.getData().getBooking_details().getDelivery_type();
                                                            if(deliveryType!=null) {
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
                                                            }else{
                                                                view_booking23.setVisibility(GONE);
                                                                tv_bookingType_label.setVisibility(GONE);
                                                                tv_bookingType_status.setVisibility(GONE);
                                                            }
                                                        }else{
                                                            view_booking23.setVisibility(GONE);
                                                            tv_bookingType_label.setVisibility(GONE);
                                                            tv_bookingType_status.setVisibility(GONE);
                                                        }
                                                    }
                                                }
                                                String pckstatus = Global.getStatus(Integer.parseInt(packageStatus));
                                                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
                                                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA) ||
                                                        preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
                                                    if (packageStatus.equals("5")) {
                                                        tvPackageStatus.setText("In Transit");
                                                    } else {
                                                        tvPackageStatus.setText(pckstatus);
                                                    }
                                                } else {
                                                    tvPackageStatus.setText(pckstatus);
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
//                                                String roundPickup = tvDropOffAddress.getText().toString();
//                                                String roundDropOff = tvPickupAddress.getText().toString();
//                                                tvDropOffAddress.setText(roundDropOff);
//                                                tvPickupAddress.setText(roundPickup);
////                                            } else {
////
////                                            }
//                                        }
                                        if (EstimatePrice != null) {
                                            tvEstimatedFareValue.setText(Global.formatValue("" + EstimatePrice) + " " + Global.CurrencySymbol);
                                        } else {
                                            tvEstimatedFareValue.setVisibility(GONE);
//                                    tvEstimatedFare.setVisibility(GONE);
                                        }

                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                            }
                                        }
                                    } else if (packageResponse.getResponse() == 0) {
                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                Packagetobetranferred = null;
                                                PckgPrgsDlg.dismiss();
                                            }
//                                            btnPickupComplete.setVisibility(GONE);
//                                            btnDropOffComplete.setVisibility(GONE);
//                                            btn_del_cash_received.setVisibility(GONE);
//                                            btnDropOffFailure.setVisibility(GONE);
//                                            btnCashReceived.setVisibility(GONE);
//                                            tvPckgAlreadyComplete.setVisibility(GONE);
                                            String message = packageResponse.getData().getMessage();
//                                            btnCancelPackage.setVisibility(GONE);
                                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                            alert.setCancelable(false);
                                            alert.setMessage(message);
                                            alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    onBackPressed();
                                                }
                                            });
                                            alert.show();
                                        }

                                    } else if (packageResponse.getData().getMessage() != null) {
                                        if (packageResponse.getData().getMessage().equals("Unauthenticated")) {
                                            Packagetobetranferred = null;
                                            Global.signInIntent(GetPackageDetailScanActivity.this);
                                        }
                                    } else {
                                        Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {

                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                Packagetobetranferred = null;
                                                PckgPrgsDlg.dismiss();
                                            }
                                        }
                                    }
                                } else {
                                    if (!(GetPackageDetailScanActivity.this.isFinishing())) {

                                        if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                            Packagetobetranferred = null;
                                            PckgPrgsDlg.dismiss();
                                        }
                                    }
                                }
                            } else {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        Packagetobetranferred = null;
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                            try {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        Packagetobetranferred = null;
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
                        Packagetobetranferred = null;
                        PckgPrgsDlg.dismiss();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_transfer:
                onClickkk = "driverdet";
                openScanner(6);
                break;

            case R.id.iv_back:
                onBackPressed();
                break;


            case R.id.btn_confirmt:
                if(packageStatus.equals("5")) {
                    transferpackage();
                }else{
                    Toast.makeText(activitycon, "We cannot transfer package at this stage, please complete pickup first", Toast.LENGTH_LONG).show();
                onBackPressed();
                }
                break;
        }
    }

    private void transferpackage() {

//        Log.d("tilluuuuu", "Packagetobetranferred = " + Packagetobetranferred);
//        Log.d("tilluuuuu", "todriver = " + todriver);

        if (getTranferResponseCall != null) {
            getTranferResponseCall.cancel();
        }
        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                PckgPrgsDlg.dismiss();
            }
        }
        if (PckgPrgsDlg == null) {
            PckgPrgsDlg = new ProgressDialog(context);
        }
        PckgPrgsDlg.setCancelable(false);
        PckgPrgsDlg.setMessage(getResources().getString(R.string.gettingdetailsdialog)+ "...");
        PckgPrgsDlg.show();

        if (Packagetobetranferred != null && Packagetobetranferred.length() > 0) {
            if (webRequest == null) {
                webRequest = WebRequest.getSingleton(this);
            }
            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
//            Log.d("tilluuuuu", "todriver = " + driverId);
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }
                Global.signInIntent(context);
            } else {
                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {

                    getTranferResponseCall = webRequest.apiInterface.TransferPackagetodriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, Packagetobetranferred, driverId, todriver);
//                    Log.d(TAG, "------Global.USER_AGENT-------" + Global.USER_AGENT);
//                    Log.d(TAG, "------Accept-------" + Accept);
//                    Log.d(TAG, "------USER_TOKEN-------" + USER_TOKEN);
//                    Log.d(TAG, "------driverId-------" + driverId);
//                    Log.d(TAG, "------pacakgeCode-------" + Packagetobetranferred);
//                    Log.d(TAG, "------newdriverId-------" + todriver);

                    getTranferResponseCall.enqueue(new Callback<TransferPackageToDriver>() {
                        @Override
                        public void onResponse(Call<TransferPackageToDriver> call, Response<TransferPackageToDriver> response) {
                            if (response.isSuccessful()) {
                                TransferPackageToDriver transferPackageToDriver = response.body();
//                                Log.d(TAG, "------response.body()-------" + response.body());
                                if (transferPackageToDriver != null) {
                                    if (transferPackageToDriver.getResponse() == 1) {

//                                        String message = transferPackageToDriver.getData().getMessage();

//                                        String length = "", width = "", depth = "", weight = "", quantity = "", EstValue = "", AddressNote = "";
//                                        String bookingType = "", pickupTime = "", deliveryTime = "", delivery_type = "", isWarehousePickup = "", isWarehouseDropoff = "", deliverySession = "";
//                                        String packageStatus = "", pickupAddress = "", pickupLat = "", pickupLng = "";
//                                        String dropoffAddress = "", dropoffLat = "", dropoffLng = "", currentDriverId = "";
//                                        String userID = "", userName = "", userEmail = "", userNumber = "", countryCode = "";
//                                        String paymentStatus = "", packageType = "", EstimatePrice = "", userType = "";
//
//                                        length = packageResponse.getData().getPackage_details().getPackage_length();
//                                        width = packageResponse.getData().getPackage_details().getPackage_width();
//                                        depth = packageResponse.getData().getPackage_details().getPackage_depth();
//                                        weight = packageResponse.getData().getPackage_details().getPackage_weight();
//                                        quantity = packageResponse.getData().getPackage_details().getPackage_quantity();
//                                        EstValue = packageResponse.getData().getPackage_details().getPackage_est_value();
//                                        packageCode = packageResponse.getData().getPackage_details().getPackage_code();
//                                        packageID = packageResponse.getData().getPackage_details().getPackage_id();


                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                                Toast.makeText(context, "The Package has been successfully transferred", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        onBackPressed();


                                    } else if (transferPackageToDriver.getResponse() == 0) {
                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                                PckgPrgsDlg.dismiss();
                                            }
                                            String message = transferPackageToDriver.getData().getMessage();
                                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                            alert.setCancelable(false);
                                            alert.setMessage(message);
                                            alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    onBackPressed();
                                                }
                                            });
                                            alert.show();
                                        }

                                    } else if (transferPackageToDriver.getData().getMessage() != null) {
                                        if (transferPackageToDriver.getData().getMessage().equals("Unauthenticated")) {
                                            Global.signInIntent(GetPackageDetailScanActivity.this);
                                        }
                                    } else {
                                        Toast.makeText(context, "" + transferPackageToDriver.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {

                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {

                                                PckgPrgsDlg.dismiss();
                                            }
                                        }
                                    }
                                } else {
                                    if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                        if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                            PckgPrgsDlg.dismiss();
                                        }
                                    }
                                }
                            } else {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TransferPackageToDriver> call, Throwable t) {
                            try {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
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

    //scanner result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        Log.d("tilluuuuu", "result =" + result);
//        Log.d("tilluuuuu", "onClickkk = " + onClickkk);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                if (onClickkk != null && onClickkk.equalsIgnoreCase("driverdet")) {
//                    Log.d("tilluuuuu", "result" + result);
                    String decodedid = Global.decodeBase(result.getContents());
//                    Log.d("tilluuuuu", "decodedid = " + decodedid);
                    getDriverProfileApi(decodedid);
                } else {
//                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        } else {
        }
    }

    private void getDriverProfileApi(String driverId) {
        if (getProfileResponseCall != null) {
            getProfileResponseCall.cancel();
        }
        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                driverProfileProgress.dismiss();
            }
        }
        if (driverProfileProgress == null) {
            driverProfileProgress = new ProgressDialog(context);
        }

        driverProfileProgress.setMessage(getResources().getString(R.string.gettingdetailsdialog)+ "...");
        driverProfileProgress.setCancelable(false);
        driverProfileProgress.show();


        if (driverId != null && driverId.length() > 0) {
            if (webRequest == null) {
                webRequest = WebRequest.getSingleton(this);
            }

            final String loggedinDriver = preferences.getString(SharedValues.DRIVER_ID, "");
            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;

//            Log.d("tilluuuuu", "todriver = " + driverId);
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                    if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                        driverProfileProgress.dismiss();
                    }
                }
                Global.signInIntent(context);
            } else {
                String country_code = CountryCodeCheck.countrycheck(GetPackageDetailScanActivity.this);

                if (country_code != null && country_code.trim().length() > 0) {

                    //API HIT
//                    Log.d("tilluuuuu", " " + Global.USER_AGENT + " --- " + Accept + " ---" + USER_TOKEN + " --- ");
//                    Log.d("tilluuuuu", driverId + " --- " + driverId);

                    getProfileResponseCall = webRequest.apiInterface.getProfileApi(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId);
                    getProfileResponseCall.enqueue(new Callback<GetProfileResponse>() {
                        @Override
                        public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

//                            Log.d("tilluuuuu", "response.body() = " + response.body());
//                            Log.d("tilluuuuu", "response.isSuccessful() = " + response.isSuccessful());
//                            Log.d("tilluuuuu", "response.errorBody() = " + response.errorBody());

                            if (response.isSuccessful()) {
                                getProfileResponse = response.body();
//                                Log.d("tilluuuuu", "tilluuuuutilluuuuutilluuuuu " + getProfileResponse);
//                                Log.d("tilluuuuu", "tilluuuuutilluuuuutilluuuuu " + getProfileResponse.getData().getMessage());
                                //Check Response
                                if (getProfileResponse.getResponse() == 1) {
                                    //Get Driver Data
                                    String driverId = getProfileResponse.getData().getId();
                                    String driverName = getProfileResponse.getData().getDriver_name();
                                    String driverEmail = getProfileResponse.getData().getDriver_email();
                                    String driverPhone = getProfileResponse.getData().getDriver_phone();
                                    String code = getProfileResponse.getData().getCode();
                                    String driver_uni_id = getProfileResponse.getData().getDriver_unique_id();
//                                String vehicleIdFk = getProfileResponse.getData().getVehicle_id_fk();
//                                String driverImage = getProfileResponse.getData().getDriver_image();
//                                String vehicleType = getProfileResponse.getData().getVehicle_type();
//                                String vehicleName = getProfileResponse.getData().getVehicle_name();
//                                String vehicleNumber = getProfileResponse.getData().getVehicle_number();
//                                String vehicleColor = getProfileResponse.getData().getVehicle_color();
//                                String vehicleOwnerShip = getProfileResponse.getData().getVehicle_ownership();
//                                String isDriverActivate = getProfileResponse.getData().getDriver_active_status();
//                                String driverDeviceId = getProfileResponse.getData().getDriver_device_id();


//                                    Log.d("tilluuuuu", "loggedinDriver = " + loggedinDriver);
//                                    Log.d("tilluuuuu", "driverId = " + driverId);


                                    if (driverId.equals(loggedinDriver)) {
                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                                driverProfileProgress.dismiss();
                                                todriver = null;
                                            }
                                        }
                                        Toast.makeText(GetPackageDetailScanActivity.this, "You can't scan your own code", Toast.LENGTH_SHORT).show();
                                    } else {

                                        btntransfer.setVisibility(GONE);
                                        downarrow.setVisibility(VISIBLE);
                                        driverDetails.setVisibility(VISIBLE);

                                        if (driverId != null && driverId.trim().length() > 0) {
                                            tvDriverUniqueId.setText(driver_uni_id);
                                            todriver = driverId;
                                        }
                                        if (driverName != null && driverName.trim().length() > 0) {
                                            tvDriverName.setText(driverName);
                                        }
                                        if (driverEmail != null && driverEmail.trim().length() > 0) {
                                            tvDriverEmail.setText(driverEmail);
                                        }
                                        if (driverPhone != null && driverPhone.trim().length() > 0) {
                                            if (code != null && code.trim().length() > 0) {
                                                tvDriverPhone.setText(code + " " + driverPhone);
                                            } else {
                                                tvDriverPhone.setText(driverPhone);
                                            }
                                        }

                                        if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                                driverProfileProgress.dismiss();
                                            }
                                        }
                                    }
                                } else if (getProfileResponse.getResponse() == 0) {
                                    if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                        if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                            driverProfileProgress.dismiss();

                                        }
                                        todriver = null;
                                        Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
//                                        String message = getProfileResponse.getData().getMessage();
//                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                                        alert.setCancelable(false);
//                                        alert.setMessage(message);
//                                        alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
////                                                onBackPressed();
//                                            }
//                                        });
//                                        alert.show();
                                    }

                                } else if (getProfileResponse.getData().getMessage() != null) {
                                    if (getProfileResponse.getData().getMessage().equals("Unauthenticated")) {
                                        Global.signInIntent(GetPackageDetailScanActivity.this);
                                        todriver = null;
                                    }
                                } else {
                                    Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
                                    if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                        if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                            driverProfileProgress.dismiss();
                                            todriver = null;
                                            Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                    if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                        driverProfileProgress.dismiss();
                                        todriver = null;
                                        Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                            try {
                                if (!(GetPackageDetailScanActivity.this.isFinishing())) {
                                    if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                        driverProfileProgress.dismiss();
                                        todriver = null;
                                        Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                t.printStackTrace();
                            } catch (Exception e) {
                            }
                        }
                    });

                } else {
                    if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                        driverProfileProgress.dismiss();
                        todriver = null;
                        Toast.makeText(context, "Driver not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
