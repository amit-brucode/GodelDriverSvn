package com.driver.godel.response;

import java.util.ArrayList;

/**
 * Created by QSYS\simarjot.singh on 30/6/17.
 */

public class BookingHistoryResponse
{

    private int total_records;

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    private int response;

    private ArrayList<BookingData> data;

    public int getResponse()
    {
        return response;
    }

    public void setResponse(int response)
    {
        this.response = response;
    }

    public ArrayList<BookingData> getData()
    {
        return data;
    }

    public void setData(ArrayList<BookingData> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}