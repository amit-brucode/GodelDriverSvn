package com.driver.godel.RefineCode.RefineFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineAdapter.BookingHistoryAdapterNew;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.response.HistoryResponse.HistoryResponse;
import com.driver.godel.response.SignatureResponse.BookingHistoryResponseDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by Ajay2.Sharma on 11-Aug-17.
 */

public class PendingHistoryNew extends GodelDriverFragment implements View.OnClickListener {
    RecyclerView rvHistory;
    RelativeLayout rlConnection;
    Button btnTryAgain;
    RelativeLayout rlLoadMore;
    ImageView ivReload;
    TextView tvMore;
    TextView tvNoHistory;
    static Context context;
    //Web Service
    Call<HistoryResponse> bookingHistoryCall;
    HistoryResponse bookingHistoryResp;

    //Adapter
    BookingHistoryAdapterNew bookingAdapter;
    private LinearLayoutManager layoutManager;

    int pageNo = 1;
    int previousSize = 0;
    int totalItems = 0;
    public static ArrayList<BookingHistoryResponseDetails> HistoryData = new ArrayList<BookingHistoryResponseDetails>();

    private ProgressDialog progressDialog;

    public PendingHistoryNew() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        context=getActivity();

        Global global = new Global(context);
        global.setCurrencySymbol();

        //Initialization
        rlLoadMore = (RelativeLayout) view.findViewById(R.id.rl_load_more);
        ivReload = (ImageView) view.findViewById(R.id.iv_reLoad);
        tvMore = (TextView) view.findViewById(R.id.tv_more);
        tvNoHistory = (TextView) view.findViewById(R.id.tv_no_history);

        rvHistory = (RecyclerView) view.findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlConnection = (RelativeLayout) view.findViewById(R.id.rl_connection);
        btnTryAgain = (Button) view.findViewById(R.id.btn_try_again);

        rlLoadMore.setOnClickListener(this);
        rvHistory.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());

        try {
            HistoryData.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //BOOKING API HIT
        bookingApiHit(pageNo);

        //Scroll Listener
        rvHistory.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {

                    if (HistoryData != null) {

                        if (HistoryData.size() < totalItems) {
                            rlLoadMore.setVisibility(View.VISIBLE);
                            tvNoHistory.setVisibility(View.GONE);
                        } else {
                            rlLoadMore.setVisibility(GONE);
                            tvNoHistory.setVisibility(View.GONE);
                        }
                    }
                } else if (HistoryData.size() != 0) {
                    rlLoadMore.setVisibility(GONE);
                    tvNoHistory.setVisibility(View.GONE);
                } else {
                    rlLoadMore.setVisibility(GONE);
                    tvNoHistory.setVisibility(View.GONE);
                }
            }
        });


        //Set On Click Listener
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //API HIT
                bookingApiHit(pageNo);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            rlConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //Driver Booking API Hit
    private void bookingApiHit(int pageNo) {

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            if(!(((Activity)context).isFinishing())) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            Global.signInIntent(getActivity());
        }
        else
        {

    String country_code=CountryCodeCheck.countrycheck(context);
                if(country_code!=null&&country_code.trim().length()>0){
                    //Get Booking History
                    bookingHistoryCall = webRequest.apiInterface.getBookingHistory(country_code,Global.USER_AGENT, Accept, USER_TOKEN,driverId, pageNo, driverId);
                    bookingHistory(bookingHistoryCall);
                        }else{
                    if(!(((Activity)context).isFinishing())) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                        }


        }

    }

    //Booking History API Response
    private void bookingHistory(Call<HistoryResponse> bookingHistory) {
        if(!(((Activity)context).isFinishing())) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        progressDialog = ProgressDialog.show(context, "", "Loading...");
        bookingHistory.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if(!(((Activity)context).isFinishing())) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (response.isSuccessful()) {
                    rlConnection.setVisibility(GONE);
                    bookingHistoryResp = response.body();
                    if (bookingHistoryResp != null) {
                        if (bookingHistoryResp.getData().size() > 0) {
                            //Hide Visibility
                            tvNoHistory.setVisibility(View.GONE);

                            previousSize = HistoryData.size();
                            totalItems = Integer.parseInt(bookingHistoryResp.getTotal_records());
                            BookingHistoryResponseDetails datalist = null;
                            for (int i = 0; i < bookingHistoryResp.getData().size(); i++) {
                                datalist = new BookingHistoryResponseDetails();
                                String isWhPickup = bookingHistoryResp.getData().get(i).getIs_warehouse_pickup();
                                String isWhDropOff = bookingHistoryResp.getData().get(i).getIs_warehouse_dropoff();

                                //WarHouse Check
                                datalist.setIsWarehouseDropoffCheck(bookingHistoryResp.getData().get(i).getIs_warehouse_dropoff_check());
                                datalist.setIsWarehouseDropoff(bookingHistoryResp.getData().get(i).getIs_warehouse_dropoff());
                                datalist.setIsWarehousePickup(bookingHistoryResp.getData().get(i).getIs_warehouse_pickup());

                                datalist.setBooking_id(bookingHistoryResp.getData().get(i).getBooking_id());

                                datalist.setBooking_type(bookingHistoryResp.getData().get(i).getBooking_type());
                                datalist.setBooking_note(bookingHistoryResp.getData().get(i).getBooking_note());
                                datalist.setIs_cancel(bookingHistoryResp.getData().get(i).getIs_cancel());
                                datalist.setPreferred_deliver_time(bookingHistoryResp.getData().get(i).getPreferred_deliver_time());

                                datalist.setBooking_code(bookingHistoryResp.getData().get(i).getBooking_code());
                                datalist.setBooking_pickup_datetime(bookingHistoryResp.getData().get(i).getBooking_pickup_datetime());
                                datalist.setDelivery_type(bookingHistoryResp.getData().get(i).getDelivery_type());
                                datalist.setBooking_delivery_datetime(bookingHistoryResp.getData().get(i).getBooking_delivery_datetime());
                                datalist.setDelivery_session(bookingHistoryResp.getData().get(i).getDelivery_session());
                                if (bookingHistoryResp.getData().get(i).getUser_details() != null && bookingHistoryResp.getData().get(i).getUser_details().size() > 0) {
                                    datalist.setUser_id(bookingHistoryResp.getData().get(i).getUser_details().get(0).getId());
                                    datalist.setUser_unique_id(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_unique_id());
                                    datalist.setName(bookingHistoryResp.getData().get(i).getUser_details().get(0).getName());
                                    datalist.setUser_email(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_email());
                                    datalist.setPassword(bookingHistoryResp.getData().get(i).getUser_details().get(0).getPassword());
                                    datalist.setUser_phone(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_phone());
                                    datalist.setSinch_code(bookingHistoryResp.getData().get(i).getUser_details().get(0).getSinch_code());
                                    datalist.setUser_app_notification(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_app_notification());
                                    datalist.setUser_image(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_image());
                                    datalist.setUser_status(bookingHistoryResp.getData().get(i).getUser_details().get(0).getUser_status());
                                    datalist.setToken(bookingHistoryResp.getData().get(i).getUser_details().get(0).getToken());
                                    datalist.setFacebook_id(bookingHistoryResp.getData().get(i).getUser_details().get(0).getFacebook_id());
                                    datalist.setLogin_type(bookingHistoryResp.getData().get(i).getUser_details().get(0).getLogin_type());
                                    datalist.setSource(bookingHistoryResp.getData().get(i).getUser_details().get(0).getSource());
                                    datalist.setValidate_code(bookingHistoryResp.getData().get(i).getUser_details().get(0).getValidate_code());
                                    datalist.setEmail_verify(bookingHistoryResp.getData().get(i).getUser_details().get(0).getEmail_verify());
                                    datalist.setPhone_verify(bookingHistoryResp.getData().get(i).getUser_details().get(0).getPhone_verify());
                                    datalist.setRemember_token(bookingHistoryResp.getData().get(i).getUser_details().get(0).getRemember_token());
                                } else {
                                    datalist.setUser_id("");
                                    datalist.setName("");
                                    datalist.setUser_email("");
                                    datalist.setPassword("");
                                    datalist.setUser_phone("");
                                    datalist.setSinch_code("");
                                    datalist.setUser_app_notification("");
                                    datalist.setUser_image("");
                                    datalist.setUser_status("");
                                    datalist.setToken("");
                                    datalist.setFacebook_id("");
                                    datalist.setLogin_type("");
                                    datalist.setSource("");
                                    datalist.setValidate_code("");
                                    datalist.setEmail_verify("");
                                    datalist.setPhone_verify("");
                                    datalist.setRemember_token("");
                                }

                                if (bookingHistoryResp.getData().get(i).getPackage_details() != null) {
                                    datalist.setPackage_id(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_id());
                                    datalist.setPackage_code(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_code());
                                    datalist.setFk_booking_id(bookingHistoryResp.getData().get(i).getPackage_details().getFk_booking_id());
                                    datalist.setPackage_est_value(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_est_value());
                                    datalist.setPackage_length(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_length());
                                    datalist.setPackage_depth(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_depth());
                                    datalist.setPackage_width(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_width());
                                    datalist.setPackage_weight(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_weight());
                                    datalist.setPackage_quantity(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_quantity());
                                    datalist.setPackage_cancel(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_cancel());
                                    datalist.setDriver_id(bookingHistoryResp.getData().get(i).getPackage_details().getDriver_id());
                                    datalist.setWarehouse_user_id(bookingHistoryResp.getData().get(i).getPackage_details().getWarehouse_user_id());
                                    datalist.setPartner_user_id(bookingHistoryResp.getData().get(i).getPackage_details().getPartner_user_id());
                                    datalist.setDriver_device_token(bookingHistoryResp.getData().get(i).getPackage_details().getDriver_device_token());
                                    datalist.setHandle_by(bookingHistoryResp.getData().get(i).getPackage_details().getHandle_by());
                                    datalist.setPackage_status(bookingHistoryResp.getData().get(i).getPackage_details().getPackage_status());
                                    datalist.setRating(bookingHistoryResp.getData().get(i).getPackage_details().getRating());
                                    datalist.setSignature(bookingHistoryResp.getData().get(i).getPackage_details().getSignature());
                                    datalist.setSign_name(bookingHistoryResp.getData().get(i).getPackage_details().getSign_name());
                                    datalist.setRound_sign_name(bookingHistoryResp.getData().get(i).getPackage_details().getRound_sign_name());
                                    datalist.setRound_signature(bookingHistoryResp.getData().get(i).getPackage_details().getRound_signature());
                                    datalist.setFeedback(bookingHistoryResp.getData().get(i).getPackage_details().getFeedback());
                                    datalist.setCreated_at(bookingHistoryResp.getData().get(i).getPackage_details().getCreated_at());

                                    Log.d("Tag","-----------is_round_trip----history----"+bookingHistoryResp.getData().get(i).getPackage_details().getIs_round_trip());
                                    datalist.setIs_round_trip(bookingHistoryResp.getData().get(i).getPackage_details().getIs_round_trip());
                                    datalist.setIs_first_round_completed(bookingHistoryResp.getData().get(i).getPackage_details().getIs_first_round_completed());
                                    datalist.setIs_first_round(bookingHistoryResp.getData().get(i).getPackage_details().getIs_first_round());
                                } else {
                                    datalist.setPackage_id("");
                                    datalist.setPackage_code("");
                                    datalist.setFk_booking_id("");
                                    datalist.setPackage_est_value("");
                                    datalist.setPackage_length("");
                                    datalist.setPackage_depth("");
                                    datalist.setPackage_width("");
                                    datalist.setPackage_weight("");
                                    datalist.setPackage_quantity("");
                                    datalist.setPackage_cancel("");
                                    datalist.setDriver_id("");
                                    datalist.setWarehouse_user_id("");
                                    datalist.setPartner_user_id("");
                                    datalist.setDriver_device_token("");
                                    datalist.setHandle_by("");
                                    datalist.setPackage_status("");
                                    datalist.setRating("");
                                    datalist.setSignature("");
                                    datalist.setRound_signature("");
                                    datalist.setRound_sign_name("");
                                    datalist.setSign_name("");
                                    datalist.setFeedback("");
                                    datalist.setCreated_at("");
                                    datalist.setIs_round_trip("");
                                    datalist.setIs_first_round_completed("");
                                    datalist.setIs_first_round("");
                                }

                                if (bookingHistoryResp.getData().get(i).getLocation_details() != null && bookingHistoryResp.getData().get(i).getLocation_details().size() > 0) {
                                    datalist.setBooking_id(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getId());
                                    datalist.setFk_booking_id(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getFk_booking_id());
                                    datalist.setFk_package_code(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getFk_package_code());
                                    datalist.setPickup_house_no(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_house_no());
                                    datalist.setPickup_street(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_street());
                                    datalist.setPickup_landmark(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_landmark());
                                    datalist.setPickup_location(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_location());
                                    datalist.setPickup_location_lat(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_location_lat());
                                    datalist.setPickup_location_lng(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_location_lng());
                                    datalist.setPickup_datetime(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getPickup_datetime());
                                    datalist.setDropoff_house_no(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_house_no());
                                    datalist.setDropoff_street(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_street());
                                    datalist.setDropoff_landmark(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_landmark());
                                    datalist.setDropoff_location(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_location());
                                    datalist.setDropoff_location_lat(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_location_lat());
                                    datalist.setDropoff_location_lng(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_location_lng());
                                    datalist.setDropoff_location_datetime(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getDropoff_location_datetime());
                                    datalist.setRound_pickup_location(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getRound_pickup_location());
                                    datalist.setRound_dropoff_location(bookingHistoryResp.getData().get(i).getLocation_details().get(0).getRound_dropoff_location());

                                } else {
                                    datalist.setBooking_id("");
                                    datalist.setFk_booking_id("");
                                    datalist.setFk_package_code("");
                                    datalist.setPickup_house_no("");
                                    datalist.setPickup_street("");
                                    datalist.setPickup_landmark("");
                                    datalist.setPickup_location("");
                                    datalist.setPickup_location_lat("");
                                    datalist.setPickup_location_lng("");
                                    datalist.setPickup_datetime("");
                                    datalist.setDropoff_house_no("");
                                    datalist.setDropoff_street("");
                                    datalist.setDropoff_landmark("");
                                    datalist.setDropoff_location("");
                                    datalist.setDropoff_location_lat("");
                                    datalist.setDropoff_location_lng("");
                                    datalist.setDropoff_location_datetime("");
                                    datalist.setRound_pickup_location("");
                                    datalist.setRound_dropoff_location("");
                                }

                                //Check WareHouse Pickup Address
                                if (bookingHistoryResp.getData().get(i).getWarehouse_pickup_address() != null && bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().size() > 0) {
                                    for (int j = 0; j < bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().size(); j++) {
                                        datalist.setWhPickupId(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getId());
                                        datalist.setWhPickupAddress(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getAddress());
                                        datalist.setWhPickupLatitude(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getLatitude());
                                        datalist.setWhPickupLongtitude(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getLongtitude());
                                        datalist.setWhPickupName(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getName());
                                        datalist.setWhPickupPhone(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getPhone());
                                        datalist.setWhPickupStateId(bookingHistoryResp.getData().get(i).getWarehouse_pickup_address().get(j).getState_id());
                                    }
                                } else {
                                    datalist.setWhPickupId("");
                                    datalist.setWhPickupAddress("");
                                    datalist.setWhPickupLatitude("");
                                    datalist.setWhPickupLongtitude("");
                                    datalist.setWhPickupName("");
                                    datalist.setWhPickupPhone("");
                                    datalist.setWhPickupStateId("");
                                }

                                //Check WareHouse DropOff Address
                                if (bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address() != null && bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().size() > 0) {
                                    for (int j = 0; j < bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().size(); j++) {
                                        datalist.setWhDropOffId(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getId());
                                        datalist.setWhDropOffAddress(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getAddress());
                                        datalist.setWhDropOffLatitude(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getLatitude());
                                        datalist.setWhDropOffLongtitude(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getLongtitude());
                                        datalist.setWhDropOffName(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getName());
                                        datalist.setWhDropOffPhone(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getPhone());
                                        datalist.setWhDropOffStateId(bookingHistoryResp.getData().get(i).getWarehouse_drop_off_address().get(j).getState_id());
                                    }
                                } else {
                                    datalist.setWhDropOffId("");
                                    datalist.setWhDropOffAddress("");
                                    datalist.setWhDropOffLatitude("");
                                    datalist.setWhDropOffLongtitude("");
                                    datalist.setWhDropOffName("");
                                    datalist.setWhDropOffPhone("");
                                    datalist.setWhDropOffStateId("");
                                }

                                //Add to ArrayList
                                HistoryData.add(datalist);
                            }
                            bookingAdapter = new BookingHistoryAdapterNew(context, HistoryData, true);
                            rvHistory.setAdapter(bookingAdapter);
                            rvHistory.getLayoutManager().scrollToPosition(previousSize);

                            pageNo = pageNo + 1;
                            bookingHistoryResp = null;
                            rlLoadMore.setVisibility(GONE);

                            if (previousSize > 1) {
                                rvHistory.getLayoutManager().scrollToPosition(previousSize - 1);
                            } else {
                                rvHistory.getLayoutManager().scrollToPosition(previousSize);
                            }
                        } else if (bookingHistoryResp.getData().size() == 0 && HistoryData.size() == 0) {
                            //Show Visibility
                            tvNoHistory.setVisibility(View.VISIBLE);
                            rlLoadMore.setVisibility(GONE);
                        }
                        else if (bookingHistoryResp.getData().get(0).getMessage()!=null) {
                            if (bookingHistoryResp.getData().get(0).getMessage().trim().equals("Unauthenticated")) {
                                Global.signInIntent(getActivity());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                if(!(((Activity)context).isFinishing())) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_load_more:
                rlLoadMore.setVisibility(GONE);
                LoadMore();
                rlLoadMore.setVisibility(GONE);
                break;

            case R.id.iv_back:
                //Back Press
//                onBackPressed();
                break;
        }
    }

    public void LoadMore() {
        //Get Booking History API HIT
        bookingApiHit(pageNo);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
