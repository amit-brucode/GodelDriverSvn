package com.driver.godel.RefineCode.RefineUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;

import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineActivities.MyPackagesActivity;
import com.driver.godel.RefineCode.RefineActivities.Selected_Country;
import com.driver.godel.RefineCode.RefineActivities.SignIn;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.RefineCode.Service.ForegroundService;
import com.driver.godel.RefineCode.Service.UpdateDriverLocationServiceNew;
import com.driver.godel.response.ForgotPaswdResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ajay2.Sharma on 7/9/2018.
 */

public class Global {
    Context context;

    public Global(Context context) {
        this.context = context;

    }

    public static float MAP_ZOOM = 14;
    public static String APK_VERSION = "driver_refine_live_v7.8";
    public static String VersionCode = "73";

    //production
    public static final String BASE_URL = "https://www.go-del.com/";

    //development
//    public static String BASE_URL = "https://sandbox.go-del.com/";

    public static String API_URL = BASE_URL + "backend/api/v2/";

    //    public static String PROFILE_PIC_URL = BASE_URL + "backend/public/uploads/driver_profile/";
    public static String PROFILE_PIC_URL = BASE_URL + "backend/public/uploads/";

    //    public static String PRIVACY_POLICY = BASE_URL + "m-privacy-policy";
    public static String PRIVACY_POLICY = BASE_URL;
    //    public static String TERMS_CONDITIONS = BASE_URL + "m-terms";
    public static String TERMS_CONDITIONS = BASE_URL;

    public static String GOOGLE_API_WEB_KEY = "AIzaSyD0GSLzg6t4sQwc-2RkKJaZYYihVMkBN6c";

    public static String ACCEPT = "application/json";
    public static String USER_AGENT = "Com AndroidDriver";
    public static String ONGOING_BROADCAST = "onGoingBroadcast";
    public static String PICKUP_BROADCAST = "pickupBroadcast";
    public static String DELIVERY_BROADCAST = "deliveryBroadcast";
    public static String INACTIVE_BROADCAST = "inactive_broadcast";
    public static String SERVICE_ERROR_BROADCAST = "service_error_broadcast";

    public static String DISPLAY_PACKAGE_LENGTH = "display_package_length";
    public static String DISPLAY_PACKAGE_WIDTH = "display_package_width";
    public static String DISPLAY_PACKAGE_DEPTH = "display_package_depth";
    public static String DISPLAY_PACKAGE_WEIGHT = "display_package_weight";
    public static String DISPLAY_PACKAGE_QUANTITY = "display_package_quantity";
    public static String DISPLAY_PACKAGE_VALUE = "display_package_value";
    public static String DISPLAY_PACKAGE_TYPE = "display_package_type";
    public static String DISPLAY_PACKAGE_BRIEF_DESCRIPTION_OF_GOODS = "display_brief_description_of_goods";
    public static String DISPLAY_PACKAGE_VAT = "display_VAT";
    public static String DISPLAY_PACKAGE_UCC = "display_UCC";
    public static String DISPLAY_PACKAGE_INSURANCE = "display_insurance";
    public static String DISPLAY_PACKAGE_LOADER = "display_loader";
    public static String DISPLAY_PACKAGE_INSURANCE_COVER = "display_insurance_cover";


    //Address Note
    public static String AddressNoteSeprator = "##&&##";
    public static String CurrencySymbol = "";

    public void setCurrencySymbol() {
         SharedPreferences preferences = context.getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        String name = preferences.getString(SharedValues.SELECTED_COUNTRY, "");
        if (name != null) {
            if (name.trim().length() > 0) {
                CurrencySymbol=preferences.getString(SharedValues.CurrencySymbolSHARED,"");
//                if (name.equals(SharedValues.SELECTED_COUNTRY_UGANDA)) {
//                    CurrencySymbol = "UGX";
//                } else if (name.equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//                    CurrencySymbol = " TZS";
//                }
            }
        }
    }

    public static String STATUS_PICKUP_COMPLETE = "5";
    public static String STATUS_DROP_COMPLETE = "7";
    public static String driverLatitude = "0";
    public static String driverLongitude = "0";

    //TANZANIA


    //Get Exact Address
    public static String getExactAddress(String pickupHouse, String pickupStreet, String pickupLandmark, String pickupAddress) {
        String address = "";
        if (pickupHouse == null && pickupStreet != null && pickupLandmark != null) {
            address = pickupStreet + ", " + pickupLandmark + ", " + pickupAddress;
        } else if (pickupStreet == null && pickupHouse != null && pickupLandmark != null) {
            address = pickupHouse + ", " + pickupLandmark + ", " + pickupAddress;
        } else if (pickupLandmark == null && pickupHouse != null && pickupStreet != null) {
            address = pickupHouse + ", " + pickupStreet + ", " + pickupAddress;
        } else if (pickupLandmark == null && pickupHouse == null && pickupStreet == null) {
            address = pickupAddress;
        } else {
            address = pickupHouse + ", " + pickupStreet + ", " + pickupLandmark + ", " + pickupAddress;
        }
        return address;
    }

    public static CharSequence getPackageCm(String value) {
        String result = new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
        CharSequence chars;
        String first = result;
        String next = "<font color='#939393'>cm</font>";
        chars = Html.fromHtml(first + " " + next);
        return chars;
    }

    public static CharSequence getPackageKg(String value) {
        String result = new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
//        String result = value;
        CharSequence chars;
        String first = result;
        String next = "<font color='#939393'>kg</font>";
        chars = Html.fromHtml(first + " " + next);
        return chars;
    }

    public static CharSequence getPackageItems(String value) {
        String result = new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
        CharSequence chars;
        String next = "";

        if (result.equalsIgnoreCase("0") || result.equalsIgnoreCase("1")) {
            next = "<font color='#939393'>item</font>";
        } else {
            next = "<font color='#939393'>items</font>";
        }

        String first = result;
        chars = Html.fromHtml(first + "" );
        return chars;
    }

    public static String formatValue(String s) {
        String value = s;
        try {
            String givenstring = s.toString();
            Double longval;
            if (givenstring.contains(",")) {
                givenstring = givenstring.replaceAll(",", "");
            }
            longval = Double.parseDouble(givenstring);
            DecimalFormat formatter = new DecimalFormat("###,###,###,###");
            value = formatter.format(longval);

            // to place the cursor at the end of text
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
       public static String formatValueNumber(String s) {
        String value = s;
        try {
            String givenstring = s.toString();;

            value = givenstring.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1 $2$3");


            // to place the cursor at the end of text
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

public static String formatValueNumber2(String s) {

    String value = s;
    try {
        String givenstring = s.toString();;

        value = givenstring.replaceFirst("(\\d{2})(\\d{3})(\\d+)", "$1 $2$3");


        // to place the cursor at the end of text
    } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return value;
    }


    public static String getStatus(int statusCode) {
        String Status = "";
        switch (statusCode) {
            case 0:
                Status = "Ready to pickup";
                break;
            case 1:
                Status = "Accepted";
                break;
            case 2:
                Status = "Arriving";
                break;
            case 3:
                Status = "Arrived";
                break;
            case 4:
                Status = "Waiting";
                break;
            case 5:
                Status = "Start";
                break;
            case 6:
                Status = "Out for delivery";
                break;
            case 7:
                Status = "Completed";
                break;
            case 8:
                Status = "Canceled";
                break;
            case 9:
                Status = "Waiting for delivery";
                break;
            case 10:
                Status = "In warehouse";
                break;
            case 11:
                Status = "Forwarded to admin";
                break;
            case 12:
                Status = "Delivery failure";
                break;
            case 13:
                Status = "Return to user";
                break;
        }
        return Status;
    }

    public static String getPickupDay(int statusCode) {
        String day = "";
        switch (statusCode) {
            case 0:
                day = "Sunday";
                break;
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday ";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;
        }
        return day;
    }

    public static String getConvertedDateTime(String dateTime) {
        String dateString = "";
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date1);

        //Convert Date to timestamp Milliseconds
        long millis = date1.getTime();

        //Convert Milliseconds to String
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        dateString = formatter1.format(new Date(millis));
        return dateString;
    }

    public static String getConvertedPickupDateTime(String dateTime) {
        String dateString = "";
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date1);

        //Convert Date to timestamp Milliseconds
        long millis = date1.getTime();

        //Convert Milliseconds to String
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateString = formatter1.format(new Date(millis));
        return dateString;
    }


    public static void signInIntent(final Context context) {


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setTitle("Login Required");
        alert.setMessage("Something Went Wrong Please Login Again");
        alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent next = new Intent(context, Selected_Country.class);
//                next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(next);
//                ((Activity) context).finish();
                logoutuser(context);
            }
        });

        alert.show();

    }


    public static void logoutuser(final Context context) {
        Call<ForgotPaswdResponse> notificationCall;
        ForgotPaswdResponse forgotPaswdResponse;
        WebRequest webRequest;
        WebRequest.APIInterface apiInterface;
        webRequest=WebRequest.getSingleton(context);
        SharedPreferences preferences = context.getSharedPreferences(SharedValues.PREF_NAME, context.MODE_PRIVATE);
        final SharedPreferences.Editor editor;
        editor = preferences.edit();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logout");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();


        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        String driverId = preferences.getString(SharedValues.DRIVER_ID, "");

        String country_code = CountryCodeCheck.countrycheck(context);
        if (country_code != null && country_code.trim().length() > 0) {

            //API HIT LOGOUT
            notificationCall = webRequest.apiInterface.logoutDriver(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId);
            notificationCall.enqueue(new Callback<ForgotPaswdResponse>() {
                @Override
                public void onResponse(Call<ForgotPaswdResponse> call, Response<ForgotPaswdResponse> response) {
                    if (response.isSuccessful()) {
                        ForgotPaswdResponse notificationResponse = response.body();
                        if (notificationResponse.getResponse() == 1) {

                            progressDialog.dismiss();
                            Intent i1 = new Intent().setClass(context, UpdateDriverLocationServiceNew.class);
                            context.stopService(i1);
                            Intent serviceIntent = new Intent(context, ForegroundService.class);
                            context.stopService(serviceIntent);
                            editor.clear().commit();
                            Intent next = new Intent(context, Selected_Country.class);
                            context.stopService(next);
                            editor.clear().commit();
                            next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(next);
                            ((Activity) context).finish();



                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ForgotPaswdResponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });



        }



    }

    public static String encodeBase(String packegeId) {

        //encoding  byte array into base 64
        byte[] encoded = Base64.encodeBase64(packegeId.getBytes());
        packegeId = new String(encoded);
        return packegeId;
    }

    public static String decodeBase(String packegeId) {

        byte[] byteArray = Base64.decodeBase64(packegeId.getBytes());
        //decoding byte array into base64
        String decodedString = new String(byteArray);

        return decodedString;

    }
}