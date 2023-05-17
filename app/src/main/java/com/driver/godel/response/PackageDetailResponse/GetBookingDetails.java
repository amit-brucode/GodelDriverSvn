package com.driver.godel.response.PackageDetailResponse;

/**
 * Created by root on 24/1/18.
 */

public class GetBookingDetails
{
    private String handle_by;

    private String preferred_deliver_time;

    private String delivery_type;

    private String vehicle_type;

    private String recurring_id;

    private String id;

    private String is_cancel;

    private String updated_at;

    private String delivery_session;

    private String booking_delivery_datetime;

    private String booking_type;

    private String created_at;

    private String is_recurring_parent;

    private String booking_note;

    private String booking_code;

    private String user_id;

    private String booking_pickup_datetime;

    public String getHandle_by ()
    {
        return handle_by;
    }

    public void setHandle_by (String handle_by)
    {
        this.handle_by = handle_by;
    }

    public String getDelivery_type ()
    {
        return delivery_type;
    }

    public void setDelivery_type (String delivery_type)
    {
        this.delivery_type = delivery_type;
    }

    public String getVehicle_type ()
    {
        return vehicle_type;
    }

    public void setVehicle_type (String vehicle_type)
    {
        this.vehicle_type = vehicle_type;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
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

    public String getBooking_delivery_datetime ()
    {
        return booking_delivery_datetime;
    }

    public void setBooking_delivery_datetime (String booking_delivery_datetime)
    {
        this.booking_delivery_datetime = booking_delivery_datetime;
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

    public String getIs_recurring_parent ()
    {
        return is_recurring_parent;
    }

    public void setIs_recurring_parent (String is_recurring_parent)
    {
        this.is_recurring_parent = is_recurring_parent;
    }

    public String getBooking_code ()
    {
        return booking_code;
    }

    public void setBooking_code (String booking_code)
    {
        this.booking_code = booking_code;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getBooking_pickup_datetime ()
    {
        return booking_pickup_datetime;
    }

    public void setBooking_pickup_datetime (String booking_pickup_datetime)
    {
        this.booking_pickup_datetime = booking_pickup_datetime;
    }

    public String getPreferred_deliver_time() {
        return preferred_deliver_time;
    }

    public void setPreferred_deliver_time(String preferred_deliver_time) {
        this.preferred_deliver_time = preferred_deliver_time;
    }

    public String getRecurring_id() {
        return recurring_id;
    }

    public void setRecurring_id(String recurring_id) {
        this.recurring_id = recurring_id;
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

    @Override
    public String toString()
    {
        return "ClassPojo [handle_by = "+handle_by+", preferred_deliver_time = "+preferred_deliver_time+", delivery_type = "+delivery_type+", vehicle_type = "+vehicle_type+", recurring_id = "+recurring_id+", id = "+id+", is_cancel = "+is_cancel+", updated_at = "+updated_at+", delivery_session = "+delivery_session+", booking_delivery_datetime = "+booking_delivery_datetime+", booking_type = "+booking_type+", created_at = "+created_at+", is_recurring_parent = "+is_recurring_parent+", booking_note = "+booking_note+", booking_code = "+booking_code+", user_id = "+user_id+", booking_pickup_datetime = "+booking_pickup_datetime+"]";
    }
}
