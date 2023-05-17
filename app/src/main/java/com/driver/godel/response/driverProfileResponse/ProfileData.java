package com.driver.godel.response.driverProfileResponse;

/**
 * Created by QSYS\simarjot.singh on 17/8/17.
 */

public class ProfileData
{
    private String message;

    private Driver driver;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Driver getDriver ()
    {
        return driver;
    }

    public void setDriver (Driver driver)
    {
        this.driver = driver;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", driver = "+driver+"]";
    }
}
