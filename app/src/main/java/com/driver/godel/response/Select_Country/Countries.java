package com.driver.godel.response.Select_Country;

public class Countries
{
    private String country_code;

    private String name;

    private String type;

    private String currency_code;

    public String getCountry_code ()
    {
        return country_code;
    }

    public void setCountry_code (String country_code)
    {
        this.country_code = country_code;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getCurrency_code ()
    {
        return currency_code;
    }

    public void setCurrency_code (String currency_code)
    {
        this.currency_code = currency_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [country_code = "+country_code+", name = "+name+", type = "+type+", currency_code = "+currency_code+"]";
    }
}

