package com.driver.godel.RefineCode.RefineWebServices.PendingPickupResp;

import java.util.List;

/**
 * Created by Ajay2.Sharma on 7/13/2018.
 */

public class PendingPickupResp {
    private int response;

    private List<Data> data;

    private String total_records;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", data = " + data + ", total_records = " + total_records + "]";
    }
}
