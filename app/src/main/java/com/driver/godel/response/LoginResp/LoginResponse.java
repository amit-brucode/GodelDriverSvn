package com.driver.godel.response.LoginResp;

/**
 * Created by Ajay2.Sharma on 8/9/2018.
 */

public class LoginResponse {
    private int response;

    private Data data;

    public int getResponse ()
    {
        return response;
    }

    public void setResponse (int response)
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
