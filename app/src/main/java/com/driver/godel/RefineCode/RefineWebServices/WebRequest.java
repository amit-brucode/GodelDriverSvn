package com.driver.godel.RefineCode.RefineWebServices;

import android.content.Context;

import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineWebServices.COPResponse.COPDriverResp;
import com.driver.godel.RefineCode.RefineWebServices.CancelPackage.cancelPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.DropOffFailureResponse.MainRes;
import com.driver.godel.RefineCode.RefineWebServices.NewRequestResponse.NewRequestsResponse;
import com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse.GetPackageResponse;
import com.driver.godel.RefineCode.RefineWebServices.PendingDeliveryResponse.pendingDelvResp;
import com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp.PendingPickupResp;
import com.driver.godel.RefineCode.RefineWebServices.packageDropWarehouse.PackageDropWareHouseResponse;
import com.driver.godel.RefineCode.RefineWebServices.packageStatus.UpdatePackageStatus;
import com.driver.godel.response.CrashReport.CrashEmailResponse;
import com.driver.godel.response.ForgotPaswdResponse;
import com.driver.godel.response.GetFareResponse;
import com.driver.godel.response.HistoryResponse.HistoryResponse;
import com.driver.godel.response.LoginResp.LoginResponse;
import com.driver.godel.response.MarkroundTrip.MarkroundTripResponse;
import com.driver.godel.response.Select_Country.SelectedCountry;
import com.driver.godel.response.TransferPackageResponse.TransferPackageToDriver;
import com.driver.godel.response.assignBarcode.AssignBarcodeResponse;
import com.driver.godel.response.changePasswordResp.ChangePasswordResponse;
import com.driver.godel.response.driverProfileResponse.DriverProfileResponse;
import com.driver.godel.response.fareApiResponse.FareResponse;
import com.driver.godel.response.getDriverDetails.GetProfileResponse;
import com.driver.godel.response.setActivePackage.ActivePackageResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by baljit.kaur on 4/4/2017.
 */

public class WebRequest {
    private static Context mContext;
    private static WebRequest mWebrequest;
    public static APIInterface apiInterface;

    public static WebRequest getSingleton(Context context) {
        if (mWebrequest == null) {
            synchronized ((WebRequest.class)) {
                if (mWebrequest == null) {
                    mWebrequest = new WebRequest(context);
                }
            }
        }
        return mWebrequest;
    }

    public WebRequest(Context context) {
        apiInterface = RestClient.getClient();
        mContext = context;
    }

    private static class RestClient {
        private static APIInterface apiInterface;

        public static APIInterface getClient() {
            if (apiInterface == null) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                OkHttpClient client = new OkHttpClient.Builder().
                        connectTimeout(30, TimeUnit.SECONDS).
                        addInterceptor(logging).
                        readTimeout(30, TimeUnit.SECONDS).
                        writeTimeout(30, TimeUnit.SECONDS).
                        build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Global.API_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                apiInterface = retrofit.create(APIInterface.class);
            }
            return apiInterface;
        }
    }

    public interface APIInterface {
        //Pending Pickup API
        @FormUrlEncoded
        @POST("driver_booking_history_pending_pickup_v2?")
        Call<PendingPickupResp> pendingPickup(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Query("page") int pageNo, @Field("driver_id") String driverId);

        //Pending deliveries
        @FormUrlEncoded
        @POST("driver_booking_history_pending_delivery_v2_new?")
        Call<pendingDelvResp> pendingDeliveries(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Query("page") int pageNo, @Field("driver_id") String driverId);

        //New requests
        @FormUrlEncoded
        @POST("get_active_bookings_nearby_v2_new")
        Call<NewRequestsResponse> getActiveBookings(@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("lat") String latitude, @Field("lng") String lng, @Field("driver_id") String id);

        //Package details
        @FormUrlEncoded
        @POST("package_details_driver_v2?")
        Call<GetPackageResponse> getPacakgeDetail(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode);

        //Package details by scanning
        @FormUrlEncoded
        @POST("scan_package_details_by_driver_id")
        Call<GetPackageResponse> ScangetPacakgeDetail(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token,@Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode, @Field("driver_id") String driverId);


        //Transfer package to other driver
        @FormUrlEncoded
        @POST("transfer_package_to_another_driver")
        Call<TransferPackageToDriver> TransferPackagetodriver(@Query("country") String country, @Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode, @Field("driver_id") String driverId, @Field("new_driver_id") String newDriverid);



        //Booking Payment API
        @FormUrlEncoded
        @POST("complete_cash_on_pickup_v2?")
        Call<COPDriverResp> CashOnPickup(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode, @Field("driver_id") String driver_id);
        @FormUrlEncoded
        @POST("complete_cash_on_delivery?")
        Call<COPDriverResp> CashOnPickup2(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode, @Field("driver_id") String driver_id);

        //Update Package Status Pickup
        @FormUrlEncoded
        @POST("update_package_status_v2?")
        Call<UpdatePackageStatus> updatePackageStatus(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_id") String packageId, @Field("driver_id") String driverId,
                                                      @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
                                                      @Field("handle_by") String handleBy, @Field("pop_image") String base64);


        //Update Package Status Pickup
        @FormUrlEncoded
        @POST("update_failure_package_status?")
        Call<UpdatePackageStatus> updateFailurePackageStatus(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_id") String packageId, @Field("driver_id") String driverId,
                                                      @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
                                                      @Field("handle_by") String handleBy, @Field("pop_image") String base64);

        //Drop off failure api
        @FormUrlEncoded
        @POST("package_failure_v2?")
        Call<MainRes> packageFailure(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode, @Field("comments") String comments,
                                     @Field("pof_image") String pofimage, @Field("driver_id") String driverid);

        //Drop WareHouse Package API
        @FormUrlEncoded
        @POST("package_drop_warehouse_v2?")
        Call<PackageDropWareHouseResponse> packageDropWarahouse(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode,
                                                                @Field("driver_id") String driverId, @Field("pod_image") String podImage);

        //Set Active Package Status
        @FormUrlEncoded
        @POST("set_active_package?")
        Call<ActivePackageResponse> setActivePackage(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("package_code") String packageCode,
                                                     @Field("current_lat") String currentLat, @Field("current_lng") String currentLng, @Field("active_type") String type);

        //Set reassign Package Status
        @FormUrlEncoded
        @POST("own_reassign?")
        Call<com.driver.godel.response.ownResign.MainRes> ownResign(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept,
                                                                    @Header("Authorization") String token, @Field("driver_id") String driverId,
                                                                    @Field("package_code") String packageCode,
                                                                    @Field("current_lat") String currentLat, @Field("current_lng") String currentLng, @Field("active_type") String type);

        //Get Driver Profile Update
        @FormUrlEncoded
        @POST("update_driver?")
        Call<DriverProfileResponse> getDriverProfile(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("driver_name") String driverName,
                                                     @Field("driver_email") String driverEmail, @Field("password") String password,
                                                     @Field("driver_phone") String driverPhone, @Field("vehicle") String vehicle,
                                                     @Field("driver_image") String driverImage, @Field("status") String status,
                                                     @Field("app_notification") String notification);

        //Change Password API
        @FormUrlEncoded
        @POST("driver_password_update?")
        Call<ChangePasswordResponse> changePasswordApi(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("password") String password);

        //Get Driver Details
        @FormUrlEncoded
        @POST("get_driver_details?")
        Call<GetProfileResponse> getProfileApi(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId);

        //Status API
        @FormUrlEncoded
        @POST("driver_status_update?")
        Call<ForgotPaswdResponse> statusDriver(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("driver_status") String status);

        //Update Package Status Delivered
        @FormUrlEncoded
        @POST("update_package_status_v2?")
        Call<UpdatePackageStatus> updatePackageDelivered(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept,
                                                         @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId,
                                                         @Field("package_id") String packageId, @Field("driver_id") String driverId,
                                                         @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
                                                         @Field("handle_by") String handleBy, @Field("pod_image") String base64, @Field("sign_name") String signName);

        @FormUrlEncoded
        @POST("mark_round_trip_booking?")
        Call<MarkroundTripResponse> updatePackageDeliveredForFirstRound(@Query("country") String country, @Header("User-Agent") String agent, @Header("Accept") String accept,
                                                                        @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId,
                                                                        @Field("package_code") String packageId, @Field("driver_id") String driverId,
//                                                         @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken, @Field("handle_by") String handleBy,
                                                                        @Field("round_pod_image") String base64, @Field("round_sign_name") String signName);

        //Get Total Fare
        @FormUrlEncoded
        @POST("final_fare_v2?")
        Call<FareResponse> getFinalFare(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode);

        //Booking Payment API
        @FormUrlEncoded
        @POST("set_booking_payment_status?")
        Call<com.driver.godel.response.BookingPaymentStatusRes.MainRes> setBookingPaymentStatus(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("package_code") String packageCode);

        //Assign Barcode API
        @FormUrlEncoded
        @POST("assign_barcode_on_package?")
        Call<AssignBarcodeResponse> assignBarcodeApi(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("package_code") String packageCode, @Field("barcode") String barCode);

        //Crash Mail
        @FormUrlEncoded
        @POST("crash_mail?")
        Call<CrashEmailResponse> SendCrashEmail(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("reason") String reason);

        //Get Estimated Fare API
        @GET("get_estimated_fare?")
        Call<GetFareResponse> getFare(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId);

        //Driver Logout API
        @FormUrlEncoded
        @POST("driver_logout?")
        Call<ForgotPaswdResponse> logoutDriver(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId);

        //Get pending Completed History
        @GET("driver_booking_history_completed_v2_new?")
        Call<HistoryResponse> getBookingHistoryCompleted(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Query("page") int pageNo, @Query("driver_id") String driver_id);

        //Get pending Booking History
        @GET("driver_booking_history_pending?")
        Call<HistoryResponse> getBookingHistory(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Query("page") int pageNo, @Query("driver_id") String driver_id);

        //Track Driver API
        @FormUrlEncoded
        @POST("driver_tracking?")
        Call<ForgotPaswdResponse> trackDriver(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_id") String driverId, @Field("driver_lat") String driverLat, @Field("driver_lng") String driverLng);


        //driver login
        @FormUrlEncoded
        @POST("driver_login?")
        Call<LoginResponse> loginDriver(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_email") String driverEmail, @Field("password") String password,
                                        @Field("driver_lat") String driverLat, @Field("driver_lng") String driverLng, @Field("device_id") String deviceId,
                                        @Field("device_token") String deviceToken, @Field("device_name") String device_name,
                                        @Field("api_version") String api_version, @Field("apk_version") String apk_version);
        //Tazania Login
        @FormUrlEncoded
        @POST("driver_agent_login?")
        Call<LoginResponse> driverAgentLogin(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token,
                                             @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_email") String driverEmail,
                                             @Field("password") String password,
                                        @Field("driver_lat") String driverLat, @Field("driver_lng") String driverLng, @Field("device_id") String deviceId,
                                        @Field("device_token") String deviceToken, @Field("device_name") String device_name,
                                        @Field("api_version") String api_version, @Field("apk_version") String apk_version,
                                             @Field("driver_type") String driver_type);
        //Reset Password API
        @FormUrlEncoded
        @POST("driver_forgot_password?")
        Call<ForgotPaswdResponse> resetPasswd(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("driver_email") String driverEmail);

        //Forgot Password API
        @FormUrlEncoded
        @POST("driver_forgot_password?")
        Call<ForgotPaswdResponse> forgotPasswd(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("password") String password, @Field("code") String code);


        @FormUrlEncoded
        @POST("information_mail?")
        Call<CrashEmailResponse> SendInformationEmail(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-Driver-Id") String headerDriverId, @Field("reason") String reason);



        @FormUrlEncoded
        @POST("cancel_package_by_driver?")
        Call<cancelPackageResponse> cancelPackage(@Query("country") String country,@Header("User-Agent") String agent, @Header("Accept") String accept, @Header("Authorization") String token, @Header("Godel-User-Id") String headerUserId, @Field("package_id") String package_id, @Field("cancel_reason") String cancel_reason );

        @GET("get_countries")
        Call<SelectedCountry> selectedcountry();

    }
}


//
//    String country_code=CountryCodeCheck.countrycheck(context);
//                if(country_code!=null&&country_code.trim().length()>0){
//                        pendingResponseCall = webRequest.apiInterface.pendingPickup(country_code,Global.USER_AGENT, Accept, USER_TOKEN,driverId, pageNo, driverId);
//                        getPendingPickup(pendingResponseCall);
//                        }else{
//                        if (mPendingPickupsProgress != null && mPendingPickupsProgress.isShowing()) {
//                        mPendingPickupsProgress.dismiss();
//                        }
//                        }