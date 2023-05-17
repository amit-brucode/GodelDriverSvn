package com.driver.godel.RefineCode.RefineWebServices.NewRequestResponse;

import java.util.List;

/**
 * Created by Ajay2.Sharma on 7/20/2018.
 */

public class NewRequestsResponse {
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
