package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 21/6/17.
 */

public class LoginResponse
{
    private int response;

    private LoginData data;

    public LoginData getData()
    {
        return data;
    }

    public void setData(LoginData data)
    {
        this.data = data;
    }

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}

