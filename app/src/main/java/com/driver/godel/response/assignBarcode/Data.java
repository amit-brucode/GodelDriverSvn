package com.driver.godel.response.assignBarcode;

/**
 * Created by root on 22/3/18.
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
