package com.driver.godel.response.getPdfName;

/**
 * Created by root on 5/2/18.
 */

public class PdfData
{
    private String pdf_name;

    public String getPdf_name ()
    {
        return pdf_name;
    }

    public void setPdf_name (String pdf_name)
    {
        this.pdf_name = pdf_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pdf_name = "+pdf_name+"]";
    }
}
