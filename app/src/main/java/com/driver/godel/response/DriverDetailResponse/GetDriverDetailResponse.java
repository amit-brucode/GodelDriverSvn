package com.driver.godel.response.DriverDetailResponse;

/**
 * Created by root on 16/2/18.
 */

public class GetDriverDetailResponse
{
    private String response;

    private DriverDetailsData data;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public DriverDetailsData getData() {
        return data;
    }

    public void setData(DriverDetailsData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}