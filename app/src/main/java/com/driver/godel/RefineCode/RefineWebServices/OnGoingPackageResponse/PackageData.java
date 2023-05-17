package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by root on 24/1/18.
 */

public class PackageData implements Parcelable
{
    private String message;

    private String current_driver_id;
    //Check WaeHouse Changes
    private String is_warehouse_dropoff_check;

    private String is_warehouse_pickup;

    private String is_warehouse_dropoff;

    private String min_delivery_time;

    private User_details user_details;

    private GetPackageDetails package_details;

    private List<GetDriverDetails> driver_details;

    private GetBookingDetails booking_details;

    private GetLocationDetails location_details;

    private List<String> payment_details;

    private List<Warehouse_details> warehouse_details;

    private RecurringDetails recurring_details;

    private List<Package_tracking_details> package_tracking_details;

    private List<Warehouse_pickup_address> warehouse_pickup_address;

    private List<Warehouse_drop_off_address> warehouse_drop_off_address;


    protected PackageData(Parcel in) {
        message = in.readString();
        current_driver_id = in.readString();
        is_warehouse_dropoff_check = in.readString();
        is_warehouse_pickup = in.readString();
        is_warehouse_dropoff = in.readString();
        min_delivery_time = in.readString();
        user_details = in.readParcelable(User_details.class.getClassLoader());
        package_details = in.readParcelable(GetPackageDetails.class.getClassLoader());
        driver_details = in.createTypedArrayList(GetDriverDetails.CREATOR);
        booking_details = in.readParcelable(GetBookingDetails.class.getClassLoader());
        location_details = in.readParcelable(GetLocationDetails.class.getClassLoader());
        payment_details = in.createStringArrayList();
        warehouse_details = in.createTypedArrayList(Warehouse_details.CREATOR);
        recurring_details = in.readParcelable(RecurringDetails.class.getClassLoader());
        package_tracking_details = in.createTypedArrayList(Package_tracking_details.CREATOR);
        warehouse_pickup_address = in.createTypedArrayList(Warehouse_pickup_address.CREATOR);
        warehouse_drop_off_address = in.createTypedArrayList(Warehouse_drop_off_address.CREATOR);
    }

    public static final Creator<PackageData> CREATOR = new Creator<PackageData>() {
        @Override
        public PackageData createFromParcel(Parcel in) {
            return new PackageData(in);
        }

        @Override
        public PackageData[] newArray(int size) {
            return new PackageData[size];
        }
    };

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

    public void setBooking_details(GetBookingDetails booking_details)    {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(current_driver_id);
        dest.writeString(is_warehouse_dropoff_check);
        dest.writeString(is_warehouse_pickup);
        dest.writeString(is_warehouse_dropoff);
        dest.writeString(min_delivery_time);
        dest.writeParcelable(user_details, flags);
        dest.writeParcelable(package_details, flags);
        dest.writeTypedList(driver_details);
        dest.writeParcelable(booking_details, flags);
        dest.writeParcelable(location_details, flags);
        dest.writeStringList(payment_details);
        dest.writeTypedList(warehouse_details);
        dest.writeParcelable(recurring_details, flags);
        dest.writeTypedList(package_tracking_details);
        dest.writeTypedList(warehouse_pickup_address);
        dest.writeTypedList(warehouse_drop_off_address);
    }
}
