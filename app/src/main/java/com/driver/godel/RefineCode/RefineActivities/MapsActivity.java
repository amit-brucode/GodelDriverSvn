package com.driver.godel.RefineCode.RefineActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.GodelFragmentActivity;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.response.POJO.Example;
import com.driver.godel.response.setActivePackage.ActivePackageResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

public class MapsActivity extends GodelFragmentActivity implements OnMapReadyCallback, View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String pickupDateTime, deliveryDateTime;
    TextView tvPickupDateTime, tvDeliveryDateTime;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 1000;
    public static CardView cardViewDetail;
    public static Double originLat = 0.0, originLng = 0.0, destLat = 0.0, destLng = 0.0;
    public static Boolean check = false;
    ImageView ivBack;
    TextView tvTitle, tvPickup, tvDropoff, tvHeight, tvWidth, tvWeight, tvQuantity, tvName, tvEmail,tv_email_label, tvPhone;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    Location location;
    Bundle bundle;
    FloatingActionButton fabLocation;
    String driverId;
    String isWareHouseDropoff, isWareHousePickup, checkFrom, driverID_, packageCode, packageStatus, height, width, weight, quantity, userEmail, userPhone, pickupAddress, dropAddress;
    static String userName, packageId;
    Double latitude, longitude;
    //Progress Dialog
    ProgressDialog progressDialog;
    //Markers
    Marker marker1, marker2;
    //Buttons
    Button btnArrival, btnStartRide, btnStopRide;
    TextView btnWaiting, btnWaitingTime, btnWaitingStop;
    long startTime;
    //Web Services
    WebRequest webRequest;
    //Set Active Package
    Call<ActivePackageResponse> activePackageCall;
    ActivePackageResponse activePackageResponse;
    //Session Manage
    private GoogleMap mMap;
    //Google ApiClient
    private GoogleApiClient googleApiClient;
    //ImageView
    ImageView ivArrowUp, ivArrowDown;
    //Database
    LinearLayout llStartWaiting, llStopWaiting;
    RelativeLayout rlWaiting;
    TextView btnWaitingTimeStop, tv_waiting;
    RelativeLayout rl_waiting_stop;
    ProgressDialog fareDialog;
    static Context con;
    Button btnStart;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String OnGOingPckgCode, OnGoingDropWH, OnGoingPickupWH;
    String language_type;
     View view_contact1;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        con = MapsActivity.this;

        Global global = new Global(MapsActivity.this);
        global.setCurrencySymbol();

        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);

        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

        editor = preferences.edit();
           if(language_type.equals("1")){
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }else if(language_type.equals("2")){
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }
        webRequest = com.driver.godel.RefineCode.RefineWebServices.WebRequest.getSingleton(this);
        OnGOingPckgCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
        OnGoingDropWH = preferences.getString(SharedValues.IS_PACKAGE_DROP_WH, "");
        OnGoingPickupWH = preferences.getString(SharedValues.IS_PACKAGE_PICKUP_WH, "");
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        //Initialization
        btnStart = (Button) findViewById(R.id.btn_start);
        //Get Intent
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            checkFrom = bundle.getString("CheckClick");
            if (checkFrom.equalsIgnoreCase("pickups") || checkFrom.equalsIgnoreCase("deliveries")) {
                pickupAddress = bundle.getString("PickupAddress");
                dropAddress = bundle.getString("DropoffAddress");
                packageId = bundle.getString("PackageId");
                packageStatus = bundle.getString("packageStatus");
                driverID_ = bundle.getString("Driverid");
                packageCode = bundle.getString("PackageCode");
                try {
                if(bundle.getString("OriginLat") != null || !bundle.getString("OriginLat").equals("null")) {
                    originLat = Double.parseDouble(bundle.getString("OriginLat"));
                }
                if(bundle.getString("OriginLng") != null || !bundle.getString("OriginLng").equals("null")) {
                    originLng = Double.parseDouble(bundle.getString("OriginLng"));
                }
                if(bundle.getString("DestLat") != null || !bundle.getString("DestLat").equals("null")) {
                    destLat = Double.parseDouble(bundle.getString("DestLat"));
                }
                if(bundle.getString("DestLng") != null || !bundle.getString("DestLng").equals("null")) {
                    destLng = Double.parseDouble(bundle.getString("DestLng"));
                }
                } catch (NumberFormatException e) {
                    Log.d("Number_point===","======="+e);
                }
                isWareHouseDropoff = bundle.getString("isDropWarehouse");
                isWareHousePickup = bundle.getString("isPickupWarehouse");
                btnStart.setVisibility(View.VISIBLE);
            } else {
                pickupAddress = bundle.getString("PickupAddress");
                dropAddress = bundle.getString("DropoffAddress");
                try{
                if(bundle.getString("OriginLat") != null || !bundle.getString("OriginLat").equals("null")) {
                    originLat = Double.parseDouble(bundle.getString("OriginLat"));
                }
                if(bundle.getString("OriginLng") != null || !bundle.getString("OriginLng").equals("null")) {
                    originLng = Double.parseDouble(bundle.getString("OriginLng"));
                }
                if(bundle.getString("DestLat") != null || !bundle.getString("DestLat").equals("null")) {
                    destLat = Double.parseDouble(bundle.getString("DestLat"));
                }
                if(bundle.getString("DestLng") != null || !bundle.getString("DestLng").equals("null")) {
                    destLng = Double.parseDouble(bundle.getString("DestLng"));
                }
            } catch (NumberFormatException e) {
                Log.d("Number_point===","======="+e);
            }
                packageStatus = bundle.getString("packageStatus");
                btnStart.setVisibility(View.GONE);
            }
        }


        Log.d("MAPS_PACKAGE_STATUS", "packageStatus " + packageStatus);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        view_contact1 = (View) findViewById(R.id.view_contact1);
        cardViewDetail = (CardView) findViewById(R.id.card_view_info);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPickup = (TextView) findViewById(R.id.tv_pickup);
        tvDropoff = (TextView) findViewById(R.id.tv_drop_off);
        tvHeight = (TextView) findViewById(R.id.tv_height);
        tvWidth = (TextView) findViewById(R.id.tv_width);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        tvQuantity = (TextView) findViewById(R.id.tv_quantity);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tv_email_label = (TextView) findViewById(R.id.tv_email_label);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tv_waiting = (TextView) findViewById(R.id.tv_waiting);
        tvPickupDateTime = (TextView) findViewById(R.id.tv_pickup_date_time);
        tvDeliveryDateTime = (TextView) findViewById(R.id.tv_delivery_date_time);
        btnWaitingTime = (TextView) findViewById(R.id.btn_waiting_time);
        btnWaitingTimeStop = (TextView) findViewById(R.id.btn_waiting_time_stop);
        btnWaitingStop = (TextView) findViewById(R.id.btn_waiting_stop);
        rl_waiting_stop = (RelativeLayout) findViewById(R.id.rl_waiting_stop);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        fabLocation = (FloatingActionButton) findViewById(R.id.fab_location);
        btnArrival = (Button) findViewById(R.id.btn_arrival);
        btnWaiting = (TextView) findViewById(R.id.btn_waiting);
        btnStartRide = (Button) findViewById(R.id.btn_start_trip);
        btnStopRide = (Button) findViewById(R.id.btn_stop_trip);
        ivArrowUp = (ImageView) findViewById(R.id.iv_up);
        ivArrowDown = (ImageView) findViewById(R.id.iv_down);
        rlWaiting = (RelativeLayout) findViewById(R.id.rl_waiting);
        llStartWaiting = (LinearLayout) findViewById(R.id.ll_start_waiting);
        llStopWaiting = (LinearLayout) findViewById(R.id.ll_stop_waiting);
        llStartWaiting.setVisibility(View.GONE);
        llStopWaiting.setVisibility(View.GONE);

        //Set On Click Listener
        btnWaitingTimeStop.setOnClickListener(this);
        btnWaitingStop.setOnClickListener(this);
        rl_waiting_stop.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        fabLocation.setOnClickListener(this);
        btnWaiting.setOnClickListener(this);
        btnStartRide.setOnClickListener(this);
        btnStopRide.setOnClickListener(this);
        ivArrowUp.setOnClickListener(this);
        ivArrowDown.setOnClickListener(this);
        rlWaiting.setOnClickListener(this);
        llStopWaiting.setOnClickListener(this);
        btnWaitingTime.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        //Location Request
        //Initializing google api client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        if (packageStatus != null) {
            showStatusButton(Integer.parseInt(packageStatus));
        }
        if (pickupDateTime != null) {
            tvPickupDateTime.setText(pickupDateTime);
        }
        if (deliveryDateTime != null) {
            tvDeliveryDateTime.setText(deliveryDateTime);
        }
        if (height != null) {
            tvHeight.setText(height + " " + "cm");
        }
        if (width != null) {
            tvWidth.setText(width + " " + "cm");
        }
        if (weight != null) {
            tvWeight.setText(Global.formatValue(weight) + " " + "kg");
        }
        if (quantity != null) {
            tvQuantity.setText(quantity);
        }
        if (userName != null) {
            tvName.setText(userName);
        }
        if (userEmail != null) {
            if(userEmail.equals("nomail@nomail.com")) {
                tvEmail.setVisibility(GONE);
                tv_email_label.setVisibility(GONE);
                view_contact1.setVisibility(GONE);
//            tv_Email.setHeight(0);
            }else{
            tvEmail.setText(userEmail);}
        }
        if (tvPhone != null) {
            tvPhone.setText(userPhone);
        }

        ivBack.setVisibility(View.VISIBLE);
        if (OnGOingPckgCode.equalsIgnoreCase(packageCode)) {
            btnStart.setText(getResources().getString(R.string.packagealreadysetongoing));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro);
        googleMap.setMapStyle(style);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(originLat == 0.0 ||originLng == 0.0 ||destLat == 0.0 ||destLng == 0.0 || originLat == null ||originLng == null ||destLat == null ||destLng == null){
            Toast.makeText(con, "Unable to get location. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Pickup and DropOff Latlng
        LatLng srcLatLng = new LatLng(originLat, originLng);
        LatLng desLatLng = new LatLng(destLat, destLng);
        //Draw Route from Pickup to DropOff
        routeLocation(srcLatLng, desLatLng);
        //Map Settings
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
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
//                        else {
//                            String locale;
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                            } else {
//                                locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                            }
//
////                            Log.d(TAG,"locale :"+ locale );
//
//                            if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
////                                Log.d("check_type","language_type1---  "+language_type );
//                                Locale myLocale = new Locale("sw");
//                                Resources res = getResources();
//                                DisplayMetrics dm = res.getDisplayMetrics();
//                                Configuration conf = res.getConfiguration();
//                                conf.locale = myLocale;
//                                res.updateConfiguration(conf, dm);
//
//                            } else {
////                           language_type="2";
////                        Log.d("check_type","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                                Locale myLocale = new Locale("en");
//                                Resources res = getResources();
//                                DisplayMetrics dm = res.getDisplayMetrics();
//                                Configuration conf = res.getConfiguration();
//                                conf.locale = myLocale;
//                                res.updateConfiguration(conf, dm);
//                            }
//
//                            Log.d("check_type", "language_typelllll--  " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                        }
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

        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        createLocationRequest();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (OnGOingPckgCode != null && OnGOingPckgCode.trim().length() > 0) {
                    if (OnGOingPckgCode.equalsIgnoreCase(packageCode)) {

                        btnStart.setEnabled(false);
                        btnStart.setClickable(false);
                        Intent i = new Intent().setClass(MapsActivity.this, MyPackagesActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();


                    } else {
                        if (checkFrom.equalsIgnoreCase("pickups")) {
                            if (OnGoingPickupWH != null && isWareHousePickup != null && OnGoingPickupWH.trim().equalsIgnoreCase("true") && isWareHousePickup.trim().equalsIgnoreCase("true")) {
                                whShowAlert(getResources().getString(R.string.alreadywaywarehouse));
                            } else {
                                showAlertDialog(getResources().getString(R.string.alreadyongoingpackage));
                            }
                        } else if (checkFrom.equalsIgnoreCase("deliveries")) {
                            //Set Active Package API HIT
                            if (OnGoingDropWH != null && isWareHouseDropoff != null && OnGoingDropWH.trim().equalsIgnoreCase("true") && isWareHouseDropoff.trim().equalsIgnoreCase("true")) {
                                whShowAlert(getResources().getString(R.string.warehousedroppackage));
                            } else {
                                showAlertDialog(getResources().getString(R.string.alreadyongoingpackage));
                            }
                        }
                    }
                } else {
                    showAlertDialog("Do you want to start this package?");
                }
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.fab_location:
                TargetCurrentLocation();
                break;
        }
    }

    private void TargetCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)//connection listeners  (connected, suspended)
                    .addOnConnectionFailedListener(this) // connection failed listener
                    .build();
            googleApiClient.connect();
        } else {
            //=============== get fused api location ===============//
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                //=============== move map camera to position ===============//
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(14)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 0050, null);
            } else {
            }
        }
    }

    //Change Package Check
    public void showAlertDialog(String messsage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage(messsage);
        builder.setCancelable(false);

        //ok Button
        builder.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();

                String currentLat = null, currentLng = null;
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null) {
                    currentLat = String.valueOf(location.getLatitude());
                    currentLng = String.valueOf(location.getLongitude());
                }

                if (isNetworkAvailable()) {
                    //Check from Pickups or Deliveries
                    if (checkFrom.equalsIgnoreCase("pickups")) {
                        //Set Active Package API HIT
//                    if (packageStatus != null && packageStatus.length() > 0 && packageStatus.trim().equalsIgnoreCase("12")) {
//                        setReassignPackage(driverId, packageCode, currentLat, currentLng, "0");
//                    } else {
                        setActivePackage(currentLat, currentLng, "0");
//                    }
                    } else if (checkFrom.equalsIgnoreCase("deliveries")) {
                        //Set Active Package API HIT
//                    if (packageStatus != null && packageStatus.length() > 0 && packageStatus.trim().equalsIgnoreCase("12")) {
//                        setReassignPackage(driverId, packageCode, currentLat, currentLng, "1");
//                    } else {
                        setActivePackage(currentLat, currentLng, "1");
//                    }
                    }
                } else {
                    Snackbar1();
                }

            }
        });

        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.canceltextt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    //Ongoing WareHouse Alert Dialog on Start Deliveries
    public void whShowAlert(String messsage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage(messsage);
        builder.setCancelable(false);
        //ok Button
        builder.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Close Screen
                finish();
            }
        });
        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.canceltextt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
                //Close Screen
                finish();
            }
        });
        builder.show();
    }

    private void showStatusButton(int status) {
        switch (status) {
            case 1:
                tv_waiting.setVisibility(View.GONE);
                packageStatus = "1";
                llStartWaiting.setVisibility(View.VISIBLE);
                //btnArrival.setVisibility(View.GONE);
                llStopWaiting.setVisibility(View.GONE);
                btnStartRide.setVisibility(View.VISIBLE);
                break;

            case 2:
                packageStatus = "2";
                tv_waiting.setVisibility(View.GONE);
                llStartWaiting.setVisibility(View.VISIBLE);
                btnStartRide.setVisibility(View.VISIBLE);
                break;

            case 3:
                packageStatus = "3";
                tv_waiting.setVisibility(View.GONE);
                llStartWaiting.setVisibility(View.VISIBLE);
                llStopWaiting.setVisibility(View.GONE);
                btnStartRide.setVisibility(View.VISIBLE);
                break;

            case 4:
                packageStatus = "4";
                llStartWaiting.setVisibility(View.VISIBLE);
                llStopWaiting.setVisibility(View.GONE);
                btnStartRide.setVisibility(View.VISIBLE);
                break;

            case 5:
                packageStatus = "5";
                tv_waiting.setVisibility(View.GONE);
                llStartWaiting.setVisibility(View.GONE);
                llStopWaiting.setVisibility(View.VISIBLE);
                btnStartRide.setVisibility(View.GONE);
                break;

            case 6:
                packageStatus = "6";
                tv_waiting.setVisibility(View.GONE);
                btnStartRide.setVisibility(View.GONE);
                break;

            case 7:
                packageStatus = "7";
                tv_waiting.setVisibility(View.GONE);
                fareDialog = new ProgressDialog(this);
                fareDialog.setCancelable(false);
                btnStartRide.setVisibility(View.GONE);
                fareDialog.setMessage(getResources().getString(R.string.calculatingfinaslfare));
                fareDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                fareDialog.show();
                break;

            case 9:
                packageStatus = "9";
                llStartWaiting.setVisibility(View.GONE);
                llStopWaiting.setVisibility(View.VISIBLE);
                btnStartRide.setVisibility(View.GONE);
                break;
        }
    }

    //Set Active Package API HIT
    private void setActivePackage(String currentLat, String currentLng, String type) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        Log.d("ACTIVE_PACKAGE", "Accept " + Accept);
        Log.d("ACTIVE_PACKAGE", "USER_TOKEN " + USER_TOKEN);
        Log.d("ACTIVE_PACKAGE", "driverId " + driverId);
        Log.d("ACTIVE_PACKAGE", "packageCode " + packageCode);
        Log.d("ACTIVE_PACKAGE", "Global.driverLatitude " + Global.driverLatitude);
        Log.d("ACTIVE_PACKAGE", "driverLongitude " + Global.driverLongitude);
        Log.d("ACTIVE_PACKAGE", "type " + type);


        //Toast.makeText(con, ""+Accept+" "+USER_TOKEN+" "+driverId+" "+packageCode+" "+Global.driverLatitude+" "+Global.driverLongitude+" "+type, Toast.LENGTH_SHORT).show();
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");

        if (TextUtils.isEmpty(Global.driverLatitude) && TextUtils.isEmpty(Global.driverLongitude)) {
            Global.driverLatitude = "0.0000";
            Global.driverLongitude = "0.0000";
            Snackbar();

        }
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(MapsActivity.this.isFinishing())) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            if (packageStatus.equalsIgnoreCase("12")) {


                // Toast.makeText(con, "Failure Package", Toast.LENGTH_SHORT).show();
                updatePackageStatus(type);

            } else {


                String country_code = CountryCodeCheck.countrycheck(MapsActivity.this);
                if (country_code != null && country_code.trim().length() > 0) {


                    activePackageCall = webRequest.apiInterface.setActivePackage(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, packageCode, Global.driverLatitude, Global.driverLongitude, type);
                    activePackageCall.enqueue(new Callback<ActivePackageResponse>() {
                        @Override
                        public void onResponse(Call<ActivePackageResponse> call, Response<ActivePackageResponse> response) {
                            if (!(MapsActivity.this.isFinishing())) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                            if (response.body() != null) {
                                activePackageResponse = response.body();
                                //Check response
                                if (activePackageResponse.getResponse() == 1) {
                                    //Add Booking Detail to Shared Prefs
                                    editor.putString(SharedValues.PACKAGE_CODE, packageCode);
                                    editor.putString(SharedValues.PACKAGE_ID, packageId);
                                    editor.putString(SharedValues.IS_PACKAGE_PICKUP_WH, isWareHousePickup);
                                    editor.putString(SharedValues.IS_PACKAGE_DROP_WH, isWareHouseDropoff);
                                    editor.commit();
                                    //Pass Intent
                                    Intent tabIntent = new Intent(getApplicationContext(), MyPackagesActivity.class);
                                    tabIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(tabIntent);
                                    finish();
                                }else if(activePackageResponse.getData().getMessage()!=null){
                                    if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                        Global.signInIntent(MapsActivity.this);
                                    }
                                }


                                else {

                                    Toast.makeText(MapsActivity.this, activePackageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ActivePackageResponse> call, Throwable t) {
                            if (!(MapsActivity.this.isFinishing())) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                            t.printStackTrace();
                        }
                    });
                } else {

                    if (!(MapsActivity.this.isFinishing())) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                }
            }

        }

    }

    private void updatePackageStatus(final String type) {
        String FCM_token = preferences.getString(SharedValues.FCM_TOKEN, "0");
        final String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");


        String country_code = CountryCodeCheck.countrycheck(MapsActivity.this);

        Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> updatePackageStatusCall = webRequest.apiInterface.updateFailurePackageStatus(country_code, Global.USER_AGENT, Global.ACCEPT, USER_TOKEN, driverId,
                packageId, driverId, "5", FCM_token, "0", " ");
        updatePackageStatusCall.enqueue(new Callback<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus>() {
            @Override
            public void onResponse(Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> call, Response<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> response) {
                if (!(MapsActivity.this.isFinishing())) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (response.body() != null) {
                    com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus updatePackageStatus = response.body();
                    if (updatePackageStatus.getResponse() == 1) {


                        String country_code = CountryCodeCheck.countrycheck(MapsActivity.this);
                        if (country_code != null && country_code.trim().length() > 0) {


                            activePackageCall = webRequest.apiInterface.setActivePackage(country_code, Global.USER_AGENT, Global.ACCEPT, USER_TOKEN, driverId, driverId, packageCode, Global.driverLatitude, Global.driverLongitude, type);
                            activePackageCall.enqueue(new Callback<ActivePackageResponse>() {
                                @Override
                                public void onResponse(Call<ActivePackageResponse> call, Response<ActivePackageResponse> response) {

                                    if (response.body() != null) {
                                        activePackageResponse = response.body();
                                        //Check response
                                        if (activePackageResponse.getResponse() == 1) {


                                            //Add Booking Detail to Shared Prefs
                                            editor.putString(SharedValues.PACKAGE_CODE, packageCode);
                                            editor.putString(SharedValues.PACKAGE_ID, packageId);
                                            editor.putString(SharedValues.IS_PACKAGE_PICKUP_WH, isWareHousePickup);
                                            editor.putString(SharedValues.IS_PACKAGE_DROP_WH, isWareHouseDropoff);
                                            editor.commit();
                                            //Pass Intent
                                            Intent tabIntent = new Intent(getApplicationContext(), MyPackagesActivity.class);
                                            tabIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(tabIntent);
                                            finish();
                                        }else if(activePackageResponse.getData().getMessage()!=null){
                                            if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                                Global.signInIntent(MapsActivity.this);
                                            }
                                        }
                                        else {

                                            Toast.makeText(MapsActivity.this, activePackageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ActivePackageResponse> call, Throwable t) {

                                    t.printStackTrace();
                                }
                            });

                        } else {
                            if (!(MapsActivity.this.isFinishing())) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }

                    } else if(updatePackageStatus.getData().getMessage()!=null){
                        if (updatePackageStatus.getData().getMessage().trim().equals("Unauthenticated")) {
                            Global.signInIntent(MapsActivity.this);
                        }
                    } else {
                        Toast.makeText(con, "" + updatePackageStatus.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(con, "null response", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus> call, Throwable t) {
                if (!(MapsActivity.this.isFinishing())) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                Toast.makeText(con, "Package picked Failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Snackbar1() {
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
                    Snackbar();
                }
            }
        });
        mySnackbar.show();
    }

    public void Snackbar() {
        final Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.NoLocation, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();

        snackBarView.setBackgroundColor(Color.RED);

        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);

        mySnackbar.setAction(getResources().getString(R.string.retrytext), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mySnackbar.show();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }

    //Get Current Location
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(14)
                    .build();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //the include method will calculate the min and max bound.
            if(marker1 != null) {
                builder.include(marker1.getPosition());
            }else{
                Toast.makeText(MapsActivity.this, "Can't find the location, please try again later.", Toast.LENGTH_SHORT).show();
                return ;
            }
            if(marker2 != null) {
                builder.include(marker2.getPosition());
            }else{
                Toast.makeText(MapsActivity.this, "Can't find the location, please try again later.", Toast.LENGTH_SHORT).show();
                return ;
            }
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mCurrentLocation = location;
    }

    //Create Route From Source to Destination
    public void routeLocation(LatLng origin, LatLng dest) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        String pickupTitle = "", deliveryWarehouse = "";
        if (isWareHousePickup != null && isWareHousePickup.trim().length() > 0 && (isWareHousePickup.trim().equalsIgnoreCase("true") || isWareHousePickup.trim().equalsIgnoreCase("1"))) {
            pickupTitle = "Godel warehouse";
        } else {
            pickupTitle = "Pickup Location";
        }

        if (isWareHouseDropoff != null && isWareHouseDropoff.trim().length() > 0 && (isWareHouseDropoff.trim().equalsIgnoreCase("true") || isWareHouseDropoff.trim().equalsIgnoreCase("1"))) {
            deliveryWarehouse = "Godel warehouse";
        } else {
            deliveryWarehouse = "Dropoff Location";
        }

        //Adding Source marker to map
        Drawable circleDrawable1 = con.getResources().getDrawable(R.drawable.ic_dropoff_marker);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable1);
        marker1 = mMap.addMarker(new MarkerOptions()
                .icon(markerIcon)
                .position(origin) //setting position
                .title(pickupTitle)
                .snippet(pickupAddress)
                .draggable(true));//Making the marker draggable

        //Adding Destination marker to map
        Drawable circleDrawable2 = con.getResources().getDrawable(R.drawable.ic_pickup_marker);
        BitmapDescriptor markerIcon2 = getMarkerIconFromDrawable(circleDrawable2);
        marker2 = mMap.addMarker(new MarkerOptions()
                .icon(markerIcon2)
                .position(dest) //setting position
                .title(deliveryWarehouse)
                .snippet(dropAddress)
                .draggable(true)); //Making the marker draggable
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //the include method will calculate the min and max bound.
        builder.include(marker1.getPosition());
        builder.include(marker2.getPosition());
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

        build_retrofit_and_get_response();
    }

    private static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    Polyline line;

    private void build_retrofit_and_get_response() {
        if(originLat == 0.0 ||originLng == 0.0 ||destLat == 0.0 ||destLng == 0.0 || originLat == null ||originLng == null ||destLat == null ||destLng == null){
            Toast.makeText(con, "Unable to get location. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://maps.googleapis.com/maps/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        Call<Example> call = service.getDistanceDuration(Global.USER_AGENT, "metric", originLat + "," + originLng, destLat + "," + destLng, "driving", Global.GOOGLE_API_WEB_KEY);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (!MapsActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                try {
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        //Remove previous line from map
                        if (line != null) {
                            line.remove();
                        }
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(5)
                                .color(Color.parseColor("#1C00F9"))
                                .geodesic(true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                if (!MapsActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
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

    ProgressDialog ActivePckgProgress;
    Call<com.driver.godel.response.ownResign.MainRes> reassignPackageCall;

    private void setReassignPackage(String driverId, final String packageCode, String currentLat, String currentLng, String type) {
        if (reassignPackageCall != null) {
            reassignPackageCall.cancel();
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
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!MapsActivity.this.isFinishing()) {
                if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                    ActivePckgProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {


            String country_code = CountryCodeCheck.countrycheck(MapsActivity.this);
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
                                editor.putString(SharedValues.PACKAGE_CODE, packageCode);
                                editor.putString(SharedValues.PACKAGE_ID, packageId);
                                editor.putString(SharedValues.IS_PACKAGE_PICKUP_WH, isWareHousePickup);
                                editor.putString(SharedValues.IS_PACKAGE_DROP_WH, isWareHouseDropoff);
                                editor.commit();
                                //Pass Intent
                                Intent tabIntent = new Intent(getApplicationContext(), MyPackagesActivity.class);
                                tabIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(tabIntent);
                                finish();
                            }else if(activePackageResponse.getData().getMessage()!=null){
                                if (activePackageResponse.getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(MapsActivity.this);
                                }
                            }
                            else {
                                Toast.makeText(MapsActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.driver.godel.response.ownResign.MainRes> call, Throwable t) {
                        if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                            ActivePckgProgress.dismiss();
                        }
                    }
                });

            } else {
                if (!MapsActivity.this.isFinishing()) {
                    if (ActivePckgProgress != null && ActivePckgProgress.isShowing()) {
                        ActivePckgProgress.dismiss();
                    }
                }
            }
        }
    }

}


interface RetrofitMaps {
    @GET("api/directions/json?")
    Call<Example> getDistanceDuration(@Header("User-Agent") String agent, @Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode, @Query("key") String key);

}