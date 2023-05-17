package com.driver.godel.response.driverProfileResponse;

/**
 * Created by QSYS\simarjot.singh on 17/8/17.
 */

public class DriverProfileResponse
{
    private int response;

    private ProfileData data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
