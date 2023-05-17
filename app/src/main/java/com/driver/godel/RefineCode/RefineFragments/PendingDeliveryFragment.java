package com.driver.godel.RefineCode.RefineFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineAdapter.PendingDeliveryAdapter;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.PendingDeliveryResponse.pendingDelvResp;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupPojo;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ajay2.Sharma on 7/6/2018.
 */

public class PendingDeliveryFragment extends GodelDriverFragment implements View.OnClickListener {
    private View view;
    private int pageNo = 1, previousListSize = 0, totalItems = 0;
    private TextView tvNoDlvryPckg, tvMore;
    private RecyclerView rvDeliveryPackages;
    private LinearLayoutManager layoutManager;
    private static Context context;
    private Call<pendingDelvResp> pendingResponseCall;
    private ProgressDialog deliveryDialog;
    private pendingDelvResp pendingResponse;
    private ArrayList<PendingPickupPojo> PendingDeliveryBeans;
    private ImageView ivLoadMore;
    boolean isOpen = false;
    final String TAG="PENDINGDELIVERY_LOG";
    String language_type,is_round_trip="0",is_first_round_completed="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_deliveries_new, container, false);
        Log.d(TAG,"fragment call");
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

        PendingDeliveryBeans = new ArrayList<PendingPickupPojo>();
        PendingDeliveryBeans.clear();
        isOpen = false;

        ivLoadMore = (ImageView) view.findViewById(R.id.iv_load_more);
        tvMore = (TextView) view.findViewById(R.id.tv_more);
        tvNoDlvryPckg = (TextView) view.findViewById(R.id.tv_no_delivery_package_available);

        rvDeliveryPackages = (RecyclerView) view.findViewById(R.id.rv_delivery_packages);
        rvDeliveryPackages.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        rvDeliveryPackages.setLayoutManager(layoutManager);
        rvDeliveryPackages.setItemAnimator(new DefaultItemAnimator());

        rvDeliveryPackages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    if (PendingDeliveryBeans.size() < totalItems) {
                        if (PendingDeliveryBeans != null && PendingDeliveryBeans.size() > 5) {
                            tvMore.setVisibility(VISIBLE);
                            ivLoadMore.setVisibility(VISIBLE);
                        }
                    }
                } else {
                    tvMore.setVisibility(GONE);
                    ivLoadMore.setVisibility(GONE);
                }
            }
        });

        ivLoadMore.setOnClickListener(this);
        tvMore.setOnClickListener(this);

        tvNoDlvryPckg.setVisibility(GONE);
        ivLoadMore.setVisibility(GONE);
        tvMore.setVisibility(GONE);
        rvDeliveryPackages.setVisibility(GONE);

        return view;
    }

    private BroadcastReceiver deliveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // intent can contain anydata
            if (isOpen == true) {
                pageNo = 1;
                previousListSize = 0;
                getPendingDeliveryPackages();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
// language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
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
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
//            PendingDeliveryFragment fragment = (PendingDeliveryFragment)
//             getFragmentManager().findFragmentById(R.id.rv_pending_deliveries);
//
//               getFragmentManager().beginTransaction()
//                    .detach(fragment)
//                    .attach(fragment)
//                    .commit();


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

        isOpen = true;
        LocalBroadcastManager.getInstance(context).registerReceiver(deliveryReceiver, new IntentFilter(Global.DELIVERY_BROADCAST));
        pageNo = 1;
        previousListSize = 0;

        getPendingDeliveryPackages();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(deliveryReceiver);
        isOpen = false;
        if (pendingResponseCall != null) {
            pendingResponseCall.cancel();
        }
    }

    private void getPendingDeliveryPackages() {
        context = getActivity();
        if (isNetworkAvailable()) {
            if (pendingResponseCall != null) {
                pendingResponseCall.cancel();
            }

            if (deliveryDialog == null) {
                deliveryDialog = new ProgressDialog(getActivity());
            }
            if(!(((Activity)context).isFinishing())) {
                if (deliveryDialog != null && deliveryDialog.isShowing()) {
                    deliveryDialog.dismiss();
                }
            }
            deliveryDialog.setMessage(getResources().getString(R.string.loadingpackages)+"...");
            deliveryDialog.setCancelable(false);
            deliveryDialog.show();

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");
            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if(!(((Activity)context).isFinishing())) {
                    if (deliveryDialog != null && deliveryDialog.isShowing()) {
                        deliveryDialog.dismiss();
                    }
                }
                Global.signInIntent(context);
            }
            else {

                String country_code=CountryCodeCheck.countrycheck(context);
                if(country_code!=null&&country_code.trim().length()>0){
                    Log.d(TAG,"driver id"+driverId);
                    Log.d(TAG,"page no"+pageNo);
                    Log.d(TAG,"token"+USER_TOKEN);
                    Log.d("Delivery Data","qweqweqw "+Global.USER_AGENT+" "+Accept+" "+USER_TOKEN+" "+driverId+" "+pageNo+" "+driverId);
                    pendingResponseCall = webRequest.apiInterface.pendingDeliveries(country_code,Global.USER_AGENT, Accept, USER_TOKEN, driverId, pageNo, driverId);
                    getPendingDeliveries(pendingResponseCall);
                }else{
                    if (deliveryDialog != null && deliveryDialog.isShowing()) {
                        deliveryDialog.dismiss();
                    }
                }


            }
        } else {
            Snackbar();
        }
    }

    public void getPendingDeliveries(Call<pendingDelvResp> pendingResponseCall) {
        pendingResponseCall.enqueue(new Callback<pendingDelvResp>() {
            @Override
            public void onResponse(Call<pendingDelvResp> call, Response<pendingDelvResp> response) {
                if (!isNetworkAvailable()) {
                    Snackbar();
                } else {
                    if (response.isSuccessful()) {
                        pendingResponse = response.body();
                        if (pageNo == 1) {
                            PendingDeliveryBeans = new ArrayList<PendingPickupPojo>();
                            PendingDeliveryBeans.clear();
                        }
                        if (pendingResponse != null) {
                            if (pendingResponse.getData().size() > 0) {
                                //Get Previous Size

                                previousListSize = PendingDeliveryBeans.size();
                                totalItems = Integer.parseInt(pendingResponse.getTotal_records());

                                if (pendingResponse.getResponse() == 1) {
                                    PendingPickupPojo pickupPojo = null;
                                    for (int i = 0; i < pendingResponse.getData().size(); i++) {

                                        if (driverId.equalsIgnoreCase(pendingResponse.getData().get(i).getCurrent_driver_id())) {
                                            pickupPojo = new PendingPickupPojo();
                                            pickupPojo.setIs_warehouse_pickup(pendingResponse.getData().get(i).getIs_warehouse_pickup());
                                            pickupPojo.setIs_warehouse_dropoff(pendingResponse.getData().get(i).getIs_warehouse_dropoff());
                                            pickupPojo.setMin_delivery_time(pendingResponse.getData().get(i).getMin_delivery_time());
                                            pickupPojo.setBooking_id(pendingResponse.getData().get(i).getBooking_id());
                                            pickupPojo.setBooking_type(pendingResponse.getData().get(i).getBooking_type());
                                            pickupPojo.setBooking_note(pendingResponse.getData().get(i).getBooking_note());
                                            pickupPojo.setIs_cancel(pendingResponse.getData().get(i).getIs_cancel());
                                            pickupPojo.setPreferred_deliver_time(pendingResponse.getData().get(i).getPreferred_deliver_time());
                                            pickupPojo.setBooking_code(pendingResponse.getData().get(i).getBooking_code());
                                            pickupPojo.setBooking_pickup_datetime(pendingResponse.getData().get(i).getBooking_pickup_datetime());
                                            pickupPojo.setDelivery_type(pendingResponse.getData().get(i).getDelivery_type());
                                            pickupPojo.setBooking_delivery_datetime(pendingResponse.getData().get(i).getBooking_delivery_datetime());
                                            pickupPojo.setDelivery_session(pendingResponse.getData().get(i).getDelivery_session());

                                            //Get User Details
                                            if (pendingResponse.getData().get(i).getUser_details() != null && pendingResponse.getData().get(i).getUser_details().size() > 0) {
                                                for (int k = 0; k < pendingResponse.getData().get(i).getUser_details().size(); k++) {
                                                    pickupPojo.setUser_id(pendingResponse.getData().get(i).getUser_details().get(k).getId());
                                                    pickupPojo.setUser_unique_id(pendingResponse.getData().get(i).getUser_details().get(k).getUser_unique_id());
                                                    pickupPojo.setType(pendingResponse.getData().get(i).getUser_details().get(k).getType());
                                                    pickupPojo.setCountry_code(pendingResponse.getData().get(i).getUser_details().get(k).getCountry_code());
                                                    pickupPojo.setName(pendingResponse.getData().get(i).getUser_details().get(k).getName());
                                                    pickupPojo.setUser_email(pendingResponse.getData().get(i).getUser_details().get(k).getUser_email());
                                                    pickupPojo.setPassword(pendingResponse.getData().get(i).getUser_details().get(k).getPassword());
                                                    pickupPojo.setUser_phone(pendingResponse.getData().get(i).getUser_details().get(k).getUser_phone());
                                                    pickupPojo.setSinch_code(pendingResponse.getData().get(i).getUser_details().get(k).getSinch_code());
                                                    pickupPojo.setUser_app_notification(pendingResponse.getData().get(i).getUser_details().get(k).getUser_app_notification());
                                                    pickupPojo.setUser_image(pendingResponse.getData().get(i).getUser_details().get(k).getUser_image());
                                                    pickupPojo.setUser_status(pendingResponse.getData().get(i).getUser_details().get(k).getUser_status());
                                                    pickupPojo.setCompany_name(pendingResponse.getData().get(i).getUser_details().get(k).getCompany_name());
                                                    pickupPojo.setCompany_registration_no(pendingResponse.getData().get(i).getUser_details().get(k).getCompany_registration_no());
                                                    pickupPojo.setTrading_licence_no(pendingResponse.getData().get(i).getUser_details().get(k).getTrading_licence_no());
                                                    pickupPojo.setSecondary_contact_no(pendingResponse.getData().get(i).getUser_details().get(k).getSecondary_contact_no());
                                                    pickupPojo.setTrading_address(pendingResponse.getData().get(i).getUser_details().get(k).getTrading_address());
                                                    pickupPojo.setNature_of_business(pendingResponse.getData().get(i).getUser_details().get(k).getNature_of_business());
                                                    pickupPojo.setVehicle_type(pendingResponse.getData().get(i).getUser_details().get(k).getVehicle_type());
                                                    pickupPojo.setGender(pendingResponse.getData().get(i).getUser_details().get(k).getGender());
                                                    pickupPojo.setDob(pendingResponse.getData().get(i).getUser_details().get(k).getDob());
                                                    pickupPojo.setToken(pendingResponse.getData().get(i).getUser_details().get(k).getToken());
                                                    pickupPojo.setFacebook_id(pendingResponse.getData().get(i).getUser_details().get(k).getFacebook_id());
                                                    pickupPojo.setLogin_type(pendingResponse.getData().get(i).getUser_details().get(k).getLogin_type());
                                                    pickupPojo.setSource(pendingResponse.getData().get(i).getUser_details().get(k).getSource());
                                                    pickupPojo.setValidate_code(pendingResponse.getData().get(i).getUser_details().get(k).getValidate_code());
                                                    pickupPojo.setCredit_limit(pendingResponse.getData().get(i).getUser_details().get(k).getCredit_limit());
                                                    pickupPojo.setIs_verified_by_admin(pendingResponse.getData().get(i).getUser_details().get(k).getIs_verified_by_admin());
                                                    pickupPojo.setAdd_by_agent(pendingResponse.getData().get(i).getUser_details().get(k).getAdd_by_agent());
                                                    pickupPojo.setAgent_id(pendingResponse.getData().get(i).getUser_details().get(k).getAgent_id());
                                                    pickupPojo.setEmail_verify(pendingResponse.getData().get(i).getUser_details().get(k).getEmail_verify());
                                                    pickupPojo.setPhone_verify(pendingResponse.getData().get(i).getUser_details().get(k).getPhone_verify());
                                                    pickupPojo.setRemember_token(pendingResponse.getData().get(i).getUser_details().get(k).getRemember_token());
                                                    pickupPojo.setUser_created_at(pendingResponse.getData().get(i).getUser_details().get(k).getCreated_at());
                                                    pickupPojo.setUser_updated_at(pendingResponse.getData().get(i).getUser_details().get(k).getUpdated_at());
                                                }
                                            } else {
                                                pickupPojo.setUser_id("");
                                                pickupPojo.setType("");
                                                pickupPojo.setName("");
                                                pickupPojo.setUser_email("");
                                                pickupPojo.setPassword("");
                                                pickupPojo.setUser_phone("");
                                                pickupPojo.setSinch_code("");
                                                pickupPojo.setUser_app_notification("");
                                                pickupPojo.setUser_image("");
                                                pickupPojo.setUser_status("");
                                                pickupPojo.setCompany_name("");
                                                pickupPojo.setCompany_registration_no("");
                                                pickupPojo.setTrading_licence_no("");
                                                pickupPojo.setSecondary_contact_no("");
                                                pickupPojo.setTrading_address("");
                                                pickupPojo.setNature_of_business("");
                                                pickupPojo.setVehicle_type("");
                                                pickupPojo.setGender("");
                                                pickupPojo.setDob("");
                                                pickupPojo.setToken("");
                                                pickupPojo.setFacebook_id("");
                                                pickupPojo.setLogin_type("");
                                                pickupPojo.setSource("");
                                                pickupPojo.setValidate_code("");
                                                pickupPojo.setCredit_limit("");
                                                pickupPojo.setIs_verified_by_admin("");
                                                pickupPojo.setAdd_by_agent("");
                                                pickupPojo.setAgent_id("");
                                                pickupPojo.setEmail_verify("");
                                                pickupPojo.setPhone_verify("");
                                                pickupPojo.setRemember_token("");
                                                pickupPojo.setUser_created_at("");
                                                pickupPojo.setUser_updated_at("");
                                            }

                                            //Get Package Detail
                                            if (pendingResponse.getData().get(i).getPackage_details() != null) {
                                                pickupPojo.setPackage_id(pendingResponse.getData().get(i).getPackage_details().getPackage_id());
                                                pickupPojo.setPackage_code(pendingResponse.getData().get(i).getPackage_details().getPackage_code());
                                                pickupPojo.setPackage_fk_booking_id(pendingResponse.getData().get(i).getPackage_details().getFk_booking_id());
                                                pickupPojo.setPackage_length(pendingResponse.getData().get(i).getPackage_details().getPackage_length());
                                                pickupPojo.setPackage_depth(pendingResponse.getData().get(i).getPackage_details().getPackage_depth());
                                                pickupPojo.setPackage_est_value(pendingResponse.getData().get(i).getPackage_details().getPackage_est_value());
                                                is_round_trip=pendingResponse.getData().get(i).getPackage_details().getIs_round_trip();
                                                is_first_round_completed=pendingResponse.getData().get(i).getPackage_details().getIs_first_round_completed();
                                                pickupPojo.setIs_round_trip(pendingResponse.getData().get(i).getPackage_details().getIs_round_trip());
                                                pickupPojo.setIs_first_round_completed(pendingResponse.getData().get(i).getPackage_details().getIs_first_round_completed());
                                                pickupPojo.setPackage_description(pendingResponse.getData().get(i).getPackage_details().getPackage_description());
                                                pickupPojo.setPackage_height(pendingResponse.getData().get(i).getPackage_details().getPackage_height());
                                                pickupPojo.setPackage_width(pendingResponse.getData().get(i).getPackage_details().getPackage_width());
                                                pickupPojo.setPackage_weight(pendingResponse.getData().get(i).getPackage_details().getPackage_weight());
                                                pickupPojo.setPackage_quantity(pendingResponse.getData().get(i).getPackage_details().getPackage_quantity());
                                                pickupPojo.setPackage_cancel(pendingResponse.getData().get(i).getPackage_details().getPackage_cancel());
                                                pickupPojo.setDriver_id(pendingResponse.getData().get(i).getPackage_details().getDriver_id());
                                                pickupPojo.setWarehouse_user_id(pendingResponse.getData().get(i).getPackage_details().getWarehouse_user_id());
                                                pickupPojo.setPartner_user_id(pendingResponse.getData().get(i).getPackage_details().getPartner_user_id());
                                                pickupPojo.setDriver_device_token(pendingResponse.getData().get(i).getPackage_details().getDriver_device_token());
                                                pickupPojo.setHandle_by(pendingResponse.getData().get(i).getPackage_details().getHandle_by());
                                                pickupPojo.setPackage_status(pendingResponse.getData().get(i).getPackage_details().getPackage_status());
                                                pickupPojo.setSend_selected_driver(pendingResponse.getData().get(i).getPackage_details().getSend_selected_driver());
                                                pickupPojo.setRating(pendingResponse.getData().get(i).getPackage_details().getRating());
                                                pickupPojo.setSignature(pendingResponse.getData().get(i).getPackage_details().getSignature());
                                                pickupPojo.setSign_name(pendingResponse.getData().get(i).getPackage_details().getSign_name());
                                                pickupPojo.setFeedback(pendingResponse.getData().get(i).getPackage_details().getFeedback());
                                                pickupPojo.setPop_image(pendingResponse.getData().get(i).getPackage_details().getPop_image());
                                                pickupPojo.setPod_image(pendingResponse.getData().get(i).getPackage_details().getPod_image());
                                                pickupPojo.setEstimate_price(pendingResponse.getData().get(i).getPackage_details().getEstimate_price());
                                                pickupPojo.setFinal_price(pendingResponse.getData().get(i).getPackage_details().getFinal_price());
                                                pickupPojo.setPayment_status(pendingResponse.getData().get(i).getPackage_details().getPayment_status());
                                                pickupPojo.setPackage_created_at(pendingResponse.getData().get(i).getPackage_details().getCreated_at());
                                                pickupPojo.setPackage_updated_at(pendingResponse.getData().get(i).getPackage_details().getUpdated_at());
//                                            pickupPojo.setPayment_details(pendingResponse.getData().get(i).getPackage_details().getPayment_details());
                                            } else {
                                                pickupPojo.setPackage_id("");
                                                pickupPojo.setPackage_code("");
                                                pickupPojo.setPackage_fk_booking_id("");
                                                pickupPojo.setPackage_length("");
                                                pickupPojo.setPackage_depth("");
                                                pickupPojo.setPackage_est_value("");
                                                pickupPojo.setIs_round_trip("");
                                                pickupPojo.setIs_first_round_completed("");
                                                pickupPojo.setPackage_description("");
                                                pickupPojo.setPackage_height("");
                                                pickupPojo.setPackage_width("");
                                                pickupPojo.setPackage_weight("");
                                                pickupPojo.setPackage_quantity("");
                                                pickupPojo.setPackage_cancel("");
                                                pickupPojo.setDriver_id("");
                                                pickupPojo.setWarehouse_user_id("");
                                                pickupPojo.setPartner_user_id("");
                                                pickupPojo.setDriver_device_token("");
                                                pickupPojo.setHandle_by("");
                                                pickupPojo.setPackage_status("");
                                                pickupPojo.setSend_selected_driver("");
                                                pickupPojo.setRating("");
                                                pickupPojo.setSignature("");
                                                pickupPojo.setSign_name("");
                                                pickupPojo.setFeedback("");
                                                pickupPojo.setPop_image("");
                                                pickupPojo.setPod_image("");
                                                pickupPojo.setEstimate_price("");
                                                pickupPojo.setFinal_price("");
                                                pickupPojo.setPayment_status("");
                                                pickupPojo.setPackage_created_at("");
                                                pickupPojo.setPackage_updated_at("");
                                                //pickupPojo.setPayment_details("");
                                            }
                                            //Get Location Details
                                            if (pendingResponse.getData().get(i).getLocation_details() != null && pendingResponse.getData().get(i).getLocation_details().size() > 0) {
                                                for (int j = 0; j < pendingResponse.getData().get(i).getLocation_details().size(); j++) {
                                                    pickupPojo.setId(pendingResponse.getData().get(i).getLocation_details().get(j).getId());
                                                    pickupPojo.setFk_booking_id(pendingResponse.getData().get(i).getLocation_details().get(j).getFk_booking_id());
                                                    pickupPojo.setFk_package_code(pendingResponse.getData().get(i).getLocation_details().get(j).getFk_package_code());
                                                    pickupPojo.setPickup_house_no(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_house_no());
                                                    pickupPojo.setPickup_street(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_street());
                                                    pickupPojo.setPickup_landmark(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_landmark());
                                                    pickupPojo.setPickup_datetime(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_datetime());
                                                    pickupPojo.setDropoff_house_no(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_house_no());
                                                    pickupPojo.setDropoff_street(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_street());
                                                    pickupPojo.setDropoff_landmark(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_landmark());
                                                    if(is_round_trip.equals("1")) {
                                                        if(is_first_round_completed.equals("0")) {
                                                            pickupPojo.setPickup_location(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location());
                                                            pickupPojo.setPickup_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lat());
                                                            pickupPojo.setPickup_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lng());

                                                            pickupPojo.setDropoff_location(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location());
                                                            pickupPojo.setDropoff_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lat());
                                                            pickupPojo.setDropoff_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lng());
                                                        }else{
                                                            pickupPojo.setPickup_location(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location());
                                                            pickupPojo.setPickup_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lat());
                                                            pickupPojo.setPickup_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lng());

                                                            pickupPojo.setDropoff_location(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location());
                                                            pickupPojo.setDropoff_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lat());
                                                            pickupPojo.setDropoff_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lng());

                                                        }
                                                    }else {
                                                        pickupPojo.setPickup_location(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location());
                                                        pickupPojo.setPickup_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lat());
                                                        pickupPojo.setPickup_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getPickup_location_lng());

                                                        pickupPojo.setDropoff_location(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location());
                                                        pickupPojo.setDropoff_location_lat(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lat());
                                                        pickupPojo.setDropoff_location_lng(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_lng());
                                                    }
                                                    pickupPojo.setDropoff_location_datetime(pendingResponse.getData().get(i).getLocation_details().get(j).getDropoff_location_datetime());
                                                    pickupPojo.setCreated_at(pendingResponse.getData().get(i).getLocation_details().get(j).getCreated_at());
                                                    pickupPojo.setUpdated_at(pendingResponse.getData().get(i).getLocation_details().get(j).getUpdated_at());
                                                    pickupPojo.setAddress_note(pendingResponse.getData().get(i).getLocation_details().get(j).getAddress_note());
                                                }
                                            } else {
                                                pickupPojo.setId("");
                                                pickupPojo.setFk_booking_id("");
                                                pickupPojo.setFk_package_code("");
                                                pickupPojo.setPickup_house_no("");
                                                pickupPojo.setPickup_street("");
                                                pickupPojo.setPickup_landmark("");
                                                pickupPojo.setPickup_location("");
                                                pickupPojo.setPickup_location_lat("");
                                                pickupPojo.setPickup_location_lng("");
                                                pickupPojo.setPickup_datetime("");
                                                pickupPojo.setDropoff_house_no("");
                                                pickupPojo.setDropoff_street("");
                                                pickupPojo.setDropoff_landmark("");
                                                pickupPojo.setDropoff_location("");
                                                pickupPojo.setDropoff_location_lat("");
                                                pickupPojo.setDropoff_location_lng("");
                                                pickupPojo.setDropoff_location_datetime("");
                                                pickupPojo.setCreated_at("");
                                                pickupPojo.setUpdated_at("");
                                                pickupPojo.setAddress_note("");
                                            }

                                            //Get Recurring Details
                                            if (pendingResponse.getData().get(i).getRecurring_setting_details() != null && pendingResponse.getData().get(i).getRecurring_setting_details().size() > 0) {
                                                for (int j = 0; j < pendingResponse.getData().get(i).getRecurring_setting_details().size(); j++) {
                                                    pickupPojo.setRecurring_id(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getId());
                                                    pickupPojo.setIs_deleted(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getIs_deleted());
                                                    pickupPojo.setRecurring_day_time(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getRecurring_day_time());
                                                    pickupPojo.setRecurring_week(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getRecurring_week());
                                                    pickupPojo.setRecurring_status(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getRecurring_status());
                                                    pickupPojo.setRecurring_pickup_type(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getRecurring_pickup_type());
                                                    pickupPojo.setRecurring_day(pendingResponse.getData().get(i).getRecurring_setting_details().get(j).getRecurring_day());
                                                }
                                            } else {
                                                pickupPojo.setRecurring_id("");
                                                pickupPojo.setIs_deleted("");
                                                pickupPojo.setRecurring_day_time("");
                                                pickupPojo.setRecurring_week("");
                                                pickupPojo.setRecurring_status("");
                                                pickupPojo.setRecurring_pickup_type("");
                                                pickupPojo.setRecurring_day("");
                                            }

                                            //Get WareHouse Details
                                            if (pendingResponse.getData().get(i).getWarehouse_details() != null && pendingResponse.getData().get(i).getWarehouse_details().size() > 0) {
                                                for (int j = 0; j < pendingResponse.getData().get(i).getWarehouse_details().size(); j++) {
                                                    pickupPojo.setWareHouse_id(pendingResponse.getData().get(i).getWarehouse_details().get(j).getId());
                                                    pickupPojo.setWareHouse_name(pendingResponse.getData().get(i).getWarehouse_details().get(j).getName());
                                                    pickupPojo.setWareHouse_user_email(pendingResponse.getData().get(i).getWarehouse_details().get(j).getUser_email());
                                                    pickupPojo.setWareHouse_phone(pendingResponse.getData().get(i).getWarehouse_details().get(j).getPhone());
                                                    pickupPojo.setWareHouse_state_id(pendingResponse.getData().get(i).getWarehouse_details().get(j).getState_id());
                                                    pickupPojo.setWareHouse_address(pendingResponse.getData().get(i).getWarehouse_details().get(j).getAddress());
                                                    pickupPojo.setWareHouse_latitude(pendingResponse.getData().get(i).getWarehouse_details().get(j).getLatitude());
                                                    pickupPojo.setWareHouse_longtitude(pendingResponse.getData().get(i).getWarehouse_details().get(j).getLongtitude());
                                                    pickupPojo.setWareHouse_created_at(pendingResponse.getData().get(i).getWarehouse_details().get(j).getCreated_at());
                                                    pickupPojo.setWareHouse_updated_at(pendingResponse.getData().get(i).getWarehouse_details().get(j).getUpdated_at());
                                                }
                                            } else {
                                                pickupPojo.setWareHouse_id("");
                                                pickupPojo.setWareHouse_name("");
                                                pickupPojo.setWareHouse_user_email("");
                                                pickupPojo.setWareHouse_phone("");
                                                pickupPojo.setWareHouse_state_id("");
                                                pickupPojo.setWareHouse_address("");
                                                pickupPojo.setWareHouse_latitude("");
                                                pickupPojo.setWareHouse_longtitude("");
                                                pickupPojo.setWareHouse_created_at("");
                                                pickupPojo.setWareHouse_updated_at("");
                                            }

                                            if (pendingResponse.getData().get(i).getWarehouse_pickup_address() != null && pendingResponse.getData().get(i).getWarehouse_pickup_address().size() > 0) {
                                                pickupPojo.setPickupWarehouseAddress(pendingResponse.getData().get(i).getWarehouse_pickup_address().get(0).getAddress());
                                                pickupPojo.setPickupWarehouseName(pendingResponse.getData().get(i).getWarehouse_pickup_address().get(0).getName());
                                                pickupPojo.setPickupWarehouseLatitude(pendingResponse.getData().get(i).getWarehouse_pickup_address().get(0).getLatitude());
                                                pickupPojo.setPickupWarehouseLongitude(pendingResponse.getData().get(i).getWarehouse_pickup_address().get(0).getLongtitude());
                                                pickupPojo.setPickupWarehouseUser_email(pendingResponse.getData().get(i).getWarehouse_pickup_address().get(0).getUser_email());
                                            } else {
                                                pickupPojo.setPickupWarehouseAddress("");
                                                pickupPojo.setPickupWarehouseName("");
                                                pickupPojo.setPickupWarehouseLatitude("");
                                                pickupPojo.setPickupWarehouseLongitude("");
                                                pickupPojo.setPickupWarehouseUser_email("");
                                            }

                                            if (pendingResponse.getData().get(i).getWarehouse_drop_off_address() != null && pendingResponse.getData().get(i).getWarehouse_drop_off_address().size() > 0) {
                                                pickupPojo.setDropOffWarehouseAddress(pendingResponse.getData().get(i).getWarehouse_drop_off_address().get(0).getAddress());
                                                pickupPojo.setDropOffWarehouseName(pendingResponse.getData().get(i).getWarehouse_drop_off_address().get(0).getName());
                                                pickupPojo.setDropOffWarehouseLatitude(pendingResponse.getData().get(i).getWarehouse_drop_off_address().get(0).getLatitude());
                                                pickupPojo.setDropOffWarehouseLongitude(pendingResponse.getData().get(i).getWarehouse_drop_off_address().get(0).getLongtitude());
                                                pickupPojo.setDropOffWarehouseUser_email(pendingResponse.getData().get(i).getWarehouse_drop_off_address().get(0).getUser_email());
                                            } else {
                                                pickupPojo.setDropOffWarehouseAddress("");
                                                pickupPojo.setDropOffWarehouseName("");
                                                pickupPojo.setDropOffWarehouseLatitude("");
                                                pickupPojo.setDropOffWarehouseLongitude("");
                                                pickupPojo.setDropOffWarehouseUser_email("");
                                            }
                                            //Add Data to ArrayList
                                            PendingDeliveryBeans.add(pickupPojo);
                                        }

                                    }
                                    Log.d("PendingDel", " " + PendingDeliveryBeans.size());

                                    //Set Adapter
                                    PendingDeliveryAdapter pendingDeliveryAdapter = new PendingDeliveryAdapter(context, PendingDeliveryBeans);
                                    rvDeliveryPackages.setAdapter(pendingDeliveryAdapter);

                                    pageNo = pageNo + 1;
                                    pendingResponse = null;

                                    tvNoDlvryPckg.setVisibility(GONE);
                                    rvDeliveryPackages.setVisibility(VISIBLE);
                                    ivLoadMore.setVisibility(GONE);
                                    tvMore.setVisibility(GONE);

                                    //Check Previous List Size
                                    if (previousListSize > 1) {
                                        rvDeliveryPackages.getLayoutManager().scrollToPosition(previousListSize - 1);
                                    } else {
                                        rvDeliveryPackages.getLayoutManager().scrollToPosition(previousListSize);
                                    }
                                }else if(pendingResponse.getResponse() == 0){

                                    Log.d(TAG,"=-----------in response 0-------------");
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                    alert.setCancelable(false);
                                    alert.setMessage("This package has been deleted by admin");
                                    alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new PendingDeliveryFragment();

                                        }
                                    });
                                    alert.show();


                            }else if(pendingResponse.getData().get(0).getMessage()!=null) {
                                    if (pendingResponse.getData().get(0).getMessage().trim().equals("Unauthenticated")) {
                                        Global.signInIntent(context);
                                    }
                                }


                                if(PendingDeliveryBeans.size()==0)
                                {
                                    tvNoDlvryPckg.setVisibility(View.VISIBLE);
                                    rvDeliveryPackages.setVisibility(View.GONE);
                                }
                                else
                                {
                                    tvNoDlvryPckg.setVisibility(View.GONE);
                                    rvDeliveryPackages.setVisibility(View.VISIBLE);
                                }
                            } else if (PendingDeliveryBeans.size() == 0) {
                                if (pageNo == 1) {
                                    tvNoDlvryPckg.setVisibility(VISIBLE);
                                    rvDeliveryPackages.setVisibility(GONE);
                                    ivLoadMore.setVisibility(GONE);
                                    tvMore.setVisibility(GONE);
                                }
                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                tvOffline.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                if(!(((Activity)context).isFinishing())) {
                    if (deliveryDialog != null && deliveryDialog.isShowing()) {
                        deliveryDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<pendingDelvResp> call, Throwable t) {
                if(!(((Activity)context).isFinishing())) {
                    if (deliveryDialog != null && deliveryDialog.isShowing()) {
                        deliveryDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (deliveryDialog != null && deliveryDialog.isShowing()) {
            deliveryDialog.dismiss();
        }
    }

    public void Snackbar() {
        final Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(android.R.id.content), R.string.NoInternet, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();

        snackBarView.setBackgroundColor(Color.RED);

        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);

        mySnackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPendingDeliveryPackages();
            }
        });
        mySnackbar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_load_more:
            case R.id.tv_more:
                getPendingDeliveryPackages();
                break;
        }
    }
}
