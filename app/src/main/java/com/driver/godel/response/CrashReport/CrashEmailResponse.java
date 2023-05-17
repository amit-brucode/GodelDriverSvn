package com.driver.godel.response.CrashReport;

/**
 * Created by Ajay2.Sharma on 24-Oct-17.
 */

public class CrashEmailResponse {
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
