package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 21/6/17.
 */

public class ForgotPaswdResponse
{
    private int response;

    private ForgotData data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public ForgotData getData()
    {
        return data;
    }

    public void setData(ForgotData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}

