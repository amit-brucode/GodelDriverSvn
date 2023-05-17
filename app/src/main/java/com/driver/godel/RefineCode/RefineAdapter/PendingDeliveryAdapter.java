package com.driver.godel.RefineCode.RefineAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
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
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.PhoneFunctions;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupPojo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by root on 23/1/18.
 */

public class PendingDeliveryAdapter extends RecyclerView.Adapter<PendingDeliveryAdapter.Holder> {
    Context context;
    ArrayList<PendingPickupPojo> pendingPickupBeans = new ArrayList<PendingPickupPojo>();
    Holder holder;
    String exactPickupAddress, exactDropoffAddress;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PendingDeliveryAdapter(Context context, ArrayList<PendingPickupPojo> pendingPickupBeans) {
        this.context = context;
        this.pendingPickupBeans = pendingPickupBeans;
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
        String country_code=CountryCodeCheck.countrycheck(context);


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
        String deliveryType = pendingPickupBeans.get(position).getDelivery_type();
        String minDeliveryTime = pendingPickupBeans.get(position).getMin_delivery_time();
        String bookingType = pendingPickupBeans.get(position).getBooking_type();
        String bookingPickupDate = pendingPickupBeans.get(position).getBooking_pickup_datetime();
        String bookinDeliveryDate = pendingPickupBeans.get(position).getBooking_delivery_datetime();
        String userId = pendingPickupBeans.get(position).getUser_id();
        String userName = pendingPickupBeans.get(position).getName();
        String userImage = pendingPickupBeans.get(position).getUser_image();
        String pickupTime = pendingPickupBeans.get(position).getBooking_pickup_datetime();
        String deliveryTime = pendingPickupBeans.get(position).getBooking_delivery_datetime();
        String userType=pendingPickupBeans.get(position).getType();

        //Get Pickup and DropOff of WareHOuse Check
        String isWarehousePickup = pendingPickupBeans.get(position).getIs_warehouse_pickup();
        String isWareHouseDropOff = pendingPickupBeans.get(position).getIs_warehouse_dropoff();

        //Get WareHouse Address
        String whAddressId = pendingPickupBeans.get(position).getWareHouse_id();
        String whAddressName = pendingPickupBeans.get(position).getWareHouse_name();
        String whAddressEmail = pendingPickupBeans.get(position).getWareHouse_user_email();
        String whAddressPhone = pendingPickupBeans.get(position).getWareHouse_phone();
        String whAddressStateId = pendingPickupBeans.get(position).getWareHouse_state_id();
        String whAddressAddress = pendingPickupBeans.get(position).getWareHouse_address();
        String whAddressLat = pendingPickupBeans.get(position).getWareHouse_latitude();
        String whAddressLng = pendingPickupBeans.get(position).getWareHouse_longtitude();
        String whAddressCreated = pendingPickupBeans.get(position).getWareHouse_created_at();
        String whAddressUpdated = pendingPickupBeans.get(position).getWareHouse_updated_at();
        final String warehousePickupAddress = pendingPickupBeans.get(position).getPickupWarehouseAddress();
        final String warehouseDropAddress = pendingPickupBeans.get(position).getDropOffWarehouseAddress();

        final String pickupWarehouseLatitude = pendingPickupBeans.get(position).getPickupWarehouseLatitude();
        final String pickupWarehouseLongitude = pendingPickupBeans.get(position).getPickupWarehouseLongitude();
        final String dropWarehouseLatitude = pendingPickupBeans.get(position).getDropOffWarehouseLatitude();
        final String dropWarehouseLongitude = pendingPickupBeans.get(position).getDropOffWarehouseLongitude();


        //Get Location Details
        String pickupHouse = pendingPickupBeans.get(position).getPickup_house_no();
        String pickupStreet = pendingPickupBeans.get(position).getPickup_street();
        String pickupLandmark = pendingPickupBeans.get(position).getPickup_landmark();
        String pickupAddress = pendingPickupBeans.get(position).getPickup_location();
        exactPickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, pickupAddress);

        String dropoffHouse = pendingPickupBeans.get(position).getDropoff_house_no();
        String dropoffStreet = pendingPickupBeans.get(position).getDropoff_street();
        String dropoffLandmark = pendingPickupBeans.get(position).getDropoff_landmark();
        String dropOffAddress = pendingPickupBeans.get(position).getDropoff_location();
        exactDropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);

        //Get Package Details
        String packageLength = pendingPickupBeans.get(position).getPackage_length();
        String packageDepth = pendingPickupBeans.get(position).getPackage_depth();
        String packageEstValue = pendingPickupBeans.get(position).getPackage_est_value();
        String packageWidth = pendingPickupBeans.get(position).getPackage_width();
        String packageWeight = pendingPickupBeans.get(position).getPackage_weight();
        String packageQuantity = pendingPickupBeans.get(position).getPackage_quantity();

        String packageId = pendingPickupBeans.get(position).getPackage_code();
        String packageStatus = pendingPickupBeans.get(position).getPackage_status();
        //Get User Details
        String countryCode=pendingPickupBeans.get(position).getCountry_code();
        String name = pendingPickupBeans.get(position).getName();
        String email = pendingPickupBeans.get(position).getUser_email();
        String phone = pendingPickupBeans.get(position).getUser_phone();
        String uniqueID = pendingPickupBeans.get(position).getUser_unique_id();

        //Set Visibility Gone of Rating Bar
        holder.ratingBar.setVisibility(View.GONE);

        //Get Address Note
        String addressNote = pendingPickupBeans.get(position).getAddress_note();

        //Show Address Note
        if (addressNote != null) {
            //Show Address Bar
            holder.rlAddressNote.setVisibility(View.VISIBLE);

            if (addressNote != null || addressNote.length() > 0) {
                String def[] = addressNote.split(Global.AddressNoteSeprator);
                for (int i = 0; i < def.length; i++) {
                    if (def[i] != null || def[i].length() > 0) {
                        if (i == 0) {
                            holder.tvPickupNote.setText(def[i]);
                        }
                        if (i == 1) {
                            holder.tvDropOffNote.setText(def[i].replaceAll(Global.AddressNoteSeprator, ""));
                        }
                    }
                }
            }
        }

        //Set Package Length
        if (packageLength != null) {
            if(packageLength.equals("0")){
                holder.tvLength.setText(" ");
            }else{
                CharSequence charSequence = Global.getPackageCm(packageLength);
                holder.tvLength.setText(charSequence);
            }
        }else {
            //holder.tvWidth.setText("NA");
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
        }

        //Set Package Depth
        if (packageDepth != null) {
            if(packageDepth.equals("0")){
                holder.tvDepth.setText(" ");
            }else{

                CharSequence charSequence = Global.getPackageCm(packageDepth);
            holder.tvDepth.setText(charSequence);}
        } else {
            //holder.tvDepth.setText("NA");
        }

        //Set Package Weight
        if (packageWeight != null) {
            CharSequence charSequence = Global.getPackageKg(packageWeight);
            holder.tvWeight.setText(charSequence);
        } else {
            //holder.tvWeight.setText("NA");
        }

        //Set Package Quantity
        if (packageQuantity != null) {
            CharSequence charSequence = Global.getPackageItems(packageQuantity);
            holder.tvQuantity.setText(charSequence);
        }

        //Set Package Est. Value
        if (packageEstValue != null) {
            String estValue = Global.formatValue(packageEstValue);
            String currency = "<font color='#939393'>"+Global.CurrencySymbol+"</font>";
            holder.tvEstValue.setText(Html.fromHtml(estValue + " " + currency));
        }

        //Set User Name
        if (name == null || name.isEmpty()) {
            //holder.tvName.setText("NA");
        } else {
            holder.tvName.setText(name);
        }

        //Set User Email
        if (email == null || email.isEmpty()) {
            // holder.tvEmail.setText("NA");
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
            //holder.tvPhone.setText("NA");
        } else {
            if(userType.equals("1") || userType.equals("0")){
            if(phone.startsWith("+255")){
                Log.d("User_TYPE","-----1---255----user type-----"+userType);
                holder.tvPhone.setText(Global.formatValueNumber(phone));
            }else if(phone.startsWith("+254")){
                Log.d("User_TYPE","-----1---254----user type-----"+userType);
                holder.tvPhone.setText(Global.formatValueNumber(phone));
            }else if(phone.startsWith("+256")){
                Log.d("User_TYPE","---1--256---user type-----"+userType);
                holder.tvPhone.setText(Global.formatValueNumber(phone));
            }else if(phone.startsWith("+800")){
                holder.tvPhone.setText(Global.formatValueNumber(phone));
            }else if(phone.startsWith("+91")){
                Log.d("User_TYPE","----1--91--user type-----"+userType);
                holder.tvPhone.setText(Global.formatValueNumber2(phone));
            }else if(phone.startsWith("+44")){
                holder.tvPhone.setText(Global.formatValueNumber2(phone));
            }else {
                holder.tvPhone.setText(phone);
            }
            }else if(userType.equals("2")){
                Log.d("User_TYPE","-----2---user type-----"+userType);
                holder.tvPhone.setText(countryCode+" "+phone);
            }else if(userType.equals("4")){
                Log.d("User_TYPE","-----4---user type-----"+userType);
                holder.tvPhone.setText(countryCode+" "+phone);
            }else{
                holder.tvPhone.setText(phone);
            }


        }

        if (uniqueID != null || !uniqueID.isEmpty()) {
            holder.tvUserUniqueID.setText(uniqueID);
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
                    int recurringWeekDay = 0;
                    String recurringDayTime = null;

                    if (pendingPickupBeans.get(position).getRecurring_day() != null) {
                        recurringWeekDay = Integer.parseInt(pendingPickupBeans.get(position).getRecurring_day());
                    }

                    //Get Recurring Day Time
                    if (pendingPickupBeans.get(position).getRecurring_day_time() != null) {
                        recurringDayTime = pendingPickupBeans.get(position).getRecurring_day_time();
                    }

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
                        || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

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
            } else {
                //Hide Visibility of Pickup and Timing
                holder.rlRecurringPickupDelivery.setVisibility(View.GONE);

                //Show Visibility of Pickup and Timing
                holder.rlPickupDeliveryTiming.setVisibility(View.VISIBLE);

                //Set Booking Pickup Time
                if (pickupTime != null) {
                    String packagePickupTime = Global.getConvertedDateTime(pickupTime);
                    holder.tvPickupTiming.setText(packagePickupTime);
                }
                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//                || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                        ||preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

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
                            if (isWareHouseDropOff.equalsIgnoreCase("true")) {

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
                        if (isWareHouseDropOff.equalsIgnoreCase("true")) {

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
        }

        //Set Package Id
        if (packageId != null) {
            holder.tvPackageId.setText(packageId);
        }

        //Set Booking Picup Date Time
        if (bookinDeliveryDate != null) {
            holder.tvDateTime.setText(bookinDeliveryDate.replaceAll("00:00:00", ""));
        }

        //Set User Details
        if (userName != null) {
            holder.tvUserName.setText(userName);
        }

        if (userImage != null) {
            String imagePath = Global.BASE_URL + "backend/public/uploads/user_profile/" + userId + "/" + userImage;
            Glide.with(context).load(imagePath).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ic_user).into(holder.ivUser);
        }

        //Set Pickup LocationDetails
        if (isWarehousePickup != null) {
            if (isWarehousePickup.equalsIgnoreCase("true")) {
                //Set WareHouse Pickup Location
                if (whAddressAddress != null) {
                    holder.tvPickupAddress.setText(warehousePickupAddress);
                }
            } else if (isWarehousePickup.equalsIgnoreCase("false")) {
                //Set User Pickup Location
                if (exactPickupAddress != null) {
                    holder.tvPickupAddress.setText(exactPickupAddress);
                }
            }
        }

        //Set DropOff Location Details
        if (isWareHouseDropOff != null) {
            if (isWareHouseDropOff.equalsIgnoreCase("true")) {
                holder.tvDropOffAddress.setText(warehouseDropAddress);
            } else if (isWareHouseDropOff.equalsIgnoreCase("false")) {
                if (exactDropoffAddress != null) {
                    holder.tvDropOffAddress.setText(exactDropoffAddress);
                }
            }
        }

        //Set Package Details
        if (packageStatus != null) {
            //Set Package Status
            if (packageStatus.equalsIgnoreCase("0")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.pendingtext));
            } else if (packageStatus.equalsIgnoreCase("1")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.assignedtext));
            } else if (packageStatus.equalsIgnoreCase("2")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.arrivingtext));
            } else if (packageStatus.equalsIgnoreCase("3")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.arrivedtext));
            } else if (packageStatus.equalsIgnoreCase("4")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.waitingtext));
            } else if (packageStatus.equalsIgnoreCase("5")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.starttext));
            } else if (packageStatus.equalsIgnoreCase("6")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.inprogresstext));
            } else if (packageStatus.equalsIgnoreCase("7")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.completedtextt));
            } else if (packageStatus.equalsIgnoreCase("8")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.canceltextt));
            } else if (packageStatus.equalsIgnoreCase("9")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.waitingbeforetext));
            } else if (packageStatus.equalsIgnoreCase("10")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.warehousetext));
            } else if (packageStatus.equalsIgnoreCase("11")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.rejectedtext));
            } else if (packageStatus.equalsIgnoreCase("12")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.deliveryfailuretext));
            }
        }

        //Set on Click Listener CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickupAddress = "", dropAddress = "", packageId = "", packageCode = "", OriginLat = "", OriginLng = "", DestLat = "", DestLng = "", IsDropWarehouse = "";
                String isPickupWarehouse = pendingPickupBeans.get(position).getIs_warehouse_pickup();
                if (isPickupWarehouse != null && isPickupWarehouse.trim().length() > 0 && isPickupWarehouse.trim().equalsIgnoreCase("true")) {
                    pickupAddress = pendingPickupBeans.get(position).getPickupWarehouseAddress();
                    OriginLat = pendingPickupBeans.get(position).getPickupWarehouseLatitude();
                    OriginLng = pendingPickupBeans.get(position).getPickupWarehouseLongitude();
                } else {
                    pickupAddress = pendingPickupBeans.get(position).getPickup_location();
                    OriginLat = pendingPickupBeans.get(position).getPickup_location_lat();
                    OriginLng = pendingPickupBeans.get(position).getPickup_location_lng();
                }

                String isDropWarehouse = pendingPickupBeans.get(position).getIs_warehouse_dropoff();
                if (isDropWarehouse != null && isDropWarehouse.trim().length() > 0 && isDropWarehouse.trim().equalsIgnoreCase("true")) {
                    dropAddress = pendingPickupBeans.get(position).getDropOffWarehouseAddress();
                    DestLat = pendingPickupBeans.get(position).getDropOffWarehouseLatitude();
                    DestLng = pendingPickupBeans.get(position).getDropOffWarehouseLongitude();
                } else {
                    dropAddress = pendingPickupBeans.get(position).getDropoff_location();
                    DestLat = pendingPickupBeans.get(position).getDropoff_location_lat();
                    DestLng = pendingPickupBeans.get(position).getDropoff_location_lng();
                }
                packageId = pendingPickupBeans.get(position).getPackage_id();
                packageCode = pendingPickupBeans.get(position).getPackage_code();
                String packageStatus = pendingPickupBeans.get(position).getPackage_status();
                String driverID = pendingPickupBeans.get(position).getDriver_id();
                Log.d("Status"," "+packageStatus);
                Intent i = new Intent().setClass(context, MapsActivity.class);
                i.putExtra("PickupAddress", "" + pickupAddress);
                i.putExtra("DropoffAddress", "" + dropAddress);
                i.putExtra("PackageId", "" + packageId);
                i.putExtra("PackageCode", "" + packageCode);
                i.putExtra("OriginLat", "" + OriginLat);
                i.putExtra("OriginLng", "" + OriginLng);
                i.putExtra("DestLat", "" + DestLat);
                i.putExtra("DestLng", "" + DestLng);
                i.putExtra("Driverid", "" + driverID);
                i.putExtra("isDropWarehouse", "" + isDropWarehouse);
                i.putExtra("isPickupWarehouse", "" + isPickupWarehouse);
                i.putExtra("packageStatus", "" + packageStatus);
                i.putExtra("CheckClick", "deliveries");

                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingPickupBeans.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvPackageId, tvStatus, tvUserName, tvPickupAddress, tvDropOffAddress, tvDateTime, tvPickupTiming, tvDropoffTiming,tv_round_trip, tvName,tv_email_label, tvEmail, tvPhone;
        TextView tvAddressNote, tvLength, tvWidth, tvDepth, tvQuantity, tvEstValue, tvWeight, tvUserUniqueID;
        ImageView ivUser;
        RatingBar ratingBar;
        CardView cardView;
        View view_contact1;
        private LinearLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;

        TextView tvRecurringType, tvRecurringPickup, tvRecurringDelivery, tvPickupNote, tvDropOffNote;
        RelativeLayout rlPickupDeliveryTiming, rlRecurringPickupDelivery, rlAddressNote;

        public Holder(View itemView) {
            super(itemView);
            //Initialization
            view_contact1 = (View) itemView.findViewById(R.id.view_customer_detail2);
            tvPackageId = (TextView) itemView.findViewById(R.id.tv_package_id);
            tvUserUniqueID = (TextView) itemView.findViewById(R.id.tv_user_unique);
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
            rlPickupDeliveryTiming = (RelativeLayout) itemView.findViewById(R.id.rl_pickup_delivery_timing);
            rlRecurringPickupDelivery = (RelativeLayout) itemView.findViewById(R.id.rl_recurring_pickup_delivery_timing);
            tvRecurringType = (TextView) itemView.findViewById(R.id.tv_recurring_type);
            tvRecurringPickup = (TextView) itemView.findViewById(R.id.tv_recurring_pickup_timing);
            tvRecurringDelivery = (TextView) itemView.findViewById(R.id.tv_recurring_delivery_timing);
            rlAddressNote = (RelativeLayout) itemView.findViewById(R.id.rl_address_note);
            tvAddressNote = (TextView) itemView.findViewById(R.id.tv_address_note);
            tvPickupNote = (TextView) itemView.findViewById(R.id.tv_pickup_address_note);
            tvDropOffNote = (TextView) itemView.findViewById(R.id.tv_dropoff_address_note);

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
