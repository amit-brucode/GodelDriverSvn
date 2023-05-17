package com.driver.godel.response.SignatureResponse;

/**
 * Created by Ajay2.Sharma on 29-Aug-17.
 */

public class BookingHistoryResponseDetails {

    String booking_id;
    String booking_type;
    String booking_note;
    String is_cancel;
    String preferred_deliver_time;
    String booking_code;
    String booking_pickup_datetime;
    String delivery_type;
    String booking_delivery_datetime;
    String delivery_session;

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    String package_type;

    String user_id;
    String user_unique_id;
    String name;
    String user_email;
    String password;
    String user_phone;
    String sinch_code;
    String user_app_notification;
    String user_image;
    String user_status;
    String token;
    String facebook_id;
    String login_type;
    String source;
    String validate_code;
    String email_verify;
    String phone_verify;
    String remember_token;
    String created_at;
    String updated_at;

    String package_id;
    String package_code;
    String fk_booking_id;
    String package_est_value;
    String package_depth;
    String package_length;
    String package_width;
    String package_weight;
    String package_quantity;
    String package_cancel;
    String driver_id;
    String warehouse_user_id;
    String partner_user_id;
    String driver_device_token;
    String handle_by;
    String round_signature;
    String round_sign_name;
    public String getRound_signature() {
        return round_signature;
    }

    public void setRound_signature(String round_signature) {
        this.round_signature = round_signature;
    }

    public String getRound_sign_name() {
        return round_sign_name;
    }

    public void setRound_sign_name(String round_sign_name) {
        this.round_sign_name = round_sign_name;
    }

    String package_status;
    String rating;
    String signature;

    String sign_name;

    String feedback;

    String recurring_day;
    String recurring_day_time;
    String recurring_week;
    String recurring_pickup_type;
    String recurring_status;



    String is_deleted;
    private String is_first_round_completed;
    private String is_first_round;
    private String is_round_trip;

    public String getIs_first_round_completed() {
        return is_first_round_completed;
    }

    public void setIs_first_round_completed(String is_first_round_completed) {
        this.is_first_round_completed = is_first_round_completed;
    }
    public String getIs_first_round() {
        return is_first_round;
    }

    public void setIs_first_round(String is_first_round) {
        this.is_first_round = is_first_round;
    }
    public String getIs_round_trip() {
        return is_round_trip;
    }

    public void setIs_round_trip(String is_round_trip) {
        this.is_round_trip = is_round_trip;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    String cancel_reason;

    public String getRecurring_day() {
        return recurring_day;
    }

    public void setRecurring_day(String recurring_day) {
        this.recurring_day = recurring_day;
    }

    public String getRecurring_day_time() {
        return recurring_day_time;
    }

    public void setRecurring_day_time(String recurring_day_time) {
        this.recurring_day_time = recurring_day_time;
    }

    public String getRecurring_week() {
        return recurring_week;
    }

    public void setRecurring_week(String recurring_week) {
        this.recurring_week = recurring_week;
    }

    public String getRecurring_pickup_type() {
        return recurring_pickup_type;
    }

    public void setRecurring_pickup_type(String recurring_pickup_type) {
        this.recurring_pickup_type = recurring_pickup_type;
    }

    public String getRecurring_status() {
        return recurring_status;
    }

    public void setRecurring_status(String recurring_status) {
        this.recurring_status = recurring_status;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }


    public String getDriver_package_status() {
        return driver_package_status;
    }

    public void setDriver_package_status(String driver_package_status) {
        this.driver_package_status = driver_package_status;
    }

    String driver_package_status;


    String location_id;
    String fk_package_code;
    String pickup_house_no;
    String pickup_street;
    String pickup_landmark;
    String pickup_location;
    String pickup_location_lat;
    String pickup_location_lng;
    String pickup_datetime;
    String dropoff_house_no;
    String dropoff_street;
    String dropoff_landmark;
    String dropoff_location;
    String dropoff_location_lat;
    String dropoff_location_lng;
    String dropoff_location_datetime;
    private String round_pickup_location;
    private String round_dropoff_location;



    //WareHouse Changes
    private String isWarehouseDropoffCheck;
    private String isWarehousePickup;
    private String isWarehouseDropoff;

    //WareHouse Pickup Address
    private String whPickupId;
    private String whPickupStateId;
    private String whPickupPhone;
    private String whPickupUpdatedAt;
    private String whPickupAddress;
    private String whPickupName;
    private String whPickupCreatedAt;
    private String whPickupLongtitude;
    private String whPickupLatitude;
    private String whPickupUserEmail;

    //WareHouse DropOff Address
    private String whDropOffId;
    private String whDropOffStateId;
    private String whDropOffPhone;
    private String whDropOffUpdatedAt;
    private String whDropOffAddress;
    private String whDropOffName;
    private String whDropOffCreatedAt;
    private String whDropOffLongtitude;
    private String whDropOffLatitude;
    private String whDropOffUserEmail;

    public String getPackage_est_value() {
        return package_est_value;
    }

    public void setPackage_est_value(String package_est_value) {
        this.package_est_value = package_est_value;
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getPackage_depth() {
        return package_depth;
    }

    public void setPackage_depth(String package_depth) {
        this.package_depth = package_depth;
    }

    public String getPackage_length() {
        return package_length;
    }

    public void setPackage_length(String package_length) {
        this.package_length = package_length;
    }

    public String getWhPickupId() {
        return whPickupId;
    }

    public void setWhPickupId(String whPickupId) {
        this.whPickupId = whPickupId;
    }

    public String getWhPickupStateId() {
        return whPickupStateId;
    }

    public void setWhPickupStateId(String whPickupStateId) {
        this.whPickupStateId = whPickupStateId;
    }
    public String getRound_pickup_location() {
        return round_pickup_location;
    }

    public void setRound_pickup_location(String round_pickup_location) {
        this.round_pickup_location = round_pickup_location;
    }

    public String getRound_dropoff_location() {
        return round_dropoff_location;
    }

    public void setRound_dropoff_location(String round_dropoff_location) {
        this.round_dropoff_location = round_dropoff_location;
    }
    public String getWhPickupPhone() {
        return whPickupPhone;
    }

    public void setWhPickupPhone(String whPickupPhone) {
        this.whPickupPhone = whPickupPhone;
    }

    public String getWhPickupUpdatedAt() {
        return whPickupUpdatedAt;
    }

    public void setWhPickupUpdatedAt(String whPickupUpdatedAt) {
        this.whPickupUpdatedAt = whPickupUpdatedAt;
    }

    public String getWhPickupAddress() {
        return whPickupAddress;
    }

    public void setWhPickupAddress(String whPickupAddress) {
        this.whPickupAddress = whPickupAddress;
    }

    public String getWhPickupName() {
        return whPickupName;
    }

    public void setWhPickupName(String whPickupName) {
        this.whPickupName = whPickupName;
    }

    public String getWhPickupCreatedAt() {
        return whPickupCreatedAt;
    }

    public void setWhPickupCreatedAt(String whPickupCreatedAt) {
        this.whPickupCreatedAt = whPickupCreatedAt;
    }

    public String getWhPickupLongtitude() {
        return whPickupLongtitude;
    }

    public void setWhPickupLongtitude(String whPickupLongtitude) {
        this.whPickupLongtitude = whPickupLongtitude;
    }

    public String getWhPickupLatitude() {
        return whPickupLatitude;
    }

    public void setWhPickupLatitude(String whPickupLatitude) {
        this.whPickupLatitude = whPickupLatitude;
    }

    public String getWhPickupUserEmail() {
        return whPickupUserEmail;
    }

    public void setWhPickupUserEmail(String whPickupUserEmail) {
        this.whPickupUserEmail = whPickupUserEmail;
    }

    public String getWhDropOffId() {
        return whDropOffId;
    }

    public void setWhDropOffId(String whDropOffId) {
        this.whDropOffId = whDropOffId;
    }

    public String getWhDropOffStateId() {
        return whDropOffStateId;
    }

    public void setWhDropOffStateId(String whDropOffStateId) {
        this.whDropOffStateId = whDropOffStateId;
    }

    public String getWhDropOffPhone() {
        return whDropOffPhone;
    }

    public void setWhDropOffPhone(String whDropOffPhone) {
        this.whDropOffPhone = whDropOffPhone;
    }

    public String getWhDropOffUpdatedAt() {
        return whDropOffUpdatedAt;
    }

    public void setWhDropOffUpdatedAt(String whDropOffUpdatedAt) {
        this.whDropOffUpdatedAt = whDropOffUpdatedAt;
    }

    public String getWhDropOffAddress() {
        return whDropOffAddress;
    }

    public void setWhDropOffAddress(String whDropOffAddress) {
        this.whDropOffAddress = whDropOffAddress;
    }

    public String getWhDropOffName() {
        return whDropOffName;
    }

    public void setWhDropOffName(String whDropOffName) {
        this.whDropOffName = whDropOffName;
    }

    public String getWhDropOffCreatedAt() {
        return whDropOffCreatedAt;
    }

    public void setWhDropOffCreatedAt(String whDropOffCreatedAt) {
        this.whDropOffCreatedAt = whDropOffCreatedAt;
    }

    public String getWhDropOffLongtitude() {
        return whDropOffLongtitude;
    }

    public void setWhDropOffLongtitude(String whDropOffLongtitude) {
        this.whDropOffLongtitude = whDropOffLongtitude;
    }

    public String getWhDropOffLatitude() {
        return whDropOffLatitude;
    }

    public void setWhDropOffLatitude(String whDropOffLatitude) {
        this.whDropOffLatitude = whDropOffLatitude;
    }

    public String getWhDropOffUserEmail() {
        return whDropOffUserEmail;
    }

    public void setWhDropOffUserEmail(String whDropOffUserEmail) {
        this.whDropOffUserEmail = whDropOffUserEmail;
    }

    public String getIsWarehouseDropoffCheck() {
        return isWarehouseDropoffCheck;
    }

    public void setIsWarehouseDropoffCheck(String isWarehouseDropoffCheck) {
        this.isWarehouseDropoffCheck = isWarehouseDropoffCheck;
    }

    public String getIsWarehousePickup() {
        return isWarehousePickup;
    }

    public void setIsWarehousePickup(String isWarehousePickup) {
        this.isWarehousePickup = isWarehousePickup;
    }

    public String getIsWarehouseDropoff() {
        return isWarehouseDropoff;
    }

    public void setIsWarehouseDropoff(String isWarehouseDropoff) {
        this.isWarehouseDropoff = isWarehouseDropoff;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getBooking_note() {
        return booking_note;
    }

    public void setBooking_note(String booking_note) {
        this.booking_note = booking_note;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getPreferred_deliver_time() {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time(String preferred_deliver_time) {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public String getBooking_code() {
        return booking_code;
    }

    public void setBooking_code(String booking_code) {
        this.booking_code = booking_code;
    }

    public String getBooking_pickup_datetime() {
        return booking_pickup_datetime;
    }

    public void setBooking_pickup_datetime(String booking_pickup_datetime) {
        this.booking_pickup_datetime = booking_pickup_datetime;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getBooking_delivery_datetime() {
        return booking_delivery_datetime;
    }

    public void setBooking_delivery_datetime(String booking_delivery_datetime) {
        this.booking_delivery_datetime = booking_delivery_datetime;
    }

    public String getDelivery_session() {
        return delivery_session;
    }

    public void setDelivery_session(String delivery_session) {
        this.delivery_session = delivery_session;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getSinch_code() {
        return sinch_code;
    }

    public void setSinch_code(String sinch_code) {
        this.sinch_code = sinch_code;
    }

    public String getUser_app_notification() {
        return user_app_notification;
    }

    public void setUser_app_notification(String user_app_notification) {
        this.user_app_notification = user_app_notification;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(String validate_code) {
        this.validate_code = validate_code;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getPhone_verify() {
        return phone_verify;
    }

    public void setPhone_verify(String phone_verify) {
        this.phone_verify = phone_verify;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_code() {
        return package_code;
    }

    public void setPackage_code(String package_code) {
        this.package_code = package_code;
    }

    public String getFk_booking_id() {
        return fk_booking_id;
    }

    public void setFk_booking_id(String fk_booking_id) {
        this.fk_booking_id = fk_booking_id;
    }

    public String getPackage_width() {
        return package_width;
    }

    public void setPackage_width(String package_width) {
        this.package_width = package_width;
    }

    public String getPackage_weight() {
        return package_weight;
    }

    public void setPackage_weight(String package_weight) {
        this.package_weight = package_weight;
    }

    public String getPackage_quantity() {
        return package_quantity;
    }

    public void setPackage_quantity(String package_quantity) {
        this.package_quantity = package_quantity;
    }

    public String getPackage_cancel() {
        return package_cancel;
    }

    public void setPackage_cancel(String package_cancel) {
        this.package_cancel = package_cancel;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getWarehouse_user_id() {
        return warehouse_user_id;
    }

    public void setWarehouse_user_id(String warehouse_user_id) {
        this.warehouse_user_id = warehouse_user_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public void setPartner_user_id(String partner_user_id) {
        this.partner_user_id = partner_user_id;
    }

    public String getDriver_device_token() {
        return driver_device_token;
    }

    public void setDriver_device_token(String driver_device_token) {
        this.driver_device_token = driver_device_token;
    }

    public String getHandle_by() {
        return handle_by;
    }

    public void setHandle_by(String handle_by) {
        this.handle_by = handle_by;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getFk_package_code() {
        return fk_package_code;
    }

    public void setFk_package_code(String fk_package_code) {
        this.fk_package_code = fk_package_code;
    }

    public String getPickup_house_no() {
        return pickup_house_no;
    }

    public void setPickup_house_no(String pickup_house_no) {
        this.pickup_house_no = pickup_house_no;
    }

    public String getPickup_street() {
        return pickup_street;
    }

    public void setPickup_street(String pickup_street) {
        this.pickup_street = pickup_street;
    }

    public String getPickup_landmark() {
        return pickup_landmark;
    }

    public void setPickup_landmark(String pickup_landmark) {
        this.pickup_landmark = pickup_landmark;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getPickup_location_lat() {
        return pickup_location_lat;
    }

    public void setPickup_location_lat(String pickup_location_lat) {
        this.pickup_location_lat = pickup_location_lat;
    }

    public String getPickup_location_lng() {
        return pickup_location_lng;
    }

    public void setPickup_location_lng(String pickup_location_lng) {
        this.pickup_location_lng = pickup_location_lng;
    }

    public String getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(String pickup_datetime) {
        this.pickup_datetime = pickup_datetime;
    }

    public String getDropoff_house_no() {
        return dropoff_house_no;
    }

    public void setDropoff_house_no(String dropoff_house_no) {
        this.dropoff_house_no = dropoff_house_no;
    }

    public String getDropoff_street() {
        return dropoff_street;
    }

    public void setDropoff_street(String dropoff_street) {
        this.dropoff_street = dropoff_street;
    }

    public String getDropoff_landmark() {
        return dropoff_landmark;
    }

    public void setDropoff_landmark(String dropoff_landmark) {
        this.dropoff_landmark = dropoff_landmark;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getDropoff_location_lat() {
        return dropoff_location_lat;
    }

    public void setDropoff_location_lat(String dropoff_location_lat) {
        this.dropoff_location_lat = dropoff_location_lat;
    }

    public String getDropoff_location_lng() {
        return dropoff_location_lng;
    }

    public void setDropoff_location_lng(String dropoff_location_lng) {
        this.dropoff_location_lng = dropoff_location_lng;
    }

    public String getDropoff_location_datetime() {
        return dropoff_location_datetime;
    }

    public void setDropoff_location_datetime(String dropoff_location_datetime) {
        this.dropoff_location_datetime = dropoff_location_datetime;
    }



}
