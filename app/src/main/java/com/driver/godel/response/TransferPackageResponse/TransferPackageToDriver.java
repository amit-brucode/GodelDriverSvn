package com.driver.godel.response.TransferPackageResponse;

/**
 * Created by Kawaldeep.Singh on 7/31/2019.
 */

public class TransferPackageToDriver {

    private Data data;

    private int response;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public int getResponse ()
    {
        return response;
    }

    public void setResponse (int response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", response = "+response+"]";
    }


}
