package com.driver.godel.response.SignatureResponse;

/**
 * Created by QSYS\simarjot.singh on 11/8/17.
 */

public class SignatureData
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
