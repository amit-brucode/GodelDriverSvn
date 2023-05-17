package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 30/6/17.
 */

public class Location_details
{
    private String dropoff_location_lat;

    private String id;

    private String updated_at;

    private String fk_booking_id;

    private String pickup_location_lng;

    private String dropoff_location_datetime;

    private String pickup_location_lat;

    private String dropoff_location;

    private String created_at;

    private String pickup_datetime;

    private String dropoff_location_lng;

    private String pickup_location;

    public String getDropoff_location_lat ()
    {
        return dropoff_location_lat;
    }

    public void setDropoff_location_lat (String dropoff_location_lat)
    {
        this.dropoff_location_lat = dropoff_location_lat;
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

    public String getDropoff_location_datetime ()
    {
        return dropoff_location_datetime;
    }

    public void setDropoff_location_datetime (String dropoff_location_datetime)
    {
        this.dropoff_location_datetime = dropoff_location_datetime;
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

    public String getPickup_datetime ()
    {
        return pickup_datetime;
    }

    public void setPickup_datetime (String pickup_datetime)
    {
        this.pickup_datetime = pickup_datetime;
    }

    public String getDropoff_location_lng ()
    {
        return dropoff_location_lng;
    }

    public void setDropoff_location_lng (String dropoff_location_lng)
    {
        this.dropoff_location_lng = dropoff_location_lng;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [dropoff_location_lat = "+dropoff_location_lat+", id = "+id+", updated_at = "+updated_at+", fk_booking_id = "+fk_booking_id+", pickup_location_lng = "+pickup_location_lng+", dropoff_location_datetime = "+dropoff_location_datetime+", pickup_location_lat = "+pickup_location_lat+", dropoff_location = "+dropoff_location+", created_at = "+created_at+", pickup_datetime = "+pickup_datetime+", dropoff_location_lng = "+dropoff_location_lng+", pickup_location = "+pickup_location+"]";
    }
}

