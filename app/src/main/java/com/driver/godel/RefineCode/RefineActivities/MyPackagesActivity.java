package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineAdapter.PagerAdapter;
import com.driver.godel.RefineCode.RefineFragments.OngoingFragment;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.driver.godel.RefineCode.Service.ForegroundService;
import com.driver.godel.RefineCode.Service.UpdateDriverLocationServiceNew;
import com.driver.godel.response.ForgotPaswdResponse;
import com.driver.godel.response.assignBarcode.AssignBarcodeResponse;
import com.driver.godel.response.getDriverDetails.GetProfileResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class MyPackagesActivity extends GodelActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    public static TextView tvStatus;
    public static int isNotificationAvailable = 0;
    public static String notificationContent, notificationMessage;
    public static boolean isServiceError = false;
    static Activity activitycon;
    static int ScanType = 0;
    private static boolean is_Active = false;
    public TextView tabOne;
    public TextView tabTwo;
    public TextView tabThree;
    public TextView tabFour;
    public TextView tabFive;
    public TextView tvUsername;
    public TextView tvDriverUniqueId;
    public TextView tvTitle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TabLayout tabLayout;
    CustomViewPager viewPager;
    Context con;
    String refreshedToken;
    Call<ForgotPaswdResponse> forgotPaswdResponseCall;
    String language_type;
    Call<ForgotPaswdResponse> notificationCall;
    ProgressDialog logoutProgress;
    ProgressDialog stickerProgress;
    Call<AssignBarcodeResponse> barcodeResponseCall;
    Call<GetPackageResponse> callPackageDetails;
    ProgressDialog packageProgress;
    private BroadcastReceiver mMessageReceiver;
    private ImageView ivNavigation, ivBack;
    private ProgressDialog PckgPrgsDlg;
    private Call<GetPackageResponse> getPackageResponseCall;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerRelativeLayout, llNotification, llAccount, llPdfView, ll_history, ll_Language, llTerms, llPrivacyPolicy, llAboutUs, llLogout, llQRCode;
    private CircleImageView profileImage;

//    private Button transfer;

    private BroadcastReceiver broadcastReceiver;
    private Button btnStatus;
    private Switch switchStatus;
    private String driverStatus,alternatePickupAndDropoffLocations="0";
    private Call<GetProfileResponse> getProfileResponseCall;
    private ProgressDialog driverProfileProgress;
    private GetProfileResponse getProfileResponse;
    private BroadcastReceiver DriverDeactiveBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
            // intent can contain anydata
            String driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (driverId == null || driverId.length() == 0) {
                if (is_Active == true) UserDeactivateEvent();
            }
        }
    };
    private BroadcastReceiver serviceErrorBroadcaste = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (is_Active == true && notificationContent != null && notificationMessage != null) {
                showErrorDialog();
            }
        }
    };

    public static void UserDeactivateEvent() {
        if (is_Active == true) {
            SharedPreferences preferences = activitycon.getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().commit();
            Intent i = new Intent(activitycon, SignIn.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activitycon.startActivity(i);
            activitycon.finish();
        }
    }

    // open scanner for scan package
    public static void openScanner(int type) {
        IntentIntegrator integrator = new IntentIntegrator(activitycon);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Package");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public static void changeStatus(int status) {
        try {
            if (is_Active == true) {
                if (status == 0) {
                    tvStatus.setText("Online");
                } else {
                    tvStatus.setText("Not connected");
                }
            }
        } catch (Exception e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

//        qrScan = new IntentIntegrator(this);

        Global global = new Global(MyPackagesActivity.this);
        global.setCurrencySymbol();

        is_Active = false;
        con = MyPackagesActivity.this;
        preferences = con.getSharedPreferences(SharedValues.PREF_NAME, con.MODE_PRIVATE);
        editor = preferences.edit();

        activitycon = MyPackagesActivity.this;
        FirebaseApp.initializeApp(this);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        editor.putString(SharedValues.FCM_TOKEN, "" + refreshedToken);
        editor.commit();
        isNotificationAvailable = 0;

        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");

        Intent i= getIntent();
        alternatePickupAndDropoffLocations=i.getStringExtra("is_alternate_pickupAndDropoff_locations");

        Log.d("MYPACKAGE_LOG", "alternatePickupAndDropoffLocations----------" + alternatePickupAndDropoffLocations);

//            if(language_type!=null) {
//                if{
//                    Locale locale = new Locale("sw");
//                    Locale.setDefault(locale);
//
//                    Resources resources = getResources();
//
//                    Configuration configuration = resources.getConfiguration();
//                    configuration.locale = locale;
//
//                    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//                } else {
//
//                    Locale locale = new Locale("en");
//                    Locale.setDefault(locale);
//
//                    Resources resources = getResources();
//
//                    Configuration configuration = resources.getConfiguration();
//                    configuration.locale = locale;
//
//                    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//                }
//            }
//        }else {

        if (language_type.equals("1")) {
            Locale myLocale = new Locale("sw");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        } else if (language_type.equals("2")) {
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        ivNavigation = (ImageView) findViewById(R.id.iv_navigation);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnStatus = (Button) findViewById(R.id.btn_status);
        switchStatus = (Switch) findViewById(R.id.switch_status);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        setupTabNames();
        setupNavigationDrawer();
        tvStatus.setText(getResources().getString(R.string.connectingtext));
        String type = preferences.getString(SharedValues.DRIVER_TYPE, "");
        if (type != null && type.length() > 0 && type.trim().equals("0")) {
            tvTitle.setText(getResources().getString(R.string.godeldrivertitle));
        } else {
            tvTitle.setText(getResources().getString(R.string.godelAgenttitle));
        }
        ivNavigation.setVisibility(VISIBLE);
        ivBack.setVisibility(View.INVISIBLE);
        btnStatus.setVisibility(View.VISIBLE);
        switchStatus.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);

        ivNavigation.setOnClickListener(this);
        btnStatus.setOnClickListener(this);

        driverStatus = preferences.getString(SharedValues.DRIVER_STATUS, "0");
        if (driverStatus != null && driverStatus.length() > 0 && driverStatus.trim().equals("1")) {
            switchStatus.setChecked(false);
        } else {
            switchStatus.setChecked(true);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    if (checkLocationOnOff()) {
                    } else {
                        Intent i = new Intent().setClass(MyPackagesActivity.this, LocationOffGPSOff.class);
                        startActivity(i);
                    }
                }
            }
        };

        con.registerReceiver(broadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        Intent intent = new Intent(MyPackagesActivity.this, UpdateDriverLocationServiceNew.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//           startForegroundService(intent);
//        } else {
            startService(intent);
//        }
//        startService(intent);

//        Intent serviceIntent = new Intent(this, ForegroundService.class);
//        serviceIntent.putExtra("inputExtra", "Godel Driver ");
//            ContextCompat.startForegroundService(this, serviceIntent);
//        startService(serviceIntent);



    }

    public boolean checkLocationOnOff() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setupNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_slide_navigation, R.string.drawer_opened, R.string.drawer_closed) {
            public void onDrawerClosed(View view) {
                ActivityCompat.invalidateOptionsMenu(MyPackagesActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");
                if (language_type.trim().length() > 0) {

                    Log.d("MYPACKAGE_LOG", "drawer opened----------" + language_type);
                    if (language_type.equals("1")) {
                        Locale myLocale = new Locale("sw");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                    } else if (language_type.equals("2")) {
                        Locale myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                    }
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerLayout.getApplicationWindowToken(), 0);
                ActivityCompat.invalidateOptionsMenu(MyPackagesActivity.this);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        invalidateOptionsMenu();

        llNotification = (LinearLayout) findViewById(R.id.ll_notification);
        llAccount = (LinearLayout) findViewById(R.id.ll_account);
        llQRCode = (LinearLayout) findViewById(R.id.ll_qrcode);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        ll_Language = (LinearLayout) findViewById(R.id.ll_Language);
        llTerms = (LinearLayout) findViewById(R.id.ll_terms);
        llPrivacyPolicy = (LinearLayout) findViewById(R.id.ll_privacy_policy);
        llAboutUs = (LinearLayout) findViewById(R.id.ll_about_us);
        llPdfView = (LinearLayout) findViewById(R.id.ll_download_pdfs);
        llLogout = (LinearLayout) findViewById(R.id.ll_logout);
        mDrawerRelativeLayout = (LinearLayout) findViewById(R.id.sliding_drawer);
        ll_Language.setVisibility(VISIBLE);

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvDriverUniqueId = (TextView) findViewById(R.id.tv_driver_unique_id);

        llNotification.setOnClickListener(this);
        llQRCode.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_Language.setOnClickListener(this);
        llTerms.setOnClickListener(this);
        llPrivacyPolicy.setOnClickListener(this);
        llAboutUs.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llPdfView.setOnClickListener(this);
    }

    public void setupTabNames() {
        tabLayout.removeAllTabs();

        tabOne = (TextView) LayoutInflater.from(con).inflate(R.layout.custom_tab_new, null);
        tabOne.setText(getResources().getString(R.string.ongoingtext));
        tabOne.setTextColor(Color.WHITE);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_ongoing_select_new,0,0);
//        tabOne.setCompoundDrawables(null,con.getResources().getDrawable(R.drawable.ic_ongoing_select),null,null);
        tabOne.setGravity(Gravity.CENTER);

        tabTwo = (TextView) LayoutInflater.from(con).inflate(R.layout.custom_tab_new, null);
        tabTwo.setText(getResources().getString(R.string.pickuptext));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickup_select_new,0,0);
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickups_unselect,0,0);
        tabTwo.setGravity(Gravity.CENTER);

        tabThree = (TextView) LayoutInflater.from(con).inflate(R.layout.custom_tab_new, null);
        tabThree.setText(getResources().getString(R.string.deliverytext));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_select_new,0,0);
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_unselect,0,0);
        tabThree.setGravity(Gravity.CENTER);

        tabFour = (TextView) LayoutInflater.from(con).inflate(R.layout.custom_tab_new, null);
        tabFour.setText(getResources().getString(R.string.scan));
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_qr_code_unselect,0,0);
        tabFour.setGravity(Gravity.CENTER);


//        tabFive = (TextView) LayoutInflater.from(con).inflate(R.layout.custom_tab_new, null);
//        tabFive.setText("New");
//        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ongoing_blue, 0, 0);
//        tabFive.setGravity(Gravity.CENTER);


        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabOne));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabTwo));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabThree));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabFour));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(tabFive));

        viewPager.setOffscreenPageLimit(4);
        viewPager.beginFakeDrag();
        viewPager.setPagingEnabled(false);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
    }

    private void showErrorDialog() {
        isServiceError = false;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(notificationMessage);
        alert.setTitle(notificationContent);
        alert.setCancelable(false);
        alert.setPositiveButton(getResources().getString(R.string.closeapptext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                Intent intent=new Intent(MyPackagesActivity.this,Splash.class);
//                startActivity(intent);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                finish();
                PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getApplicationContext().getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                getApplicationContext().startActivity(mainIntent);
                Runtime.getRuntime().exit(0);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAndRemoveTask();
//                } else {
//                    finishAffinity();
//                    System.exit(0);
//                }


            }
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");


        String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");
//        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type1----"+language_type );
        String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2, "");
//        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type2----"+language_type2 );
        if (language_type.equals(language_type2)) {
//            Log.d("MYPACKAGE_LOG","language_type_onResume--equal1--"+language_type );
            if (language_type.equals("1")) {
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            } else if (language_type.equals("2")) {
//                Log.d("MYPACKAGE_LOG","language_type--onResume_equals=2----"+language_type );
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }
        } else {
//            Log.d("MYPACKAGE_LOG","language_type_on else----"+language_type +language_type2);
            editor.putString(SharedValues.LANGUAGE_Type2, language_type);
            editor.commit();
//            Log.d("MYPACKAGE_LOG","editor--commit 1 & 2---"+language_type +language_type2);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }


//        Log.d("MYPACKAGE_LOG","language_type_onResume  finaltype---"+language_type );

//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//
//
//                        if(language_type.equals("1")&&language_type!=null){
//                            Locale myLocale = new Locale("sw");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }else if(language_type.equals("2")&&language_type!=null){
//                            Locale myLocale = new Locale("en");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }else {
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
//                    if(language_type.equals(null)){
//                        Log.d("Mypackage_log","language_type-----"+language_type);
//                        String locale;
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                        } else {
//                            locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                        }
//
//                            Log.d("Mypackage_log","locale :"+ locale );
//
//                        if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
//                                Log.d("Mypackage_log","language_type1---  "+language_type );
//                            Locale myLocale = new Locale("sw");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//
//                        } else {
////                           language_type="2";
//                        Log.d("Mypackage_log","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                            Locale myLocale = new Locale("en");
//                            Resources res = getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//                        }
//                    }
//                    else\\
//                        if(language_type.equals("1")){
//                        Log.d("Mypackage_log","language_type1--not_equal null  "+language_type );
//
//                        Locale myLocale = new Locale("sw");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }else if(language_type.equals("2")){
//                        Log.d("Mypackage_log","language_type2--not_equal null  "+language_type );
//                        Locale myLocale = new Locale("en");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }//
//                }
//            }
//        }
//        setupTabNames();
//        setupNavigationDrawer();
        LocalBroadcastManager.getInstance(con).registerReceiver(DriverDeactiveBroadcast, new IntentFilter(Global.INACTIVE_BROADCAST));
        LocalBroadcastManager.getInstance(con).registerReceiver(serviceErrorBroadcaste, new IntentFilter(Global.SERVICE_ERROR_BROADCAST));
        is_Active = true;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (driverId == null || driverId.trim().length() == 0) {
            UserDeactivateEvent();
        } else {
            if (isNotificationAvailable == 1) {
                setupTabNames();
            }
            isNotificationAvailable = 0;

            driverName = preferences.getString(SharedValues.DRIVER_NAME, "");
            driverImage = preferences.getString(SharedValues.DRIVER_IMAGE, "");
            driverUniqueId = preferences.getString(SharedValues.DRIVER_UNIQUES_CODE, "");

            tvUsername.setText(driverName);
            tvDriverUniqueId.setText(driverUniqueId);

            String countrycode = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
            String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;

            Log.d("IMAGE_PATH_LOG", imagePath);

            Glide.with(con).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(profileImage);

            getDriverProfileApi(driverId);
        }
        if (isServiceError == true) {
            showErrorDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        is_Active = false;
        LocalBroadcastManager.getInstance(con).unregisterReceiver(DriverDeactiveBroadcast);
        LocalBroadcastManager.getInstance(con).unregisterReceiver(serviceErrorBroadcaste);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            tabOne.setTextColor(Color.WHITE);
            viewPager.setCurrentItem(0);
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_ongoing_select_new,0,0);
//            tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_ongoing_select,0,0);
        } else if (tab.getPosition() == 1) {
            viewPager.setCurrentItem(1);
            tabTwo.setTextColor(Color.WHITE);
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickup_unselect_new,0,0);
//            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickups_selected,0,0);
        } else if (tab.getPosition() == 2) {
            viewPager.setCurrentItem(2);
            tabThree.setTextColor(Color.WHITE);
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_unselect_new,0,0);
//            tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_selected,0,0);
        } else if (tab.getPosition() == 3) {

//            if(isNetworkAvailable()){
//
//            }else{
//                Snackbar(ivBack);
//            }
            openScanner(6);

            OngoingFragment.click = "scanning";
            viewPager.setCurrentItem(3);
            tabFour.setTextColor(Color.WHITE);
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_qr_code_selected,0,0);

        }else if (tab.getPosition() == 4) {
//            viewPager.setCurrentItem(4);
//            tabFive.setTextColor(Color.WHITE);
//            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_new_req_selected, 0, 0);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            tabOne.setTextColor(Color.parseColor("#98c4eb"));
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ongoing_unselect_new,0,0);
//            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_ongoing_unselect,0,0);
        } else if (tab.getPosition() == 1) {
            tabTwo.setTextColor(Color.parseColor("#98c4eb"));
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickup_select_new,0,0);
//            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pickups_unselect,0,0);
        } else if (tab.getPosition() == 2) {
            tabThree.setTextColor(Color.parseColor("#98c4eb"));
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_select_new,0,0);
//            tabThree.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_delivery_unselect,0,0);
        } else if (tab.getPosition() == 3) {
            tabFour.setTextColor(Color.parseColor("#98c4eb"));
            tabFour.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_qr_code_unselect,0,0);
        } else if (tab.getPosition() == 4) {
            tabFive.setTextColor(Color.parseColor("#98c4eb"));
//            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_new_req_unselect, 0, 0);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    //scanner result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("SCANNING_LOG","result"+result);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("SCANNING_LOG","result ===== "+result);
            } else {
                Log.d("SCANNING_LOG","result"+result);
                Log.d("SCANNING_LOG","OngoingFragment.click"+OngoingFragment.click);
                if (OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("Barcode")) {
                    String scanPackageCode = result.getContents();
                    getPackageDetail(scanPackageCode);
                } else if ((OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("delivery"))) {
                    getPackageDetail(result.getContents());
                } else if (OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("Sticker")) {
                    showAlert(getResources().getString(R.string.doyourealluwantto) + " " + result.getContents() + " sticker ?", result.getContents());
                } else if (OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("scanning")) {
                    if (isNetworkAvailable()){
                    Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailScanActivity.class);
                    intent.putExtra("PackageCode", "" + result.getContents());
                    intent.putExtra("is_alternate_pickupAndDropoff_locations", "" + alternatePickupAndDropoffLocations);
                    startActivity(intent);
                }}else{
                    Snackbar(ivBack);
                }
            }
            OngoingFragment.isScanningPackage = false;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getPackageDetail(final String pacakgeCode) {
        if (getPackageResponseCall != null) {
            getPackageResponseCall.cancel();
        }

        if (!(MyPackagesActivity.this.isFinishing())) {
            if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                PckgPrgsDlg.dismiss();
            }
        }

        if (PckgPrgsDlg == null) {
            PckgPrgsDlg = new ProgressDialog(activitycon);
        }

        PckgPrgsDlg = PckgPrgsDlg.show(activitycon, "", "Loading...");

        if (pacakgeCode != null && pacakgeCode.length() > 0) {

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {

                if (!(MyPackagesActivity.this.isFinishing())) {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }
                Global.signInIntent(this);
            } else {
                String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                if (country_code != null && country_code.trim().length() > 0) {


                    getPackageResponseCall = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode);
                    getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
                        @Override
                        public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                            try {
                                if (!(MyPackagesActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                            } catch (Exception ex) {
                            }

                            if (response.isSuccessful()) {
                                GetPackageResponse packageResponse = response.body();
                                if (packageResponse != null) {
//                                    Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()------------"+packageResponse.getResponse());

                                    if (packageResponse.getResponse() == 1) {
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------111------"+packageResponse.getResponse());
                                        String selectedPckgCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        String packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");
                                        if (packageCode != null && packageCode.length() > 0 && selectedPckgCode != null && selectedPckgCode.length() > 0 && (!packageCode.equalsIgnoreCase(selectedPckgCode))) {
                                            Toast.makeText(con, "Scanned package not matched with ongoing package", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String packageStatus = packageResponse.getData().getPackage_details().getPackage_status();
//                                            Log.d("MYPACKAGE_LOG","-----packageStatus-------------"+packageStatus);
                                            if (packageStatus != null && packageStatus.trim().length() > 0 && Integer.parseInt(packageStatus) < 5) {

                                                if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("1")) {
                                                    Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
                                                    intent.putExtra("packageResponse", "" + packageResponse);
                                                    startActivity(intent);
                                                }
//                                                      else if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("2")) {
//                                                    Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
//                                                    intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                    intent.putExtra("packageResponse", "" + packageResponse);
//                                                    startActivity(intent);
//                                                }
                                                else {
                                                    if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("1")) {
                                                        //Pass Intent to Get Package Detail Activity
                                                        Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                        intent.putExtra("PackageCode", "" + pacakgeCode);
                                                        intent.putExtra("packageResponse", "" + packageResponse);
                                                        startActivity(intent);
                                                    }
//                                                    else if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("2")) {
//                                                        //Pass Intent to Get Package Detail Activity
//                                                        Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
//                                                        intent.putExtra("PackageCode", "" + pacakgeCode);
//                                                        intent.putExtra("packageResponse", "" + packageResponse);
//                                                        startActivity(intent);
//                                                    }
                                                    else {
                                                        String isDOP = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                                                        if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("1")) {
                                                            Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                            intent.putExtra("PackageCode", "" + pacakgeCode);
                                                            intent.putExtra("packageResponse", "" + packageResponse);
                                                            startActivity(intent);
                                                        } else if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("2")) {
                                                            Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                            intent.putExtra("PackageCode", "" + pacakgeCode);
                                                            intent.putExtra("packageResponse", "" + packageResponse);
                                                            startActivity(intent);
                                                        } else {
                                                            new AlertDialog.Builder(MyPackagesActivity.this)
                                                                    .setMessage(getResources().getString(R.string.packagepaymentisincomplete))
                                                                    .setCancelable(false).setPositiveButton(getResources().getString(R.string.oktext), null)
                                                                    .create()
                                                                    .show();
                                                        }
                                                    }
                                                }
                                            } else {
                                                Intent intent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                intent.putExtra("PackageCode", "" + pacakgeCode);
                                                intent.putExtra("packageResponse", "" + packageResponse);
                                                startActivity(intent);
                                            }
                                        }
                                    } else if (response.body().getData().getMessage() != null) {
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getResponse());
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+response.body().getData().getMessage());
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------000------"+packageResponse.getData().getMessage());

                                        if (response.body().getData().getMessage().trim().equals("Unauthenticated")) {
                                            Global.signInIntent(MyPackagesActivity.this);
                                        }
                                        Toast.makeText(activitycon, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();

                                    } else {
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+packageResponse.getResponse());
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else------"+response.body().getData().getMessage());
//                                        Log.d("MYPACKAGE_LOG","-----packageResponse.getResponse()-------else-----"+packageResponse.getData().getMessage());

                                        Toast.makeText(activitycon, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                            try {
                                if (!(MyPackagesActivity.this.isFinishing())) {
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
                    if (!(MyPackagesActivity.this.isFinishing())) {
                        if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                            PckgPrgsDlg.dismiss();
                        }
                    }
                }

            }

        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

//                IntentIntegrator integrator = new IntentIntegrator(activitycon);
////        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Scan a barcode");
//                integrator.setCameraId(0);  // Use a specific camera of the device
//                integrator.setOrientationLocked(true);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();


            case R.id.iv_navigation:
                language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS, "");
                drawerLayout.openDrawer(mDrawerRelativeLayout);
//                language_type=preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
                if (language_type.equals("1")) {
                    Locale myLocale = new Locale("sw");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                } else if (language_type.equals("2")) {
                    Locale myLocale = new Locale("en");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }
//                else {
//                    String locale;
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
//                    } else {
//                        locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
//                    }
//
////                            Log.d(TAG,"locale :"+ locale );
//
//                    if (locale.equals("hi_") || locale.equals("hi_IN")) {
////                        language_type="1";
//
////                                Log.d("check_type","language_type1---  "+language_type );
//                        Locale myLocale = new Locale("sw");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//
//                    } else {
////                           language_type="2";
////                        Log.d("check_type","language_type2--  "+language_type );
////                                Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
//                        Locale myLocale = new Locale("en");
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = myLocale;
//                        res.updateConfiguration(conf, dm);
//                    }
//
//                    Log.d("check_type", "language_typelllll--  " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                }

                break;
            case R.id.ll_qrcode:
                closeDrawer();
                Intent QrIntent = new Intent().setClass(MyPackagesActivity.this, QrCodeActivity.class);
                QrIntent.putExtra("name", driverName);
                QrIntent.putExtra("unid", driverUniqueId);
                QrIntent.putExtra("id", driverId);
                startActivity(QrIntent);
                break;
            case R.id.ll_account:
                closeDrawer();
                Intent accountIntent = new Intent().setClass(MyPackagesActivity.this, UserAccountActivityNew.class);
                startActivity(accountIntent);
                break;
            case R.id.ll_privacy_policy:
                closeDrawer();
                Intent privacyIntent = new Intent().setClass(MyPackagesActivity.this, PrivacyPolicyActivityNew.class);
                startActivity(privacyIntent);
                break;
            case R.id.ll_about_us:
                closeDrawer();
                Intent aboutUsIntent = new Intent().setClass(MyPackagesActivity.this, AboutUsActivityNew.class);
                startActivity(aboutUsIntent);
                break;
            case R.id.ll_terms:
                closeDrawer();
                Intent termsIntent = new Intent().setClass(MyPackagesActivity.this, TermsConditionsActivityNew.class);
                startActivity(termsIntent);
                break;
            case R.id.ll_download_pdfs:
                closeDrawer();
                Intent labelIntent = new Intent().setClass(MyPackagesActivity.this, PdfListActivityNew.class);
                startActivity(labelIntent);
                break;
            case R.id.ll_logout:
                closeDrawer();
                if (isNetworkAvailable()) {
                    showLogoutDialog();
                } else {
                    Snackbar(ivBack);
                }
                break;
            case R.id.ll_history:
                closeDrawer();
                Intent historyIntent = new Intent().setClass(MyPackagesActivity.this, HistoryActivityNew.class);
                startActivity(historyIntent);
                break;
            case R.id.ll_Language:
                closeDrawer();
                Intent LanguageIntent = new Intent().setClass(MyPackagesActivity.this, LanguageSettings.class);
                startActivity(LanguageIntent);
                break;

            case R.id.btn_status:
                if (switchStatus.isChecked()) {

                 AlertDialog.Builder builder = new AlertDialog.Builder(MyPackagesActivity.this);
                    builder.setTitle(getResources().getString(R.string.offlineconfirmation));
                    builder.setMessage(getResources().getString(R.string.reallywantoffline));
                    builder.setCancelable(false);
                    //ok Button
                    builder.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (forgotPaswdResponseCall != null) {
                                forgotPaswdResponseCall.cancel();
                            }

                            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                            String Accept = Global.ACCEPT;
                            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
                            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                                Global.signInIntent(con);
                            } else {
                                if (isNetworkAvailable()) {
                                    //
                                    String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                                    if (country_code != null && country_code.trim().length() > 0) {


                                        forgotPaswdResponseCall = webRequest.apiInterface.statusDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, "1");
                                        statusChange(forgotPaswdResponseCall, "1");

                                    }
                                } else {
                                    Snackbar(ivBack);
                                }

                            }

                        }
                    });
                    //Cancel Button
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                } else {
                    //API HIT For Online Switch
                    if (forgotPaswdResponseCall != null) {
                        forgotPaswdResponseCall.cancel();
                    }

                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                    String Accept = Global.ACCEPT;
                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");
                    if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                        Global.signInIntent(con);
                    } else {
                        if (isNetworkAvailable()) {


                            String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                            if (country_code != null && country_code.trim().length() > 0) {

                                forgotPaswdResponseCall = webRequest.apiInterface.statusDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, "0");
                                statusChange(forgotPaswdResponseCall, "0");
                            }

                        } else {
                            Snackbar(ivBack);
                        }


                    }

                }
                break;
        }
    }

    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(getResources().getString(R.string.wanttologout));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.logouttext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (notificationCall != null) {
                    notificationCall.cancel();
                }

                String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");

                String Accept = Global.ACCEPT;
                driverId = preferences.getString(SharedValues.DRIVER_ID, "");
                if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                    if (logoutProgress != null && logoutProgress.isShowing()) {
                        logoutProgress.dismiss();
                    }
                    Global.signInIntent(MyPackagesActivity.this);
                } else {


                    String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                    if (country_code != null && country_code.trim().length() > 0) {

                        //API HIT LOGOUT
                        notificationCall = webRequest.apiInterface.logoutDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId);

                        if (isNetworkAvailable()) {
                            logoutUser(notificationCall);
                        } else {
                            Snackbar(ivBack);
                        }


                    } else {
                        if (logoutProgress != null && logoutProgress.isShowing()) {
                            logoutProgress.dismiss();
                        }
                    }
                }
                //Dialog Dismiss
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.canceltextt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dialog Dismiss
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void logoutUser(Call<ForgotPaswdResponse> logoutUser) {
        if (!(MyPackagesActivity.this.isFinishing())) {
            if (logoutProgress != null && logoutProgress.isShowing()) {
                logoutProgress.dismiss();
            }
        }
        if (logoutProgress == null) {
            logoutProgress = new ProgressDialog(this);
        }
        logoutProgress.setMessage(getResources().getString(R.string.logouttext) + "...");
        logoutProgress.setCancelable(false);
        logoutProgress.show();
        logoutUser.enqueue(new Callback<ForgotPaswdResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
                if (response.isSuccessful()) {
                    ForgotPaswdResponse notificationResponse = response.body();
                    if (notificationResponse.getResponse() == 1) {
                        Intent i1 = new Intent().setClass(MyPackagesActivity.this, UpdateDriverLocationServiceNew.class);
                        stopService(i1);
                        Intent serviceIntent = new Intent(MyPackagesActivity.this, ForegroundService.class);
                        stopService(serviceIntent);
                        editor.clear().commit();
                        editor.putString(SharedValues.LANGUAGE_SETTINGS, "");
                        editor.clear().commit();
                        new removeToken().execute();
                        Intent i = new Intent().setClass(MyPackagesActivity.this, Selected_Country.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Toast.makeText(con, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!MyPackagesActivity.this.isFinishing()) {
                    if (logoutProgress != null && logoutProgress.isShowing()) {
                        logoutProgress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                if (!MyPackagesActivity.this.isFinishing()) {
                    if (logoutProgress != null && logoutProgress.isShowing()) {
                        logoutProgress.dismiss();
                    }
                }
                t.printStackTrace();
            }
        });
    }

    private void statusChange(Call<ForgotPaswdResponse> status, final String statusChange) {
        status.enqueue(new Callback<ForgotPaswdResponse>() {
            @Override
            public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
                if (response.isSuccessful()) {
                    ForgotPaswdResponse forgotPaswdResponse = response.body();
                    if (forgotPaswdResponse.getResponse() == 1) {
                        if (statusChange.equalsIgnoreCase("0")) {
                            switchStatus.setChecked(true);
                            //Save Status
                            editor.putString(SharedValues.DRIVER_STATUS, "0");
                            editor.commit();
                            //Save Value to Prefs
                            editor.putBoolean("IsChecked", true);
                            editor.apply();
                            tvStatus.setText(getResources().getString(R.string.onlinetext));
                            NotificationManager  noti = (NotificationManager) con
                                    .getSystemService(Context.NOTIFICATION_SERVICE);

                            Intent intent = new Intent(MyPackagesActivity.this, UpdateDriverLocationServiceNew.class);
//                            startService(intent);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                startForegroundService(intent);
//                            } else {
                                startService(intent);
//                            }
                        } else if (statusChange.equalsIgnoreCase("1")) {
                            switchStatus.setChecked(false);
                            //Save Status
                            editor.putString(SharedValues.DRIVER_STATUS, "1");
                            editor.commit();
                            //Save Value to Prefs
                            editor.putBoolean("IsChecked", false);
                            editor.apply();
                            tvStatus.setText(getResources().getString(R.string.offlinetext));
                        }
                    } else if (response.body().getData().getMessage() != null) {
                        if (response.body().getData().getMessage().equals("Unauthenticated")) {
                            Global.signInIntent(MyPackagesActivity.this);
                        }
                    } else {
                        Toast.makeText(con, getResources().getString(R.string.pleasetryagain), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void showAlert(String messsage, final String contents) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("");
        builder.setMessage(messsage);
        builder.setCancelable(false);
        //ok Button
        builder.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //API HIT Sticker Attach
                assignBarcode(contents);
            }
        });
        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.canceltextt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void assignBarcode(final String stickerCode) {
        if (barcodeResponseCall != null) {
            barcodeResponseCall.cancel();
        }
        if (!(MyPackagesActivity.this.isFinishing())) {
            if (stickerProgress != null && stickerProgress.isShowing()) {
                stickerProgress.dismiss();
            }
        }
        if (stickerProgress == null) {
            stickerProgress = new ProgressDialog(con);
        }
        stickerProgress.setMessage(getResources().getString(R.string.assigningstickertext) + "...");
        stickerProgress.setCancelable(false);
        stickerProgress.show();
        packageCode = preferences.getString(SharedValues.PACKAGE_CODE, "");

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(MyPackagesActivity.this.isFinishing())) {
                if (stickerProgress != null && stickerProgress.isShowing()) {
                    stickerProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            if (isNetworkAvailable()) {
                String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                if (country_code != null && country_code.trim().length() > 0) {

                    barcodeResponseCall = webRequest.apiInterface.assignBarcodeApi(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, packageCode, stickerCode);
                    barcodeResponseCall.enqueue(new Callback<AssignBarcodeResponse>() {
                        @Override
                        public void onResponse(Call<AssignBarcodeResponse> call, Response<AssignBarcodeResponse> response) {
                            AssignBarcodeResponse barcodeResponse = response.body();

//                            Log.d("STICKER_LOG", "-----response.body()------" + response.body());
//                            Log.d("STICKER_LOG", "-----barcodeResponse.getResponse()------" + barcodeResponse.getResponse());
                            if (barcodeResponse.getResponse().equalsIgnoreCase("1")) {
                                // showToastMessage(MyPackages1.this, barcodeResponse.getData().getMessage());
                                Log.d("STICKER_LOG", "-----response==1------" + response.body());
                                getPackageDetailsPickup(packageCode, "Attach");
                                //Pass Intent to Get Package Detail Activity
                            } else if (barcodeResponse.getData().getMessage() != null) {
                                if (barcodeResponse.getData().getMessage().trim().equals("Unauthenticated")) {
                                    Global.signInIntent(MyPackagesActivity.this);
                                }
                                Toast.makeText(con, "" + barcodeResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Log.d("STICKER_LOG", "-----barcodeResponse.getData().getMessage()---11---" + barcodeResponse.getData().getMessage());
                            } else {
                                Toast.makeText(con, "" + barcodeResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Log.d("STICKER_LOG", "-----barcodeResponse.getData().getMessage()-----222-----" + barcodeResponse.getData().getMessage());
                            }
                            if (!MyPackagesActivity.this.isFinishing()) {
                                if (stickerProgress != null && stickerProgress.isShowing()) {
                                    stickerProgress.dismiss();
//                                    Log.d("STICKER_LOG", "-----barcodeResponse.getData().getMessage()-----333-----" + barcodeResponse.getData().getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AssignBarcodeResponse> call, Throwable t) {
                            if (!MyPackagesActivity.this.isFinishing()) {
                                if (stickerProgress != null && stickerProgress.isShowing()) {
                                    stickerProgress.dismiss();

//                                    Log.d("STICKER_LOG", "-----onFailure-----333-----");
                                }
                            }
                            t.printStackTrace();
                        }
                    });
                } else {
                    if (!(MyPackagesActivity.this.isFinishing())) {
                        if (stickerProgress != null && stickerProgress.isShowing()) {
                            stickerProgress.dismiss();
                        }
                    }
//                    Log.d("STICKER_LOG", "-----onFailure---else part--333-----");
                }
            } else {
                Snackbar(ivBack);
            }

        }


    }

    private void getPackageDetailsPickup(final String pacakgeCode, final String pickup) {
        if (callPackageDetails != null) {
            callPackageDetails.cancel();
        }
        if (!(MyPackagesActivity.this.isFinishing())) {
            if (packageProgress != null && packageProgress.isShowing()) {
                packageProgress.cancel();
            }
        }
        if (packageProgress == null) {
            packageProgress = new ProgressDialog(con);
        }
        packageProgress.setMessage(getResources().getString(R.string.loadingtext) + "...");
        packageProgress.setCancelable(false);
        packageProgress.show();


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!(MyPackagesActivity.this.isFinishing())) {
                if (packageProgress != null && packageProgress.isShowing()) {
                    packageProgress.cancel();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {


                callPackageDetails = webRequest.apiInterface.getPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode);
                callPackageDetails.enqueue(new Callback<GetPackageResponse>() {
                    @Override
                    public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                        if (response.isSuccessful()) {
                            GetPackageResponse packageResponse = response.body();

                            //Check Response
                            if (packageResponse.getResponse() == 1) {
                                if (pickup.equalsIgnoreCase("Pickup") || pickup.equalsIgnoreCase("Attach")) {
                                    if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("1")) {
                                        Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                        detailIntent.putExtra("PackageCode", pacakgeCode);
                                        startActivity(detailIntent);
                                    } else {
                                        if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("1")) {
                                            //Pass Intent to Get Package Detail Activity
                                            //Pass Intent to Get Package Detail Activity
                                            Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                            detailIntent.putExtra("PackageCode", pacakgeCode);
                                            startActivity(detailIntent);
                                        } else {
                                            String isDOP = packageResponse.getData().getPackage_details().getIs_cash_on_pickup();
                                            if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("1")) {
                                                Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                detailIntent.putExtra("PackageCode", pacakgeCode);
                                                startActivity(detailIntent);
                                            } else if (isDOP != null && isDOP.trim().length() > 0 && isDOP.trim().equals("2")) {
                                                Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                                detailIntent.putExtra("PackageCode", pacakgeCode);
                                                startActivity(detailIntent);
                                            } else {
                                                new AlertDialog.Builder(MyPackagesActivity.this)
                                                        .setMessage(getResources().getString(R.string.packagepaymentisincomplete))
                                                        .setPositiveButton(getResources().getString(R.string.oktext), null)
                                                        .setCancelable(false)
                                                        .create()
                                                        .show();
                                            }
                                        }
                                    }
                                } else if (pickup.equalsIgnoreCase("delivery")) {
                                    if (packageResponse.getData().getUser_details().getType().equalsIgnoreCase("1")) {
                                        Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                        detailIntent.putExtra("PackageCode", pacakgeCode);
                                        startActivity(detailIntent);
                                    } else {
                                        if (packageResponse.getData().getPackage_details().getPayment_status().equalsIgnoreCase("0")) {
                                            new AlertDialog.Builder(MyPackagesActivity.this)
                                                    .setMessage(getResources().getString(R.string.packagepaymentisincomplete))
                                                    .setPositiveButton(getResources().getString(R.string.oktext), null)
                                                    .setCancelable(false)
                                                    .create()
                                                    .show();
                                        } else {
                                            Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                            detailIntent.putExtra("PackageCode", pacakgeCode);
                                            startActivity(detailIntent);
                                        }
                                    }
                                } else {
                                    Intent detailIntent = new Intent(MyPackagesActivity.this, GetPackageDetailActivity.class);
                                    detailIntent.putExtra("PackageCode", pacakgeCode);
                                    startActivity(detailIntent);
                                }
                            } else if (response.body().getData().getMessage() != null) {
                                if (response.body().getData().getMessage().trim().equals("Unauthenticated")) {
                                    Global.signInIntent(MyPackagesActivity.this);
                                }
                            } else {
                                Toast.makeText(con, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!(MyPackagesActivity.this.isFinishing())) {
                            if (packageProgress != null && packageProgress.isShowing()) {
                                packageProgress.cancel();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                        if (!(MyPackagesActivity.this.isFinishing())) {
                            if (packageProgress != null && packageProgress.isShowing()) {
                                packageProgress.cancel();
                            }
                        }
                        t.printStackTrace();
                    }
                });

            } else {
                if (packageProgress != null && packageProgress.isShowing()) {
                    packageProgress.cancel();
                }
            }

        }

    }

    private void getDriverProfileApi(String driverId) {
        if (getProfileResponseCall != null) {
            getProfileResponseCall.cancel();
        }
        if (!(MyPackagesActivity.this.isFinishing())) {
            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                driverProfileProgress.dismiss();
            }
        }
        if (driverProfileProgress == null) {
            driverProfileProgress = new ProgressDialog(con);
        }

        driverProfileProgress.setMessage(getResources().getString(R.string.gettingdetailsdialog) + "...");
        driverProfileProgress.setCancelable(false);
        driverProfileProgress.show();


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if (!MyPackagesActivity.this.isFinishing()) {
                if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                    driverProfileProgress.dismiss();
                }
            }
            Global.signInIntent(this);
        } else {

            String country_code = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
            if (country_code != null && country_code.trim().length() > 0) {

                //API HIT
                Log.d("PROFILEAPI", " " + Global.USER_AGENT + " " + Accept + " " + USER_TOKEN + " " + driverId + " " + driverId);
                getProfileResponseCall = webRequest.apiInterface.getProfileApi(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId);
                getProfileResponseCall.enqueue(new Callback<GetProfileResponse>() {
                    @Override
                    public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                        if (response.isSuccessful()) {
                            getProfileResponse = response.body();

                            Log.d("DRIVERPRF", " " + getProfileResponse);
                            //Check Response
                            if (getProfileResponse.getResponse() == 1) {
                                //Get Driver Data
                                String driverId = getProfileResponse.getData().getId();
                                String driverName = getProfileResponse.getData().getDriver_name();
                                String driverEmail = getProfileResponse.getData().getDriver_email();
                                String driverPhone = getProfileResponse.getData().getDriver_phone();
                                String vehicleIdFk = getProfileResponse.getData().getVehicle_id_fk();
                                String driverImage = getProfileResponse.getData().getDriver_image();
                                String vehicleType = getProfileResponse.getData().getVehicle_type();
                                String vehicleName = getProfileResponse.getData().getVehicle_name();
                                String vehicleNumber = getProfileResponse.getData().getVehicle_number();
                                String vehicleColor = getProfileResponse.getData().getVehicle_color();
                                String vehicleOwnerShip = getProfileResponse.getData().getVehicle_ownership();
                                String isDriverActivate = getProfileResponse.getData().getDriver_active_status();
                                String driverDeviceId = getProfileResponse.getData().getDriver_device_id();
                                Log.d("DRIVERPRF", " " + driverDeviceId);
                                String uuid = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
                                if (driverDeviceId != null && uuid != null && uuid.length() > 0 && driverDeviceId.length() > 0) {
                                    if (!(driverDeviceId.trim().equalsIgnoreCase(uuid.trim()))) {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(con);
                                        alert.setMessage(getResources().getString(R.string.accountisloginanotherr));
                                        alert.setCancelable(false);
                                        alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editor.clear().commit();
                                                Intent i = new Intent().setClass(MyPackagesActivity.this, SignIn.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        }).show();
                                    }
                                }

                                if (isDriverActivate != null && isDriverActivate.length() > 0 && isDriverActivate.equalsIgnoreCase("0")) {
                                    editor.clear().commit();
                                    Intent i = new Intent().setClass(MyPackagesActivity.this, SignIn.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                } else {

                                    if (driverName != null && driverName.trim().length() > 0) {
                                        editor.putString(SharedValues.DRIVER_NAME, driverName);
                                    }
                                    if (driverEmail != null && driverEmail.trim().length() > 0) {
                                        editor.putString(SharedValues.DRIVER_EMAIL, driverEmail);
                                    }
                                    if (driverPhone != null && driverPhone.trim().length() > 0) {
                                        editor.putString(SharedValues.DRIVER_PHONE, driverPhone);
                                    }
                                    if (driverImage != null) {
//                                        String imagePath = Global.BASE_URL + "backend/public/uploads/driver_profile/" + driverId + "/" + driverImage;
                                        String countrycode = CountryCodeCheck.countrycheck(MyPackagesActivity.this);
                                        String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;
                                        //Load Image using Glide
                                        if (!MyPackagesActivity.this.isFinishing()) {
                                            Glide.with(con).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(profileImage);
                                        }

                                        editor.putString(SharedValues.DRIVER_IMAGE, driverImage);
                                    }
                                    editor.commit();
                                }
                            } else if (response.body().getData().getMessage() != null) {
                                if (response.body().getData().getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(MyPackagesActivity.this);
                                }
                            } else {
                                Toast.makeText(con, "" + getProfileResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!MyPackagesActivity.this.isFinishing()) {
                            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                driverProfileProgress.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                        if (!MyPackagesActivity.this.isFinishing()) {
                            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                                driverProfileProgress.dismiss();
                            }
                        }

                        t.printStackTrace();
                    }
                });


            } else {
                if (!MyPackagesActivity.this.isFinishing()) {
                    if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                        driverProfileProgress.dismiss();
                    }
                }
            }
        }

    }
    @Override
    protected void onDestroy() {

        con=MyPackagesActivity.this;
        super.onDestroy();
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

    public void closeActivity() {
        finish();
    }

    class removeToken extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    Object value = getIntent().getExtras().get(key);
                }
            }
            return null;
        }
    }


}