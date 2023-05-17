package com.driver.godel.response.changePasswordResp;

public class Data
{
    private String message;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+"]";
    }
}
