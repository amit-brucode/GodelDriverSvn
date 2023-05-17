package com.driver.godel.response.packageDropWarehouse;

/**
 * Created by root on 24/2/18.
 */

public class Data
{
    private String message;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+"]";
    }
}
