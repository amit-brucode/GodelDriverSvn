package com.driver.godel.response.pendingDeliveryV2New;

import java.util.List;

/**
 * Created by root on 24/2/18.
 */

public class PendingDeliveryV2New
{
    private String response;

    private List<DeliveryData> data;

    private String total_records;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public List<DeliveryData> getData() {
        return data;
    }

    public void setData(List<DeliveryData> data) {
        this.data = data;
    }

    public String getTotal_records ()
    {
        return total_records;
    }

    public void setTotal_records (String total_records)
    {
        this.total_records = total_records;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+", total_records = "+total_records+"]";
    }
}
