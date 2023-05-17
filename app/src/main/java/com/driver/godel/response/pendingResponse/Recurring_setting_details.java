package com.driver.godel.response.pendingResponse;

/**
 * Created by root on 19/2/18.
 */

public class Recurring_setting_details
{
    private String id;

    private String is_deleted;

    private String updated_at;

    private String recurring_day_time;

    private String recurring_week;

    private String created_at;

    private String recurring_status;

    private String recurring_pickup_type;

    private String recurring_day;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIs_deleted ()
    {
        return is_deleted;
    }

    public void setIs_deleted (String is_deleted)
    {
        this.is_deleted = is_deleted;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getRecurring_day_time ()
    {
        return recurring_day_time;
    }

    public void setRecurring_day_time (String recurring_day_time)
    {
        this.recurring_day_time = recurring_day_time;
    }

    public String getRecurring_week() {
        return recurring_week;
    }

    public void setRecurring_week(String recurring_week) {
        this.recurring_week = recurring_week;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getRecurring_status ()
    {
        return recurring_status;
    }

    public void setRecurring_status (String recurring_status)
    {
        this.recurring_status = recurring_status;
    }

    public String getRecurring_pickup_type ()
    {
        return recurring_pickup_type;
    }

    public void setRecurring_pickup_type (String recurring_pickup_type)
    {
        this.recurring_pickup_type = recurring_pickup_type;
    }

    public String getRecurring_day ()
    {
        return recurring_day;
    }

    public void setRecurring_day (String recurring_day)
    {
        this.recurring_day = recurring_day;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", is_deleted = "+is_deleted+", updated_at = "+updated_at+", recurring_day_time = "+recurring_day_time+", recurring_week = "+recurring_week+", created_at = "+created_at+", recurring_status = "+recurring_status+", recurring_pickup_type = "+recurring_pickup_type+", recurring_day = "+recurring_day+"]";
    }
}
