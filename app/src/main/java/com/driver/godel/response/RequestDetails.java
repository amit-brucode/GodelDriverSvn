package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 11/7/17.
 */

public class RequestDetails
{
    private int response;

    private RequestDetailData data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public RequestDetailData getData()
    {
        return data;
    }

    public void setData(RequestDetailData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}

