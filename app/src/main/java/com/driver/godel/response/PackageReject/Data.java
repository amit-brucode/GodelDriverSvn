package com.driver.godel.response.PackageReject;

/**
 * Created by root on 22/1/18.
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
