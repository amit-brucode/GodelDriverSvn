package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 21/6/17.
 */



public class ForgotData
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
