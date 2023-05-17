package com.driver.godel.response.COPResponse;

/**
 * Created by Ajay2.Sharma on 7/18/2018.
 */

public class Data {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + "]";
    }
}
