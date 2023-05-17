package com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp;

import java.util.List;

/**
 * Created by Ajay2.Sharma on 7/13/2018.
 */

public class Data {

    public String message;

    private String preferred_deliver_time;

    private String is_warehouse_dropoff;

    private String delivery_type;

    private String booking_id;

    private List<User_details> user_details;

    private List<Location_details> location_details;

    private Package_details package_details;

    private String is_cancel;

    private String min_delivery_time;

    private String booking_type;

    private String booking_delivery_datetime;

    private String delivery_session;

    private String booking_note;

    private String booking_code;

    private List<Warehouse_details> warehouse_details;

    private String booking_pickup_datetime;

    public List<Recurring_setting_details> getRecurring_setting_details() {
        return recurring_setting_details;
    }

    public void setRecurring_setting_details(List<Recurring_setting_details> recurring_setting_details) {
        this.recurring_setting_details = recurring_setting_details;
    }

    private List<Recurring_setting_details> recurring_setting_details;


    public String getPreferred_deliver_time() {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time(String preferred_deliver_time) {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public String getIs_warehouse_dropoff() {
        return is_warehouse_dropoff;
    }

    public void setIs_warehouse_dropoff(String is_warehouse_dropoff) {
        this.is_warehouse_dropoff = is_warehouse_dropoff;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public List<User_details> getUser_details() {
        return user_details;
    }

    public void setUser_details(List<User_details> user_details) {
        this.user_details = user_details;
    }

    public List<Location_details> getLocation_details() {
        return location_details;
    }

    public void setLocation_details(List<Location_details> location_details) {
        this.location_details = location_details;
    }

    public List<Warehouse_details> getWarehouse_details() {
        return warehouse_details;
    }

    public void setWarehouse_details(List<Warehouse_details> warehouse_details) {
        this.warehouse_details = warehouse_details;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public Package_details getPackage_details() {
        return package_details;
    }

    public void setPackage_details(Package_details package_details) {
        this.package_details = package_details;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getMin_delivery_time() {
        return min_delivery_time;
    }

    public void setMin_delivery_time(String min_delivery_time) {
        this.min_delivery_time = min_delivery_time;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
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

    public String getBooking_note() {
        return booking_note;
    }

    public void setBooking_note(String booking_note) {
        this.booking_note = booking_note;
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

    @Override
    public String toString() {
        return "ClassPojo [preferred_deliver_time = " + preferred_deliver_time + ", is_warehouse_dropoff = " + is_warehouse_dropoff + ", delivery_type = " + delivery_type + ", booking_id = " + booking_id + ", user_details = " + user_details + ", location_details = " + location_details + ", package_details = " + package_details + ", is_cancel = " + is_cancel + ", min_delivery_time = " + min_delivery_time + ", booking_type = " + booking_type + ", booking_delivery_datetime = " + booking_delivery_datetime + ", delivery_session = " + delivery_session + ", booking_note = " + booking_note + ", booking_code = " + booking_code + ", warehouse_details = " + warehouse_details + ", booking_pickup_datetime = " + booking_pickup_datetime + "]";
    }
}
