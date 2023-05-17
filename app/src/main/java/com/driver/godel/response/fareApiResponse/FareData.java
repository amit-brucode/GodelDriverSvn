package com.driver.godel.response.fareApiResponse;

/**
 * Created by root on 5/2/18.
 */

public class FareData
{
    private String ucc_tax;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String vat_tax;

    private String fare_estimate;
    private String message;

    public String getUcc_tax()
    {
        return ucc_tax;
    }

    public void setUcc_tax(String ucc_tax)
    {
        this.ucc_tax = ucc_tax;
    }

    public String getVat_tax()
    {
        return vat_tax;
    }

    public void setVat_tax(String vat_tax)
    {
        this.vat_tax = vat_tax;
    }

    public String getFare_estimate()
    {
        return fare_estimate;
    }

    public void setFare_estimate(String fare_estimate)
    {
        this.fare_estimate = fare_estimate;
    }

    private String final_fare;

    public String getFinal_fare ()
    {
        return final_fare;
    }

    public void setFinal_fare (String final_fare)
    {
        this.final_fare = final_fare;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [final_fare = "+final_fare+"]";
    }
}
