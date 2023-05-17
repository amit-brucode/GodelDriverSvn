package com.driver.godel.response.TransferPackageResponse;

/**
 * Created by Kawaldeep.Singh on 7/31/2019.
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
