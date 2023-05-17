package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class PassPackageActivity extends GodelActivity implements View.OnClickListener {

    static Activity activitycon;
    String TAG = "PASSPACKAGE_LOG";
    Context context;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String packageId;
    TextView tvPackageId, tvUserName, tvPickupAddress, tvDropOffAddress, tvDateTime, tvPickupTiming, tvDropoffTiming, tvName, tv_email_label, tvEmail, tvPhone;
    TextView tvAddressNote, tvLength, tvWidth, tvDepth, tvQuantity, tvEstValue, tvWeight, tvStatusLabel, tvStatus, tvUniqueID;
    ImageView ivUser;
    RatingBar ratingBar;
    CardView cardView;
    TextView tvRecurringType, tvRecurringPickup, tvRecurringDelivery, tvPickupNote, tvDropOffNote;
    RelativeLayout rlPickupDeliveryTiming, rlRecurringPickupDelivery, rlAddressNote;
    View view_contact1;
    String exactPickupAddress, exactDropoffAddress;
    private ProgressDialog PckgPrgsDlg;
    private Call<GetPackageResponse> getPackageResponseCall;


    // open scanner for scan package
    public static void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(activitycon);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode for package");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_package);

        activitycon = PassPackageActivity.this;
        context = PassPackageActivity.this;

        Intent intent = getIntent();
        packageId = intent.getStringExtra("PackageCode");

        preferences = getSharedPreferences(SharedValues.PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);


        //Set Title
        tvTitle.setText(getResources().getString(R.string.pass_on_package));
        //Set On Click Listener
        ivBack.setOnClickListener(this);

        view_contact1 = findViewById(R.id.view_customer_detail2);
        tvPackageId = findViewById(R.id.tv_package_id);
        tvStatus = findViewById(R.id.tv_status);
        tvUserName = findViewById(R.id.tv_user_name);
        tvPickupAddress = findViewById(R.id.tv_pickup_address);
        tvDropOffAddress = findViewById(R.id.tv_drop_off_address);
        tvDateTime = findViewById(R.id.tv_date_time);
        ivUser = findViewById(R.id.iv_user_icon);
        ratingBar = findViewById(R.id.ratingbar);
        cardView = findViewById(R.id.cardview_history);
        tvPickupTiming = findViewById(R.id.tv_pickup_timing);
        tvDropoffTiming = findViewById(R.id.tv_dropoff_timing);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tv_email_label = findViewById(R.id.tv_email_label);
        tvPhone = findViewById(R.id.tv_phone);
        tvLength = findViewById(R.id.tv_length);
        tvWidth = findViewById(R.id.tv_width);
        tvDepth = findViewById(R.id.tv_depth);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvEstValue = findViewById(R.id.tv_est_value);
        tvWeight = findViewById(R.id.tv_weight);
        tvStatusLabel = findViewById(R.id.tv_status_txt);
        tvStatus = findViewById(R.id.tv_status);
        rlPickupDeliveryTiming = findViewById(R.id.rl_pickup_delivery_timing);
        rlRecurringPickupDelivery = findViewById(R.id.rl_recurring_pickup_delivery_timing);
        tvRecurringType = findViewById(R.id.tv_recurring_type);
        tvRecurringPickup = findViewById(R.id.tv_recurring_pickup_timing);
        tvRecurringDelivery = findViewById(R.id.tv_recurring_delivery_timing);
        rlAddressNote = findViewById(R.id.rl_address_note);
        tvAddressNote = findViewById(R.id.tv_address_note);
        tvPickupNote = findViewById(R.id.tv_pickup_address_note);
        tvDropOffNote = findViewById(R.id.tv_dropoff_address_note);
        tvUniqueID = findViewById(R.id.tv_user_unique);

        getPackageDetail(packageId);
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

            Intent mapIntent = new Intent(PassPackageActivity.this, MyPackagesActivity.class);
            startActivity(mapIntent);
            finish();
    }

//    //scanner result
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//            } else {
//                Toast.makeText(context, "result.getContents()" + result.getContents(), Toast.LENGTH_SHORT).show();
////                if (OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("Barcode")) {
////                    String scanPackageCode = result.getContents();
////                    getPackageDetail(scanPackageCode);
////                } else if ((OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("delivery"))) {
////                    getPackageDetail(result.getContents());
////                } else if (OngoingFragment.click != null && OngoingFragment.click.equalsIgnoreCase("Sticker")) {
////                    showAlert(getResources().getString(R.string.doyourealluwantto) + " " + result.getContents() + " sticker ?", result.getContents());
////                }
//                packageId = result.getContents();
//                getPackageDetail(packageId);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    private void getPackageDetail(String pacakgeCode) {

        if (getPackageResponseCall != null) {
            getPackageResponseCall.cancel();
        }
        if (!(PassPackageActivity.this.isFinishing())) {
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

                if (!(PassPackageActivity.this.isFinishing())) {
                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                        PckgPrgsDlg.dismiss();
                    }
                }
                Global.signInIntent(this);

            } else {
                String country_code = CountryCodeCheck.countrycheck(PassPackageActivity.this);
                if (country_code != null && country_code.trim().length() > 0) {

                    Log.d(TAG, "-----country_code------------" + country_code);
                    Log.d(TAG, "-----Global.USER_AGENT------------" + Global.USER_AGENT);
                    Log.d(TAG, "-----Accept------------" + Accept);
                    Log.d(TAG, "-----USER_TOKEN------------" + USER_TOKEN);
                    Log.d(TAG, "-----pacakgeCode------------" + pacakgeCode);
                    Log.d(TAG, "-----driverId------------" + driverId);

                    getPackageResponseCall = webRequest.apiInterface.ScangetPacakgeDetail(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, pacakgeCode, driverId);
                    getPackageResponseCall.enqueue(new Callback<GetPackageResponse>() {
                        @Override
                        public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {

                            Log.d(TAG, "-----response------------" + response);
                            Log.d(TAG, "-----response.errorBody()------------" + response.errorBody());
                            Log.d(TAG, "-----response.isSuccessful()------------" + response.isSuccessful());
                            Log.d(TAG, "-----response.message()------------" + response.message());
                            Log.d(TAG, "-----response.body()------------" + response.body());

                            try {
                                if (!(PassPackageActivity.this.isFinishing())) {
                                    if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                                        PckgPrgsDlg.dismiss();
                                    }
                                }
                            } catch (Exception ex) {
                            }

                            if (response.isSuccessful()) {
                                GetPackageResponse packageResponse = response.body();
                                if (packageResponse != null) {
                                    Log.d(TAG, "-----packageResponse.getResponse()------------" + packageResponse.getResponse());

                                    if (packageResponse.getResponse() == 1) {

                                        tvPackageId.setText(packageResponse.getData().getPackage_details().getPackage_code());

                                        //Get User Details
                                        String country_code = CountryCodeCheck.countrycheck(context);
                                        String countryCode = packageResponse.getData().getUser_details().getCountry_code();
                                        String userType = packageResponse.getData().getUser_details().getType();
                                        String minDeliveryTime = packageResponse.getData().getMin_delivery_time();
                                        String deliveryType = packageResponse.getData().getBooking_details().getDelivery_type();
                                        String bookingType = packageResponse.getData().getBooking_details().getBooking_type();
                                        String bookingPicupDate = packageResponse.getData().getBooking_details().getBooking_pickup_datetime();
                                        String userId = packageResponse.getData().getBooking_details().getUser_id();

                                        String userUniqueID = packageResponse.getData().getUser_details().getUser_unique_id();
                                        String name = packageResponse.getData().getUser_details().getName();
                                        String email = packageResponse.getData().getUser_details().getUser_email();
                                        String phone = packageResponse.getData().getUser_details().getUser_phone();

                                        String pickupTime = packageResponse.getData().getBooking_details().getBooking_pickup_datetime();
                                        String deliveryTime = packageResponse.getData().getBooking_details().getBooking_delivery_datetime();
                                        String dropOffAddress = packageResponse.getData().getLocation_details().getDropoff_location();

                                        //Get Location Details
                                        String dropoffHouse = packageResponse.getData().getLocation_details().getDropoff_house_no();
                                        String dropoffStreet = packageResponse.getData().getLocation_details().getDropoff_street();
                                        String dropoffLandmark = packageResponse.getData().getLocation_details().getDropoff_landmark();
                                        dropOffAddress = packageResponse.getData().getLocation_details().getDropoff_location();
                                        exactDropoffAddress = Global.getExactAddress(dropoffHouse, dropoffStreet, dropoffLandmark, dropOffAddress);

                                        //Get Pickup Address
                                        String pickupHouse = packageResponse.getData().getLocation_details().getPickup_house_no();
                                        String pickupStreet = packageResponse.getData().getLocation_details().getPickup_street();
                                        String pickupLandmark = packageResponse.getData().getLocation_details().getPickup_landmark();
                                        final String pickupAddress = packageResponse.getData().getLocation_details().getPickup_location();
                                        exactPickupAddress = Global.getExactAddress(pickupHouse, pickupStreet, pickupLandmark, pickupAddress);

                                        //Set Pickup & DropOff Address
                                        tvPickupAddress.setText(exactPickupAddress);
                                        tvDropOffAddress.setText(exactDropoffAddress);

                                        //Hide Status Label and status
                                        tvStatusLabel.setVisibility(View.GONE);
                                        tvStatus.setVisibility(View.GONE);

                                        //Get Package Details
                                        String packageLength = packageResponse.getData().getPackage_details().getPackage_length();
                                        String packageDepth = packageResponse.getData().getPackage_details().getPackage_depth();
                                        String packageEstValue = packageResponse.getData().getPackage_details().getPackage_est_value();
                                        String packageWidth = packageResponse.getData().getPackage_details().getPackage_width();
                                        String packageWeight = packageResponse.getData().getPackage_details().getPackage_weight();
                                        String packageQuantity = packageResponse.getData().getPackage_details().getPackage_quantity();

                                        final String packageCode = packageResponse.getData().getPackage_details().getPackage_code();
                                        final String packageStatus = packageResponse.getData().getPackage_details().getPackage_status();

                                        //Get Address Note
                                        String addressNote = packageResponse.getData().getLocation_details().getAddress_note();

                                        //Show Address Note
                                        if (addressNote != null) {
                                            //Show Address Bar
                                            rlAddressNote.setVisibility(View.VISIBLE);

                                            if (addressNote != null || addressNote.length() > 0) {
                                                Log.d("AddressNote_Log", addressNote);
                                                String def[] = addressNote.split(Global.AddressNoteSeprator);
                                                for (int i = 0; i < def.length; i++) {
                                                    if (def[i] != null || def[i].length() > 0) {
                                                        if (i == 0) {
                                                            Log.d("AddressNote_Log", "" + def[i]);
                                                            tvPickupNote.setText(def[i]);
                                                        }
                                                        if (i == 1) {
                                                            Log.d("AddressNote_Log", "" + def[i]);
                                                            //String dropOffNote = def[i].replaceAll(Global.AddressNoteSeprator, "");
                                                            tvDropOffNote.setText(def[i].replaceAll(Global.AddressNoteSeprator, ""));
                                                        }
                                                    }
                                                }
                                            }

                                            //Show Address
                                            //holder.tvAddressNote.setText(addressNote);
                                        }
                                        if (packageLength != null) {
                                            if (packageLength.equals("0")) {
                                                tvLength.setText(" ");
                                            } else {
                                                CharSequence charSequence = Global.getPackageCm(packageLength);
                                                tvLength.setText(charSequence);
                                            }
                                        } else {
//                                            tvWidth.setText("");
                                            tvWidth.setText("NA");
                                        }

                                        //Set Package Width
                                        if (packageWidth != null) {
                                            if (packageWidth.equals("0")) {
                                                tvWidth.setText(" ");
                                            } else {
                                                CharSequence charSequence = Global.getPackageCm(packageWidth);
                                                tvWidth.setText(charSequence);
                                            }
                                        } else {
                                            tvWidth.setText("NA");
//                                            tvWidth.setText("");
                                        }

                                        //Set Package Depth
                                        if (packageDepth != null) {
                                            if (packageDepth.equals("0")) {
                                                tvDepth.setText(" ");
                                            } else {

                                                CharSequence charSequence = Global.getPackageCm(packageDepth);
                                                tvDepth.setText(charSequence);
                                            }
                                        } else {
//                                            tvDepth.setText("");
                                            tvDepth.setText("NA");
                                        }

                                        //Set Package Weight
                                        if (packageWeight != null) {
                                            CharSequence charSequence = Global.getPackageKg(packageWeight);
                                            tvWeight.setText(charSequence);
                                        } else {
                                            tvWeight.setText("");
                                        }

                                        //Set Package Quantity
                                        if (packageQuantity != null) {
                                            CharSequence charSequence = Global.getPackageItems(packageQuantity);
                                            tvQuantity.setText(charSequence);
                                        } else {
                                            tvQuantity.setText("");
                                        }

                                        //Set Package Est. Value
                                        if (packageEstValue != null) {
                                            String estValue = Global.formatValue(packageEstValue);
                                            String currency = "<font color='#939393'>" + Global.CurrencySymbol + "</font>";
                                            tvEstValue.setText(Html.fromHtml(estValue + " " + currency));
//          CharSequence charSequence = Global.getPackageValue(packageEstValue);
                                        } else {
                                            tvEstValue.setText("");
                                        }

                                        if (userUniqueID != null || !userUniqueID.isEmpty()) {
                                            tvUniqueID.setText(userUniqueID);
                                        } else {
                                            tvUniqueID.setText("");
                                        }
                                        //Set User Name
                                        if (name == null || name.isEmpty()) {
                                            //holder.tvName.setText("NA");
                                        } else {
                                            tvName.setText(name);
                                        }

                                        //Set User Email
                                        if (email == null || email.isEmpty()) {
                                            tvEmail.setText("");
//            holder.tvEmail.setText("NA");
                                        } else {
                                            if (email.equals("nomail@nomail.com")) {
                                                tvEmail.setVisibility(GONE);
                                                tv_email_label.setVisibility(GONE);
                                                view_contact1.setVisibility(GONE);
//            tv_Email.setHeight(0);
                                            } else {
                                                tvEmail.setText(email);
                                            }
                                        }

                                        //Set User Phone
                                        if (phone == null || phone.isEmpty()) {
                                            tvPhone.setText("");
                                        } else {
                                            if (userType.equals("1") || userType.equals("0")) {
                                                if (phone.startsWith("+255")) {
//                    Log.d("User_TYPE","-----1---255----user type-----"+userType);
                                                    tvPhone.setText(Global.formatValueNumber(phone));
                                                } else if (phone.startsWith("+254")) {
//                    Log.d("User_TYPE","-----1---255----user type-----"+userType);
                                                    tvPhone.setText(Global.formatValueNumber(phone));
                                                } else if (phone.startsWith("+256")) {
//                    Log.d("User_TYPE","---1--256---user type-----"+userType);
                                                    tvPhone.setText(Global.formatValueNumber(phone));
                                                } else if (phone.startsWith("+800")) {
                                                    tvPhone.setText(Global.formatValueNumber(phone));
                                                } else if (phone.startsWith("+91")) {
//                    Log.d("User_TYPE","----1--91--user type-----"+userType);
                                                    tvPhone.setText(Global.formatValueNumber2(phone));
                                                } else if (phone.startsWith("+44")) {
                                                    tvPhone.setText(Global.formatValueNumber2(phone));
                                                } else {
                                                    tvPhone.setText(Global.formatValueNumber(phone));
                                                }
                                            } else if (userType.equals("2")) {
//                Log.d("User_TYPE","-----2---user type-----"+userType);
                                                tvPhone.setText(countryCode + " " + phone);
                                            } else if (userType.equals("4")) {
//                Log.d("User_TYPE","-----4---user type-----"+userType);
                                                tvPhone.setText(countryCode + " " + phone);
                                            } else {
                                                tvPhone.setText(phone);
                                            }

                                        }

//                                        //Check Booking Type
//                                        if (bookingType != null) {
//                                            //Check for Recurrence booking
//                                            if (bookingType.equalsIgnoreCase("2")) {
//                                                //Hide Visibility of Pickup and Timing
//                                                rlPickupDeliveryTiming.setVisibility(View.GONE);
//
//                                                //Show Visibility of Pickup and Timing
//                                                rlRecurringPickupDelivery.setVisibility(View.VISIBLE);
//
//                                                //Check Recurring Status for Daily Basis
//                                                if (packageResponse.getData().getBooking_details().getRecurring_pickup_type().equalsIgnoreCase("0")) {
//                                                    //Set Recurring Type
//                                                    tvRecurringType.setText(context.getResources().getString(R.string.dailytext));
//
//                                                    //Get Recurring Day Time
//                                                    String recurringDayTime = pendingPickupBeans.get(position).getRecurring_day_time();
//
//                                                    //Check Recurring Day Time
//                                                    if (recurringDayTime != null) {
//                                                        //Set Recurring Timing
//                                                        tvRecurringPickup.setText(recurringDayTime);
//                                                    }
//                                                }
////                                                Check Recurring Status for Weekly Basis
//                                                else
//                                                if (pendingPickupBeans.get(position).getRecurring_pickup_type().equalsIgnoreCase("1")) {
//                                                    //Set Recurring Type
//                                                    holder.tvRecurringType.setText(context.getResources().getString(R.string.weeklytext));
//
//                                                    //Get Recurring Week Day
//                                                    int recurringWeekDay = Integer.parseInt(pendingPickupBeans.get(position).getRecurring_day());
//
//                                                    //Get Recurring Day Time
//                                                    String recurringDayTime = pendingPickupBeans.get(position).getRecurring_day_time();
//
//                                                    //Check Recurring Day Time and Recurring Day
//                                                    String dayOfWeek = Global.getPickupDay(recurringWeekDay);
//
//                                                    if (recurringDayTime != null) {
//                                                        //Set Recurring Timing and DayOfWeek
//                                                        holder.tvRecurringPickup.setText(dayOfWeek + " " + recurringDayTime);
//                                                    }
//                                                }
////                                                Check Recurring Status for Monthly Basis
//                                                else
//                                                if (pendingPickupBeans.get(position).getRecurring_pickup_type().equalsIgnoreCase("2")) {
//                                                    //Set Recurring Type
//                                                    holder.tvRecurringType.setText(context.getResources().getString(R.string.monthlytext));
//
//                                                    //Get Recurring Week
//                                                    String recurringWeek = pendingPickupBeans.get(position).getRecurring_week();
//
//
//                                                    //Check Recurring Week and Day Time
//                                                    if (recurringWeek != null) {
//                                                        //Set Recurring Week and Day Time
//                                                        holder.tvRecurringPickup.setText(recurringWeek);
//                                                    }
//                                                }
//                                                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//
//                                                    if (deliveryType != null) {
//                                                        if (deliveryType.equals("0")) {
//                                                            tvRecurringDelivery.setText("Instant");
//                                                        } else if (deliveryType.equals("1")) {
//                                                            tvRecurringDelivery.setText("Schedule");
//                                                        } else if (deliveryType.equals("2")) {
//                                                            tvRecurringDelivery.setText("Standard");
//                                                        } else if (deliveryType.equals("3")) {
//                                                            tvRecurringDelivery.setText("Express");
//                                                        } else {
//                                                            tvRecurringDelivery.setText("Overnight");
//                                                        }
//                                                    } else {
//                                                        if (packageResponse.getData().getBooking_details().getDelivery_session() != null) {
//                                                            String deliverySession = packageResponse.getData().getBooking_details().getDelivery_session();
//
//                                                            //Check Delivery Session for Morning or Evening
//                                                            if (deliverySession.equalsIgnoreCase("0")) {
//                                                                tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
//                                                            } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                                tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
//                                                            }
//                                                        }
//                                                    }
//                                                } else {
//
//                                                    //Check Recuurence Status for Delivery
//                                                    if (packageResponse.getData().getBooking_details().getDelivery_session() != null) {
//                                                        String deliverySession = packageResponse.getData().getBooking_details().getDelivery_session();
//
//                                                        //Check Delivery Session for Morning or Evening
//                                                        if (deliverySession.equalsIgnoreCase("0")) {
//                                                            tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
//                                                        } else if (deliverySession.equalsIgnoreCase("1")) {
//                                                            tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
//                                                        }
//                                                    }
//
//                                                }
////                                            Check Recuurence Status for Delivery
////                if (pendingPickupBeans.get(position).getDelivery_session() != null) {
////                    String deliverySession = pendingPickupBeans.get(position).getDelivery_session();
////
////                    //Check Delivery Session for Morning or Evening
////                    if (deliverySession.equalsIgnoreCase("0")) {
////                        holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.morningtext));
////                    } else if (deliverySession.equalsIgnoreCase("1")) {
////                        holder.tvRecurringDelivery.setText(context.getResources().getString(R.string.eveningtext));
////                    }
////                }
//                                            } else {
//                                                //Hide Visibility of Pickup and Timing
//                                                rlRecurringPickupDelivery.setVisibility(View.GONE);
//                                                //Show Visibility of Pickup and Timing
//                                                rlPickupDeliveryTiming.setVisibility(View.VISIBLE);
//                                                //Set Booking Pickup Time
//                                                if (pickupTime != null) {
//                                                    if (bookingType != null && bookingType.trim().equalsIgnoreCase("0")) {
//                                                        String packagePickupTime = Global.getConvertedDateTime(pickupTime);
//                                                        tvPickupTiming.setText(packagePickupTime.replaceAll("00:00:00", ""));
//                                                    } else {
//                                                        String packagePickupTime = Global.getConvertedPickupDateTime(pickupTime);
//                                                        tvPickupTiming.setText(packagePickupTime.replaceAll("00:00:00", ""));
//                                                    }

                                                } else {
//                                        Log.d(TAG,"-----packageResponse.getResponse()-------else------"+packageResponse.getResponse());
//                                        Log.d(TAG,"-----packageResponse.getResponse()-------else------"+response.body().getData().getMessage());
//                                        Log.d(TAG,"-----packageResponse.getResponse()-------else-----"+package
                                                    Toast.makeText(context, "" + packageResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
//                                }
//                            }
//                        }

                        @Override
                        public void onFailure(Call<GetPackageResponse> call, Throwable t) {

                            Log.d(TAG, "-----packageResponse.getResponse()-------failure---t.getStackTrace()---" + t.getStackTrace());
                            Log.d(TAG, "-----packageResponse.getResponse()-------failure---t.getMessage()---" + t.getMessage());
                            Log.d(TAG, "-----packageResponse.getResponse()-------failure---t.getCause()---" + t.getCause());
                            Log.d(TAG, "-----packageResponse.getResponse()-------failure---t.getLocalizedMessage()---" + t.getLocalizedMessage());

                            try {
                                if (!(PassPackageActivity.this.isFinishing())) {
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
                    if (!(PassPackageActivity.this.isFinishing())) {
                        if (PckgPrgsDlg != null && PckgPrgsDlg.isShowing()) {
                            PckgPrgsDlg.dismiss();
                        }
                    }
                }
            }
        }
    }



}
