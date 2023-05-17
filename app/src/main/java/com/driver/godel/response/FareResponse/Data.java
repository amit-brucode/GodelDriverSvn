package com.driver.godel.response.FareResponse;

/**
 * Created by Ajay2.Sharma on 07-Sep-17.
 */

public class Data
{
    private String ucc_tax;

    private String vat_tax;

    private String fare_estimate;

    private String final_fare;

    private String total_distance;

    public String getFinal_fare()
    {
        return final_fare;
    }

    public void setFinal_fare(String final_fare)
    {
        this.final_fare = final_fare;
    }

    public String getTotal_distance()
    {
        return total_distance;
    }

    public void setTotal_distance(String total_distance)
    {
        this.total_distance = total_distance;
    }

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
}
