//package com.driver.godel.webServices;
//
//import android.content.Context;
//
//import com.driver.godel.response.BookingPaymentStatusRes.MainRes;
//import com.driver.godel.response.COPResponse.COPDriverResp;
//import com.driver.godel.response.CrashReport.CrashEmailResponse;
//import com.driver.godel.response.ForgotPaswdResponse;
//import com.driver.godel.response.GetActiveBookingData.GetActiveBookingsNearBy;
//import com.driver.godel.response.GetFareResponse;
//import com.driver.godel.response.HistoryResponse.HistoryResponse;
//import com.driver.godel.response.LoginResponse;
//import com.driver.godel.response.PackageDetailResponse.GetPackageResponse;
//import com.driver.godel.response.PackageReject.PackageRejectResponse;
//import com.driver.godel.response.PackageStatusResponse;
//import com.driver.godel.response.FareResponse.FinalFareResponse;
//import com.driver.godel.response.SignatureResponse.SignatureResponse;
//import com.driver.godel.response.assignBarcode.AssignBarcodeResponse;
//import com.driver.godel.response.changePasswordResp.ChangePasswordResponse;
//import com.driver.godel.response.driverProfileResponse.DriverProfileResponse;
//import com.driver.godel.response.driverRating.GetDriverRatingResponse;
//import com.driver.godel.response.fareApiResponse.FareResponse;
//import com.driver.godel.response.getDriverDetails.GetProfileResponse;
//import com.driver.godel.response.getPdfName.PdfResponse;
//import com.driver.godel.response.packageDropWarehouse.PackageDropWareHouseResponse;
//import com.driver.godel.response.packageStatus.UpdatePackageStatus;
//import com.driver.godel.response.pendingResponse.PendingResponse;
//import com.driver.godel.response.setActivePackage.ActivePackageResponse;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.driver.godel.utils.GlobalVariables;
//import java.util.concurrent.TimeUnit;
//import okhttp3.OkHttpClient;
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.GET;
//import retrofit2.http.Header;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//
///**
// * Created by baljit.kaur on 4/4/2017.
// */
//
//public class WebRequest
//{
//    private static Context mContext;
//    private static WebRequest mWebrequest;
//    public static APIInterface apiInterface;
//
//    public static WebRequest getSingleton(Context context)
//    {
//        if (mWebrequest == null)
//        {
//            synchronized ((WebRequest.class))
//            {
//                if (mWebrequest == null)
//                {
//                    mWebrequest = new WebRequest(context);
//                }
//            }
//        }
//        return mWebrequest;
//    }
//
//    public WebRequest(Context context)
//    {
//        apiInterface = RestClient.getClient();
//        mContext = context;
//    }
//
//    private static class RestClient
//    {
//        private static APIInterface apiInterface;
//
//        public static APIInterface getClient()
//        {
//            if (apiInterface == null)
//            {
//                Gson gson = new GsonBuilder()
//                        .setLenient()
//                        .create();
//
//                OkHttpClient client = new OkHttpClient.Builder().
//                        connectTimeout(30, TimeUnit.SECONDS).
//                        readTimeout(30, TimeUnit.SECONDS).
//                        writeTimeout(30, TimeUnit.SECONDS).
//                        build();
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(GlobalVariables.BASE_URL)
//                        .addConverterFactory(ScalarsConverterFactory.create())
//                        .client(client)
//                        .addConverterFactory(GsonConverterFactory.create(gson))
//                        .build();
//                apiInterface = retrofit.create(APIInterface.class);
//            }
//            return apiInterface;
//        }
//    }
//
//    public interface APIInterface
//    {
//        //Driver Login API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_login")
//        Call<LoginResponse> loginDriver(@Header("User-Agent") String agent, @Field("driver_email") String driverEmail, @Field("password") String password, @Field("driver_lat") String driverLat,
//                                        @Field("driver_lng") String driverLng, @Field("device_id") String deviceId, @Field("device_token")
//                                                String deviceToken, @Field("device_name") String device_name, @Field("api_version") String api_version, @Field("apk_version") String apk_version);
//
//        //Reset Password API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_forgot_password")
//        Call<ForgotPaswdResponse> resetPasswd(@Header("User-Agent") String agent,@Field("driver_email") String driverEmail);
//
//        //Forgot Password API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_forgot_password")
//        Call<ForgotPaswdResponse> forgotPasswd(@Header("User-Agent") String agent,@Field("password") String password, @Field("code") String code);
//
//        //Driver Logout API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_logout")
//        Call<ForgotPaswdResponse> logoutDriver(@Header("User-Agent") String agent,@Field("driver_id") String driverId);
//
//        //Status API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_status_update")
//        Call<ForgotPaswdResponse> statusDriver(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("driver_status") String status);
//
//        //Track Driver API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_tracking")
//        Call<ForgotPaswdResponse> trackDriver(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("driver_lat") String driverLat, @Field("driver_lng") String driverLng);
//
//        //Get pending Booking History
//        @GET("/backend/api/driver_booking_history_pending")
//        Call<HistoryResponse> getBookingHistory(@Header("User-Agent") String agent,@Query("page") int pageNo, @Query("driver_id") String driver_id);
//
//       /* //Get pending Completed History
//        @GET("/backend/api/driver_booking_history_completed")
//        Call<HistoryResponse> getBookingHistoryCompleted(@Query("page") int pageNo, @Query("driver_id") String driver_id);
//*/
//
//        //Get pending Completed History
//        @GET("/backend/api/driver_booking_history_completed_v2_new")
//        Call<HistoryResponse> getBookingHistoryCompleted(@Header("User-Agent") String agent,@Query("page") int pageNo, @Query("driver_id") String driver_id);
//
//
//        //Update Package Status
//        @FormUrlEncoded
//        @POST("/backend/api/update_package_status")
//        Call<PackageStatusResponse> rideRequest(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId,
//                                                @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
//                                                @Field("handle_by") String handleBy);
//
//
//        //Accept Package Update Package Status
//        @FormUrlEncoded
//        @POST("/backend/api/update_package_status_v2")
//        Call<UpdatePackageStatus> acceptPackage(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId,
//                                              @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
//                                              @Field("handle_by") String handleBy);
//
//        //Reject Package
//        @FormUrlEncoded
//        @POST("/backend/api/rejected_package_v2")
//        Call<PackageRejectResponse> rejectPackage(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId);
//
//        //Get Estimated Fare API
//        @GET("/backend/api/get_estimated_fare")
//        Call<GetFareResponse> getFare(@Header("User-Agent") String agent);
//
//        //Noification On/Off API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_app_notification_update")
//        Call<ForgotPaswdResponse> notificationStatus(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("driver_app_notification") String driver_app_notification);
//
//        //Package Track API
//        @FormUrlEncoded
//        @POST("/backend/api/add_package_route")
//        Call<ForgotPaswdResponse> trackPackage(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("lat") String latitude, @Field("lng") String longitude);
//
//        //Get Active Bookings nearby
//        @FormUrlEncoded
//        @POST("/backend/api/get_active_bookings_nearby_v2_new")
//        Call<GetActiveBookingsNearBy> getActiveBookings(@Header("User-Agent") String agent,@Field("lat") String latitude, @Field("lng") String lng, @Field("driver_id") String id);
//
//        //Send Signature
//        @FormUrlEncoded
//        @POST("/backend/api/signature")
//        Call<SignatureResponse> getSignature(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("sign_name") String signName, @Field("image") String imageBase64);
//
//        //Get Driver Profile Update
//        @FormUrlEncoded
//        @POST("/backend/api/update_driver")
//        Call<DriverProfileResponse> getDriverProfile(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("driver_name") String driverName,
//                                                     @Field("driver_email") String driverEmail, @Field("password") String password,
//                                                     @Field("driver_phone") String driverPhone, @Field("vehicle") String vehicle,
//                                                     @Field("driver_image") String driverImage, @Field("status") String status,
//                                                     @Field("app_notification") String notification);
//
//        //Calculate final fare
//        @FormUrlEncoded
//        @POST("/backend/api/final_fare")
//        Call<FinalFareResponse> getfare(@Header("User-Agent") String agent,@Field("package_id") String packageId);
//
//        //Crash Mail
//        @FormUrlEncoded
//        @POST("/backend/api/crash_mail")
//        Call<CrashEmailResponse> SendCrashEmail(@Header("User-Agent") String agent,@Field("reason") String reason);
//
//        //Pending Pickup API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_booking_history_pending_pickup_v2")
//        Call<PendingResponse> pendingPickup(@Header("User-Agent") String agent,@Query("page") int pageNo, @Field("driver_id") String driverId);
//
//       /* //Pending Deliveries API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_booking_history_pending_delivery_v2")
//        Call<PendingResponse> pendingDeliveries(@Query("page") int pageNo, @Field("driver_id") String driverId);
//*/
//        //Pending Deliveries v2 New API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_booking_history_pending_delivery_v2_new")
//        Call<PendingResponse> pendingDeliveries(@Header("User-Agent") String agent,@Query("page") int pageNo, @Field("driver_id") String driverId);
//
//       /* //Get Package Details
//        @FormUrlEncoded
//        @POST("/backend/api/package_details_v2")
//        Call<GetPackageResponse> getPacakgeDetail(@Field("package_code") String packageCode);*/
//
//        //Get Package Details
//        @FormUrlEncoded
//        @POST("/backend/api/package_details_driver_v2")
//        Call<GetPackageResponse> getPacakgeDetail(@Header("User-Agent") String agent,@Field("package_code") String packageCode);
//
//
//        //Update Package Status Pickup
//        @FormUrlEncoded
//        @POST("/backend/api/update_package_status_v2")
//        Call<UpdatePackageStatus> updatePackageStatus(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId,
//                                                      @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
//                                                      @Field("handle_by") String handleBy, @Field("pop_image") String base64);
//
//        //Update Package Status Delivered
//        @FormUrlEncoded
//        @POST("/backend/api/update_package_status_v2")
//        Call<UpdatePackageStatus> updatePackageDelivered(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId,
//                                                      @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
//                                                      @Field("handle_by") String handleBy, @Field("pod_image") String base64, @Field("sign_name") String signName);
//
//
//
//        //Update Package Status In Progress
//        @FormUrlEncoded
//        @POST("/backend/api/update_package_status_v2")
//        Call<UpdatePackageStatus> updatePackageInProgress(@Header("User-Agent") String agent,@Field("package_id") String packageId, @Field("driver_id") String driverId,
//                                                         @Field("package_status") String packageStatus, @Field("driver_device_token") String deviceToken,
//                                                         @Field("handle_by") String handleBy, @Field("current_lat") String currentLat, @Field("current_lng") String currentLng);
//
//        //Get PDF Name
//        @FormUrlEncoded
//        @POST("/backend/api/generate_barcode_v2")
//        Call<PdfResponse> getPdfLink(@Header("User-Agent") String agent,@Field("package_code") String packageCode);
//
//        //Get Total Fare
//        @FormUrlEncoded
//        @POST("/backend/api/final_fare_v2")
//        Call<FareResponse> getFinalFare(@Header("User-Agent") String agent,@Field("package_code") String packageCode);
//
//        //Drop WareHouse Package API
//        @FormUrlEncoded
//        @POST("/backend/api/package_drop_warehouse_v2")
//        Call<PackageDropWareHouseResponse> packageDropWarahouse(@Header("User-Agent") String agent,@Field("package_code") String packageCode, @Field("driver_id") String driverId, @Field("pod_image") String podImage);
//
//        //Set Active Package Status
//        @FormUrlEncoded
//        @POST("/backend/api/set_active_package")
//        Call<ActivePackageResponse> setActivePackage(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("package_code") String packageCode, @Field("current_lat") String currentLat, @Field("current_lng") String currentLng, @Field("active_type") String type);
//
//        //Assign Barcode API
//        @FormUrlEncoded
//        @POST("/backend/api/assign_barcode_on_package")
//        Call<AssignBarcodeResponse> assignBarcodeApi(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("package_code") String packageCode, @Field("barcode") String barCode);
//
//        //Get Driver Details
//        @FormUrlEncoded
//        @POST("/backend/api/get_driver_details")
//        Call<GetProfileResponse> getProfileApi(@Header("User-Agent") String agent,@Field("driver_id") String driverId);
//
//        //Change Password API
//        @FormUrlEncoded
//        @POST("/backend/api/driver_password_update")
//        Call<ChangePasswordResponse> changePasswordApi(@Header("User-Agent") String agent,@Field("driver_id") String driverId, @Field("password") String password);
//
//        //Get Driver Rating
//        @FormUrlEncoded
//        @POST("/backend/api/driver_rating")
//        Call<GetDriverRatingResponse> getDriverRating(@Header("User-Agent") String agent,@Field("driver_id") String driverId);
//
//        //Booking Payment API
//        @FormUrlEncoded
//        @POST("/backend/api/set_booking_payment_status")
//        Call<MainRes> setBookingPaymentStatus(@Header("User-Agent") String agent,@Field("package_code") String packageCode);
//
//        //Booking Payment API
//        @FormUrlEncoded
//        @POST("/backend/api/complete_cash_on_pickup_v2")
//        Call<COPDriverResp> CashOnPickup(@Header("User-Agent") String agent, @Field("package_code") String packageCode, @Field("driver_id") String driver_id);
//
//
//        //Drop off failure api
//        @FormUrlEncoded
//        @POST("/backend/api/package_failure")
//        Call<com.driver.godel.response.DropOffFailure.MainRes> packageFailure(@Header("User-Agent") String agent,
//                                                                              @Field("package_code") String packageCode,
//                                                                              @Field("comments") String comments,
//                                                                              @Field("pof_image") String pofimage,
//                                                                              @Field("driver_id") String driverid,
//                                                                              @Field("vehicle_id") String vehicleid);
//    }
//}
