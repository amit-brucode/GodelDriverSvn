package com.driver.godel.RefineCode.RefineActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;

public class PrivacyPolicyActivityNew extends GodelActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    WebView webView;
    ProgressDialog requestProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webview);

        tvTitle.setText("Privacy Policy");

        //Set On Click Listener
        ivBack.setOnClickListener(this);
        if (isNetworkAvailable()) {

            webView.setInitialScale(1);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().supportZoom();
            webView.getSettings().getTextZoom();
            webView.getSettings().setBuiltInZoomControls(true);
//            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setDomStorageEnabled(true);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            String countrycode=CountryCodeCheck.countrycheck(PrivacyPolicyActivityNew.this);
            webView.loadUrl(Global.PRIVACY_POLICY+countrycode+"/m-privacy-policy");
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                if (requestProgress == null) {
                    requestProgress = new ProgressDialog(PrivacyPolicyActivityNew.this);
                }
                if(!(PrivacyPolicyActivityNew.this.isFinishing())) {
                    if (requestProgress != null && requestProgress.isShowing()) {
                        requestProgress.dismiss();
                    }
                }
                requestProgress.setMessage("Loading...");
                requestProgress.setCancelable(false);
                requestProgress.show();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                if (requestProgress == null) {
                    requestProgress = new ProgressDialog(PrivacyPolicyActivityNew.this);
                }
                if(!(PrivacyPolicyActivityNew.this.isFinishing())) {
                    if (requestProgress != null && requestProgress.isShowing()) {
                        requestProgress.dismiss();
                    }
                }
                requestProgress.setMessage(getResources().getString(R.string.loadingtext)+"...");
                requestProgress.setCancelable(false);
                requestProgress.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!(PrivacyPolicyActivityNew.this.isFinishing())) {
                    if (requestProgress != null && requestProgress.isShowing()) {
                        requestProgress.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //Back Press
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
