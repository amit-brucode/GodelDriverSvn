package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 30/6/17.
 */

public class BookingData
{
    private String preferred_deliver_time;

    private Package_details package_details;

    private String is_cancel;

    private String booking_id;

    private User_details user_details;

    private String booking_type;

    private String booking_note;

    private Location_details location_details;

    public String getPreferred_deliver_time() {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time(String preferred_deliver_time) {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public String getBooking_note() {
        return booking_note;
    }

    public void setBooking_note(String booking_note) {
        this.booking_note = booking_note;
    }

    public Package_details getPackage_details ()
    {
        return package_details;
    }

    public void setPackage_details (Package_details package_details)
    {
        this.package_details = package_details;
    }

    public String getIs_cancel ()
    {
        return is_cancel;
    }

    public void setIs_cancel (String is_cancel)
    {
        this.is_cancel = is_cancel;
    }

    public String getBooking_id ()
    {
        return booking_id;
    }

    public void setBooking_id (String booking_id)
    {
        this.booking_id = booking_id;
    }

    public User_details getUser_details ()
    {
        return user_details;
    }

    public void setUser_details (User_details user_details)
    {
        this.user_details = user_details;
    }

    public String getBooking_type ()
    {
        return booking_type;
    }

    public void setBooking_type (String booking_type)
    {
        this.booking_type = booking_type;
    }



    public Location_details getLocation_details ()
    {
        return location_details;
    }

    public void setLocation_details (Location_details location_details)
    {
        this.location_details = location_details;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [preferred_deliver_time = "+preferred_deliver_time+", package_details = "+package_details+", is_cancel = "+is_cancel+", booking_id = "+booking_id+", user_details = "+user_details+", booking_type = "+booking_type+", booking_note = "+booking_note+", location_details = "+location_details+"]";
    }
}

