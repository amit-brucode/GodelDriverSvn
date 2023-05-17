package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 13/7/17.
 */

public class PackageStatusResponse
{
    private int response;

    private PackageStatusData data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public PackageStatusData getData()
    {
        return data;
    }

    public void setData(PackageStatusData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}

