package com.driver.godel.RefineCode.RefineFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.RecurringDetails;
import com.driver.godel.response.POJO.Example;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ajay2.Sharma on 7/6/2018.
 */

public class OngoingFragment extends GodelDriverFragment implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    View view;
    private LinearLayout layoutBottomSheet, llPickupDeliveryTiming,l1_bookingtype;
    private RelativeLayout rlPickupDeliveryNote, rlRecurringPickupDeliveryTiming;
    private FrameLayout flBookingDetails;
    private BottomSheetBehavior sheetBehavior;
    private Context context;
    private RelativeLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;
    private ImageView ivUp, ivDown;
    private ProgressDialog PckgPrgsDlg;
    private Call<GetPackageResponse> getPackageResponseCall;
    private TextView tvLength, tvWidth, tvDepth, tvWeight, tvQuantity, tvEstValue, tvPickupNote, tvDeliveryNote, tvRecurringType, tvRecurringPickup;
    private TextView tvRecurringDelivery, tvPickupTime, tvDeliveryTime, tvPackageId, tvNoPackage, tvScanInWarehouse,tv_booking_value;
    private Button btnScanPickup, btnScanAttachSticker, btnDropoffPackage;
    private CardView rlPickupDropOff;
    private EditText etPickupAddress, etDropoffAddress;
    static public boolean isScanningPackage = false;
    private static MapView mMapView;
    private GoogleApiClient googleApiClient;
    private static Polyline line;
    private List<LatLng> polylineList;
    private GoogleMap googleMap;
    private Location location;
    private static final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();
    private String pickupLat = "", pickupLng = "", dropoffLat = "", dropoffLng = "";
    public static String click = "scanning";
    private FloatingActionButton fbDirection;
    private String packageStatus = "";
//    SharedPreferences preferences;
    String language_type;
    String  isWarehouseDropoff = "";
    boolean isValidPackage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ongoing_new, container, false);
        context = getActivity();

        Global global = new Global(context);
        global.setCurrencySymbol();
//        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
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

        isScanningPackage = false;
        click = "";
        //================= Initialise widgets =================//
        ivUp = (ImageView) view.findViewById(R.id.iv_up);
        ivDown = (ImageView) view.findViewById(R.id.iv_down);
        tvLength = (TextView) view.findViewById(R.id.tv_length);
        tv_booking_value = (TextView) view.findViewById(R.id.tv_booking_value);
        l1_bookingtype = (LinearLayout) view.findViewById(R.id.l1_bookingtype);
        tvWidth = (TextView) view.findViewById(R.id.tv_width);
        tvDepth = (TextView) view.findViewById(R.id.tv_depth);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        tvQuantity = (TextView) view.findViewById(R.id.tv_quantity);
        tvEstValue = (TextView) view.findViewById(R.id.tv_est_value);
        tvPickupNote = (TextView) view.findViewById(R.id.tv_pickup_note_value);
        tvDeliveryNote = (TextView) view.findViewById(R.id.tv_delivery_note_value);
        tvRecurringType = (TextView) view.findViewById(R.id.tv_recurring_type);
        tvRecurringPickup = (TextView) view.findViewById(R.id.tv_recurring_pickup_timing);
        tvRecurringDelivery = (TextView) view.findViewById(R.id.tv_recurring_delivery_timing);
        tvPickupTime = (TextView) view.findViewById(R.id.tv_pickup_date_time);
        tvDeliveryTime = (TextView) view.findViewById(R.id.tv_delivery_date_time);
        tvPackageId = (TextView) view.findViewById(R.id.tv_packages_id);
        tvNoPackage = (TextView) view.findViewById(R.id.tv_no_package);
        tvScanInWarehouse = (TextView) view.findViewById(R.id.tv_scan_in_warehouse);

        rlPickupDropOff = (CardView) view.findViewById(R.id.cv_locations_addresse);

        btnScanPickup = (Button) view.findViewById(R.id.btn_scan_pickup);
        btnScanAttachSticker = (Button) view.findViewById(R.id.btn_scan_attach_sticker);
        btnDropoffPackage = (Button) view.findViewById(R.id.btn_dropoff_package);

        etPickupAddress = (EditText) view.findViewById(R.id.et_pickup_address);
        etDropoffAddress = (EditText) view.findViewById(R.id.et_dropoff_address);

        rlPickupDeliveryNote = (RelativeLayout) view.findViewById(R.id.rl_address_note);
        rlRecurringPickupDeliveryTiming = (RelativeLayout) view.findViewById(R.id.rl_recurring_pickup_delivery_timing);
        llPickupDeliveryTiming = (LinearLayout) view.findViewById(R.id.ll_pickup_delivery_timing);
        flBookingDetails = (FrameLayout) view.findViewById(R.id.fl_booking_details);
        layoutBottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet);

        fbDirection = (FloatingActionButton) view.findViewById(R.id.fb_direction);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        ll_det_visi_length = (RelativeLayout) view.findViewById(R.id.ll_det_visi_length);
        ll_det_visi_width = (RelativeLayout) view.findViewById(R.id.ll_det_visi_width);
        ll_det_visi_depth = (RelativeLayout) view.findViewById(R.id.ll_det_visi_depth);
        ll_det_visi_weight = (RelativeLayout) view.findViewById(R.id.ll_det_visi_weight);
        ll_det_visi_quan = (RelativeLayout) view.findViewById(R.id.ll_det_visi_quan);
//            ll_det_visi_type = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_type);
        ll_det_visi_value = (RelativeLayout) view.findViewById(R.id.ll_det_visi_value);

        //================= detect bottom sheet events =================//
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                    }
                    break;

                    case BottomSheetBehavior.STATE_EXPANDED: {
                        ivUp.setVisibility(View.GONE);
                        ivDown.setVisibility(View.VISIBLE);
                    }
                    break;

                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        ivUp.setVisibility(View.VISIBLE);
                        ivDown.setVisibility(View.GONE);
                    }
                    break;

                    case BottomSheetBehavior.STATE_DRAGGING: {

                    }
                    break;

                    case BottomSheetBehavior.STATE_SETTLING: {

                    }
                    break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

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

//        if(preferences.getString(GlobalVariables.DISPLAY_PACKAGE_TYPE, "").equals("0")) {
//            ll_det_visi_type.setVisibility(GONE);
//        }else {
//            ll_det_visi_type.setVisibility(VISIBLE);
//        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_VALUE, "").equals("0")) {
            ll_det_visi_value.setVisibility(GONE);
        }else {
           ll_det_visi_value.setVisibility(VISIBLE);
        }

        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);
        layoutBottomSheet.setOnClickListener(this);
        layoutBottomSheet.setOnClickListener(this);
        fbDirection.setOnClickListener(this);
        btnScanPickup.setOnClickListener(this);
        btnScanAttachSticker.setOnClickListener(this);
        btnDropoffPackage.setOnClickListener(this);

        ivUp.setVisibility(View.GONE);
        ivDown.setVisibility(View.VISIBLE);
        btnScanPickup.setVisibility(GONE);
        btnScanAttachSticker.setVisibility(GONE);
        btnDropoffPackage.setVisibility(GONE);
        layoutBottomSheet.setVisibility(GONE);
        flBookingDetails.setVisibility(GONE);
        tvNoPackage.setVisibility(GONE);
        btnDropoffPackage.setVisibility(GONE);
        tvScanInWarehouse.setVisibility(GONE);

        // open bottom sheet default
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mMapView.getMapAsync(this);

        return view;
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Intent broadcaste = new Intent(Global.INACTIVE_BROADCAST);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcaste);
            // intent can contain anydata
            packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
            Log.d("Taggg", "------------packageCode-------"+packageCode);
            if (packageCode != null && packageCode.length() > 0) {

                Log.d("Taggg", "------------packageCode-----78787788--"+packageCode);
                getPackageDetail(packageCode);
            }
        }
    };

    @Override
    public void onResume() {
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
            }
        }else{
            Log.d("MYPACKAGE_LOG","language_type_on else----"+language_type +language_type2);
            editor.putString(SharedValues.LANGUAGE_Type2, language_type);
            editor.commit();
            Log.d("MYPACKAGE_LOG","editor--commit 1 & 2---"+language_type +language_type2);
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
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


        LocalBroadcastManager.getInstance(context).registerReceiver(onNotice, new IntentFilter(Global.ONGOING_BROADCAST));
        packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");

        Log.d("Taggg", "------------packageCode----22---"+packageCode);
        if (packageCode != null && packageCode.length() > 0) {
            getPackageDetail(packageCode);
        } else {
            layoutBottomSheet.setVisibility(GONE);
            flBookingDetails.setVisibility(GONE);
            tvNoPackage.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(onNotice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_up:
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ivUp.setVisibility(View.GONE);
                ivDown.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_down:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                ivUp.setVisibility(View.VISIBLE);
                ivDown.setVisibility(View.GONE);
                break;

            case R.id.bottom_sheet:
                if (ivDown.getVisibility() == View.VISIBLE) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    ivUp.setVisibility(View.VISIBLE);
                    ivDown.setVisibility(View.GONE);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    ivUp.setVisibility(View.GONE);
                    ivDown.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_scan_pickup:
                MyPackagesActivity.openScanner(0);
                click = "Barcode";


//                Log.d("TAgg","---------------packageCode--------11------"+packageCode);
//             final String pacakgeCode=packageCode;
//                if (pacakgeCode != null && pacakgeCode.length() > 0) {
//
//                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
//                    String Accept = Global.ACCEPT;
//                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");
//                    if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
//
////                        if (!(MyPackagesActivity.this.isFinishing())) {
////                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                PckgPrgsDlg.dismiss();
////                            }
////                        }
//                        Global.signInIntent(context);
//                    } else {
//                        String country_code = CountryCodeCheck.countrycheck(context);
//                        if (country_code != null && country_code.trim().length() > 0) {
//
//
//                            getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode);
//                            getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
//                                @Override
//                                public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
////                                    try {
////                                        if (!(MyPackagesActivity.this.isFinishing())) {
////                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                                PckgPrgsDlg.dismiss();
////                                            }
////                                        }
////                                    } catch (Exception ex) {
////                                    }
//
//                                    if (response.isSuccessful()) {
//                                        GetPackageResponse packageResponse = response.body();
//                                        if (packageResponse != null) {
//                                    Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()------------"+packageResponse.getResponse());
//
//                                            if (packageResponse.getResponse() == 1) {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------111------"+packageResponse.getResponse());
//                                                String selectedPckgCode = packageResponse.getData().getPackage_details().getPackage_code();
//                                                String packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
//                                                if (packageCode != null && packageCode.length() > 0 && selectedPckgCode != null && selectedPckgCode.length() > 0 && (!packageCode.equalsIgnoreCase(selectedPckgCode))) {
////                                                    Toast.makeText(con, "Scanned package not matched with ongoing package", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    String packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
////                                            Log.d("MYPACKAGE_LOG","-----packageStatus-------------"+packageStatus);
//                                                    if (packageStatus != null && packageStatus.trim().length() > 0 && Integer.parseInt(packageStatus) < 5) {
//
//                                                        if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("1")) {
//                                                            Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                            intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                            intent.putExtra("packageResponse", "" + packageResponse);
//                                                            startActivity(intent);
//                                                        }
////                                                      else if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("2")) {
////                                                    Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
////                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
////                                                    intent.putExtra("packageResponse", "" + packageResponse);
////                                                    startActivity(intent);
////                                                }
//                                                        else {
//                                                            if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("1")) {
//                                                                //Pass Intent to Get Package Detail Activity
//                                                                Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                                intent.putExtra("packageResponse", "" + packageResponse);
//                                                                startActivity(intent);
//                                                            }
////                                                    else if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("2")) {
////                                                        //Pass Intent to Get Package Detail Activity
////                                                        Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
////                                                        intent.putExtra("PackageCode", "" + pacakgeCode);
////                                                        intent.putExtra("packageResponse", "" + packageResponse);
////                                                        startActivity(intent);
////                                                    }
//                                                            else {
//                                                                String isDOP = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
//                                                                if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("1")) {
//                                                                    Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                                    intent.putExtra("packageResponse", "" + packageResponse);
//                                                                    startActivity(intent);
//                                                                } else if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("2")) {
//                                                                    Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                                    intent.putExtra("packageResponse", "" + packageResponse);
//                                                                    startActivity(intent);
//                                                                } else {
//                                                                    new android.support.v7.app.AlertDialog.Builder(context)
//                                                                            .setMessage(getResources().getString(R.string.packagepaymentisincomplete))
//                                                                            .setCancelable(false).setPositiveButton(getResources().getString(R.string.oktext), null)
//                                                                            .create()
//                                                                            .show();
//                                                                }
//                                                            }
//                                                        }
//                                                    } else {
//                                                        Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                        intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                        intent.putExtra("packageResponse", "" + packageResponse);
//                                                        startActivity(intent);
//                                                    }
//                                                }
//                                            } else if (response.body().getData().getMessage() != null) {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getResponse());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+response.body().getData().getMessage());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getData().getMessage());
//
//                                                if (response.body().getData().getMessage().trim().equals("Unauthenticated")) {
//                                                    Global.signInIntent(context);
//                                                }
//                                                Toast.makeText(context, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                            } else {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+packageResponse.getResponse());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+response.body().getData().getMessage());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else-----"+packageResponse.getData().getMessage());
//
//                                                Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<GetPackageResponse> call, Throwable t) {
//
//                                    Toast.makeText(context, "" + "Failure", Toast.LENGTH_SHORT).show();
////                                    try {
////                                        if (!(MyPackagesActivity.this.isFinishing())) {
////                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                                PckgPrgsDlg.dismiss();
////                                            }
////                                        }
////                                        t.printStackTrace();
////
////                                    } catch (Exception e) {
////
////                                    }
//                                }
//                            });
//
//                        } else {
////                            if (!(MyPackagesActivity.this.isFinishing())) {
////                                if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                    PckgPrgsDlg.dismiss();
////                                }
////                            }
//                        }
//
//                    }
//
//                }
                break;

            case R.id.btn_scan_attach_sticker:
                click = "Sticker";
                MyPackagesActivity.openScanner(1);
                break;

            case R.id.btn_dropoff_package:
                MyPackagesActivity.openScanner(2);
                click = "delivery";


//                Log.d("TAgg","---------------packageCode--------222------"+packageCode);
////                final String pacakgeCode=packageCode;
//                if (packageCode != null && packageCode.length() > 0) {
//
//                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
//                    String Accept = Global.ACCEPT;
//                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");
//                    if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
//
////                        if (!(MyPackagesActivity.this.isFinishing())) {
////                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                PckgPrgsDlg.dismiss();
////                            }
////                        }
//                        Global.signInIntent(context);
//                    } else {
//                        String country_code = CountryCodeCheck.countrycheck(context);
//                        if (country_code != null && country_code.trim().length() > 0) {
//
//
//                            getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, packageCode);
//                            getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
//                                @Override
//                                public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
////                                    try {
////                                        if (!(MyPackagesActivity.this.isFinishing())) {
////                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                                PckgPrgsDlg.dismiss();
////                                            }
////                                        }
////                                    } catch (Exception ex) {
////                                    }
//
//                                    if (response.isSuccessful()) {
//                                        GetPackageResponse packageResponse = response.body();
//                                        if (packageResponse != null) {
//                                            Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()------------"+packageResponse.getResponse());
//
//                                            if (packageResponse.getResponse() == 1) {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------111------"+packageResponse.getResponse());
//                                                String selectedPckgCode = packageResponse.getData().getPackage_details().getPackage_code();
//                                                String packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
//                                                if (packageCode != null && packageCode.length() > 0 && selectedPckgCode != null && selectedPckgCode.length() > 0 && (!packageCode.equalsIgnoreCase(selectedPckgCode))) {
////                                                    Toast.makeText(con, "Scanned package not matched with ongoing package", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    String packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
////                                            Log.d("MYPACKAGE_LOG","-----packageStatus-------------"+packageStatus);
//                                                    if (packageStatus != null && packageStatus.trim().length() > 0 && Integer.parseInt(packageStatus) < 5) {
//
//                                                        if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("1")) {
//                                                            Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                            intent.putExtra("PackageCode", "" + packageCode);
//                                                            intent.putExtra("packageResponse", "" + packageResponse);
//                                                            startActivity(intent);
//                                                        }
////                                                      else if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("2")) {
////                                                    Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
////                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
////                                                    intent.putExtra("packageResponse", "" + packageResponse);
////                                                    startActivity(intent);
////                                                }
//                                                        else {
//                                                            if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("1")) {
//                                                                //Pass Intent to Get Package Detail Activity
//                                                                Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                intent.putExtra("PackageCode", "" + packageCode);
//                                                                intent.putExtra("packageResponse", "" + packageResponse);
//                                                                startActivity(intent);
//                                                            }
////                                                    else if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("2")) {
////                                                        //Pass Intent to Get Package Detail Activity
////                                                        Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
////                                                        intent.putExtra("PackageCode", "" + pacakgeCode);
////                                                        intent.putExtra("packageResponse", "" + packageResponse);
////                                                        startActivity(intent);
////                                                    }
//                                                            else {
//                                                                String isDOP = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
//                                                                if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("1")) {
//                                                                    Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                    intent.putExtra("PackageCode", "" + packageCode);
//                                                                    intent.putExtra("packageResponse", "" + packageResponse);
//                                                                    startActivity(intent);
//                                                                } else if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("2")) {
//                                                                    Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                                    intent.putExtra("PackageCode", "" + packageCode);
//                                                                    intent.putExtra("packageResponse", "" + packageResponse);
//                                                                    startActivity(intent);
//                                                                } else {
//                                                                    new android.support.v7.app.AlertDialog.Builder(context)
//                                                                            .setMessage(getResources().getString(R.string.packagepaymentisincomplete))
//                                                                            .setCancelable(false).setPositiveButton(getResources().getString(R.string.oktext), null)
//                                                                            .create()
//                                                                            .show();
//                                                                }
//                                                            }
//                                                        }
//                                                    } else {
//                                                        Intent intent = new Intent(context, GetPackageDetailActivity.class);
//                                                        intent.putExtra("PackageCode", "" + packageCode);
//                                                        intent.putExtra("packageResponse", "" + packageResponse);
//                                                        startActivity(intent);
//                                                    }
//                                                }
//                                            } else if (response.body().getData().getMessage() != null) {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getResponse());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+response.body().getData().getMessage());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getData().getMessage());
//
//                                                if (response.body().getData().getMessage().trim().equals("Unauthenticated")) {
//                                                    Global.signInIntent(context);
//                                                }
//                                                Toast.makeText(context, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                            } else {
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+packageResponse.getResponse());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+response.body().getData().getMessage());
////                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else-----"+packageResponse.getData().getMessage());
//
//                                                Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<GetPackageResponse> call, Throwable t) {
//
//                                    Toast.makeText(context, "" + "Failure", Toast.LENGTH_SHORT).show();
////                                    try {
////                                        if (!(MyPackagesActivity.this.isFinishing())) {
////                                            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                                PckgPrgsDlg.dismiss();
////                                            }
////                                        }
////                                        t.printStackTrace();
////
////                                    } catch (Exception e) {
////
////                                    }
//                                }
//                            });
//
//                        } else {
////                            if (!(MyPackagesActivity.this.isFinishing())) {
////                                if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
////                                    PckgPrgsDlg.dismiss();
////                                }
////                            }
//                        }
//
//                    }
//
//                }

                break;

            case R.id.fb_direction:
                if (packageStatus != null && packageStatus.trim().length() > 0) {
                    if (Integer.parseInt(packageStatus) < 5) {
                        Uri.Builder directionsBuilder = new Uri.Builder()
                                .scheme("https")
                                .authority("www.google.com")
                                .appendPath("maps")
                                .appendPath("dir")
                                .appendPath("")
                                .appendQueryParameter("api", "1")
                                .appendQueryParameter("destination", pickupLat + "," + pickupLng);
                        startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
                    } else {
                        Uri.Builder directionsBuilder = new Uri.Builder()
                                .scheme("https")
                                .authority("www.google.com")
                                .appendPath("maps")
                                .appendPath("dir")
                                .appendPath("")
                                .appendQueryParameter("api", "1")
                                .appendQueryParameter("destination", dropoffLat + "," + dropoffLng);

                        startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
                    }
                }
                break;
        }
    }


    private void getPackageDetail(final String pacakgeCode) {
        if (getPackageResponseCall != null) {
            getPackageResponseCall.cancel();
        }

        if (context != null) {
            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                PckgPrgsDlg.dismiss();

            }
        }

        try {
            PckgPrgsDlg = new ProgressDialog(context);
            PckgPrgsDlg = PckgPrgsDlg.show(context, "", "Loading...");
            Log.d("MYPACKAGE_LOG","--------loading----");
        } catch (Exception e) {

        }
        Log.d("MYPACKAGE_LOG","-----after loading----");

        if (packageCode != null && packageCode.length() > 0) {
            Log.d("MYPACKAGE_LOG","-----packageCode != null----");
            Log.d("MYPACKAGE_LOG","-----packageCode != null----"+pacakgeCode);

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                Log.d("MYPACKAGE_LOG","-----USER_TOKEN----"+USER_TOKEN);
                if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                    PckgPrgsDlg.dismiss();
                }
                Global.signInIntent(context);
            } else {
                String country_code = CountryCodeCheck.countrycheck(context);
                if (country_code != null && country_code.trim().length() > 0) {
                    Log.d("MYPACKAGE_LOG","-----USER_TOKEN--   -----else--"+USER_TOKEN);
                    Log.d("MYPACKAGE_LOG","-----pacakgeCode------------   -----else---------"+pacakgeCode);
//                    getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, "437c8b36");
                    getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode);
                    getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
                        @Override
                        public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {


                            Log.d("Tagg","-------------response---------------"+response);
                            Log.d("Tagg","-------------response---------------"+response.body());
                            try {
                                if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                    PckgPrgsDlg.dismiss();
                                }
                            } catch (Exception ex) {
                            }

                            if (response.isSuccessful()) {
                                GetPackageResponse packageResponse = response.body();
                                Log.d("MYPACKAGE_LOG","-----response.body()------in fragment------>>"+response.body());

                                if (packageResponse != null) {
                                    if (packageResponse.getResponse() == 1) {
                                        String length = "", width = "", depth = "", weight = "", quantity = "", EstValue = "", AddressNote = "";
                                        String bookingType = "", pickupTime = "", deliveryTime = "",deliveryType = "", isWarehousePickup = "", deliverySession = "";
                                        String packageCode = "", pickupAddress = "";
                                        String dropoffAddress = "", currentDriverId = "",isCashOnPickup ="";

                                        length = packageResponse.getData().getPackage_details().getPackage_length();
                                        width = packageResponse.getData().getPackage_details().getPackage_width();
                                        depth = packageResponse.getData().getPackage_details().getPackage_depth();
                                        weight = packageResponse.getData().getPackage_details().getPackage_weight();
                                        quantity = packageResponse.getData().getPackage_details().getPackage_quantity();
                                        EstValue = packageResponse.getData().getPackage_details().getPackage_est_value();
                                        packageCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        Log.d("TAG","---------packageCode----------"+packageCode);
                                        packageCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
                                        Log.d("TAG","---------packageStatus----------"+packageStatus);
                                        AddressNote = packageResponse.getData().getLocation_details().getAddress_note();
                                        bookingType = packageResponse.getData().getBooking_details().getBooking_type();
                                        deliveryType = packageResponse.getData().getBooking_details().getDelivery_type();
                                        pickupTime = packageResponse.getData().getBooking_details().getBooking_pickup_datetime();
                                        deliveryTime = packageResponse.getData().getBooking_details().getBooking_delivery_datetime();
                                        deliverySession = packageResponse.getData().getBooking_details().getDelivery_session();
                                        isCashOnPickup = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();

                                        Log.d("NOTIFICATIONS_TAG","---------packageResponse.getData()---------"+packageResponse.getData());

                                        isWarehousePickup = packageResponse.getData().getIs_warehouse_pickup();
                                        isWarehouseDropoff = packageResponse.getData().getIs_warehouse_dropoff();
                                        currentDriverId = packageResponse.getData().getCurrent_driver_id();

                                       isValidPackage = true;
                                       Boolean isDriverIdMatched = false;
                                        String message = "";
                                        message=packageResponse.getData().getMessage();
                                        Log.d("NOTIFICATIONS_TAG","-------message--------------------"+message);

                                        boolean isPackageTransfered = false;
                                        if (currentDriverId != null && currentDriverId.length() > 0 && Integer.parseInt(packageStatus) != 10) {
                                            if (!(driverId.trim().equalsIgnoreCase(currentDriverId.trim()))) {
                                                isPackageTransfered = true;
                                                isValidPackage = false;
                                                showAlert("Package assigned to another driver");
                                            }
                                        }
                                        Log.d("MYPACKAGE_LOG","-----packageStatus------in fragment------>>"+packageStatus);
                                        if (packageStatus != null && packageStatus.length() > 0 && isPackageTransfered == false) {
                                            switch (Integer.parseInt(packageStatus)) {

//                                                case (-1):
//                                                    showAlert("Package is deleted");
//                                                    isValidPackage = false;
//                                                    btnScanPickup.setVisibility(GONE);
//                                                    btnScanAttachSticker.setVisibility(GONE);
//                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
//                                                        btnDropoffPackage.setVisibility(VISIBLE);
//                                                        tvScanInWarehouse.setVisibility(GONE);
//                                                    } else {
//                                                        tvScanInWarehouse.setVisibility(VISIBLE);
//                                                        btnDropoffPackage.setVisibility(GONE);
//                                                    }
//                                                    break;
                                                case 0:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(VISIBLE);
                                                    btnScanAttachSticker.setVisibility(VISIBLE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                                case 1:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(VISIBLE);
                                                    btnScanAttachSticker.setVisibility(VISIBLE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                                case 2:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(VISIBLE);
                                                    btnScanAttachSticker.setVisibility(VISIBLE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                                case 3:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(VISIBLE);
                                                    btnScanAttachSticker.setVisibility(VISIBLE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                                case 4:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(VISIBLE);
                                                    btnScanAttachSticker.setVisibility(VISIBLE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                                case 5:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 6:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 7:
                                                    showAlert("Package delivered");
                                                    isValidPackage = false;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 8:

                                                    showAlert("Package is cancelled");
                                                    isValidPackage = false;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 9:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 10:
                                                    showAlert("Package scan-in by warehouse");
                                                    isValidPackage = false;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 11:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 12:
                                                    isValidPackage = true;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                                        btnDropoffPackage.setVisibility(VISIBLE);
                                                        tvScanInWarehouse.setVisibility(GONE);
                                                    } else {
                                                        tvScanInWarehouse.setVisibility(VISIBLE);
                                                        btnDropoffPackage.setVisibility(GONE);
                                                    }
                                                    break;
                                                case 13:
                                                    isValidPackage = false;
                                                    btnScanPickup.setVisibility(GONE);
                                                    btnScanAttachSticker.setVisibility(GONE);
                                                    btnDropoffPackage.setVisibility(GONE);
                                                    tvScanInWarehouse.setVisibility(GONE);
                                                    break;
                                            }
                                        }

                                        if (isValidPackage == true) {
                                            layoutBottomSheet.setVisibility(VISIBLE);
                                            flBookingDetails.setVisibility(VISIBLE);
                                            tvNoPackage.setVisibility(GONE);
                                            if (pacakgeCode != null && packageCode.length() > 0) {
                                                tvPackageId.setText(packageCode);
                                            }
                                            if (length != null && length.length() > 0) {
                                                tvLength.setText(Global.getPackageCm(length));
                                            }
                                            if (width != null && width.length() > 0) {
                                                tvWidth.setText(Global.getPackageCm(width));
                                            }
                                            if (depth != null && depth.length() > 0) {
                                                tvDepth.setText(Global.getPackageCm(depth));
                                            }
                                            if (weight != null && weight.length() > 0) {
                                                tvWeight.setText(Global.getPackageKg(weight));
                                            }
                                            if (quantity != null && quantity.length() > 0) {
                                                tvQuantity.setText(Global.getPackageItems(quantity));
                                            }
                                            if (EstValue != null && EstValue.length() > 0) {
                                                String value = Global.formatValue(EstValue);
                                                String currency = "<font color='#939393'>"+Global.CurrencySymbol+"</font>";
                                                CharSequence chars = Html.fromHtml(value + " " + currency);
                                                tvEstValue.setText(chars);
                                            }
                                            // add address notes
                                            if (AddressNote != null && AddressNote.trim().length() > 0) {
                                                rlPickupDeliveryNote.setVisibility(VISIBLE);
                                                String def[] = AddressNote.split(Global.AddressNoteSeprator);
                                                for (int i = 0; i < def.length; i++) {
                                                    if (def[i] != null || def[i].length() > 0) {
                                                        if (i == 0) {
                                                            if (def[i].replaceAll(Global.AddressNoteSeprator, "").length() > 0) {
                                                                tvPickupNote.setText(def[i]);
                                                                tvPickupNote.setTextColor(Color.parseColor("#000000"));
                                                            } else {
                                                                tvPickupNote.setText(getResources().getString(R.string.nopickupaddressnote));
                                                            }
                                                        }
                                                        if (i == 1) {
                                                            if (def[i].replaceAll(Global.AddressNoteSeprator, "").length() > 0) {
                                                                tvDeliveryNote.setText(def[i].replaceAll(Global.AddressNoteSeprator, ""));
//                                                                tvDeliveryNote.setTextColor(context.getResources().getColor(R.color.blackColor));
                                                                tvDeliveryNote.setTextColor(Color.parseColor("#000000"));
                                                            } else {
                                                                tvDeliveryNote.setText(getResources().getString(R.string.nodeliveryaddressnote));
                                                            }
                                                        }
                                                    } else {
                                                        if (i == 0) {
                                                            tvPickupNote.setText(getResources().getString(R.string.nopickupaddressnote));
                                                        }
                                                        if (i == 1) {
                                                            tvDeliveryNote.setText(getResources().getString(R.string.nodeliveryaddressnote));
                                                        }
                                                    }
                                                }
                                            } else {
                                                rlPickupDeliveryNote.setVisibility(GONE);
                                            }

                                            // pickup and drop timings
                                            if (bookingType != null && bookingType.trim().length() > 0) {
                                                if (bookingType.equalsIgnoreCase("2")) {
                                                    RecurringDetails recurringDetails = packageResponse.getData().getRecurring_details();
                                                    //Check Recurring Details
                                                    if (recurringDetails != null) {
                                                        //Hide Pickup
                                                        llPickupDeliveryTiming.setVisibility(GONE);
                                                        //Show Recurring Details
                                                        rlRecurringPickupDeliveryTiming.setVisibility(VISIBLE);
                                                        //Check Recurring Status for Daily Basis
                                                        if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("0")) {
                                                            //Set Recurring Type
                                                            if(packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")){
                                                                tvRecurringType.setText(getResources().getString(R.string.dailytext)+" (Round Trip)");
                                                            }else {
                                                                tvRecurringType.setText(getResources().getString(R.string.dailytext));
                                                            }
                                                            //Get Recurring Day Time
                                                            String recurringDayTime = packageResponse.getData().getRecurring_details().getRecurring_day_time();
                                                            //Check Recurring Day Time
                                                            if (recurringDayTime != null) {
                                                                //Set Recurring Timing
                                                                tvRecurringPickup.setText(recurringDayTime);
                                                            }
                                                        }
                                                        //Check Recurring Status for Weekly Basis
                                                        else if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("1")) {
                                                            //Set Recurring Type
                                                            if(packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")) {
                                                                tvRecurringType.setText(getResources().getString(R.string.weeklytext)+" (Round Trip)");
                                                            }else {
                                                                tvRecurringType.setText(getResources().getString(R.string.weeklytext));
                                                            }
                                                            //Get Recurring Week Day
                                                            int recurringWeekDay = Integer.parseInt(packageResponse.getData().getRecurring_details().getRecurring_day());
                                                            //Get Recurring Day Time
                                                            String recurringDayTime = packageResponse.getData().getRecurring_details().getRecurring_day_time();
                                                            //Check Recurring Day Time and Recurring Day
                                                            String dayOfWeek = Global.getPickupDay(recurringWeekDay);
                                                            if (recurringDayTime != null) {
                                                                //Set Recurring Timing and DayOfWeek
                                                                tvRecurringPickup.setText(dayOfWeek + " " + recurringDayTime);
                                                            }
                                                        }
                                                        //Check Recurring Status for Monthly Basis
                                                        else if (packageResponse.getData().getRecurring_details().getRecurring_pickup_type().equalsIgnoreCase("2")) {
                                                            //Set Recurring Type
                                                            if(packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")) {
                                                                tvRecurringType.setText(getResources().getString(R.string.monthlytext)+" (Round Trip)");
                                                            }else {
                                                                tvRecurringType.setText(getResources().getString(R.string.monthlytext));
                                                            }
                                                            //Get Recurring Week
                                                            String recurringWeek = packageResponse.getData().getRecurring_details().getRecurring_week();
                                                            //Check Recurring Week and Day Time
                                                            if (recurringWeek != null) {
                                                                //Set Recurring Week and Day Time
                                                                tvRecurringPickup.setText(recurringWeek);
                                                            }
                                                        }
                                                        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                                                        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                                ||
                                                                preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                            if(deliveryType!=null) {
                                                                if (deliveryType.equals("0")) {
                                                                    tvRecurringDelivery.setText("Instant");
                                                                } else if (deliveryType.equals("1")) {
                                                                    tvRecurringDelivery.setText("Schedule");
                                                                } else if (deliveryType.equals("2")) {
                                                                    tvRecurringDelivery.setText("Standard");
                                                                } else if (deliveryType.equals("3")) {
                                                                    tvRecurringDelivery.setText("Express");
                                                                } else  {
                                                                    tvRecurringDelivery.setText("Overnight");
                                                                }
                                                            }else {
                                                                if (deliverySession != null) {
                                                                    //Check Delivery Session for Morning or Evening
                                                                    if (deliverySession.equalsIgnoreCase("0")) {
                                                                        tvRecurringDelivery.setText(getResources().getString(R.string.morningtext));
                                                                    } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                        tvRecurringDelivery.setText(getResources().getString(R.string.eveningtext));
                                                                    }
                                                                }
                                                            }
                                                        }else {
                                                            //Check Recuurence Status for Delivery
                                                            if (deliverySession != null) {
                                                                //Check Delivery Session for Morning or Evening
                                                                if (deliverySession.equalsIgnoreCase("0")) {
                                                                    tvRecurringDelivery.setText(getResources().getString(R.string.morningtext));
                                                                } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                    tvRecurringDelivery.setText(getResources().getString(R.string.eveningtext));
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    //Hide Recurring Details
                                                    rlRecurringPickupDeliveryTiming.setVisibility(GONE);
                                                    //Show Pickup
                                                    llPickupDeliveryTiming.setVisibility(VISIBLE);
                                                    //Set Pickup Time
                                                    if (pickupTime != null) {
                                                        if (bookingType != null && bookingType.equalsIgnoreCase("0")) {
                                                            String packagePickupTime = Global.getConvertedDateTime(pickupTime);
                                                            tvPickupTime.setText(packagePickupTime.replaceAll("00:00:00", ""));
                                                        } else {
                                                            String packagePickupTime = Global.getConvertedPickupDateTime(pickupTime);
                                                            tvPickupTime.setText(packagePickupTime.replaceAll("00:00:00", ""));
                                                        }
                                                    }
                                                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                                                         || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                                            ||
                                                            preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                                                        if(deliveryType!=null) {
                                                        if (deliveryType.equals("0")) {
                                                            tvDeliveryTime.setText("Instant");
                                                        } else if (deliveryType.equals("1")) {
                                                            tvDeliveryTime.setText("Schedule");
                                                        } else if (deliveryType.equals("2")) {
                                                            tvDeliveryTime.setText("Standard");
                                                        } else if (deliveryType.equals("3")) {
                                                            tvDeliveryTime.setText("Express");
                                                        } else  {
                                                            tvDeliveryTime.setText("Overnight");
                                                        }
                                                       }else {
                                                        if (deliveryTime != null) {
                                                            if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
                                                                tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                            } else {
                                                                String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                                                if (deliverySession != null) {
                                                                    //Check Delivery Session for Morning or Evening
                                                                    if (deliverySession.equalsIgnoreCase("0")) {
                                                                        tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
                                                                    } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                        tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
                                                                    }
                                                                } else {
                                                                    tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    }else {

                                                    //Check Recuurence Status for Delivery


                                                    if (deliveryTime != null) {
                                                        if (isWarehouseDropoff.equalsIgnoreCase("true")) {
                                                            String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
                                                            tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                        } else {
                                                            String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                                            if (deliverySession != null) {
                                                                //Check Delivery Session for Morning or Evening
                                                                if (deliverySession.equalsIgnoreCase("0")) {
                                                                    tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
                                                                } else if (deliverySession.equalsIgnoreCase("1")) {
                                                                    tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
                                                                }
                                                            } else {
                                                                tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
                                                            }
                                                        }
                                                    }
                                                    if(packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")){
                                                        l1_bookingtype.setVisibility(VISIBLE);
                                                        if(deliveryType!=null) {
                                                            if (deliveryType.equals("0")) {
                                                                tv_booking_value.setText("Instant (Round Trip)");
                                                            } else if (deliveryType.equals("1")) {
                                                                tv_booking_value.setText("Schedule (Round Trip)");
                                                            } else if (deliveryType.equals("2")) {
                                                                tv_booking_value.setText("Standard (Round Trip)");
                                                            } else if (deliveryType.equals("3")) {
                                                                tv_booking_value.setText("Express (Round Trip)");
                                                            } else {
                                                                tv_booking_value.setText("Overnight (Round Trip)");
                                                            }
                                                        }else{
                                                            l1_bookingtype.setVisibility(GONE);
                                                        }
                                                    }else {
                                                        l1_bookingtype.setVisibility(GONE);
                                                    }


                                                }
                                                    //Set DropOff Time
//                                                    if (deliveryTime != null) {
//                                                        if (isWarehouseDropoff.equalsIgnoreCase("true")) {
//                                                            String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
//                                                            tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                        } else {
//                                                            String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
//                                                            if (deliverySession != null) {
//                                                                //Check Delivery Session for Morning or Evening
//                                                                if (deliverySession.equalsIgnoreCase("0")) {
//                                                                    tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Morning)");
//                                                                } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                                    tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", "") + " (Evening)");
//                                                                }
//                                                            } else {
//                                                                tvDeliveryTime.setText(packageDeliveryTime.replaceAll("00:00:00", ""));
//                                                            }
//                                                        }
//                                                    }
                                                }
                                            }
                                            //Show details according to package status
                                            //set pickup address
                                            if (isWarehousePickup != null) {
                                                if (isWarehousePickup.equalsIgnoreCase("true")) {
//                                                    if (packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")){
//                                                        if (packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")){

                                                            pickupAddress = packageResponse.getData().getWarehouse_pickup_address().get(0).getAddress();
                                                            pickupLat = packageResponse.getData().getWarehouse_pickup_address().get(0).getLatitude();
                                                            pickupLng = packageResponse.getData().getWarehouse_pickup_address().get(0).getLongtitude();
//                                                        }else{
//
//                                                            pickupAddress = packageResponse.getData().getWarehouse_pickup_address().get(0).getAddress();
//                                                            pickupLat = packageResponse.getData().getWarehouse_pickup_address().get(0).getLatitude();
//                                                            pickupLng = packageResponse.getData().getWarehouse_pickup_address().get(0).getLongtitude();
//
////                                                            pickupAddress = packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress();
////                                                            pickupLat = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLatitude();
////                                                            pickupLng = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLongtitude();
//                                                             }
//
//                                                } else {
//                                                        pickupAddress = packageResponse.getData().getWarehouse_pickup_address().get(0).getAddress();
//                                                        pickupLat = packageResponse.getData().getWarehouse_pickup_address().get(0).getLatitude();
//                                                        pickupLng = packageResponse.getData().getWarehouse_pickup_address().get(0).getLongtitude();
//
//                                                    }
                                                    //Set WareHouse Pickup Address
                                                    if (pickupAddress != null) {
                                                        etPickupAddress.setText(pickupAddress);
                                                    }
                                                } else {
                                                    if (packageResponse.getData().getLocation_details() != null) {
                                                        if (packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")) {
                                                            if (packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")) {

                                                                pickupLat = packageResponse.getData().getLocation_details().getPickup_location_lat();
                                                                pickupLng = packageResponse.getData().getLocation_details().getPickup_location_lng();
                                                                String pickupHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                                                String pickupStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                                                String pickupLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                                                String exactpickupAddress = packageResponse.getData().getLocation_details().getPickup_location();
                                                                pickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, exactpickupAddress);
                                                            } else {
                                                                pickupLat = packageResponse.getData().getLocation_details().getRound_pickup_location_lat();
                                                                pickupLng = packageResponse.getData().getLocation_details().getRound_pickup_location_lng();
                                                                String pickupHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                                                String pickupStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                                                String pickupLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                                                String exactpickupAddress = packageResponse.getData().getLocation_details().getRound_pickup_location();
                                                                pickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, exactpickupAddress);

                                                            }
                                                        }else {
                                                            pickupLat = packageResponse.getData().getLocation_details().getPickup_location_lat();
                                                            pickupLng = packageResponse.getData().getLocation_details().getPickup_location_lng();
                                                            String pickupHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                                            String pickupStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                                            String pickupLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                                            String exactpickupAddress = packageResponse.getData().getLocation_details().getPickup_location();
                                                            pickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, exactpickupAddress);

                                                        }
                                                        //Set User Pickup Location
                                                        if (pickupAddress != null) {
                                                            etPickupAddress.setText(pickupAddress);
                                                        }
                                                    }
                                                }
                                            }

                                            Log.d("Tagg","----------isWarehouseDropoff------------"+isWarehouseDropoff);
//                                            Log.d("Tagg","----------packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress()------------"+packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress());
                                            //set dropoff address
                                            if (isWarehouseDropoff != null) {
                                                if (isWarehouseDropoff.equalsIgnoreCase("true")) {
//                                                    if (packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")) {
//                                                        if (packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")) {

                                                            dropoffAddress = packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress();
                                                            dropoffLat = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLatitude();
                                                            dropoffLng = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLongtitude();
//                                                        } else {
//                                                            dropoffAddress = packageResponse.getData().getWarehouse_pickup_address().get(0).getAddress();
//                                                            dropoffLat = packageResponse.getData().getWarehouse_pickup_address().get(0).getLatitude();
//                                                            dropoffLng = packageResponse.getData().getWarehouse_pickup_address().get(0).getLongtitude();
//
//                                                        }
//                                                    }else{
//                                                        dropoffAddress = packageResponse.getData().getWarehouse_drop_off_address().get(0).getAddress();
//                                                        dropoffLat = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLatitude();
//                                                        dropoffLng = packageResponse.getData().getWarehouse_drop_off_address().get(0).getLongtitude();
//
//                                                    }
                                                    //Set WareHouse Address
                                                    if (dropoffAddress != null) {
                                                        etDropoffAddress.setText(dropoffAddress);
                                                    }
                                                } else {
                                                    if (packageResponse.getData().getLocation_details() != null) {
                                                        if (packageResponse.getData().getPackage_details().getIs_round_trip().equals("1")) {
                                                            if (packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("0")) {

                                                                dropoffLat = packageResponse.getData().getLocation_details().getDropoff_location_lat();
                                                                dropoffLng = packageResponse.getData().getLocation_details().getDropoff_location_lng();
                                                                String dropoffHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                                                String dropoffStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                                                String dropoffLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                                                String exactDropOffAddress = packageResponse.getData().getLocation_details().getDropoff_location();
                                                                dropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, exactDropOffAddress);
                                                            }else {
                                                                dropoffLat = packageResponse.getData().getLocation_details().getRound_dropoff_location_lat();
                                                                dropoffLng = packageResponse.getData().getLocation_details().getRound_dropoff_location_lng();
                                                                String dropoffHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                                                String dropoffStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                                                String dropoffLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                                                String exactDropOffAddress = packageResponse.getData().getLocation_details().getRound_dropoff_location();
                                                                dropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, exactDropOffAddress);
                                                            }
                                                        }else {
                                                                dropoffLat = packageResponse.getData().getLocation_details().getDropoff_location_lat();
                                                                dropoffLng = packageResponse.getData().getLocation_details().getDropoff_location_lng();
                                                                String dropoffHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                                                String dropoffStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                                                String dropoffLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                                                String exactDropOffAddress = packageResponse.getData().getLocation_details().getDropoff_location();
                                                                dropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, exactDropOffAddress);
                                                            }
                                                        //Set DropOff LOcation
                                                        if (dropoffAddress != null) {
                                                            etDropoffAddress.setText(dropoffAddress);
                                                        }
                                                    }
                                                }
                                            }

//                                            if(packageResponse.getData().getPackage_details().getIs_first_round_completed().equals("1")) {
////
//                                                String roundPickup = etDropoffAddress.getText().toString();
//                                                String roundDropOff = etPickupAddress.getText().toString();
//                                                etDropoffAddress.setText(roundDropOff);
//                                                etPickupAddress.setText(roundPickup);
////
//                                            }
                                            remove("Pickup Location");
                                            remove("Dropoff Location");
                                            remove("Godel warehouse");

                                            if (isWarehousePickup != null && isWarehousePickup.trim().equalsIgnoreCase("true")) {
                                                add("Godel warehouse", new LatLng(Double.parseDouble(pickupLat), Double.parseDouble(pickupLng)), pickupAddress, "Pickup_warehouse");
                                            } else {
                                                add("Pickup Location", new LatLng(Double.parseDouble(pickupLat), Double.parseDouble(pickupLng)), pickupAddress, "Pickup");
                                            }

                                            if (isWarehouseDropoff != null && isWarehouseDropoff.trim().equalsIgnoreCase("true")) {
                                                add("Godel warehouse", new LatLng(Double.parseDouble(dropoffLat), Double.parseDouble(dropoffLng)), dropoffAddress, "Dropoff");
                                            } else {
                                                add("Dropoff Location", new LatLng(Double.parseDouble(dropoffLat), Double.parseDouble(dropoffLng)), dropoffAddress, "Dropoff");
                                            }
                                        } else {

                                        }
                                    }else if(packageResponse.getResponse() == 0){
                                        showAlert("This package has been deleted by admin");
                                        isValidPackage = false;
                                        btnScanPickup.setVisibility(GONE);
                                        btnScanAttachSticker.setVisibility(GONE);
                                        if (!(isWarehouseDropoff != null && isWarehouseDropoff.equalsIgnoreCase("true"))) {
                                            btnDropoffPackage.setVisibility(VISIBLE);
                                            tvScanInWarehouse.setVisibility(GONE);
                                        } else {
                                            tvScanInWarehouse.setVisibility(VISIBLE);
                                            btnDropoffPackage.setVisibility(GONE);
                                        }


                                    }


                                    else if(packageResponse.getData().getMessage()!=null){
                                        if (packageResponse.getData().getMessage().trim().equals("Unauthenticated")) {
                                            Global.signInIntent(getActivity());
                                        }
                                    } else {
                                        Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                            try {
                                if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                    PckgPrgsDlg.dismiss();
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

    private void showAlert(String message) {
        editor.remove(SharedValues.PACKAGE_CODE);
        editor.remove(SharedValues.PACKAGE_ID);
        editor.remove(SharedValues.IS_PACKAGE_DROP_WH);
        editor.remove(SharedValues.IS_PACKAGE_PICKUP_WH);
        editor.commit();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layoutBottomSheet.setVisibility(GONE);
                flBookingDetails.setVisibility(GONE);
                tvNoPackage.setVisibility(VISIBLE);
            }
        });
        alert.show();
    }

    private void build_retrofit_and_get_response(String pickupLat, String pickupLng, String dropLat, String dropLng) {
        String url = "https://maps.googleapis.com/maps/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(30, TimeUnit.SECONDS).
                build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration(Global.USER_AGENT, "metric", pickupLat + "," + pickupLng, dropLat + "," + dropLng, "driving", Global.GOOGLE_API_WEB_KEY);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }

                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
//                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
//                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
//                        ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        polylineList = decodePoly(encodedString);
                        line = googleMap.addPolyline(new PolylineOptions()
                                .addAll(polylineList)
                                .width(5)
                                .color(Color.parseColor("#1C00F9"))
                                .geodesic(true)
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }

        });
    }

    private void add(String name, LatLng ll, String snippedText, String markerType) {

        if (googleMap != null) {
            final MarkerOptions marker;
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(ll)
                    .zoom(googleMap.getCameraPosition().zoom)
                    .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 0050, null);
            if (markerType.trim().equals("Pickup") || markerType.trim().equals("Pickup_warehouse")) {
                Drawable circleDrawable = context.getResources().getDrawable(R.drawable.ic_dropoff_marker);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                marker = new MarkerOptions().position(ll).title(name).snippet(snippedText).icon(markerIcon);
            } else if (markerType.trim().equals("currentLoc")) {
                marker = new MarkerOptions().position(ll).title(name).snippet(snippedText);
            } else {
                Drawable circleDrawable = context.getResources().getDrawable(R.drawable.ic_pickup_marker);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                int height = 40;
                int width = 40;
                marker = new MarkerOptions().position(ll).title(name).snippet(snippedText).icon(markerIcon);
            }

            mMarkers.put(name, marker);
            for (MarkerOptions item : mMarkers.values()) {
//            if (item.getTitle().toString().equals("currentLoc")) {
//                Bitmap bitmap;
//                int px = context.getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
//                bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//
//                Drawable shape = context.getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp);
//                shape.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
//                shape.draw(canvas);
//            } else {
                googleMap.addMarker(item);
//            }
            }

            if (pickupLat != null && pickupLat.length() > 0 && pickupLng != null && pickupLng.length() > 0 && dropoffLat != null && dropoffLat.length() > 0 && dropoffLng != null && dropoffLng.length() > 0) {
                googleMap.clear();
                for (MarkerOptions item : mMarkers.values()) {
                    googleMap.addMarker(item);
                }
                addBounds();
            }
        }


    }

    private static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //========================== Remove Marker ==========================//
    private static void remove(String name) {
        mMarkers.remove(name);
    }

    static LatLngBounds.Builder markersBuilder;

    private void addBounds() {
        CameraPosition cameraPosition;
        if (location != null) {
            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(14)
                    .build();
        } else {
            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(Double.parseDouble(pickupLat), Double.parseDouble(pickupLng)))
                    .zoom(14)
                    .build();
        }

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 0050, null);
        markersBuilder = new LatLngBounds.Builder();
        for (MarkerOptions item : mMarkers.values()) {
            markersBuilder.include(item.getPosition());
        }
        LatLngBounds bounds = markersBuilder.build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
        if (polylineList != null && polylineList.size() > 0 && line == null) {
            if (line != null) {
                line.remove();
            }
            line = googleMap.addPolyline(new PolylineOptions()
                    .addAll(polylineList)
                    .width(5)
                    .color(Color.parseColor("#1C00F9"))
                    .geodesic(true)
            );
        } else {
            build_retrofit_and_get_response(pickupLat, pickupLng, dropoffLat, dropoffLng);
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_retro);
        googleMap.setMapStyle(style);

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Map Settings
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);

        //Get Current Location
        //getCurrentLocation();

        LatLng myPosition;

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. Se.e the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
//Move Camera to Current Location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), Global.MAP_ZOOM));
        } else {
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalVariables.driverLatitude, GlobalVariables.driverLongitude), GlobalVariables.MAP_ZOOM));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void getCurrentLocation() {
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            //Move Camera to Current Location
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), GlobalVariables.MAP_ZOOM));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), Global.MAP_ZOOM));
        }
    }

    interface RetrofitMaps {
        /*
         * Retrofit get annotation with our URL
         * And our method that will return us details of student.
         */
        @GET("api/directions/json?")
        Call<Example> getDistanceDuration(@Header("User-Agent") String agent, @Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode, @Query("key") String key);
    }
}
