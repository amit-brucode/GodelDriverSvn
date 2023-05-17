package com.driver.godel.response.GetActiveBookingData;

import java.util.List;

/**
 * Created by QSYS\simarjot.singh on 10/8/17.
 */

public class GetActiveBookingsNearBy
{
    private int response;

    private List<GetActiveBookingData> data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public List<GetActiveBookingData> getData()
    {
        return data;
    }

    public void setData(List<GetActiveBookingData> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}