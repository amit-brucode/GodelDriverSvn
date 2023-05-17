package com.driver.godel.response.fareApiResponse;

/**
 * Created by root on 5/2/18.
 */

public class FareResponse
{
    private int response;

    private FareData data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public FareData getData() {
        return data;
    }

    public void setData(FareData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
