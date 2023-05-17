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

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineAdapter.PendingPickupAdapter;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupPojo;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupResp;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Ajay2.Sharma on 7/6/2018.
 */

public class PickupFragment extends GodelDriverFragment implements View.OnClickListener {
    private View view;
    private int pageNo = 1, previousListSize = 0, totalRecords = 0;
    private Call<PendingPickupResp> pendingResponseCall;
    private static Context context;
    private PendingPickupResp pendingResponse;
    private List<PendingPickupPojo> PendingPickupBeans = new ArrayList<PendingPickupPojo>();
    private ProgressDialog mPendingPickupsProgress;
    private TextView tvNoPackageAvailable, tvMore;
    private RecyclerView rvPickupPackages;
    private LinearLayoutManager layoutManager;
    private ImageView ivLoadMore;
    boolean isOpen = false;
    boolean isDeleted=false;
    String language_type, is_round_trip="0",is_first_round_completed="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pickup_new, container, false);
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

        PendingPickupBeans = new ArrayList<PendingPickupPojo>();
        PendingPickupBeans.clear();
        isOpen = false;
        isDeleted=false;
        Log.d("PICKUP_FRAGMENT","------in on create------");
        ivLoadMore = (ImageView) view.findViewById(R.id.iv_load_more);
        tvNoPackageAvailable = (TextView) view.findViewById(R.id.tv_no_pickup_package_available);
        tvMore = (TextView) view.findViewById(R.id.tv_more);

        rvPickupPackages = (RecyclerView) view.findViewById(R.id.rv_pickup_packages);
        rvPickupPackages.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        rvPickupPackages.setLayoutManager(layoutManager);
        rvPickupPackages.setItemAnimator(new DefaultItemAnimator());

        rvPickupPackages.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                Log.d("PICKUP_FRAGMENT","=-----------getPendingPickupsPackages()-------------");
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    Log.d("LOCATION_DETAILS","==========HistoryData.size()--------=="+PendingPickupBeans.size());
                    Log.d("LOCATION_DETAILS","==========totalItems--------=="+totalRecords);
                    Log.d("LOCATION_DETAILS","==========totalItemCount--------=="+totalItemCount);
//                    Log.d("LOCATION_DETAILS","==========bookingAdapter.getItemCount()---22222-----=="+bookingAdapter.getItemCount());
                    Log.d("LOCATION_DETAILS","==========pastVisibleItems--------=="+pastVisibleItems);
                    Log.d("LOCATION_DETAILS","==========visibleItemCount--------=="+visibleItemCount);
                    Log.d("LOCATION_DETAILS","==========pastVisibleItems + visibleItemCount--------=="+pastVisibleItems + visibleItemCount);
                    if (PendingPickupBeans.size() < totalRecords) {
                        if (PendingPickupBeans != null && PendingPickupBeans.size() > 5) {
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

        tvNoPackageAvailable.setVisibility(GONE);
        rvPickupPackages.setVisibility(GONE);

        ivLoadMore.setOnClickListener(this);
        tvMore.setOnClickListener(this);

        return view;
    }


    private BroadcastReceiver pickupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PICKUP_FRAGMENT","--------booking assigned----------");
            // intent can contain anydata
            if (isOpen == true) {
                pageNo = 1;
                previousListSize = 0;
                getPendingPickupsPackages();
            }
            if (isDeleted == true) {
                pageNo = 1;
                previousListSize = 0;
                getPendingPickupsPackages();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        isOpen = true;
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
        Log.d("PICKUP_FRAGMENT","------in_resume------");
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
//                        Log.d("check_type", "language_typelllll--  " + preferences.getString(SharedValues.LANGUAGE_SETTINGS, ""));
//                    }
//                }
//            }
//        }

        isOpen = true;
        isDeleted=true;
        LocalBroadcastManager.getInstance(context).registerReceiver(pickupReceiver, new IntentFilter(Global.PICKUP_BROADCAST));

        pageNo = 1;
        previousListSize = 0;

        Log.d("PICKUP_FRAGMENT","------in_resume- in broadcast-----");
        getPendingPickupsPackages();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(pickupReceiver);
        isOpen = false;
        isDeleted=false;
        if (pendingResponseCall != null) {
            pendingResponseCall.cancel();
        }
    }

    private void getPendingPickupsPackages() {
        Log.d("PICKUP_FRAGMENT","=-----------getPendingPickupsPackages()-------------");

        if (isNetworkAvailable()) {
            if (pendingResponseCall != null) {
                pendingResponseCall.cancel();
            }

            if (mPendingPickupsProgress == null) {
                mPendingPickupsProgress = new ProgressDialog(getActivity());
            }

            if(!(((Activity)context).isFinishing())) {
                if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
                    mPendingPickupsProgress.dismiss();
                }
            }
            mPendingPickupsProgress.setMessage(getResources().getString(R.string.loadingpackages)+"...");
            mPendingPickupsProgress.setCancelable(false);
            mPendingPickupsProgress.show();

            String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
            String Accept = Global.ACCEPT;
            driverId = preferences.getString(SharedValues.DRIVER_ID, "");

            if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                if(!(((Activity)context).isFinishing())) {
                    if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
                        mPendingPickupsProgress.dismiss();
                    }
                }
               Global.signInIntent(context);
            }
            else
            {
                String country_code=CountryCodeCheck.countrycheck(context);
                if(country_code!=null&&country_code.trim().length()>0){
                    pendingResponseCall = webRequest.apiInterface.pendingPickup(country_code,Global.USER_AGENT, Accept, USER_TOKEN,driverId, pageNo, driverId);
                    getPendingPickup(pendingResponseCall);
                }else{
                    if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
                        mPendingPickupsProgress.dismiss();
                    }
                }

            }


        } else {
            Snackbar();
        }
    }

    private void getPendingPickup(Call<PendingPickupResp> pendingResponseCall) {
        pendingResponseCall.enqueue(new Callback<PendingPickupResp>() {
            @Override
            public void onResponse(Call<PendingPickupResp> call, Response<PendingPickupResp> response) {
                if (!isNetworkAvailable()) {
                    Snackbar();
                } else {
                    if (response.isSuccessful()) {
                        Log.d("PICKUP_FRAGMENT","=-----------response-------------"+response);

                        pendingResponse = response.body();
                        if (pageNo == 1) {
                            PendingPickupBeans = new ArrayList<PendingPickupPojo>();
                            PendingPickupBeans.clear();
                        }

                        if (pendingResponse != null) {
                            Log.d("PICKUP_FRAGMENT","------pendingResponse.getResponse()---------"+pendingResponse.getResponse());
                            if (pendingResponse.getResponse() == 1) {

                                Log.d("PICKUP_FRAGMENT","=---------packagesresponse   1-------------");
                                if (pendingResponse.getData().size() > 0) {
//                                    previousListSize = PendingPickupBeans.size();
                                    previousListSize = PendingPickupBeans.size();
                                    String totalItems = pendingResponse.getTotal_records();
                                    if (totalItems != null && totalItems.trim().length() > 0) {
                                        totalRecords = Integer.parseInt(totalItems);
                                    }
//                                    if(totalRecords>PendingPickupBeans.size()){
//                                        Log.d("PICKUP_FRAGMENT","------totalRecords>Pending---totalRecords---"+totalRecords);
//                                        Log.d("PICKUP_FRAGMENT","------totalRecords>Pending---PendingPickupBeans-----"+PendingPickupBeans.size());
//                                    }
//                                    if(PendingPickupBeans.size()>totalRecords){
//                                        Log.d("PICKUP_FRAGMENT","------PendingPickupBeans.size()>totalRecords---totalRecords---"+totalRecords);
//                                        Log.d("PICKUP_FRAGMENT","------PendingPickupBeans.size()>totalRecords---PendingPickupBeans-----"+PendingPickupBeans.size());
//                                    }
//                                    if(totalRecords<Integer.parseInt(totalItems)){
//                                        Log.d("PICKUP_FRAGMENT","---------totalRecords---"+totalRecords);
//                                        Log.d("PICKUP_FRAGMENT","------total_items---Integer.parseInt(totalItems)-----"+Integer.parseInt(totalItems));
//                                    }
                                    previousListSize = PendingPickupBeans.size();
                                    PendingPickupPojo pickupPojo = null;
                                    for (int i = 0; i < pendingResponse.getData().size(); i++) {
                                        pickupPojo = new PendingPickupPojo();
                                        Log.d("PICKUP_FRAGMENT","=-----------pickupPojo-------------"+pickupPojo);
                                        pickupPojo.setMin_delivery_time(pendingResponse.getData().get(i).getMin_delivery_time());
                                        pickupPojo.setBooking_id(pendingResponse.getData().get(i).getBooking_id());
                                        pickupPojo.setBooking_type(pendingResponse.getData().get(i).getBooking_type());
                                        pickupPojo.setBooking_note(pendingResponse.getData().get(i).getBooking_note());
                                        pickupPojo.setIs_warehouse_dropoff(pendingResponse.getData().get(i).getIs_warehouse_dropoff());
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
                                            pickupPojo.setIs_round_trip(pendingResponse.getData().get(i).getPackage_details().getIs_round_trip());
                                            is_round_trip=pendingResponse.getData().get(i).getPackage_details().getIs_round_trip();
                                            is_first_round_completed=pendingResponse.getData().get(i).getPackage_details().getIs_first_round_completed();
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
                                            pickupPojo.setIs_first_round_completed("");
                                            pickupPojo.setIs_round_trip("");
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
                                        //Add Data to ArrayList
                                        PendingPickupBeans.add(pickupPojo);
                                    }
                                    tvNoPackageAvailable.setVisibility(View.GONE);
                                    rvPickupPackages.setVisibility(VISIBLE);
                                    tvMore.setVisibility(GONE);
                                    ivLoadMore.setVisibility(GONE);
                                    pageNo = pageNo + 1;
                                    //Set Adapter
                                    Log.d("PICKUP_FRAGMENT","------set adapter------");
                                    PendingPickupAdapter pendingPickupAdapter = new PendingPickupAdapter(context, PendingPickupBeans);
                                    rvPickupPackages.setAdapter(pendingPickupAdapter);

                                    if (previousListSize > 1) {
                                        rvPickupPackages.getLayoutManager().scrollToPosition(previousListSize - 1);
                                    } else {
                                        rvPickupPackages.getLayoutManager().scrollToPosition(previousListSize);
                                    }

                                    if (PendingPickupBeans.size() == 0) {
                                        if (pageNo == 1) {
                                            tvNoPackageAvailable.setVisibility(VISIBLE);
                                            rvPickupPackages.setVisibility(GONE);
                                            tvMore.setVisibility(GONE);
                                            ivLoadMore.setVisibility(GONE);
                                        }
                                    } else {
                                        tvNoPackageAvailable.setVisibility(View.GONE);
                                        rvPickupPackages.setVisibility(VISIBLE);
                                        tvMore.setVisibility(GONE);
                                        ivLoadMore.setVisibility(GONE);
//                                        pageNo = pageNo + 1;
                                        //Set Adapter
                                        Log.d("PICKUP_FRAGMENT","------set adapter--2----");
//                                        PendingPickupAdapter pendingPickupAdapter = new PendingPickupAdapter(context, PendingPickupBeans);
//                                        rvPickupPackages.setAdapter(pendingPickupAdapter);
                                       Log.d("PICKUP_FRAGMENT","=-----------pendingResponse.getResponse()--------22----"+pendingResponse.getResponse());
                                    }
                                } else {
                                    Log.d("PICKUP_FRAGMENT","=-----------in else-------------");
                                    tvNoPackageAvailable.setVisibility(VISIBLE);
                                    rvPickupPackages.setVisibility(GONE);
                                }
                            }else if(pendingResponse.getResponse() == 0) {
                                Log.d("PICKUP_FRAGMENT","=-----------in response 0-------------");
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setCancelable(false);
                                alert.setMessage("This package has been deleted by admin");
                                alert.setPositiveButton(getResources().getString(R.string.oktext), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new PickupFragment();
                                    }
                                });
                                alert.show();
                            }else if(response.body().getData().get(0).getMessage()!=null) {
                                if (response.body().getData().get(0).getMessage().equals("Unauthenticated")) {
                                    Global.signInIntent(context);
                                }
                            }
                            else {
                                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(!(((Activity)context).isFinishing())) {
                    if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
                        mPendingPickupsProgress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingPickupResp> call, Throwable t) {
                if(!(((Activity)context).isFinishing())) {
                    if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
                        mPendingPickupsProgress.dismiss();
                    }
                }
            }
        });
    }

    private void Snackbar() {
        final Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(android.R.id.content), R.string.NoInternet, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();

        snackBarView.setBackgroundColor(Color.RED);

        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);

        mySnackbar.setAction(getResources().getString(R.string.retrytext), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPendingPickupsPackages();
            }
        });
        mySnackbar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_load_more:
            case R.id.tv_more:
                getPendingPickupsPackages();
                break;
        }
    }
}