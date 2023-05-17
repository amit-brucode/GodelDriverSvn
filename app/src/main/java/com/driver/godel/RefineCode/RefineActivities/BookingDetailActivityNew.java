package com.driver.godel.RefineCode.RefineActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.RecurringDetails;

import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BookingDetailActivityNew extends GodelActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView ivUserImg;
    private TextView tvPackageLength, tvPackageEstValue, tvPackageId, tvSignedUserName, tvUserName, tvPickup, tvDropoff, tvPaymentAmt, tvPaymentMethod, tvPaymentStatus, tvTitle, tvHeight, tvWidth, tvWeight, tvQuantity;
    private ImageView ivBack, ivSignature;
    private Bundle bundle;
    private LinearLayout ll_det_visi_length, ll_det_visi_width, ll_det_visi_depth, ll_det_visi_weight, ll_det_visi_quan, ll_det_visi_type, ll_det_visi_value;

    private RatingBar ratingBar;
    private String user_unique_id, isWareHouseDropoff, packageLength, packageDepth, packageEstValue, userId, booking_date_time, booking_type, delivery_type, delivery_date_time, delivery_session, signedName, packageId, packageCode, signedImage, rating, imagePath, isCancel, userName, pickupAddress, dropoffAddress, payment, currency, paymentStatus, paymentMethod, packageHeight, packageWidth, packageWeight, pacakageQuantity,is_first_round,is_first_round_complete,is_round_trip;
    private RelativeLayout rlSigned, rlBookingTiming, rlRecurringDetails;
    private TextView tvPackageStatus, tvPackagePickupTime, tvPackageDeliveryTime, tvRecurringPickupType, tvRecurringPickupDetails, tv_delivery_details_label,tvRecurringDeliveryDetails, tvPackageCode;
    private Boolean isPending = false;
    private String recurringDay, recurringDayTime, recurringPickupType, recurringStatus, recurringWeek, packageType,cancelReason;
    private TextView tvPackageType,tvPackageCancelText,tvPackageCancel;
final String TAG="BOOKINGDETAILS_NEW_LOG";
String language_type;
    View view_booking23;
    TextView tv_bookingType_label,tv_bookingType_status;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
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
        Global global = new Global(BookingDetailActivityNew.this);
        global.setCurrencySymbol();

        //Initialization
        Log.d(TAG,"activitycall");
        view_booking23 = (View) findViewById(R.id.view_booking23);
        tv_bookingType_label = (TextView) findViewById(R.id.tv_bookingType_label);
        tv_bookingType_status = (TextView) findViewById(R.id.tv_bookingType_status);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivUserImg = (ImageView) findViewById(R.id.iv_user_icon);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvPickup = (TextView) findViewById(R.id.tv_pickup_address);
        tvDropoff = (TextView) findViewById(R.id.tv_drop_off_address);
        tvPackageStatus = (TextView) findViewById(R.id.tvPackageStatus);
        tvHeight = (TextView) findViewById(R.id.tv_package_height);
        tvWidth = (TextView) findViewById(R.id.tv_package_width);
        tvWeight = (TextView) findViewById(R.id.tv_package_weight);
        tvQuantity = (TextView) findViewById(R.id.tv_package_quantity);
        tvPaymentAmt = (TextView) findViewById(R.id.tv_payment_amt);
        tvPaymentMethod = (TextView) findViewById(R.id.tv_payment_method);
        tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
        tvPackageId = (TextView) findViewById(R.id.tv_package_id);
        tvPackagePickupTime = (TextView) findViewById(R.id.tv_package_pickup_time);
        tvPackageDeliveryTime = (TextView) findViewById(R.id.tv_package_delivery_time);
        tvRecurringPickupType = (TextView) findViewById(R.id.tv_pickup_type);
        tvRecurringPickupDetails = (TextView) findViewById(R.id.tv_pickup_details);
        tvRecurringDeliveryDetails = (TextView) findViewById(R.id.tv_delivery_details);
        tv_delivery_details_label = (TextView) findViewById(R.id.tv_delivery_details_label);
        tvPackageCode = (TextView) findViewById(R.id.tv_package_code);
        tvPackageType = (TextView) findViewById(R.id.tv_package_type);
        tvPackageCancelText = (TextView) findViewById(R.id.tv_package_cancel_text);
        tvPackageCancel = (TextView) findViewById(R.id.tv_package_cancel);

        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        tvSignedUserName = (TextView) findViewById(R.id.tv_signed_name);
        ivSignature = (ImageView) findViewById(R.id.iv_signature);
        rlSigned = (RelativeLayout) findViewById(R.id.rl_signed);
        rlBookingTiming = (RelativeLayout) findViewById(R.id.rl_booking_timing);
        rlRecurringDetails = (RelativeLayout) findViewById(R.id.rl_recurring_details);
        tvPackageLength = (TextView) findViewById(R.id.tv_package_length);
        tvPackageEstValue = (TextView) findViewById(R.id.tv_package_est_value);


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
        //Set Text
        tvTitle.setText(getResources().getString(R.string.packagedetailstext));

        //Set On Click Listener
        ivBack.setOnClickListener(this);

        //Get Intent
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            isCancel = bundle.getString("IsCancel");
            userId = bundle.getString("UserId");
            userName = bundle.getString("UserName");
            pickupAddress = bundle.getString("PickupLocation");
            dropoffAddress = bundle.getString("DropoffLocation");
            payment = bundle.getString("PaymentAmt");
            paymentMethod = bundle.getString("PaymentMethod");
            paymentStatus = bundle.getString("PaymentStatus");
            packageLength = bundle.getString("Length");
            packageDepth = bundle.getString("Depth");
            packageEstValue = bundle.getString("EstValue");
            packageWidth = bundle.getString("Width");
            packageWeight = bundle.getString("Weight");
            is_first_round_complete = bundle.getString("is_first_round_complete");
            is_first_round = bundle.getString("is_first_round");
            is_round_trip = bundle.getString("is_round_trip");
            pacakageQuantity = bundle.getString("Quantity");
            currency = bundle.getString("Currency");
            imagePath = bundle.getString("ImagePath");
            rating = bundle.getString("Rating");
            signedName = bundle.getString("SignedBy");
            signedImage = bundle.getString("SignedImage");
            packageId = bundle.getString("packageId");
            packageCode = bundle.getString("packageCode");
            booking_date_time = bundle.getString("booking_date_time");
            booking_type = bundle.getString("booking_type");
            delivery_type = bundle.getString("delivery_type");
            delivery_date_time = bundle.getString("delivery_date_time");
            delivery_session = bundle.getString("delivery_session");
            isWareHouseDropoff = bundle.getString("isWareHouseDropoff");
            user_unique_id = bundle.getString("user_unique_id");
            isPending = bundle.getBoolean("Screen");

            recurringDay = bundle.getString("recurring_day");
            recurringDayTime = bundle.getString("recurring_day_time");
            recurringPickupType = bundle.getString("recurring_pickup_type");
            recurringStatus = bundle.getString("recurring_status");
            recurringWeek = bundle.getString("recurring_week");
            packageType = bundle.getString("packageType");
            cancelReason = bundle.getString("cancel_reason");
        }

        if (booking_type != null && booking_type.trim().equalsIgnoreCase("2")) {
            String first = packageCode;
            String next = "<font color='#616161'>Booking Id:</font>";
            tvPackageCode.setText(Html.fromHtml(next + " " + first));
            //Check Recurring Details
            //Hide Pickup
            rlBookingTiming.setVisibility(GONE);
            //Show Recurring Details
            rlRecurringDetails.setVisibility(VISIBLE);
            //Check Recurring Status for Daily Basis
            if (recurringPickupType.equalsIgnoreCase("0")) {
                //Set Recurring Type
                if(is_round_trip.equals("1")){
                    tvRecurringPickupType.setText(getResources().getString(R.string.dailytext)+" (Round Trip)");
                }else {
                    tvRecurringPickupType.setText(getResources().getString(R.string.dailytext));
                }
                //Check Recurring Day Time
                if (recurringDayTime != null) {
                    //Set Recurring Timing
                    tvRecurringPickupDetails.setText(recurringDayTime);
                }
            }
            //Check Recurring Status for Weekly Basis
            else if (recurringPickupType.equalsIgnoreCase("1")) {
                //Set Recurring Type
                if(is_round_trip.equals("1")) {
                    tvRecurringPickupType.setText(getResources().getString(R.string.weeklytext)+" (Round Trip)");
                }else {
                    tvRecurringPickupType.setText(getResources().getString(R.string.weeklytext));
                }
                //Get Recurring Week Day
                int recurringWeekDay = Integer.parseInt(recurringDay);
                //Check Recurring Day Time and Recurring Day
                String dayOfWeek = Global.getPickupDay(recurringWeekDay);
                if (recurringDayTime != null) {
                    //Set Recurring Timing and DayOfWeek
                    tvRecurringPickupDetails.setText(dayOfWeek + " " + recurringDayTime);
                }
            }
            //Check Recurring Status for Monthly Basis
            else if (recurringPickupType.equalsIgnoreCase("2")) {
                //Set Recurring Type
                if(is_round_trip.equals("1")) {
                    tvRecurringPickupType.setText(getResources().getString(R.string.monthlytext)+" (Round Trip)");
                }else {
                    tvRecurringPickupType.setText(getResources().getString(R.string.monthlytext));
                }
                //Get Recurring Week
                //Check Recurring Week and Day Time
                if (recurringWeek != null) {
                    //Set Recurring Week and Day Time
                    tvRecurringPickupDetails.setText(recurringWeek);
                }
            }
            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

             if(delivery_type!=null) {
                 if (delivery_type.equals("0")) {
                     tvRecurringDeliveryDetails.setText("Instant");
                 } else if (delivery_type.equals("1")) {
                     tvRecurringDeliveryDetails.setText("Schedule");
                 } else if (delivery_type.equals("2")) {
                     tvRecurringDeliveryDetails.setText("Standard");
                 } else if (delivery_type.equals("3")) {
                     tvRecurringDeliveryDetails.setText("Express");
                 } else  {
                     tvRecurringDeliveryDetails.setText("Overnight");
                 }
             }else {
                 if (delivery_session != null) {
                     //Check Delivery Session for Morning or Evening
                     if (delivery_session.equalsIgnoreCase("0")) {
                         tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                     } else if (delivery_session.equalsIgnoreCase("1")) {
                         tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                     }
                 }else{
                     tvRecurringDeliveryDetails.setVisibility(GONE);
                     tv_delivery_details_label.setVisibility(GONE);
                 }
             }

                Log.d(TAG,"--------------deliverysession---------11--"+delivery_session);

            }
            else {
                Log.d(TAG,"--------------dis_round_trip-----11--"+is_round_trip);
                if(is_round_trip.equals("1")){
//                    view_booking23.setVisibility(VISIBLE);
//                    tv_bookingType_label.setVisibility(VISIBLE);
//                    tv_bookingType_status.setVisibility(VISIBLE);
//                    Log.d(TAG,"--------------delivery_type----wdscds-----11--"+delivery_type);
//                    if(delivery_type!=null) {
//                        Log.d(TAG,"--------------delivery_type---------11--"+delivery_type);
//                        if (delivery_type.equals("0")) {
//                            tv_bookingType_status.setText("Instant (Round Trip)");
//                        } else if (delivery_type.equals("1")) {
//                            tv_bookingType_status.setText("Schedule (Round Trip)");
//                        } else if (delivery_type.equals("2")) {
//                            tv_bookingType_status.setText("Standard (Round Trip)");
//                        } else if (delivery_type.equals("3")) {
//                            tv_bookingType_status.setText("Express (Round Trip)");
//                        } else {
//                            tv_bookingType_status.setText("Overnight (Round Trip)");
//                        }
//                    }else{
                        view_booking23.setVisibility(GONE);
                        tv_bookingType_label.setVisibility(GONE);
                        tv_bookingType_status.setVisibility(GONE);
//                    }
                }else{
                    view_booking23.setVisibility(GONE);
                    tv_bookingType_label.setVisibility(GONE);
                    tv_bookingType_status.setVisibility(GONE);
                }

                //Check Recuurence Status for Delivery
            if (delivery_session != null) {
                //Check Delivery Session for Morning or Evening
                if (delivery_session.equalsIgnoreCase("0")) {
                    tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                } else if (delivery_session.equalsIgnoreCase("1")) {
                    tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                }
            }else{
                tvRecurringDeliveryDetails.setVisibility(GONE);
                tv_delivery_details_label.setVisibility(GONE);
            }

            }
        } else {
            rlBookingTiming.setVisibility(VISIBLE);
            //Show Recurring Details
            rlRecurringDetails.setVisibility(GONE);
            if (booking_date_time != null) {
                String bookingDateTime = Global.getConvertedDateTime(booking_date_time);
                tvPackagePickupTime.setText(bookingDateTime);
            }
            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)
//            || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                    || preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {

                if(delivery_type!=null) {
                    if (delivery_type.equals("0")) {
                        tvPackageDeliveryTime.setText("Instant");
                    } else if (delivery_type.equals("1")) {
                        tvPackageDeliveryTime.setText("Schedule");
                    } else if (delivery_type.equals("2")) {
                        tvPackageDeliveryTime.setText("Standard");
                    } else if (delivery_type.equals("3")) {
                        tvPackageDeliveryTime.setText("Express");
                    } else  {
                        tvPackageDeliveryTime.setText("Overnight");
                    }

                    Log.d(TAG,"--------------deliverysession---------22--"+delivery_session);

                }else {
                    if (delivery_session != null) {
                        //Check Delivery Session for Morning or Evening
                        if (delivery_session.equalsIgnoreCase("0")) {
                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.morningtext));
                        } else if (delivery_session.equalsIgnoreCase("1")) {
                            tvRecurringDeliveryDetails.setText(getResources().getString(R.string.eveningtext));
                        }
                    }else{
                        tvRecurringDeliveryDetails.setVisibility(GONE);
                        tv_delivery_details_label.setVisibility(GONE);
                    }
                }
                if (delivery_date_time != null) {

                    if (isWareHouseDropoff.equalsIgnoreCase("true")) {
                        //Hide Signatures
                        rlSigned.setVisibility(View.GONE);
                        String bookingDateTime = Global.getConvertedDateTime(booking_date_time);
                        if(delivery_type!=null) {
                            if (delivery_type.equals("0")) {
                                tvPackageDeliveryTime.setText("Instant");
                            } else if (delivery_type.equals("1")) {
                                tvPackageDeliveryTime.setText("Schedule");
                            } else if (delivery_type.equals("2")) {
                                tvPackageDeliveryTime.setText("Standard");
                            } else if (delivery_type.equals("3")) {
                                tvPackageDeliveryTime.setText("Express");
                            } else  {
                                tvPackageDeliveryTime.setText("Overnight");
                            }
                        }else {
                        tvPackageDeliveryTime.setText(bookingDateTime);}
                    } else {
                        String bookingDateTime = Global.getConvertedDateTime(delivery_date_time);
                        if(delivery_type!=null) {
                            if (delivery_type.equals("0")) {
                                tvPackageDeliveryTime.setText("Instant");
                            } else if (delivery_type.equals("1")) {
                                tvPackageDeliveryTime.setText("Schedule");
                            } else if (delivery_type.equals("2")) {
                                tvPackageDeliveryTime.setText("Standard");
                            } else if (delivery_type.equals("3")) {
                                tvPackageDeliveryTime.setText("Express");
                            } else  {
                                tvPackageDeliveryTime.setText("Overnight");
                            }
                        }else {
                        tvPackageDeliveryTime.setText(bookingDateTime);}
                    }
                }

                Log.d(TAG,"--------------deliverysession---------33--"+delivery_session);

            }
            else {
                Log.d(TAG,"--------------dis_round_trip-----11--"+is_round_trip);

                if (delivery_date_time != null) {

                    if (isWareHouseDropoff.equalsIgnoreCase("true")) {
                        //Hide Signatures
                        rlSigned.setVisibility(View.GONE);
                        if(is_round_trip.equals("1")) {
                            String bookingDateTime = Global.getConvertedDateTime(delivery_date_time);
                            view_booking23.setVisibility(GONE);
                            tv_bookingType_label.setVisibility(GONE);
                            tv_bookingType_status.setVisibility(GONE);
                            if(delivery_type!=null) {
                                if (delivery_type.equals("0")) {
                                    tvPackageDeliveryTime.setText("Instant (Round Trip)");
                                } else if (delivery_type.equals("1")) {
                                    tvPackageDeliveryTime.setText("Schedule (Round Trip)");
                                } else if (delivery_type.equals("2")) {
                                    tvPackageDeliveryTime.setText("Standard (Round Trip)");
                                } else if (delivery_type.equals("3")) {
                                    tvPackageDeliveryTime.setText("Express (Round Trip)");
                                } else {
                                    tvPackageDeliveryTime.setText("Overnight (Round Trip)");
                                }
                            }else{
                                tvPackageDeliveryTime.setText(bookingDateTime);
                            }

                        }else {
                            String bookingDateTime = Global.getConvertedDateTime(booking_date_time);
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
                                tvPackageDeliveryTime.setText(bookingDateTime);
                            }
                        }
                    } else {
                        if(is_round_trip.equals("1")) {
                            String bookingDateTime = Global.getConvertedDateTime(delivery_date_time);
                            view_booking23.setVisibility(GONE);
                            tv_bookingType_label.setVisibility(GONE);
                            tv_bookingType_status.setVisibility(GONE);
                            if(delivery_type!=null) {
                            if (delivery_type.equals("0")) {
                                tvPackageDeliveryTime.setText("Instant (Round Trip)");
                            } else if (delivery_type.equals("1")) {
                                tvPackageDeliveryTime.setText("Schedule (Round Trip)");
                            } else if (delivery_type.equals("2")) {
                                tvPackageDeliveryTime.setText("Standard (Round Trip)");
                            } else if (delivery_type.equals("3")) {
                                tvPackageDeliveryTime.setText("Express (Round Trip)");
                            } else {
                                tvPackageDeliveryTime.setText("Overnight (Round Trip)");
                            }
                        }else{
                                tvPackageDeliveryTime.setText(bookingDateTime);
                            }

                        }else{
                            String bookingDateTime = Global.getConvertedDateTime(delivery_date_time);
                            if(delivery_type!=null) {
                                if (delivery_type.equals("0")) {
                                    tvPackageDeliveryTime.setText("Instant");
                                } else if (delivery_type.equals("1")) {
                                    tvPackageDeliveryTime.setText("Schedule");
                                } else if (delivery_type.equals("2")) {
                                    tvPackageDeliveryTime.setText("Standard");
                                } else if (delivery_type.equals("3")) {
                                    tvPackageDeliveryTime.setText("Express");
                                } else  {
                                    tvPackageDeliveryTime.setText("Overnight");
                                }
                            }else {
                                tvPackageDeliveryTime.setText(bookingDateTime);
                            }
                            view_booking23.setVisibility(GONE);
                            tv_bookingType_label.setVisibility(GONE);
                            tv_bookingType_status.setVisibility(GONE);
                        }

                    }
                }
            }
        }

        if (packageCode != null) {
            String first = packageCode;
            String next = "<font color='#616161'>Booking Id:</font>";
            tvPackageId.setText(Html.fromHtml(next + " " + first));
        }

        if (packageType != null && packageType.length() > 0 && packageType.trim().equalsIgnoreCase("1")) {
            tvPackageType.setText(getResources().getString(R.string.documentetxt));
        } else {
            tvPackageType.setText(getResources().getString(R.string.othertext));
        }

        //Set TextView PendingData
        if (userName != null) {
            //tvUserName.setText(userName);
        }

        if (pickupAddress != null) {
            tvPickup.setText(pickupAddress);
        }

        if (dropoffAddress != null) {
            tvDropoff.setText(dropoffAddress);
        }

        if (packageDepth != null) {
            CharSequence charSequence = Global.getPackageCm(packageDepth);
            tvHeight.setText(charSequence);
        }

        if (packageWidth != null) {
            CharSequence charSequence = Global.getPackageCm(packageWidth);
            tvWidth.setText(charSequence);
        }

        if (packageWeight != null) {
            CharSequence charSequence = Global.getPackageKg(packageWeight);
            tvWeight.setText(charSequence);
        }

        if (packageLength != null) {
            CharSequence charSequence = Global.getPackageCm(packageLength);
            tvPackageLength.setText(charSequence);
        }

        if (packageEstValue != null) {
            String estValue = Global.formatValue(packageEstValue);
            String currency = "<font color='#939393'>"+Global.CurrencySymbol+"</font>";
            tvPackageEstValue.setText(Html.fromHtml(estValue + " " + currency));
        }

        if (pacakageQuantity != null) {
            CharSequence charSequence = Global.getPackageItems(pacakageQuantity);
            tvQuantity.setText(charSequence);
        }

        if (payment != null) {
            tvPaymentAmt.setText(currency + " " + payment);
        }

        if (paymentStatus != null) {
            tvPaymentStatus.setText(paymentStatus);
        }

        //Set Rating
        if (rating != null) {
            //ratingBar.setRating(Float.parseFloat(rating));
        }

        //Check Status
        if (isCancel != null && isCancel.toString().trim().equals("Canceled")) {
            tvPackageStatus.setText(isCancel);
        } else {
            tvPackageStatus.setText(isCancel);
        }

        //payment method
        if (paymentMethod != null) {
            if (paymentMethod.equalsIgnoreCase("0")) {
                tvPaymentMethod.setText(getResources().getString(R.string.cardpayment));
            } else if (paymentMethod.equalsIgnoreCase("1")) {
                tvPaymentMethod.setText(getResources().getString(R.string.walletpayment));
            }
        }

        //Signed By
        if (signedName == null && signedImage == null) {
            rlSigned.setVisibility(View.GONE);
        } else {
            if (signedName != null) {
                tvSignedUserName.setText(signedName);
            }

            if (signedImage != null) {
                //Load Signature Image
                String countrucode=CountryCodeCheck.countrycheck(BookingDetailActivityNew.this);

                String signImagePath = Global.BASE_URL + "backend/public/uploads/"+countrucode+"/signature/" + signedImage;
               Log.d(TAG,"Sign in image"+signImagePath);
                Glide.with(BookingDetailActivityNew.this).load(signImagePath).placeholder(R.drawable.sign_image_placeholder).dontAnimate().into(ivSignature);
            }
        }

        if (cancelReason!=null && cancelReason.length()>0){
            tvPackageCancelText .setVisibility(VISIBLE);
            tvPackageCancel .setVisibility(VISIBLE);
            tvPackageCancelText.setText(cancelReason);
        }
        else{
            tvPackageCancelText.setVisibility(GONE);
            tvPackageCancel.setVisibility(GONE);
        }

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
//                    Locale myLocale = new Locale("en");
//                    Resources res = getResources();
//                    DisplayMetrics dm = res.getDisplayMetrics();
//                    Configuration conf = res.getConfiguration();
//                    conf.locale = myLocale;
//                    res.updateConfiguration(conf, dm);
//
//
//                }
//
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //Back Press
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
