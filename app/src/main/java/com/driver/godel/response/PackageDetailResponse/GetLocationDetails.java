package com.driver.godel.response.PackageDetailResponse;

/**
 * Created by root on 24/1/18.
 */

public class GetLocationDetails
{
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

    public String getAddress_note() {
        return address_note;
    }

    public void setAddress_note(String address_note) {
        this.address_note = address_note;
    }

    public String getDropoff_location_lat ()
    {
        return dropoff_location_lat;
    }

    public void setDropoff_location_lat (String dropoff_location_lat)
    {
        this.dropoff_location_lat = dropoff_location_lat;
    }

    public String getFk_booking_id ()
    {
        return fk_booking_id;
    }

    public void setFk_booking_id (String fk_booking_id)
    {
        this.fk_booking_id = fk_booking_id;
    }

    public String getPickup_location_lng ()
    {
        return pickup_location_lng;
    }

    public void setPickup_location_lng (String pickup_location_lng)
    {
        this.pickup_location_lng = pickup_location_lng;
    }

    public String getDropoff_location ()
    {
        return dropoff_location;
    }

    public void setDropoff_location (String dropoff_location)
    {
        this.dropoff_location = dropoff_location;
    }

    public String getFk_package_code ()
    {
        return fk_package_code;
    }

    public void setFk_package_code (String fk_package_code)
    {
        this.fk_package_code = fk_package_code;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getPickup_location_lat ()
    {
        return pickup_location_lat;
    }

    public void setPickup_location_lat (String pickup_location_lat)
    {
        this.pickup_location_lat = pickup_location_lat;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getDropoff_location_lng ()
    {
        return dropoff_location_lng;
    }

    public void setDropoff_location_lng (String dropoff_location_lng)
    {
        this.dropoff_location_lng = dropoff_location_lng;
    }

    public String getPickup_location ()
    {
        return pickup_location;
    }

    public void setPickup_location (String pickup_location)
    {
        this.pickup_location = pickup_location;
    }

    public String getPickup_street() {
        return pickup_street;
    }

    public void setPickup_street(String pickup_street) {
        this.pickup_street = pickup_street;
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

    public String getPickup_landmark() {
        return pickup_landmark;
    }

    public void setPickup_landmark(String pickup_landmark) {
        this.pickup_landmark = pickup_landmark;
    }

    public String getDropoff_location_datetime() {
        return dropoff_location_datetime;
    }

    public void setDropoff_location_datetime(String dropoff_location_datetime) {
        this.dropoff_location_datetime = dropoff_location_datetime;
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

    @Override
    public String toString()
    {
        return "ClassPojo [dropoff_location_lat = "+dropoff_location_lat+", pickup_street = "+pickup_street+", fk_booking_id = "+fk_booking_id+", pickup_location_lng = "+pickup_location_lng+", dropoff_house_no = "+dropoff_house_no+", pickup_house_no = "+pickup_house_no+", dropoff_location = "+dropoff_location+", pickup_landmark = "+pickup_landmark+", fk_package_code = "+fk_package_code+", id = "+id+", updated_at = "+updated_at+", dropoff_location_datetime = "+dropoff_location_datetime+", pickup_location_lat = "+pickup_location_lat+", dropoff_street = "+dropoff_street+", created_at = "+created_at+", dropoff_landmark = "+dropoff_landmark+", pickup_datetime = "+pickup_datetime+", dropoff_location_lng = "+dropoff_location_lng+", pickup_location = "+pickup_location+"]";
    }
}
