package com.driver.godel.response.HistoryResponse;

import java.util.List;

/**
 * Created by Ajay2.Sharma on 30-Aug-17.
 */

public class Data
{
    //WareHouse Changes
    private String is_warehouse_dropoff_check;

    private String is_warehouse_pickup;

    private String is_warehouse_dropoff;

    private List<Warehouse_pickup_address> warehouse_pickup_address;

    private List<Warehouse_drop_off_address> warehouse_drop_off_address;

    private String preferred_deliver_time;

    private String delivery_type;

    private String booking_id;

    private List<User_details> user_details;

    private List<Location_details> location_details;

    private String is_cancel;

    private Package_details package_details;

    private String booking_type;

    private String booking_delivery_datetime;

    private String delivery_session;

    private String booking_note;

    private String booking_code;

    private String booking_pickup_datetime;

    private Recurring_details recurring_details;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Recurring_details getRecurring_details() {
        return recurring_details;
    }

    public void setRecurring_details(Recurring_details recurring_details) {
        this.recurring_details = recurring_details;
    }




    public String getIs_warehouse_dropoff_check() {
        return is_warehouse_dropoff_check;
    }

    public void setIs_warehouse_dropoff_check(String is_warehouse_dropoff_check) {
        this.is_warehouse_dropoff_check = is_warehouse_dropoff_check;
    }

    public String getIs_warehouse_pickup() {
        return is_warehouse_pickup;
    }

    public void setIs_warehouse_pickup(String is_warehouse_pickup) {
        this.is_warehouse_pickup = is_warehouse_pickup;
    }

    public String getIs_warehouse_dropoff() {
        return is_warehouse_dropoff;
    }

    public void setIs_warehouse_dropoff(String is_warehouse_dropoff) {
        this.is_warehouse_dropoff = is_warehouse_dropoff;
    }

    public List<Warehouse_pickup_address> getWarehouse_pickup_address() {
        return warehouse_pickup_address;
    }

    public void setWarehouse_pickup_address(List<Warehouse_pickup_address> warehouse_pickup_address) {
        this.warehouse_pickup_address = warehouse_pickup_address;
    }

    public List<Warehouse_drop_off_address> getWarehouse_drop_off_address() {
        return warehouse_drop_off_address;
    }

    public void setWarehouse_drop_off_address(List<Warehouse_drop_off_address> warehouse_drop_off_address) {
        this.warehouse_drop_off_address = warehouse_drop_off_address;
    }

    public String getPreferred_deliver_time() {
        return preferred_deliver_time;
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

    public void setPreferred_deliver_time(String preferred_deliver_time) {
        this.preferred_deliver_time = preferred_deliver_time;
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

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }


    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public Package_details getPackage_details() {
        return package_details;
    }

    public void setPackage_details(Package_details package_details) {
        this.package_details = package_details;
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
        return "ClassPojo [preferred_deliver_time = " + preferred_deliver_time + ", delivery_type = " + delivery_type + ", booking_id = " + booking_id + ", user_details = " + user_details + ", location_details = " + location_details + ", is_cancel = " + is_cancel + ", package_details = " + package_details + ", booking_type = " + booking_type + ", booking_delivery_datetime = " + booking_delivery_datetime + ", delivery_session = " + delivery_session + ", booking_note = " + booking_note + ", booking_code = " + booking_code + ", booking_pickup_datetime = " + booking_pickup_datetime + "]";
    }
}
