package com.driver.godel.response;

import java.util.List;

/**
 * Created by QSYS\simarjot.singh on 11/7/17.
 */

public class RequestDetailData
{
    private String preferred_deliver_time;

    private String id;

    private List<Package_details> package_details;

    private String is_cancel;

    private String updated_at;

    private RequestUserDetail user_details;

    private String booking_type;

    private String created_at;

    private String booking_note;

    private List<Location_details> location_details;

    private String user_id;

    public String getPreferred_deliver_time ()
    {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time (String preferred_deliver_time)
    {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public List<Package_details> getPackage_details() {
        return package_details;
    }

    public void setPackage_details(List<Package_details> package_details) {
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

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public RequestUserDetail getUser_details() {
        return user_details;
    }

    public void setUser_details(RequestUserDetail user_details) {
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

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getBooking_note ()
    {
        return booking_note;
    }

    public void setBooking_note (String booking_note)
    {
        this.booking_note = booking_note;
    }

    public List<Location_details> getLocation_details() {
        return location_details;
    }

    public void setLocation_details(List<Location_details> location_details) {
        this.location_details = location_details;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [preferred_deliver_time = "+preferred_deliver_time+", id = "+id+", package_details = "+package_details+", is_cancel = "+is_cancel+", updated_at = "+updated_at+", user_details = "+user_details+", booking_type = "+booking_type+", created_at = "+created_at+", booking_note = "+booking_note+", location_details = "+location_details+", user_id = "+user_id+"]";
    }
}
