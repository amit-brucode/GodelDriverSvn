package com.driver.godel.response.PackageDetailResponse;

import com.driver.godel.response.pendingResponse.Warehouse_details;

import java.util.List;

/**
 * Created by root on 24/1/18.
 */

public class PackageData
{
    private String message;

    private User_details user_details;
    private String current_driver_id;
    //Check WaeHouse Changes
    private String is_warehouse_dropoff_check;

    private String is_warehouse_pickup;

    private String is_warehouse_dropoff;
    private List<Package_tracking_details> package_tracking_details;

    private List<com.driver.godel.response.PackageDetailResponse.Warehouse_pickup_address> warehouse_pickup_address;

    private List<com.driver.godel.response.PackageDetailResponse.Warehouse_drop_off_address> warehouse_drop_off_address;

    public List<Package_tracking_details> getPackage_tracking_details() {
        return package_tracking_details;
    }

    public void setPackage_tracking_details(List<Package_tracking_details> package_tracking_details) {
        this.package_tracking_details = package_tracking_details;
    }

    public String getCurrent_driver_id() {
        return current_driver_id;
    }

    public void setCurrent_driver_id(String current_driver_id) {
        this.current_driver_id = current_driver_id;
    }

    private String min_delivery_time;

    private GetPackageDetails package_details;

    private List<GetDriverDetails> driver_details;

    private GetBookingDetails booking_details;

    private GetLocationDetails location_details;

    private List<String> payment_details;

    private List<Warehouse_details> warehouse_details;

    private RecurringDetails recurring_details;

    public User_details getUser_details() {
        return user_details;
    }

    public void setUser_details(User_details user_details) {
        this.user_details = user_details;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
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

    public List<com.driver.godel.response.PackageDetailResponse.Warehouse_pickup_address> getWarehouse_pickup_address() {
        return warehouse_pickup_address;
    }

    public void setWarehouse_pickup_address(List<com.driver.godel.response.PackageDetailResponse.Warehouse_pickup_address> warehouse_pickup_address) {
        this.warehouse_pickup_address = warehouse_pickup_address;
    }

    public List<com.driver.godel.response.PackageDetailResponse.Warehouse_drop_off_address> getWarehouse_drop_off_address() {
        return warehouse_drop_off_address;
    }

    public void setWarehouse_drop_off_address(List<com.driver.godel.response.PackageDetailResponse.Warehouse_drop_off_address> warehouse_drop_off_address) {
        this.warehouse_drop_off_address = warehouse_drop_off_address;
    }

    public List<Warehouse_details> getWarehouse_details() {
        return warehouse_details;
    }

    public String getMin_delivery_time() {
        return min_delivery_time;
    }

    public void setMin_delivery_time(String min_delivery_time) {
        this.min_delivery_time = min_delivery_time;
    }

    public void setWarehouse_details(List<Warehouse_details> warehouse_details) {
        this.warehouse_details = warehouse_details;
    }

    public RecurringDetails getRecurring_details() {
        return recurring_details;
    }

    public void setRecurring_details(RecurringDetails recurring_details) {
        this.recurring_details = recurring_details;
    }

    public GetPackageDetails getPackage_details()
    {
        return package_details;
    }

    public void setPackage_details(GetPackageDetails package_details) {
        this.package_details = package_details;
    }

    public List<GetDriverDetails> getDriver_details() {
        return driver_details;
    }

    public void setDriver_details(List<GetDriverDetails> driver_details) {
        this.driver_details = driver_details;
    }

    public GetBookingDetails getBooking_details()
    {
        return booking_details;
    }

    public void setBooking_details(GetBookingDetails booking_details)
    {
        this.booking_details = booking_details;
    }

    public GetLocationDetails getLocation_details() {
        return location_details;
    }

    public void setLocation_details(GetLocationDetails location_details) {
        this.location_details = location_details;
    }

    public List<String> getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(List<String> payment_details) {
        this.payment_details = payment_details;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [package_details = "+package_details+", driver_details = "+driver_details+", booking_details = "+booking_details+", location_details = "+location_details+", payment_details = "+payment_details+"]";
    }
}
