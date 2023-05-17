package com.driver.godel.response.Select_Country;

import java.util.List;

public class Data
{
    private List<Countries> countries;

    public List<Countries> getCountries() {
        return countries;
    }

    public void setCountries(List<Countries> countries) {
        this.countries = countries;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [countries = "+countries+"]";
    }
}

