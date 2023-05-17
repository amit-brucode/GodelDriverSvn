package com.driver.godel.RefineCode;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;

public class CountryCodeCheck {
    public static String countrycheck(Context context) {
        String countrycode = "";
        SharedPreferences preferences = context.getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        String selectedcountry=preferences.getString(SharedValues.SELECTED_COUNTRY,"");

        if (selectedcountry!= null) {
            if (selectedcountry.trim().length() > 0) {
                if(preferences.getString(SharedValues.URL_API,"").trim().length()>0){
                 countrycode=preferences.getString(SharedValues.URL_API,"");
                }
//                if (selectedcountry.equals(SharedValues.SELECTED_COUNTRY_UGANDA)) {
//                    countrycode = SharedValues.UGANDA_URL;
//                } else if (selectedcountry.equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//                    countrycode = SharedValues.TANZANIA_URL;
//                }
            }
        }
        Log.d("CHECK_LOGIN",countrycode);
        return countrycode;
    }
}
