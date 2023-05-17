package com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp;

import java.util.List;

/**
 * Created by root on 23/1/18.
 */

public class PendingPickupPojo
{
    private String min_delivery_time;

    private String preferred_deliver_time;

    private String delivery_type;

    private String booking_id;

    private List<User_details> user_details;

    private List<Location_details> location_details;

    private List<Recurring_setting_details> recurrence_details;

    private List<Warehouse_details> warehouse_details;

    //Ware House Details
    private String user_unique_id;

    private String DropOffWarehouseAddress;
    private String DropOffWarehouseName;
    private String DropOffWarehouseLongitude;
    private String DropOffWarehouseLatitude;
    private String DropOffWarehouseUser_email;

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

    //Recuurence Details
    private String recurring_id;
    private String is_deleted;
    private String recurring_day_time;
    private String recurring_week;
    private String recurring_status;
    private String recurring_pickup_type;
    private String recurring_day;

    private String is_cancel;
    private Package_details package_details;
    private String booking_type;
    private String booking_delivery_datetime;
    private String delivery_session;
    private String booking_note;
    private String booking_code;
    private String booking_pickup_datetime;
    private String is_verified_by_admin;
    private String remember_token;
    private String vehicle_type;
    private String user_app_notification;
    private String user_email;
    private String type;
    private String country_code;


    private String phone_verify;
    private String password;
    private String user_id;

    private String trading_address;
    private String credit_limit;
    private String company_name;
    private String trading_licence_no;
    private String token;
    private String email_verify;
    private String name;
    private String user_created_at;
    private String gender;
    private String user_image;
    private String sinch_code;
    private String login_type;
    private String company_registration_no;
    private String user_status;
    private String agent_id;
    private String add_by_agent;
    private String nature_of_business;
    private String user_updated_at;
    private String validate_code;
    private String source;
    private String user_phone;
    private String dob;
    private String secondary_contact_no;
    private String facebook_id;
    //Package Details
    private String handle_by;
    private String final_price;
    private String package_status;
    private String package_description;
    private String pop_image;
    private List<String> payment_details;
    private String payment_status;
    private String feedback;
    private String package_code;
    private String driver_device_token;
    private String package_created_at;
    private String driver_id;
    private String partner_user_id;
    private String signature;
    private String package_height;
    private String package_depth;
    private String package_fk_booking_id;
    private String package_quantity;
    private String warehouse_user_id;
    private String pod_image;
    private String sign_name;
    private String estimate_price;
    private String package_updated_at;
    private String package_est_value;
    private String package_width;
    private String package_id;
    private String send_selected_driver;
    private String package_cancel;
    private String rating;
    private String package_length;
    private String package_weight;

    //Location Details
    private String address_note;
    private String dropoff_location_lat;
    private String pickup_street;
    private String fk_booking_id;
    private String pickup_location_lng;
    private String dropoff_house_no;
    private String pickup_house_no;
    private String dropoff_location;
    private String pickup_landmark;
    private String fk_package_code;
    private String id;
    private String updated_at;
    private String dropoff_location_datetime;
    private String pickup_location_lat;
    private String dropoff_street;
    private String created_at;
    private String dropoff_landmark;
    private String pickup_datetime;
    private String dropoff_location_lng;
    private String pickup_location;

    //Ware House Address
    private String is_warehouse_dropoff;
    private String is_warehouse_pickup;

    private String PickupWarehouseAddress;
    private String PickupWarehouseName;
    private String PickupWarehouseLongitude;
    private String PickupWarehouseLatitude;
    private String PickupWarehouseUser_email;



    private String is_first_round_completed;
    private String is_round_trip;

    public String getPickupWarehouseAddress() {
        return PickupWarehouseAddress;
    }

    public void setPickupWarehouseAddress(String pickupWarehouseAddress) {
        PickupWarehouseAddress = pickupWarehouseAddress;
    }

    public String getPickupWarehouseName() {
        return PickupWarehouseName;
    }

    public void setPickupWarehouseName(String pickupWarehouseName) {
        PickupWarehouseName = pickupWarehouseName;
    }
    public String getIs_first_round_completed() {
        return is_first_round_completed;
    }

    public void setIs_first_round_completed(String is_first_round_completed) {
        this.is_first_round_completed = is_first_round_completed;
    }

    public String getIs_round_trip() {
        return is_round_trip;
    }

    public void setIs_round_trip(String is_round_trip) {
        this.is_round_trip = is_round_trip;
    }

    public String getPickupWarehouseLongitude() {
        return PickupWarehouseLongitude;
    }

    public void setPickupWarehouseLongitude(String pickupWarehouseLongitude) {
        PickupWarehouseLongitude = pickupWarehouseLongitude;
    }

    public String getPickupWarehouseLatitude() {
        return PickupWarehouseLatitude;
    }

    public void setPickupWarehouseLatitude(String pickupWarehouseLatitude) {
        PickupWarehouseLatitude = pickupWarehouseLatitude;
    }

    public String getPickupWarehouseUser_email() {
        return PickupWarehouseUser_email;
    }

    public void setPickupWarehouseUser_email(String pickupWarehouseUser_email) {
        PickupWarehouseUser_email = pickupWarehouseUser_email;
    }
    //    private String whAddressid;
//
//    private String whAddressState_id;
//
//    private String whAddressPhone;
//
//    private String whAddressUpdated_at;
//
//    private String whAddressAddress;
//
//    private String whAddressName;
//
//    private String whAddressCreated_at;
//
//    private String whAddressLongtitude;
//
//    private String whAddressLatitude;
//
//    private String whAddressUser_email;


    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getAddress_note() {
        return address_note;
    }

    public void setAddress_note(String address_note) {
        this.address_note = address_note;
    }

    public String getIs_warehouse_dropoff()
    {
        return is_warehouse_dropoff;
    }

    public void setIs_warehouse_dropoff(String is_warehouse_dropoff) {
        this.is_warehouse_dropoff = is_warehouse_dropoff;
    }

    public String getIs_warehouse_pickup() {
        return is_warehouse_pickup;
    }

    public void setIs_warehouse_pickup(String is_warehouse_pickup) {
        this.is_warehouse_pickup = is_warehouse_pickup;
    }


    public String getDropOffWarehouseAddress() {
        return DropOffWarehouseAddress;
    }

    public void setDropOffWarehouseAddress(String dropOffWarehouseAddress) {
        DropOffWarehouseAddress = dropOffWarehouseAddress;
    }

    public String getDropOffWarehouseName() {
        return DropOffWarehouseName;
    }

    public void setDropOffWarehouseName(String dropOffWarehouseName) {
        DropOffWarehouseName = dropOffWarehouseName;
    }

    public String getDropOffWarehouseLongitude() {
        return DropOffWarehouseLongitude;
    }

    public void setDropOffWarehouseLongitude(String dropOffWarehouseLongitude) {
        DropOffWarehouseLongitude = dropOffWarehouseLongitude;
    }

    public String getDropOffWarehouseLatitude() {
        return DropOffWarehouseLatitude;
    }

    public void setDropOffWarehouseLatitude(String dropOffWarehouseLatitude) {
        DropOffWarehouseLatitude = dropOffWarehouseLatitude;
    }

    public String getDropOffWarehouseUser_email() {
        return DropOffWarehouseUser_email;
    }

    public void setDropOffWarehouseUser_email(String dropOffWarehouseUser_email) {
        DropOffWarehouseUser_email = dropOffWarehouseUser_email;
    }

//    public String getWhAddressid() {
//        return whAddressid;
//    }
//
//    public void setWhAddressid(String whAddressid) {
//        this.whAddressid = whAddressid;
//    }
//
//    public String getWhAddressState_id() {
//        return whAddressState_id;
//    }
//
//    public void setWhAddressState_id(String whAddressState_id) {
//        this.whAddressState_id = whAddressState_id;
//    }
//
//    public String getWhAddressPhone() {
//        return whAddressPhone;
//    }
//
//    public void setWhAddressPhone(String whAddressPhone) {
//        this.whAddressPhone = whAddressPhone;
//    }
//
//    public String getWhAddressUpdated_at() {
//        return whAddressUpdated_at;
//    }
//
//    public void setWhAddressUpdated_at(String whAddressUpdated_at) {
//        this.whAddressUpdated_at = whAddressUpdated_at;
//    }
//
//    public String getWhAddressAddress() {
//        return whAddressAddress;
//    }
//
//    public void setWhAddressAddress(String whAddressAddress) {
//        this.whAddressAddress = whAddressAddress;
//    }
//
//    public String getWhAddressName() {
//        return whAddressName;
//    }
//
//    public void setWhAddressName(String whAddressName) {
//        this.whAddressName = whAddressName;
//    }
//
//    public String getWhAddressCreated_at() {
//        return whAddressCreated_at;
//    }
//
//    public void setWhAddressCreated_at(String whAddressCreated_at) {
//        this.whAddressCreated_at = whAddressCreated_at;
//    }
//
//    public String getWhAddressLongtitude() {
//        return whAddressLongtitude;
//    }
//
//    public void setWhAddressLongtitude(String whAddressLongtitude) {
//        this.whAddressLongtitude = whAddressLongtitude;
//    }
//
//    public String getWhAddressLatitude() {
//        return whAddressLatitude;
//    }
//
//    public void setWhAddressLatitude(String whAddressLatitude) {
//        this.whAddressLatitude = whAddressLatitude;
//    }
//
//    public String getWhAddressUser_email() {
//        return whAddressUser_email;
//    }
//
//    public void setWhAddressUser_email(String whAddressUser_email) {
//        this.whAddressUser_email = whAddressUser_email;
//    }

    public List<Warehouse_details> getWarehouse_details() {
        return warehouse_details;
    }

    public void setWarehouse_details(List<Warehouse_details> warehouse_details) {
        this.warehouse_details = warehouse_details;
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

    public String getMin_delivery_time() {
        return min_delivery_time;
    }

    public void setMin_delivery_time(String min_delivery_time) {
        this.min_delivery_time = min_delivery_time;
    }

    public List<Recurring_setting_details> getRecurrence_details() {
        return recurrence_details;
    }

    public void setRecurrence_details(List<Recurring_setting_details> recurrence_details) {
        this.recurrence_details = recurrence_details;
    }

    public String getRecurring_id() {
        return recurring_id;
    }

    public void setRecurring_id(String recurring_id) {
        this.recurring_id = recurring_id;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
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

    public String getRecurring_status() {
        return recurring_status;
    }

    public void setRecurring_status(String recurring_status) {
        this.recurring_status = recurring_status;
    }

    public String getRecurring_pickup_type() {
        return recurring_pickup_type;
    }

    public void setRecurring_pickup_type(String recurring_pickup_type) {
        this.recurring_pickup_type = recurring_pickup_type;
    }

    public String getRecurring_day() {
        return recurring_day;
    }

    public void setRecurring_day(String recurring_day) {
        this.recurring_day = recurring_day;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_created_at() {
        return user_created_at;
    }

    public void setUser_created_at(String user_created_at) {
        this.user_created_at = user_created_at;
    }

    public String getUser_updated_at() {
        return user_updated_at;
    }

    public void setUser_updated_at(String user_updated_at) {
        this.user_updated_at = user_updated_at;
    }

    public String getPackage_created_at() {
        return package_created_at;
    }

    public void setPackage_created_at(String package_created_at) {
        this.package_created_at = package_created_at;
    }

    public String getPackage_fk_booking_id() {
        return package_fk_booking_id;
    }

    public void setPackage_fk_booking_id(String package_fk_booking_id) {
        this.package_fk_booking_id = package_fk_booking_id;
    }

    public String getPackage_updated_at() {
        return package_updated_at;
    }

    public void setPackage_updated_at(String package_updated_at) {
        this.package_updated_at = package_updated_at;
    }

    public void setPayment_details(List<String> payment_details) {
        this.payment_details = payment_details;
    }

    public String getDropoff_location_lat() {
        return dropoff_location_lat;
    }

    public void setDropoff_location_lat(String dropoff_location_lat) {
        this.dropoff_location_lat = dropoff_location_lat;
    }

    public String getPickup_street() {
        return pickup_street;
    }

    public void setPickup_street(String pickup_street) {
        this.pickup_street = pickup_street;
    }

    public String getPickup_location_lng() {
        return pickup_location_lng;
    }

    public void setPickup_location_lng(String pickup_location_lng) {
        this.pickup_location_lng = pickup_location_lng;
    }

    public String getDropoff_house_no() {
        return dropoff_house_no;
    }

    public void setDropoff_house_no(String dropoff_house_no) {
        this.dropoff_house_no = dropoff_house_no;
    }

    public String getPickup_house_no() {
        return pickup_house_no;
    }

    public void setPickup_house_no(String pickup_house_no) {
        this.pickup_house_no = pickup_house_no;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getPickup_landmark() {
        return pickup_landmark;
    }

    public void setPickup_landmark(String pickup_landmark) {
        this.pickup_landmark = pickup_landmark;
    }

    public String getFk_package_code() {
        return fk_package_code;
    }

    public void setFk_package_code(String fk_package_code) {
        this.fk_package_code = fk_package_code;
    }

    public String getDropoff_location_datetime() {
        return dropoff_location_datetime;
    }

    public void setDropoff_location_datetime(String dropoff_location_datetime) {
        this.dropoff_location_datetime = dropoff_location_datetime;
    }

    public String getPickup_location_lat() {
        return pickup_location_lat;
    }

    public void setPickup_location_lat(String pickup_location_lat) {
        this.pickup_location_lat = pickup_location_lat;
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

    public String getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(String pickup_datetime) {
        this.pickup_datetime = pickup_datetime;
    }

    public String getDropoff_location_lng() {
        return dropoff_location_lng;
    }

    public void setDropoff_location_lng(String dropoff_location_lng) {
        this.dropoff_location_lng = dropoff_location_lng;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getHandle_by() {
        return handle_by;
    }

    public void setHandle_by(String handle_by) {
        this.handle_by = handle_by;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getPackage_description() {
        return package_description;
    }

    public void setPackage_description(String package_description) {
        this.package_description = package_description;
    }

    public String getPop_image() {
        return pop_image;
    }

    public void setPop_image(String pop_image) {
        this.pop_image = pop_image;
    }

    public List<String> getPayment_details()
    {
        return payment_details;
    }

    public String getPayment_status()
    {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPackage_code() {
        return package_code;
    }

    public void setPackage_code(String package_code) {
        this.package_code = package_code;
    }

    public String getDriver_device_token() {
        return driver_device_token;
    }

    public void setDriver_device_token(String driver_device_token) {
        this.driver_device_token = driver_device_token;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public void setPartner_user_id(String partner_user_id) {
        this.partner_user_id = partner_user_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPackage_height() {
        return package_height;
    }

    public void setPackage_height(String package_height) {
        this.package_height = package_height;
    }

    public String getPackage_depth() {
        return package_depth;
    }

    public void setPackage_depth(String package_depth) {
        this.package_depth = package_depth;
    }

    public String getFk_booking_id() {
        return fk_booking_id;
    }

    public void setFk_booking_id(String fk_booking_id) {
        this.fk_booking_id = fk_booking_id;
    }

    public String getPackage_quantity() {
        return package_quantity;
    }

    public void setPackage_quantity(String package_quantity) {
        this.package_quantity = package_quantity;
    }

    public String getWarehouse_user_id() {
        return warehouse_user_id;
    }

    public void setWarehouse_user_id(String warehouse_user_id) {
        this.warehouse_user_id = warehouse_user_id;
    }

    public String getPod_image() {
        return pod_image;
    }

    public void setPod_image(String pod_image) {
        this.pod_image = pod_image;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getEstimate_price() {
        return estimate_price;
    }

    public void setEstimate_price(String estimate_price) {
        this.estimate_price = estimate_price;
    }

    public String getPackage_est_value() {
        return package_est_value;
    }

    public void setPackage_est_value(String package_est_value) {
        this.package_est_value = package_est_value;
    }

    public String getPackage_width() {
        return package_width;
    }

    public void setPackage_width(String package_width) {
        this.package_width = package_width;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getSend_selected_driver() {
        return send_selected_driver;
    }

    public void setSend_selected_driver(String send_selected_driver) {
        this.send_selected_driver = send_selected_driver;
    }

    public String getPackage_cancel() {
        return package_cancel;
    }

    public void setPackage_cancel(String package_cancel) {
        this.package_cancel = package_cancel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPackage_length() {
        return package_length;
    }

    public void setPackage_length(String package_length) {
        this.package_length = package_length;
    }

    public String getPackage_weight() {
        return package_weight;
    }

    public void setPackage_weight(String package_weight) {
        this.package_weight = package_weight;
    }

    public String getIs_verified_by_admin() {
        return is_verified_by_admin;
    }

    public void setIs_verified_by_admin(String is_verified_by_admin) {
        this.is_verified_by_admin = is_verified_by_admin;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getUser_app_notification() {
        return user_app_notification;
    }

    public void setUser_app_notification(String user_app_notification) {
        this.user_app_notification = user_app_notification;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_verify() {
        return phone_verify;
    }

    public void setPhone_verify(String phone_verify) {
        this.phone_verify = phone_verify;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrading_address() {
        return trading_address;
    }

    public void setTrading_address(String trading_address) {
        this.trading_address = trading_address;
    }

    public String getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(String credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTrading_licence_no() {
        return trading_licence_no;
    }

    public void setTrading_licence_no(String trading_licence_no) {
        this.trading_licence_no = trading_licence_no;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getSinch_code() {
        return sinch_code;
    }

    public void setSinch_code(String sinch_code) {
        this.sinch_code = sinch_code;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getCompany_registration_no() {
        return company_registration_no;
    }

    public void setCompany_registration_no(String company_registration_no) {
        this.company_registration_no = company_registration_no;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAdd_by_agent() {
        return add_by_agent;
    }

    public void setAdd_by_agent(String add_by_agent) {
        this.add_by_agent = add_by_agent;
    }

    public String getNature_of_business() {
        return nature_of_business;
    }

    public void setNature_of_business(String nature_of_business) {
        this.nature_of_business = nature_of_business;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(String validate_code) {
        this.validate_code = validate_code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSecondary_contact_no() {
        return secondary_contact_no;
    }

    public void setSecondary_contact_no(String secondary_contact_no) {
        this.secondary_contact_no = secondary_contact_no;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getDelivery_type ()
    {
        return delivery_type;
    }

    public void setDelivery_type (String delivery_type)
    {
        this.delivery_type = delivery_type;
    }

    public String getBooking_id ()
    {
        return booking_id;
    }

    public void setBooking_id (String booking_id)
    {
        this.booking_id = booking_id;
    }

    public String getPreferred_deliver_time()
    {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time(String preferred_deliver_time)
    {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public List<User_details> getUser_details()
    {
        return user_details;
    }

    public void setUser_details(List<User_details> user_details)
    {
        this.user_details = user_details;
    }

    public List<Location_details> getLocation_details()
    {
        return location_details;
    }

    public void setLocation_details(List<Location_details> location_details)
    {
        this.location_details = location_details;
    }

    public String getDelivery_session()
    {
        return delivery_session;
    }

    public void setDelivery_session(String delivery_session)
    {
        this.delivery_session = delivery_session;
    }

    public String getIs_cancel ()
    {
        return is_cancel;
    }

    public void setIs_cancel (String is_cancel)
    {
        this.is_cancel = is_cancel;
    }

    public Package_details getPackage_details ()
    {
        return package_details;
    }

    public void setPackage_details (Package_details package_details)
    {
        this.package_details = package_details;
    }

    public String getBooking_type ()
    {
        return booking_type;
    }

    public void setBooking_type (String booking_type)
    {
        this.booking_type = booking_type;
    }

    public String getBooking_delivery_datetime ()
    {
        return booking_delivery_datetime;
    }

    public void setBooking_delivery_datetime (String booking_delivery_datetime)
    {
        this.booking_delivery_datetime = booking_delivery_datetime;
    }

    public String getBooking_note ()
    {
        return booking_note;
    }

    public void setBooking_note (String booking_note)
    {
        this.booking_note = booking_note;
    }

    public String getBooking_code ()
    {
        return booking_code;
    }

    public void setBooking_code (String booking_code)
    {
        this.booking_code = booking_code;
    }

    public String getBooking_pickup_datetime ()
    {
        return booking_pickup_datetime;
    }

    public void setBooking_pickup_datetime (String booking_pickup_datetime)
    {
        this.booking_pickup_datetime = booking_pickup_datetime;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [preferred_deliver_time = "+preferred_deliver_time+", country_code = "+country_code+",delivery_type = "+delivery_type+", booking_id = "+booking_id+", user_details = "+user_details+", location_details = "+location_details+", is_cancel = "+is_cancel+", package_details = "+package_details+", booking_type = "+booking_type+", booking_delivery_datetime = "+booking_delivery_datetime+", delivery_session = "+delivery_session+", booking_note = "+booking_note+", booking_code = "+booking_code+", booking_pickup_datetime = "+booking_pickup_datetime+"]";
    }
}
