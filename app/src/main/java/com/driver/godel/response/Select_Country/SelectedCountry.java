package com.driver.godel.response.Select_Country;

public class SelectedCountry {

    private Data data;

    private String response;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", response = "+response+"]";
    }
}
