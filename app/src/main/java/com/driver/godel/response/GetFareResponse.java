package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 4/7/17.
 */

public class GetFareResponse
{
    private int response;

    private FareData data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public FareData getData()
    {
        return data;
    }

    public void setData(FareData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
