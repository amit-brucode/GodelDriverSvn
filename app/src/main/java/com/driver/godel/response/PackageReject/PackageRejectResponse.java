package com.driver.godel.response.PackageReject;

/**
 * Created by root on 22/1/18.
 */

public class PackageRejectResponse
{
    private int response;

    private Data data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
