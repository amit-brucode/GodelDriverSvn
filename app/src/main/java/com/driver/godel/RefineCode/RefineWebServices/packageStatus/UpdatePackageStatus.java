package com.driver.godel.RefineCode.RefineWebServices.packageStatus;

/**
 * Created by root on 29/1/18.
 */

public class UpdatePackageStatus
{
    private int response;

    private UpdatePackageData data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public UpdatePackageData getData() {
        return data;
    }

    public void setData(UpdatePackageData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
