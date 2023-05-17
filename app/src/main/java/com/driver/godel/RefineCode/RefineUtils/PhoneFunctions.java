package com.driver.godel.RefineCode.RefineUtils;

import android.widget.TextView;

public class PhoneFunctions {
    private static PhoneFunctions instance;

    private PhoneFunctions(){}

    public static PhoneFunctions getInstance() {
        if (instance == null) {
            instance = new PhoneFunctions();
        }

        return instance;
    }

    public String getCountry(String[] argStringArray, TextView argText){
        String country="";

        if (argText.getText().toString().length() >= 4){
            for(int i=0;i<argStringArray.length;i++){
                String[] g=argStringArray[i].split(",");
                if(g[0].equals(getFirstFourChar(argText))){
                    country=g[1];
                    break;
                }
                if (g[0].equals(getFirstThreeChar(argText))){
                    country=g[1];
                    break;
                }
                if (g[0].equals(getFirstTwoChar(argText))){
                    country=g[1];
                    break;
                }
            }
        }

        return country;
    }

    public String getFirstFourChar(TextView argText){
        String threeChar;
        String text = argText.getText().toString();
        threeChar = text.substring(0,4);

        return threeChar;
    }

    public String getFirstThreeChar(TextView argText){
        String twoChar;
        String text = argText.getText().toString();
        twoChar = text.substring(0,3);

        return twoChar;
    }

    public String getFirstTwoChar(TextView argText){
        String oneChar;
        String text = argText.getText().toString();
        oneChar = text.substring(0,2);

        return oneChar;
    }

}
