package com.driver.godel.RefineCode.RefineAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineActivities.MapsActivity;
import com.driver.godel.RefineCode.RefineFragments.PickupFragment;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupPojo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by root on 22/1/18.
 */

public class PendingPickupAdapter extends RecyclerView.Adapter<PendingPickupAdapter.Holder> {
    Context context;
    List<PendingPickupPojo> pickupData;
    private Holder holder;
    List<PendingPickupPojo> pendingPickupBeans = new ArrayList<PendingPickupPojo>();
    int tab;
    String exactPickupAddress, exactDropoffAddress;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PendingPickupAdapter(Context context, List<PendingPickupPojo> PendingPickupPOJO) {
        this.context = context;
        this.pendingPickupBeans = PendingPickupPOJO;
        preferences = context.getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));
        Global global=new Global(context);
        global.setCurrencySymbol();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_pending_packages_new, parent, false);
        holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {


        if(preferences.getString(Global.DISPLAY_PACKAGE_LENGTH, "").equals("0")) {
            holder.ll_det_visi_length.setVisibility(GONE);
        }else {
            holder.ll_det_visi_length.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_WIDTH, "").equals("0")) {
            holder.ll_det_visi_width.setVisibility(GONE);
        }else {
            holder.ll_det_visi_width.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_DEPTH, "").equals("0")) {
            holder.ll_det_visi_depth.setVisibility(GONE);
        }else {
            holder.ll_det_visi_depth.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_WEIGHT, "").equals("0")) {
            holder.ll_det_visi_weight.setVisibility(GONE);
        }else {
            holder.ll_det_visi_weight.setVisibility(VISIBLE);
        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_QUANTITY, "").equals("0")) {
            holder.ll_det_visi_quan.setVisibility(GONE);
        }else {
            holder.ll_det_visi_quan.setVisibility(VISIBLE);
        }

//        if(preferences.getString(GlobalVariables.DISPLAY_PACKAGE_TYPE, "").equals("0")) {
//            ll_det_visi_type.setVisibility(GONE);
//        }else {
//            ll_det_visi_type.setVisibility(VISIBLE);
//        }

        if(preferences.getString(Global.DISPLAY_PACKAGE_VALUE, "").equals("0")) {
            holder.ll_det_visi_value.setVisibility(GONE);
        }else {
            holder.ll_det_visi_value.setVisibility(VISIBLE);
        }

        //Get User Details
        String country_code=CountryCodeCheck.countrycheck(context);
        String userType = pendingPickupBeans.get(position).getType();
        String minDeliveryTime = pendingPickupBeans.get(position).getMin_delivery_time();
        String deliveryType = pendingPickupBeans.get(position).getDelivery_type();
        String bookingType = pendingPickupBeans.get(position).getBooking_type();
        String bookingPicupDate = pendingPickupBeans.get(position).getBooking_pickup_datetime();
        String userId = pendingPickupBeans.get(position).getUser_id();
        String userName = pendingPickupBeans.get(position).getName();
        String userImage = pendingPickupBeans.get(position).getUser_image();
        String pickupTime = pendingPickupBeans.get(position).getBooking_pickup_datetime();
        String deliveryTime = pendingPickupBeans.get(position).getBooking_delivery_datetime();
        String dropOffAddress = pendingPickupBeans.get(position).getDropoff_location();
        final String isDropoffWareHouse = pendingPickupBeans.get(position).getIs_warehouse_dropoff();

        //Get Location Details
        if (pendingPickupBeans.get(position).getIs_warehouse_dropoff() != null || pendingPickupBeans.get(position).getIs_warehouse_dropoff().trim().length() > 0) {
            if (pendingPickupBeans.get(position).getIs_warehouse_dropoff().toString().trim().equals("0")) {
                String dropoffHouse = pendingPickupBeans.get(position).getDropoff_house_no();
                String dropoffStreet = pendingPickupBeans.get(position).getDropoff_street();
                String dropoffLandmark = pendingPickupBeans.get(position).getDropoff_landmark();
                dropOffAddress = pendingPickupBeans.get(position).getDropoff_location();
                exactDropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);
            } else {
                exactDropoffAddress = pendingPickupBeans.get(position).getWareHouse_address();
            }
        } else {
            String dropoffHouse = pendingPickupBeans.get(position).getDropoff_house_no();
            String dropoffStreet = pendingPickupBeans.get(position).getDropoff_street();
            String dropoffLandmark = pendingPickupBeans.get(position).getDropoff_landmark();
            dropOffAddress = pendingPickupBeans.get(position).getDropoff_location();
            exactDropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);
        }

        //Get Pickup Address
        String pickupHouse = pendingPickupBeans.get(position).getPickup_house_no();
        String pickupStreet = pendingPickupBeans.get(position).getPickup_street();
        String pickupLandmark = pendingPickupBeans.get(position).getPickup_landmark();
        final String pickupAddress = pendingPickupBeans.get(position).getPickup_location();
        exactPickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, pickupAddress);

        //Get Package Details
        String packageLength = pendingPickupBeans.get(position).getPackage_length();
        String packageDepth = pendingPickupBeans.get(position).getPackage_depth();
        String packageEstValue = pendingPickupBeans.get(position).getPackage_est_value();
        String packageWidth = pendingPickupBeans.get(position).getPackage_width();
        String packageWeight = pendingPickupBeans.get(position).getPackage_weight();
        String packageQuantity = pendingPickupBeans.get(position).getPackage_quantity();

        final String packageCode = pendingPickupBeans.get(position).getPackage_code();
        final String packageStatus = pendingPickupBeans.get(position).getPackage_status();

        //Get Address Note
        String addressNote = pendingPickupBeans.get(position).getAddress_note();

        //Get User Details
        String countryCode=pendingPickupBeans.get(position).getCountry_code();
        String userUniqueID = pendingPickupBeans.get(position).getUser_unique_id();
        String name = pendingPickupBeans.get(position).getName();
        String email = pendingPickupBeans.get(position).getUser_email();
        String phone = pendingPickupBeans.get(position).getUser_phone();

        //Hide Status Label and status
        holder.tvStatusLabel.setVisibility(View.GONE);
        holder.tvStatus.setVisibility(View.GONE);

        //Show Address Note
        if (addressNote != null) {
            //Show Address Bar
            holder.rlAddressNote.setVisibility(View.VISIBLE);

            if (addressNote != null || addressNote.length() > 0) {
                Log.d("AddressNote_Log",addressNote);
                String def[] = addressNote.split(Global.AddressNoteSeprator);
                for (int i = 0; i < def.length; i++) {
                    if (def[i] != null || def[i].length() > 0) {
                        if (i == 0) {
                            Log.d("AddressNote_Log",""+def[i]);
                            holder.tvPickupNote.setText(def[i]);
                        }
                        if (i == 1) {
                            Log.d("AddressNote_Log",""+def[i]);
                            //String dropOffNote = def[i].replaceAll(Global.AddressNoteSeprator, "");
                            holder.tvDropOffNote.setText(def[i].replaceAll(Global.AddressNoteSeprator, ""));
                        }
                    }
                }
            }

            //Show Address
            //holder.tvAddressNote.setText(addressNote);
        }
        if (packageLength != null) {
            if(packageLength.equals("0")){
                holder.tvLength.setText(" ");
            }else{
                CharSequence charSequence = Global.getPackageCm(packageLength);
                holder.tvLength.setText(charSequence);
            }
        }else {
            holder.tvWidth.setText("");
//            holder.tvWidth.setText("NA");
        }

        //Set Package Width
        if (packageWidth != null) {
            if(packageWidth.equals("0")){
                holder.tvWidth.setText(" ");
            }else{
                CharSequence charSequence = Global.getPackageCm(packageWidth);
                holder.tvWidth.setText(charSequence);}
        } else {
            //holder.tvWidth.setText("NA");
            holder.tvWidth.setText("");
        }

        //Set Package Depth
        if (packageDepth != null) {
            if(packageDepth.equals("0")){
                holder.tvDepth.setText(" ");
            }else{

                CharSequence charSequence = Global.getPackageCm(packageDepth);
                holder.tvDepth.setText(charSequence);}
        } else {
            holder.tvDepth.setText("");
//            holder.tvDepth.setText("NA");
        }

        //Set Package Weight
        if (packageWeight != null) {
            CharSequence charSequence = Global.getPackageKg(packageWeight);
            holder.tvWeight.setText(charSequence);
        }else {
            holder.tvWeight.setText("");
        }

        //Set Package Quantity
        if (packageQuantity != null) {
            CharSequence charSequence = Global.getPackageItems(packageQuantity);
            holder.tvQuantity.setText(charSequence);
        }else{
            holder.tvQuantity.setText("");
        }

        //Set Package Est. Value
        if (packageEstValue != null) {
            String estValue = Global.formatValue(packageEstValue);
            String currency = "<font color='#939393'>"+Global.CurrencySymbol+"</font>";
            holder.tvEstValue.setText(Html.fromHtml(estValue + " " + currency));
//          CharSequence charSequence = Global.getPackageValue(packageEstValue);
        }else {
            holder.tvEstValue.setText("");
        }

        if (userUniqueID != null || !userUniqueID.isEmpty()) {
            holder.tvUniqueID.setText(userUniqueID);
        }else{
            holder.tvUniqueID.setText("");
        }
        //Set User Name
        if (name == null || name.isEmpty()) {
            //holder.tvName.setText("NA");
        } else {
            holder.tvName.setText(name);
        }

        //Set User Email
        if (email == null || email.isEmpty()) {
            holder.tvEmail.setText("");
//            holder.tvEmail.setText("NA");
        } else {
            if(email.equals("nomail@nomail.com")) {
                holder.tvEmail.setVisibility(GONE);
                holder.tv_email_label.setVisibility(GONE);
                holder.view_contact1.setVisibility(GONE);
//            tv_Email.setHeight(0);
            }else{
            holder.tvEmail.setText(email);}
        }

        //Set User Phone
        if (phone == null || phone.isEmpty()) {
            holder.tvPhone.setText("");
        } else {
            if(userType.equals("1") || userType.equals("0")){
                if(phone.startsWith("+255")){
//                    Log.d("User_TYPE","-----1---255----user type-----"+userType);
                    holder.tvPhone.setText(Global.formatValueNumber(phone));
                }else if(phone.startsWith("+254")){
//                    Log.d("User_TYPE","-----1---255----user type-----"+userType);
                    holder.tvPhone.setText(Global.formatValueNumber(phone));
                }else if(phone.startsWith("+256")){
//                    Log.d("User_TYPE","---1--256---user type-----"+userType);
                    holder.tvPhone.setText(Global.formatValueNumber(phone));
                }else if(phone.startsWith("+800")){
                    holder.tvPhone.setText(Global.formatValueNumber(phone));
                }else if(phone.startsWith("+91")){
//                    Log.d("User_TYPE","----1--91--user type-----"+userType);
                    holder.tvPhone.setText(Global.formatValueNumber2(phone));
                }else if(phone.startsWith("+44")){
                    holder.tvPhone.setText(Global.formatValueNumber2(phone));
                }else {
                    holder.tvPhone.setText(Global.formatValueNumber(phone));
                }
            }else if(userType.equals("2")){
//                Log.d("User_TYPE","-----2---user type-----"+userType);
                holder.tvPhone.setText(countryCode+" "+phone);
            }else if(userType.equals("4")){
//                Log.d("User_TYPE","-----4---user type-----"+userType);
                holder.tvPhone.setText(countryCode+" "+phone);
            }else{
                holder.tvPhone.setText(phone);
            }

        }

        //Check Booking Type
        if (bookingType != null) {
            //Check for Recurrence booking
            if (bookingType.equalsIgnoreCase("2")) {
                //Hide Visibility of Pickup and Timing
                holder.rlPickupDeliveryTiming.setVisibility(View.GONE);

                //Show Visibility of Pickup and Timing
                holder.rlRecurringPickupDelivery.setVisibility(View.VISIBLE);

                //Check Recurring Status for Daily Basis
                if (pendingPickupBeans.get(position).getRecurring_pickup_type().equalsIgnoreCase("0")) {
                    //Set Recurring Type
                    if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.dailytext)+" (Round Trip)");
                    }else {
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.dailytext));
                    }

                    //Get Recurring Day Time
                    String recurringDayTime = pendingPickupBeans.get(position).getRecurring_day_time();

                    //Check Recurring Day Time
                    if (recurringDayTime != null) {
                        //Set Recurring Timing
                        holder.tvRecurringPickup.setText(recurringDayTime);
                    }
                }
                //Check Recurring Status for Weekly Basis
                else if (pendingPickupBeans.get(position).getRecurring_pickup_type().equalsIgnoreCase("1")) {
                    //Set Recurring Type

                    if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.weeklytext)+" (Round Trip)");
                    }else {
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.weeklytext));
                    }
                    //Get Recurring Week Day
                    int recurringWeekDay = Integer.parseInt(pendingPickupBeans.get(position).getRecurring_day());

                    //Get Recurring Day Time
                    String recurringDayTime = pendingPickupBeans.get(position).getRecurring_day_time();

                    //Check Recurring Day Time and Recurring Day
                    String dayOfWeek = Global.getPickupDay(recurringWeekDay);

                    if (recurringDayTime != null) {
                        //Set Recurring Timing and DayOfWeek
                        holder.tvRecurringPickup.setText(dayOfWeek + " " + recurringDayTime);
                    }
                }
                //Check Recurring Status for Monthly Basis
                else if (pendingPickupBeans.get(position).getRecurring_pickup_type().equalsIgnoreCase("2")) {
                    //Set Recurring Type

                    if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.monthlytext)+" (Round Trip)");
                    }else {
                        holder.tvRecurringType.setText(context.getResources().getString(R.string.monthlytext));
                    }

                    //Get Recurring Week
                    String recurringWeek = pendingPickupBeans.get(position).getRecurring_week();


                    //Check Recurring Week and Day Time
                    if (recurringWeek != null) {
                        //Set Recurring Week and Day Time
                        holder.tvRecurringPickup.setText(recurringWeek);
                    }
                }
                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                        ||
                        preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                    if(deliveryType!=null) {
                        if (deliveryType.equals("0")) {
                            holder.tvRecurringDelivery.setText("Instant");
                        } else if (deliveryType.equals("1")) {
                            holder.tvRecurringDelivery.setText("Schedule");
                        } else if (deliveryType.equals("2")) {
                            holder.tvRecurringDelivery.setText("Standard");
                        } else if (deliveryType.equals("3")) {
                            holder.tvRecurringDelivery.setText("Express");
                        } else  {
                            holder.tvRecurringDelivery.setText("Overnight");
                        }
                    }else {
                        if (pendingPickupBeans.get(position).getDelivery_session() != null) {
                            String deliverySession = pendingPickupBeans.get(position).getDelivery_session();

                            //Check Delivery Session for Morning or Evening
                            if (deliverySession.equalsIgnoreCase("0")) {
                                holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
                            } else if (deliverySession.equalsIgnoreCase("1")) {
                                holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
                            }
                        }
                    }
                }
                else {

                    //Check Recuurence Status for Delivery
                    if (pendingPickupBeans.get(position).getDelivery_session() != null) {
                        String deliverySession = pendingPickupBeans.get(position).getDelivery_session();

                        //Check Delivery Session for Morning or Evening
                        if (deliverySession.equalsIgnoreCase("0")) {
                            holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
                        } else if (deliverySession.equalsIgnoreCase("1")) {
                            holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
                        }
                    }

                }
                //Check Recuurence Status for Delivery
//                if (pendingPickupBeans.get(position).getDelivery_session() != null) {
//                    String deliverySession = pendingPickupBeans.get(position).getDelivery_session();
//
//                    //Check Delivery Session for Morning or Evening
//                    if (deliverySession.equalsIgnoreCase("0")) {
//                        holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
//                    } else if (deliverySession.equalsIgnoreCase("1")) {
//                        holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
//                    }
//                }
            } else {
                //Hide Visibility of Pickup and Timing
                holder.rlRecurringPickupDelivery.setVisibility(View.GONE);
                //Show Visibility of Pickup and Timing
                holder.rlPickupDeliveryTiming.setVisibility(View.VISIBLE);
                //Set Booking Pickup Time
                if (pickupTime != null) {
                    if (bookingType != null && bookingType.trim().equalsIgnoreCase("0")) {
                        String packagePickupTime = Global.getConvertedDateTime(pickupTime);
                        holder.tvPickupTiming.setText(packagePickupTime.replaceAll("00:00:00", ""));
                    } else {
                        String packagePickupTime = Global.getConvertedPickupDateTime(pickupTime);
                        holder.tvPickupTiming.setText(packagePickupTime.replaceAll("00:00:00", ""));
                    }
                }

                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                        ||
                        preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                    if(deliveryType!=null) {
                        if (deliveryType.equals("0")) {
                            holder.tvDropoffTiming.setText("Instant");
                        } else if (deliveryType.equals("1")) {
                            holder.tvDropoffTiming.setText("Schedule");
                        } else if (deliveryType.equals("2")) {
                            holder.tvDropoffTiming.setText("Standard");
                        } else if (deliveryType.equals("3")) {
                            holder.tvDropoffTiming.setText("Express");
                        } else  {
                            holder.tvDropoffTiming.setText("Overnight");
                        }
                        if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                            holder.tv_round_trip.setVisibility(VISIBLE);
                        }
                    }else {
                        if (deliveryTime != null) {
                            if (isDropoffWareHouse.equalsIgnoreCase("1")) {
                                String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
                                holder.tvDropoffTiming.setText(packageDeliveryTime);
                            } else {
                                String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                                holder.tvDropoffTiming.setText(packageDeliveryTime);
                            }
                            if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                                holder.tv_round_trip.setVisibility(VISIBLE);
                            }
                        }
                    }
                }
                else {
                    //Set Booking Delivery Time
                    if (deliveryTime != null) {
                        if (isDropoffWareHouse.equalsIgnoreCase("1")) {
                            String packageDeliveryTime = Global.getConvertedDateTime(pickupTime);
                            holder.tvDropoffTiming.setText(packageDeliveryTime);
                            if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                                holder.tv_round_trip.setVisibility(VISIBLE);
                            }

                        } else {
                            String packageDeliveryTime = Global.getConvertedDateTime(deliveryTime);
                            holder.tvDropoffTiming.setText(packageDeliveryTime);
                            if(pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
                                holder.tv_round_trip.setVisibility(VISIBLE);
                            }
                        }
                    }
                }
            }
        }else{

        }

        //Set Package Id
        if (packageCode != null) {
            holder.tvPackageId.setText(packageCode);
        }else{
            holder.tvPackageId.setText("");
        }

        //Set Booking Picup Date Time
        if (bookingPicupDate != null) {
            holder.tvDateTime.setText(bookingPicupDate.replaceAll("00:00:00", ""));
        }else{
            holder.tvDateTime.setText("");
        }

        //Set Visibility Gone of Rating Bar
        holder.ratingBar.setVisibility(View.GONE);

        //Set User Details
        if (userName != null) {
            holder.tvUserName.setText(userName);
        }else{
            holder.tvUserName.setText("");
        }

        if (userImage != null) {

            Log.d("User_TYPE","-----userimage-----"+userImage);
            Log.d("User_TYPE","-----context-----"+context);

//            if(context!=null){
//
//            String countrycode = CountryCodeCheck.countrycheck(context);
//            Log.d("User_TYPE","-----countrycode-----"+countrycode);
//            String imagePath = Global.BASE_URL + "backend/public/uploads/"+countrycode+"/user_profile/" + userId + "/" + userImage;
//            Glide.with(context).load(imagePath).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ic_user).into(holder.ivUser);
//        }else{
//
////                context=parent.getContext();
//
//            }
//
        }
//        else{
//            Log.d("User_TYPE","-----null usetr image-----"+context);
//
//        }

        //Set LocationDetails
        if (exactPickupAddress != null) {
            holder.tvPickupAddress.setText(exactPickupAddress);
        }else{
            holder.tvPickupAddress.setText("");
        }

        if (exactDropoffAddress != null) {
            holder.tvDropOffAddress.setText(exactDropoffAddress);
        }else{
            holder.tvDropOffAddress.setText("");
        }

        //Set Package Details
        if (packageStatus != null) {
            holder.tvStatus.setText(context.getResources().getString(R.string.acceptedtext));
        }else{
            holder.tvStatus.setText("");
        }
        //Set on Click Listener CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Log.d("PICKUP_FRAGMENT","----in adapter--pendingPickupBeans---------"+pendingPickupBeans);
//Log.d("PICKUP_FRAGMENT","----in adapter--pendingPickupBeans.size---------"+pendingPickupBeans.size());
                String pickupAddress = "", dropAddress = "", packageId = "", packageCode = "", OriginLat = "", OriginLng = "", DestLat = "", DestLng = "", IsDropWarehouse = "";

                String isPickupWarehouse = pendingPickupBeans.get(position).getIs_warehouse_pickup();
                String isDropWarehouse = pendingPickupBeans.get(position).getIs_warehouse_dropoff();

                if (isPickupWarehouse != null && isPickupWarehouse.trim().length() > 0 && isPickupWarehouse.trim().equalsIgnoreCase("1")) {
                    pickupAddress = pendingPickupBeans.get(position).getWareHouse_address();
                    OriginLat = pendingPickupBeans.get(position).getWareHouse_latitude();
                    OriginLng = pendingPickupBeans.get(position).getWareHouse_longtitude();
                } else {
                    pickupAddress = pendingPickupBeans.get(position).getPickup_location();
                    OriginLat = pendingPickupBeans.get(position).getPickup_location_lat();
                    OriginLng = pendingPickupBeans.get(position).getPickup_location_lng();
                }

                if (isDropWarehouse != null && isDropWarehouse.trim().length() > 0 && isDropWarehouse.trim().equalsIgnoreCase("1")) {
                    dropAddress = pendingPickupBeans.get(position).getWareHouse_address();
                    DestLat = pendingPickupBeans.get(position).getWareHouse_latitude();
                    DestLng = pendingPickupBeans.get(position).getWareHouse_longtitude();
                } else {
                    dropAddress = pendingPickupBeans.get(position).getDropoff_location();
                    DestLat = pendingPickupBeans.get(position).getDropoff_location_lat();
                    DestLng = pendingPickupBeans.get(position).getDropoff_location_lng();
                }

                packageId = pendingPickupBeans.get(position).getPackage_id();
                packageCode = pendingPickupBeans.get(position).getPackage_code();
                String packageStatus = pendingPickupBeans.get(position).getPackage_status();


                Intent i = new Intent().setClass(context, MapsActivity.class);

//                if( pendingPickupBeans.get(position).getIs_round_trip().equals("1")){
//                if( pendingPickupBeans.get(position).getIs_first_round_completed().equals("0")) {
//                    i.putExtra("PickupAddress", "" + pickupAddress);
//                    i.putExtra("DropoffAddress", "" + dropAddress);
//                    i.putExtra("OriginLat", "" + OriginLat);
//                    i.putExtra("OriginLng", "" + OriginLng);
//                    i.putExtra("DestLat", "" + DestLat);
//                    i.putExtra("DestLng", "" + DestLng);
//                }else{
//                    i.putExtra("PickupAddress", "" + dropAddress);
//                    i.putExtra("DropoffAddress", "" + pickupAddress);
//                    i.putExtra("OriginLat", "" + DestLat);
//                    i.putExtra("OriginLng", "" + DestLng);
//                    i.putExtra("DestLat", "" + OriginLat);
//                    i.putExtra("DestLng", "" + OriginLng);
//
//                }
//                }else {
                    i.putExtra("PickupAddress", "" + pickupAddress);
                    i.putExtra("DropoffAddress", "" + dropAddress);
                    i.putExtra("OriginLat", "" + OriginLat);
                    i.putExtra("OriginLng", "" + OriginLng);
                    i.putExtra("DestLat", "" + DestLat);
                    i.putExtra("DestLng", "" + DestLng);
//                }
                i.putExtra("PackageId", "" + packageId);
                i.putExtra("PackageCode", "" + packageCode);

                i.putExtra("isDropWarehouse", "" + isDropWarehouse);
                i.putExtra("isPickupWarehouse", "" + isPickupWarehouse);
                i.putExtra("packageStatus", "" + packageStatus);
                i.putExtra("CheckClick", "pickups");
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pendingPickupBeans.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvPackageId, tvUserName, tvPickupAddress, tvDropOffAddress, tvDateTime, tvPickupTiming, tvDropoffTiming,tv_round_trip, tvName,tv_email_label, tvEmail, tvPhone;
        TextView tvAddressNote, tvLength, tvWidth, tvDepth, tvQuantity, tvEstValue, tvWeight, tvStatusLabel, tvStatus, tvUniqueID;
        ImageView ivUser;
        RatingBar ratingBar;
        private LinearLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;

        CardView cardView;
        TextView tvRecurringType, tvRecurringPickup, tvRecurringDelivery, tvPickupNote, tvDropOffNote;
        RelativeLayout rlPickupDeliveryTiming, rlRecurringPickupDelivery, rlAddressNote;
        View view_contact1;

        public Holder(View itemView) {
            super(itemView);
            //Initialization
            view_contact1 = (View) itemView.findViewById(R.id.view_customer_detail2);
            tvPackageId = (TextView) itemView.findViewById(R.id.tv_package_id);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvPickupAddress = (TextView) itemView.findViewById(R.id.tv_pickup_address);
            tvDropOffAddress = (TextView) itemView.findViewById(R.id.tv_drop_off_address);
            tvDateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            ivUser = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            cardView = (CardView) itemView.findViewById(R.id.cardview_history);
            tvPickupTiming = (TextView) itemView.findViewById(R.id.tv_pickup_timing);
            tvDropoffTiming = (TextView) itemView.findViewById(R.id.tv_dropoff_timing);
            tv_round_trip = (TextView) itemView.findViewById(R.id.tv_round_trip);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);
            tv_email_label = (TextView) itemView.findViewById(R.id.tv_email_label);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvLength = (TextView) itemView.findViewById(R.id.tv_length);
            tvWidth = (TextView) itemView.findViewById(R.id.tv_width);
            tvDepth = (TextView) itemView.findViewById(R.id.tv_depth);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvEstValue = (TextView) itemView.findViewById(R.id.tv_est_value);
            tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
            tvStatusLabel = (TextView) itemView.findViewById(R.id.tv_status_txt);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            rlPickupDeliveryTiming = (RelativeLayout) itemView.findViewById(R.id.rl_pickup_delivery_timing);
            rlRecurringPickupDelivery = (RelativeLayout) itemView.findViewById(R.id.rl_recurring_pickup_delivery_timing);
            tvRecurringType = (TextView) itemView.findViewById(R.id.tv_recurring_type);
            tvRecurringPickup = (TextView) itemView.findViewById(R.id.tv_recurring_pickup_timing);
            tvRecurringDelivery = (TextView) itemView.findViewById(R.id.tv_recurring_delivery_timing);
            rlAddressNote = (RelativeLayout) itemView.findViewById(R.id.rl_address_note);
            tvAddressNote = (TextView) itemView.findViewById(R.id.tv_address_note);
            tvPickupNote = (TextView) itemView.findViewById(R.id.tv_pickup_address_note);
            tvDropOffNote = (TextView) itemView.findViewById(R.id.tv_dropoff_address_note);
            tvUniqueID = (TextView) itemView.findViewById(R.id.tv_user_unique);

            ll_det_visi_length = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_length);
            ll_det_visi_width = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_width);
            ll_det_visi_depth = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_depth);
            ll_det_visi_weight = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_weight);
            ll_det_visi_quan = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_quan);
//            ll_det_visi_type = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_type);
            ll_det_visi_value = (LinearLayout) itemView.findViewById(R.id.ll_det_visi_value);
        }
    }
}
