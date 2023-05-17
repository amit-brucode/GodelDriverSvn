package com.driver.godel.response.DriverDetailResponse;

/**
 * Created by root on 16/2/18.
 */

public class DriverDetailsData
{
    private DriverDetails driver_details;

    public DriverDetails getDriver_details() {
        return driver_details;
    }

    public void setDriver_details(DriverDetails driver_details) {
        this.driver_details = driver_details;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [driver_details = "+driver_details+"]";
    }
}
