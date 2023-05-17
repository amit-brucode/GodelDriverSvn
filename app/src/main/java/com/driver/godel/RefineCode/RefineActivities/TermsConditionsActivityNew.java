package com.driver.godel.RefineCode.RefineActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;

public class TermsConditionsActivityNew extends GodelActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    WebView webView;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        //Set Title
        tvTitle.setText(getResources().getString(R.string.termsandconconditions));
        //Set On Click Listener
        ivBack.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webview);
        webView.setInitialScale(1);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getLoadsImagesAutomatically();
        webView.getSettings().getUseWideViewPort();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().supportZoom();
        webView.getSettings().getTextZoom();
        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);

        String countrycode=CountryCodeCheck.countrycheck(TermsConditionsActivityNew.this);
        webView.loadUrl(Global.TERMS_CONDITIONS+countrycode+"/m-terms");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                if (dialog == null) {
                    dialog = new ProgressDialog(TermsConditionsActivityNew.this);
                }
                if(!(TermsConditionsActivityNew.this.isFinishing())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                if (dialog == null) {
                    dialog = new ProgressDialog(TermsConditionsActivityNew.this);
                }
                if(!(TermsConditionsActivityNew.this.isFinishing())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!(TermsConditionsActivityNew.this.isFinishing())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
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
        //super.onBackPressed();

//        Intent intent = new Intent(this, MyPackages1.class);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!(TermsConditionsActivityNew.this.isFinishing())) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!(TermsConditionsActivityNew.this.isFinishing())) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
