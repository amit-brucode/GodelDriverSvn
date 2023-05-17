package com.driver.godel.response.PackageDetailResponse;

/**
 * Created by root on 24/1/18.
 */

public class GetPackageResponse
{
    private String response;

    private PackageData data;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public PackageData getData() {
        return data;
    }

    public void setData(PackageData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
