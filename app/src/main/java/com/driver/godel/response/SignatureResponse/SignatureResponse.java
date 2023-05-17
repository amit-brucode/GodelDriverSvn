package com.driver.godel.response.SignatureResponse;

/**
 * Created by QSYS\simarjot.singh on 11/8/17.
 */

public class SignatureResponse
{
    private int response;

    private SignatureData data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public SignatureData getData()
    {
        return data;
    }

    public void setData(SignatureData data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
