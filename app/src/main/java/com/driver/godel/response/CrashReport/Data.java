package com.driver.godel.response.CrashReport;

/**
 * Created by Ajay2.Sharma on 24-Oct-17.
 */

public class Data {
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
