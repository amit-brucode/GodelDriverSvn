package com.driver.godel.response.HistoryResponse;

import java.util.List;

/**
 * Created by Ajay2.Sharma on 30-Aug-17.
 */

public class HistoryResponse {

    private String response;

    private List<Data> data;

    private String total_records;



    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
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
