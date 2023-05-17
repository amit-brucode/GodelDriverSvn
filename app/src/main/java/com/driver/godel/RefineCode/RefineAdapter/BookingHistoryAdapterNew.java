package com.driver.godel.RefineCode.RefineAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineActivities.BookingDetailActivityNew;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.response.SignatureResponse.BookingHistoryResponseDetails;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by QSYS\simarjot.singh on 30/6/17.
 */

public class BookingHistoryAdapterNew extends RecyclerView.Adapter<BookingHistoryAdapterNew.Holder> {
    Context context;
    ArrayList<BookingHistoryResponseDetails> HistoryData = new ArrayList<BookingHistoryResponseDetails>();
    Holder holder;
    String userName, userEmail, userPhone, userImg, paymentId, paymentMethod, paymentAmt, currency, paymentStatus, txnId, stripeToken, imagePath, locationId, pickupLocation, dropLocation, pickupDateTime, dropoffDateTime;
    String TAG = "LOCATION_DETAILS", uniqueID;
    boolean isPending = false;
    String PackgeStatus;

    public BookingHistoryAdapterNew(Context context, ArrayList<BookingHistoryResponseDetails> HistoryData, boolean isPending) {
        this.context = context;
        this.HistoryData = HistoryData;
        this.isPending = isPending;
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_booking_history_new, parent, false);
        holder = new BookingHistoryAdapterNew.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        String bookingId = HistoryData.get(position).getBooking_id();
        String bookingType = HistoryData.get(position).getBooking_type();
        String isCancel = HistoryData.get(position).getIs_cancel();
        String statusCode = HistoryData.get(position).getPackage_status();
        String is_round_trip = HistoryData.get(position).getIs_round_trip();
        Log.d("Tag","-----------is_round_trip--------"+is_round_trip);
        if (statusCode != null) {
            PackgeStatus = Global.getStatus(Integer.parseInt(statusCode));
        }


        //Get WareHouse Check
        String isWareHousePickup = HistoryData.get(position).getIsWarehousePickup();
        String isWareHouseDropOff = HistoryData.get(position).getIsWarehouseDropoff();

        //Get User Detail
//        User_details user_details = bookingData.get(position).getUser_details();

//        if (user_details != null) {
        userName = HistoryData.get(position).getName();
        userEmail = HistoryData.get(position).getUser_email();
        userPhone = HistoryData.get(position).getUser_phone();
        userImg = HistoryData.get(position).getUser_image();
//        }

        //Get Package Detail
//        Package_details package_detailses = bookingData.get(position).getPackage_details();
        final String packageId = HistoryData.get(position).getPackage_id();
        final String packageCode = HistoryData.get(position).getPackage_code();
        String packageLength = HistoryData.get(position).getPackage_length();
        String packageDepth = HistoryData.get(position).getPackage_depth();
        String packageEstValue = HistoryData.get(position).getPackage_est_value();
        final String packageWidth = HistoryData.get(position).getPackage_width();
        final String packageWeight = HistoryData.get(position).getPackage_weight();
        final String packageQuantity = HistoryData.get(position).getPackage_quantity();
        final String rating = HistoryData.get(position).getRating();
        final String signedBy = HistoryData.get(position).getSign_name();
        final String signedImageName = HistoryData.get(position).getSignature();
        String booking_date_time = HistoryData.get(position).getCreated_at();
        String booking_type = HistoryData.get(position).getBooking_type();
        String delivery_type = HistoryData.get(position).getDelivery_type();
        String delivery_date_time = HistoryData.get(position).getBooking_delivery_datetime();
        String delivery_session = HistoryData.get(position).getDelivery_session();

        paymentMethod = "";//payment_details.getPayment_method();
        paymentAmt = "";//payment_details.getAmount();
        currency = "";// payment_details.getCurrency();
        paymentStatus = "";// payment_details.getPayment_status();
        txnId = "";//payment_details.getTxn_id();
        stripeToken = "";//payment_details.getStripe_token();

        locationId = HistoryData.get(position).getLocation_id();

        //Get Pickup Location Details
        String pickupHouse = HistoryData.get(position).getPickup_house_no();
        String pickupStreet = HistoryData.get(position).getPickup_street();
        String pickupLandmark = HistoryData.get(position).getPickup_landmark();
        String pickupAddress = HistoryData.get(position).getPickup_location();
        pickupLocation = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, pickupAddress);

        //Get DropOff Location Details
        String dropoffHouse = HistoryData.get(position).getDropoff_house_no();
        String dropoffStreet = HistoryData.get(position).getDropoff_street();
        String dropoffLandmark = HistoryData.get(position).getDropoff_landmark();
        String dropOffAddress = HistoryData.get(position).getDropoff_location();
        dropLocation = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);

        pickupDateTime = HistoryData.get(position).getPickup_datetime();
        dropoffDateTime = HistoryData.get(position).getDropoff_location_datetime();


        //Set Ride Status
        holder.tvPackageId.setText(context.getResources().getString(R.string.bookingidcolon)+" " + packageCode);


        //Check Package Status
        if (statusCode != null && statusCode.trim().equals("8")) {
            holder.tvStatus.setText(PackgeStatus);
            //holder.tvStatus.setTextColor(Color.parseColor("#007f00"));
        } else {
            if (HistoryData.get(position).getDriver_package_status() != null && HistoryData.get(position).getDriver_package_status().trim().equalsIgnoreCase("1")) {
                holder.tvStatus.setText(context.getResources().getString(R.string.completedtextt));
            } else {
                holder.tvStatus.setText(PackgeStatus);
            }
            //holder.tvStatus.setTextColor(Color.parseColor("#007f00"));
        }


        //Set Pickup DateTime
        if (booking_date_time != null) {
            String bookingDateTime = Global.getConvertedDateTime(booking_date_time);
            holder.tvDateTime.setText(bookingDateTime);
        }

        //Set Pickup Address
        if (isWareHousePickup != null) {
            if (isWareHousePickup.equalsIgnoreCase("true")) {
                //Set WareHouse Pickup
                String whPickupAddress = HistoryData.get(position).getWhPickupAddress();
                holder.tvPickupAddress.setText(whPickupAddress);
            } else {
                //Set User Location Pickup
                if (pickupLocation != null) {
                    if(HistoryData.get(position).getIs_first_round().equals("0")) {
                        holder.tvPickupAddress.setText(pickupLocation);
                    }else{
                        holder.tvPickupAddress.setText(HistoryData.get(position).getRound_pickup_location());
                    }
                }
            }
        }

        //Set DropOff Address
        if (isWareHouseDropOff != null) {
            if (isWareHouseDropOff.equalsIgnoreCase("true")) {
                //Set WareHouse DropOff
                String whDropoffAddress = HistoryData.get(position).getWhDropOffAddress();
                holder.tvDropoffAddress.setText(whDropoffAddress);
            } else {
                //Set User DropOff DropOff
                if (dropLocation != null) {
                    if(HistoryData.get(position).getIs_first_round().equals("0")) {
                        holder.tvDropoffAddress.setText(dropLocation);
                    }else{
                        holder.tvDropoffAddress.setText(HistoryData.get(position).getRound_dropoff_location());
                    }
                }
            }
        }

        //Set Rating
        if (rating != null) {
            holder.ratingBar.setRating(Float.parseFloat(rating));
        }

        //Item Set On Click Listener
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String booking_date_time = HistoryData.get(position).getCreated_at();
                //Check WareHouse Pickup and DropOff
                String isWareHousePickup = HistoryData.get(position).getIsWarehousePickup();
                String isWareHouseDropOff = HistoryData.get(position).getIsWarehouseDropoff();

                //Get User Details
                String deliveryType=HistoryData.get(position).getDelivery_type();
                userName = HistoryData.get(position).getName();
                userEmail = HistoryData.get(position).getUser_email();

                userPhone = HistoryData.get(position).getUser_phone();
                userImg = HistoryData.get(position).getUser_image();
                uniqueID = HistoryData.get(position).getUser_unique_id();
                String userId = HistoryData.get(position).getUser_id();
                String rating = HistoryData.get(position).getRating();

                //Get Package Details
                String packageLength = HistoryData.get(position).getPackage_length();
                String packageDepth = HistoryData.get(position).getPackage_depth();
                String packageEstValue = HistoryData.get(position).getPackage_est_value();
                String is_round_trip = HistoryData.get(position).getIs_round_trip();
                String is_first_round_complete = HistoryData.get(position).getIs_first_round_completed();

                //Get Pickup Location
                String pickupHouse = HistoryData.get(position).getPickup_house_no();
                String pickupStreet = HistoryData.get(position).getPickup_street();
                String pickupLandmark = HistoryData.get(position).getPickup_landmark();
                String pickupAddress = HistoryData.get(position).getPickup_location();
                String pickupLocation = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, pickupAddress);

                //Get DropOff Location
                String dropoffHouse = HistoryData.get(position).getDropoff_house_no();
                String dropoffStreet = HistoryData.get(position).getDropoff_street();
                String dropoffLandmark = HistoryData.get(position).getDropoff_landmark();
                String dropOffAddress = HistoryData.get(position).getDropoff_location();
                String dropLocation = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);

                String userIMage = HistoryData.get(position).getUser_image();

                //Detail Screen
                Intent detailIntent = new Intent(context, BookingDetailActivityNew.class);
                detailIntent.putExtra("IsCancel", holder.tvStatus.getText().toString());
                detailIntent.putExtra("ImagePath", HistoryData.get(position).getUser_image());
                detailIntent.putExtra("Rating", HistoryData.get(position).getRating());
                detailIntent.putExtra("Screen", isPending);
                if (userId != null) {
                    detailIntent.putExtra("UserId", HistoryData.get(position).getUser_id());
                }
                if (userName != null) {
                    detailIntent.putExtra("UserName", HistoryData.get(position).getName());
                }
                if (uniqueID != null) {
                    detailIntent.putExtra("user_unique_id", uniqueID);
                }
                if (HistoryData.get(position).getPackage_width() != null) {
                    detailIntent.putExtra("Width", HistoryData.get(position).getPackage_width());
                }
                if (HistoryData.get(position).getPackage_weight() != null) {
                    detailIntent.putExtra("Weight", HistoryData.get(position).getPackage_weight());
                }
                if (HistoryData.get(position).getPackage_quantity() != null) {
                    detailIntent.putExtra("Quantity", HistoryData.get(position).getPackage_quantity());
                }
                if (HistoryData.get(position).getDelivery_type() != null) {
                    detailIntent.putExtra("deliveryType", HistoryData.get(position).getDelivery_type());
                }
                if (packageLength != null) {
                    detailIntent.putExtra("Length", HistoryData.get(position).getPackage_length());
                }
                if (packageDepth != null) {
                    detailIntent.putExtra("Depth", HistoryData.get(position).getPackage_depth());
                }
                if (packageEstValue != null) {
                    detailIntent.putExtra("EstValue", HistoryData.get(position).getPackage_est_value());
                }
                if (is_round_trip != null) {
                    detailIntent.putExtra("is_round_trip", HistoryData.get(position).getIs_round_trip());
                }
                if (is_first_round_complete != null) {
                    detailIntent.putExtra("is_first_round_complete", HistoryData.get(position).getIs_first_round_completed());
                }
                if (is_first_round_complete != null) {
                    detailIntent.putExtra("is_first_round", HistoryData.get(position).getIs_first_round());
                }
                if (paymentAmt != null) {
                    detailIntent.putExtra("PaymentAmt", "");
                }
                if (currency != null) {
                    detailIntent.putExtra("Currency", "");
                }
                if (paymentAmt != null) {
                    detailIntent.putExtra("PaymentAmt", "");
                }
                if (paymentMethod != null) {
                    detailIntent.putExtra("PaymentMethod", "");
                }
                if (paymentStatus != null) {
                    detailIntent.putExtra("PaymentStatus", "");
                }


                    Log.d("Tagg","---------is_round_trip----------"+is_round_trip);
                    Log.d("Tagg","---------HistoryData.get(position).getIs_first_round()----------"+HistoryData.get(position).getIs_first_round());
                    Log.d("Tagg","---------HistoryData.get(position).getRound_sign_name()----------"+HistoryData.get(position).getRound_sign_name());
                    Log.d("Tagg","---------HistoryData.get(position).getSign_name()----------"+HistoryData.get(position).getSign_name());
                    if(is_round_trip.equals("0")) {
                        if (signedBy != null) {
                            detailIntent.putExtra("SignedBy", HistoryData.get(position).getSign_name());
                        }
                    }else {
                         if (HistoryData.get(position).getIs_first_round().equals("0")) {
                             detailIntent.putExtra("SignedBy", HistoryData.get(position).getRound_sign_name());
                         }else {
                             if (signedBy != null) {
                                 detailIntent.putExtra("SignedBy", HistoryData.get(position).getSign_name());
                             }
                         }
                    }


                    if(is_round_trip.equals("0")) {
                        if (signedImageName != null) {
                            detailIntent.putExtra("SignedImage", HistoryData.get(position).getSignature());
                        }
                    }else{
                        if (HistoryData.get(position).getIs_first_round().equals("0")) {
                            detailIntent.putExtra("SignedImage", HistoryData.get(position).getRound_signature());
                        }else{
                            if (signedImageName != null) {
                                detailIntent.putExtra("SignedImage", HistoryData.get(position).getSignature());
                            }
                        }
                    }

                if (signedBy != null) {
                    detailIntent.putExtra("RoundSignedBy", HistoryData.get(position).getRound_sign_name());
                }
                if (signedImageName != null) {
                    detailIntent.putExtra("RoundSignedImage", HistoryData.get(position).getRound_signature());
                }
                if (packageId != null) {
                    detailIntent.putExtra("packageId", HistoryData.get(position).getPackage_id());
                }
                if (packageCode != null) {
                    detailIntent.putExtra("packageCode", HistoryData.get(position).getPackage_code());
                }
                String reason = HistoryData.get(position).getCancel_reason();
                if (reason != null) {
                    detailIntent.putExtra("cancel_reason", reason);
                }
                detailIntent.putExtra("booking_date_time", HistoryData.get(position).getBooking_pickup_datetime());
                detailIntent.putExtra("booking_type", HistoryData.get(position).getBooking_type());
                detailIntent.putExtra("delivery_type", HistoryData.get(position).getDelivery_type());
                detailIntent.putExtra("delivery_date_time", HistoryData.get(position).getBooking_delivery_datetime());
                detailIntent.putExtra("delivery_session", HistoryData.get(position).getDelivery_session());
                detailIntent.putExtra("isWareHouseDropoff", HistoryData.get(position).getIsWarehouseDropoff());
                detailIntent.putExtra("packageType", HistoryData.get(position).getPackage_type());


                //Check WareHouse Pickup
                if (isWareHousePickup != null) {
                    if (isWareHousePickup.equalsIgnoreCase("true")) {
                        String whPickupAddress = HistoryData.get(position).getWhPickupAddress();
                        detailIntent.putExtra("PickupLocation", whPickupAddress);
                    } else {
                        //User Location Pickup
                        if (pickupLocation != null) {
                            if (HistoryData.get(position).getIs_first_round().equals("0")){
                                detailIntent.putExtra("PickupLocation", pickupLocation);
                            }else {
                                if(HistoryData.get(position).getRound_pickup_location()!=null) {
                                    detailIntent.putExtra("PickupLocation", HistoryData.get(position).getRound_pickup_location());
                                }else{
                                    detailIntent.putExtra("PickupLocation", dropLocation);
                                }
                             }
                        }
                    }
                }

                //Check WareHouse DropOff
                if (isWareHouseDropOff != null) {
                    if (isWareHouseDropOff.equalsIgnoreCase("true")) {
                        String whDropoffAddress = HistoryData.get(position).getWhDropOffAddress();
                        detailIntent.putExtra("DropoffLocation", whDropoffAddress);
                    } else {
                        //User Location DropOff
                        if (dropLocation != null) {
                            if (HistoryData.get(position).getIs_first_round().equals("0")) {
                                detailIntent.putExtra("DropoffLocation", dropLocation);
                            }else{
                                if (HistoryData.get(position).getRound_dropoff_location() != null){
                                    detailIntent.putExtra("DropoffLocation", HistoryData.get(position).getRound_dropoff_location());
                                }else {
                                    detailIntent.putExtra("DropoffLocation", pickupLocation);
                                }
                            }
                        }
                    }
                }

                if (HistoryData.get(position).getBooking_type() != null && HistoryData.get(position).getBooking_type().trim().equalsIgnoreCase("2")) {
                    detailIntent.putExtra("recurring_day", HistoryData.get(position).getRecurring_day());
                    detailIntent.putExtra("recurring_day_time", HistoryData.get(position).getRecurring_day_time());
                    detailIntent.putExtra("recurring_pickup_type", HistoryData.get(position).getRecurring_pickup_type());
                    detailIntent.putExtra("recurring_status", HistoryData.get(position).getRecurring_status());
                    detailIntent.putExtra("recurring_week", HistoryData.get(position).getRecurring_week());
                }
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return HistoryData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvStatus, tvPickupAddress, tvDropoffAddress, tvDateTime, tvPaymentType, tvPackageId;
        LinearLayout rlItem;
        ImageView ivUserImg;
        RatingBar ratingBar;

        public Holder(View itemView) {
            super(itemView);
            rlItem = (LinearLayout) itemView.findViewById(R.id.rl_item);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvPickupAddress = (TextView) itemView.findViewById(R.id.tv_pickup_address);
            tvDropoffAddress = (TextView) itemView.findViewById(R.id.tv_drop_off_address);
            tvDateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            ivUserImg = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            tvPackageId = (TextView) itemView.findViewById(R.id.tv_package_id);

            if (isPending == true) {
                ratingBar.setVisibility(GONE);
            } else {
                ratingBar.setVisibility(VISIBLE);
            }
        }
    }
}
