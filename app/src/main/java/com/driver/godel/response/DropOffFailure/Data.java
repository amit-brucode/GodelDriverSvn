package com.driver.godel.response.DropOffFailure;

public class Data
{
    private String status;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+"]";
    }
}

