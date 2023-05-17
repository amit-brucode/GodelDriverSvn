package com.driver.godel.RefineCode.RefineActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.BuildConfig;
import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.RefineCode.RefineWebServices.WebRequest;
import com.driver.godel.response.Select_Country.SelectedCountry;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Selected_Country extends GodelActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_country;
    SharedPreferences.Editor editor;
    Button btn_SlectedCountry;

    TextView versionName, versionCode;
    ProgressDialog progressDialog, getcountrydialog;
    WebRequest webRequest;
    WebRequest.APIInterface apiInterface;
    SelectedCountry selectedCountry;
    retrofit2.Call<SelectedCountry> selectedCountryCall;
    List<String> countryname = new ArrayList<>();
    List<String> countrycode = new ArrayList<>();
    List<String> countrytype = new ArrayList<>();
    List<String> currencysymbol = new ArrayList<>();
    final String TAG = "SELECTED_COUNTRY";
    public String type,language_type2;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slected__country);
        sp_country = findViewById(R.id.spinner_countries);
        versionName = findViewById(R.id.versionName);
        versionCode = findViewById(R.id.versionCode);

        versionName.setText(BuildConfig.VERSION_NAME);
        versionCode.setText("."+BuildConfig.VERSION_CODE);

        webRequest = WebRequest.getSingleton(Selected_Country.this);
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");


        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.remove(SharedValues.SELECTED_COUNTRY);
        editor.remove(SharedValues.URL_API);
        editor.remove(SharedValues.CurrencySymbolSHARED);
        editor.commit();

        progressDialog = new ProgressDialog(Selected_Country.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        getcountrydialog = new ProgressDialog(Selected_Country.this);
        getcountrydialog.setMessage("Loading");
        getcountrydialog.setCancelable(false);
        getcountrydialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getcountrydialog.show();

        if (isNetworkAvailable()) {
            selectedCountryCall = webRequest.apiInterface.selectedcountry();
            selectedCountryCall.enqueue(new Callback<SelectedCountry>() {
                @Override
                public void onResponse(Call<SelectedCountry> call, Response<SelectedCountry> response) {
                    selectedCountry = response.body();
                    if (response != null) {
                        if (selectedCountry.getResponse().equals("1")) {
                            if (getcountrydialog != null && getcountrydialog.isShowing()) {
                                getcountrydialog.dismiss();
                            }
                            int size = 0;
                            size = selectedCountry.getData().getCountries().size();
                            if (size > 0) {
                                countrycode.clear();
                                countryname.clear();
                                countrytype.clear();
                                currencysymbol.clear();
                                countryname.add("Select Country");
                                countrycode.add("ug");
                                countrytype.add("0");
                                currencysymbol.add("UGX");
                                for (int i = 0; i < selectedCountry.getData().getCountries().size(); i++) {
                                    countryname.add(selectedCountry.getData().getCountries().get(i).getName());
                                    countrytype.add(selectedCountry.getData().getCountries().get(i).getType());
                                    countrycode.add(selectedCountry.getData().getCountries().get(i).getCountry_code());
                                    currencysymbol.add(selectedCountry.getData().getCountries().get(i).getCurrency_code());
                                }
//                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Select_Country.this, countryname, android.R.layout.simple_spinner_item);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Selected_Country.this, android.R.layout.simple_spinner_item, countryname);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp_country.setAdapter(adapter);

                                sp_country.setOnItemSelectedListener(Selected_Country.this);


                            }


                        } else {
                            if (getcountrydialog != null && getcountrydialog.isShowing()) {
                                getcountrydialog.dismiss();
                            }
                            Toast.makeText(Selected_Country.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        if (getcountrydialog != null && getcountrydialog.isShowing()) {
                            getcountrydialog.dismiss();

                        }
                        Toast.makeText(Selected_Country.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SelectedCountry> call, Throwable t) {

                    if (getcountrydialog != null && getcountrydialog.isShowing()) {
                        getcountrydialog.dismiss();
                    }
                    Toast.makeText(Selected_Country.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (getcountrydialog != null && getcountrydialog.isShowing()) {
                getcountrydialog.dismiss();
            }
            Snackbar snackbar = Snackbar.make(Selected_Country.this.findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                }

            });
        }


        btn_SlectedCountry = findViewById(R.id.btn_SlectedCountry);
        btn_SlectedCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    progressDialog.show();
                    if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
                        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
                            progressDialog.cancel();
                            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {
//
                                String locale;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
                                }else{
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
                                }

                                Log.d(TAG,"locale :"+ locale );

                                if (locale.equals("sw_") || locale.equals("sw_TZ")){
                                    type="1";
                                    language_type2="1";


                                    Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
                                    Locale myLocale = new Locale("sw");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);

                                }else{
                                    type="2";
                                    language_type2="1";

                                    Log.d(TAG,"selected_languuage tanzania "+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    editor.commit();

                                    Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
                                    Locale myLocale = new Locale("en");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);
                                }
//
                                editor.putString(SharedValues.LANGUAGE_SETTINGS,type);
                                editor.putString(SharedValues.LANGUAGE_Type2, language_type2);
                                editor.commit();

                                Log.d(TAG,"selected_typevvv"+ preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));

//                                Locale myLocale = new Locale("sw");
//                                Resources res = getResources();
//                                DisplayMetrics dm = res.getDisplayMetrics();
//                                Configuration conf = res.getConfiguration();
//                                conf.locale = myLocale;
//                                res.updateConfiguration(conf, dm);

                                Intent intent = new Intent(Selected_Country.this, Splash.class);
                                intent.putExtra("CODES", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
//                            }

                            } else  if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)) {
//
                                String locale;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
                                }else{
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
                                }

                                Log.d(TAG,"locale :"+ locale );

                                if (locale.equals("sw_") || locale.equals("sw_TZ")){
                                    type="1";
                                    language_type2="1";


                                    Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
                                    Locale myLocale = new Locale("sw");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);

                                }else{
                                    type="2";
                                    language_type2="1";

                                    Log.d(TAG,"selected_languuage tanzania "+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    editor.commit();

                                    Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
                                    Locale myLocale = new Locale("en");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);
                                }
//
                                editor.putString(SharedValues.LANGUAGE_SETTINGS,type);
                                editor.putString(SharedValues.LANGUAGE_Type2, language_type2);
                                editor.commit();

                                Log.d(TAG,"selected_typevvv"+ preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));

//                                Locale myLocale = new Locale("sw");
//                                Resources res = getResources();
//                                DisplayMetrics dm = res.getDisplayMetrics();
//                                Configuration conf = res.getConfiguration();
//                                conf.locale = myLocale;
//                                res.updateConfiguration(conf, dm);

                                Intent intent = new Intent(Selected_Country.this, Splash.class);
                                intent.putExtra("CODES", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
//                            }

                            }else  if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UNITED_KINGDOM)) {
//
                                String locale;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
                                }else{
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
                                }

                                Log.d(TAG,"locale :"+ locale );

                                if (locale.equals("sw_") || locale.equals("sw_TZ")){
                                    type="1";
                                    language_type2="1";


                                    Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
                                    Locale myLocale = new Locale("sw");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);

                                }else{
                                    type="2";
                                    language_type2="1";

                                    Log.d(TAG,"selected_languuage UK "+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    editor.commit();

                                    Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
                                    Locale myLocale = new Locale("en");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);
                                }
//
                                editor.putString(SharedValues.LANGUAGE_SETTINGS,type);
                                editor.putString(SharedValues.LANGUAGE_Type2, language_type2);
                                editor.commit();

                                Log.d(TAG,"selected_typevvv"+ preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));

//                                Locale myLocale = new Locale("sw");
//                                Resources res = getResources();
//                                DisplayMetrics dm = res.getDisplayMetrics();
//                                Configuration conf = res.getConfiguration();
//                                conf.locale = myLocale;
//                                res.updateConfiguration(conf, dm);

                                Intent intent = new Intent(Selected_Country.this, Splash.class);
                                intent.putExtra("CODES", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
//                            }

                            } else {
//                                type="2";
//                                preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS,type);
                                Log.d(TAG,"selected_language  uganda"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
//                                editor.apply();
//                                editor.commit();
                                Log.d(TAG,"locale uganda :");
                                String locale;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().getLocales().get(0));
                                }else{
                                    locale = String.valueOf(Resources.getSystem().getConfiguration().locale);
                                }

                                Log.d(TAG,"locale :"+ locale );

                                if (locale.equals("sw_") || locale.equals("sw_TZ")){
                                    type="1";
                                   language_type2="1";

                                    Log.d(TAG,"selected_languuage  "+ "set_here_sw  "+locale );
                                    Locale myLocale = new Locale("sw");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);

                                }else{
                                    type="2";
                                    language_type2="2";
                                    Log.d(TAG,"selected_languuage tanzania "+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));
                                    Log.d(TAG,"selected_languuagehgiugtygu"+preferences.edit().putString(SharedValues.LANGUAGE_SETTINGS,""));
//                                    editor.commit();

                                    Log.d(TAG,"selected_languuage"+ "set_here_english  "+locale );
                                    Locale myLocale = new Locale("en");
                                    Resources res = getResources();
                                    DisplayMetrics dm = res.getDisplayMetrics();
                                    Configuration conf = res.getConfiguration();
                                    conf.locale = myLocale;
                                    res.updateConfiguration(conf, dm);
                                }
//
                                editor.putString(SharedValues.LANGUAGE_SETTINGS,type);
                                editor.putString(SharedValues.LANGUAGE_Type2, language_type2);
                                editor.commit();

                                Log.d(TAG,"selected_typevvv"+ preferences.getString(SharedValues.LANGUAGE_SETTINGS,""));

//
                                Intent intent = new Intent(Selected_Country.this, Splash.class);
                                intent.putExtra("CODES", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(Selected_Country.this, "No Country Selected", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    } else {
                        Toast.makeText(Selected_Country.this, "No Country Selected", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(Selected_Country.this.findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                        }

                    });
                }
            }
        });
    }

//
//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder exitAlert = new AlertDialog.Builder(this);
//        exitAlert.setMessage(getResources().getString(R.string.exitext));
//        exitAlert.setPositiveButton(getResources().getString(R.string.yestext), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Select_Country.this.finish();
//                System.exit(0);
//            }
//        });
//
//        exitAlert.setNegativeButton(getResources().getString(R.string.no), null).show();
//
//    }

    @Override
    protected void onResume() {
        super.onResume();

        String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
        String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2,"");
            if(language_type.equals(language_type2)){
        if(language_type.equals("1")){
            Locale myLocale = new Locale("sw");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }else if(language_type.equals("2")){
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }}else{ editor.putString(SharedValues.LANGUAGE_Type2, language_type);
                editor.commit();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Object position = adapterView.getItemAtPosition(i);
        Log.d(TAG, "INT=" + i);
        if (i == 0) {
            editor.remove(SharedValues.SELECTED_COUNTRY);
            editor.remove(SharedValues.URL_API);
            editor.remove(SharedValues.CurrencySymbolSHARED);
            editor.commit();

        } else {
            Log.d("POSITION_LOG", "Postion :" + position);
            String postions = position.toString();
            if(countryname.get(i).equals("United Kingdom")){
                editor.putString(SharedValues.SELECTED_COUNTRY,"GB");
                editor.putString(SharedValues.URL_API, countrycode.get(i));
                editor.putString(SharedValues.CurrencySymbolSHARED, currencysymbol.get(i));
                editor.commit();
            }else {

                editor.putString(SharedValues.SELECTED_COUNTRY, countryname.get(i));
                editor.putString(SharedValues.URL_API, countrycode.get(i));
                editor.putString(SharedValues.CurrencySymbolSHARED, currencysymbol.get(i));
                editor.commit();
            }
        }


//        switch (countrytype.get(i)) {
//            case "0":
////                editor.remove(GlobalVariables.SELECTED_COUNTRY);
////                editor.commit();
//                GlobalVariables.SELECTED_COUNTRY_UGANDA=countrycode.get(i);
//                editor.putString(GlobalVariables.SELECTED_COUNTRY, GlobalVariables.SELECTED_COUNTRY_UGANDA);
//                editor.commit();
//                break;
//            case "1":
//                GlobalVariables.SELECTED_COUNTRY_TANZANIA=countrycode.get(i);
//                editor.putString(GlobalVariables.SELECTED_COUNTRY, GlobalVariables.SELECTED_COUNTRY_TANZANIA);
//                editor.commit();
//                break;
////            case "Uganda":
////                editor.putString(GlobalVariables.SELECTED_COUNTRY, GlobalVariables.SELECTED_COUNTRY_UGANDA);
////                editor.commit();
////                break;
//            default:
//                editor.remove(GlobalVariables.SELECTED_COUNTRY);
//                editor.commit();
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("POSITION_LOG", "Postion :" + "nothing");
        editor.remove(SharedValues.SELECTED_COUNTRY);
        editor.remove(SharedValues.URL_API);
        editor.remove(SharedValues.CurrencySymbolSHARED);
        editor.commit();
    }
}
