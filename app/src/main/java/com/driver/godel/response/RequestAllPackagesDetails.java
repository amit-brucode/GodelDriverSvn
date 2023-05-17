package com.driver.godel.response;

/**
 * Created by Ajay2.Sharma on 07-Jul-17.
 */

public class RequestAllPackagesDetails {

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

    public String getIs_warehouse_dropoff() {
        return is_warehouse_dropoff;
    }

    public void setIs_warehouse_dropoff(String is_warehouse_dropoff) {
        this.is_warehouse_dropoff = is_warehouse_dropoff;
    }

    String is_warehouse_dropoff;
    //Ware House Details

    private String wareHouse_id;

    private String wareHouse_state_id;

    private String wareHouse_phone;

    private String wareHouse_updated_at;

    private String wareHouse_address;

    private String wareHouse_name;

    private String wareHouse_created_at;

    private String wareHouse_longtitude;

    private String wareHouse_latitude;

    private String wareHouse_user_email;

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

    String package_id;
    String package_code;
    String package_depth;
    String package_length;
    String package_est_value;

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

    public String getPackage_est_value() {
        return package_est_value;
    }

    public void setPackage_est_value(String package_est_value) {
        this.package_est_value = package_est_value;
    }

    public String getWareHouse_id() {
        return wareHouse_id;
    }

    public void setWareHouse_id(String wareHouse_id) {
        this.wareHouse_id = wareHouse_id;
    }

    public String getWareHouse_state_id() {
        return wareHouse_state_id;
    }

    public void setWareHouse_state_id(String wareHouse_state_id) {
        this.wareHouse_state_id = wareHouse_state_id;
    }

    public String getWareHouse_phone() {
        return wareHouse_phone;
    }

    public void setWareHouse_phone(String wareHouse_phone) {
        this.wareHouse_phone = wareHouse_phone;
    }

    public String getWareHouse_updated_at() {
        return wareHouse_updated_at;
    }

    public void setWareHouse_updated_at(String wareHouse_updated_at) {
        this.wareHouse_updated_at = wareHouse_updated_at;
    }

    public String getWareHouse_address() {
        return wareHouse_address;
    }

    public void setWareHouse_address(String wareHouse_address) {
        this.wareHouse_address = wareHouse_address;
    }

    public String getWareHouse_name() {
        return wareHouse_name;
    }

    public void setWareHouse_name(String wareHouse_name) {
        this.wareHouse_name = wareHouse_name;
    }

    public String getWareHouse_created_at() {
        return wareHouse_created_at;
    }

    public void setWareHouse_created_at(String wareHouse_created_at) {
        this.wareHouse_created_at = wareHouse_created_at;
    }

    public String getWareHouse_longtitude() {
        return wareHouse_longtitude;
    }

    public void setWareHouse_longtitude(String wareHouse_longtitude) {
        this.wareHouse_longtitude = wareHouse_longtitude;
    }

    public String getWareHouse_latitude() {
        return wareHouse_latitude;
    }

    public void setWareHouse_latitude(String wareHouse_latitude) {
        this.wareHouse_latitude = wareHouse_latitude;
    }

    public String getWareHouse_user_email() {
        return wareHouse_user_email;
    }

    public void setWareHouse_user_email(String wareHouse_user_email) {
        this.wareHouse_user_email = wareHouse_user_email;
    }

    String package_width;
    String package_weight;
    String package_quantity;
    String package_cancel;
    String driver_id;
    String warehouse_user_id;
    String partner_user_id;
    String driver_device_token;
    String handle_by;
    String package_status;
    String rating;
    String signature;
    String sign_name;
    String feedback;
    private String package_created_at;

   /* private null handle_by;

    private null final_price;

    private String package_status;

    private String package_description;

    private null pop_image;

    private String[] payment_details;

    private String payment_status;

    private null feedback;

    private String[] driver_details;

    private String package_code;

    private null driver_device_token;

    private String created_at;

    private null driver_id;

    private null partner_user_id;

    private null signature;

    private null package_height;

    private String package_depth;

    private String fk_booking_id;

    private String package_quantity;

    private null warehouse_user_id;

    private null pod_image;

    private null sign_name;

    private String estimate_price;

    private String updated_at;

    private String package_est_value;

    private String package_width;

    private String package_id;

    private String send_selected_driver;

    private null package_cancel;

    private null rating;

    private String package_length;

    private String package_weight;*/

    String location_id;
    String fk_booking_id;
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

    public String getPackage_created_at() {
        return package_created_at;
    }

    public void setPackage_created_at(String package_created_at) {
        this.package_created_at = package_created_at;
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

    public String getFk_booking_id() {
        return fk_booking_id;
    }

    public void setFk_booking_id(String fk_booking_id) {
        this.fk_booking_id = fk_booking_id;
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
