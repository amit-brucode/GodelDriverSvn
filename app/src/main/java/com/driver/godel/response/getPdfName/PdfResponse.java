package com.driver.godel.response.getPdfName;

/**
 * Created by root on 5/2/18.
 */

public class PdfResponse
{
    private String response;

    private PdfData data;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public PdfData getData() {
        return data;
    }

    public void setData(PdfData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }
}
