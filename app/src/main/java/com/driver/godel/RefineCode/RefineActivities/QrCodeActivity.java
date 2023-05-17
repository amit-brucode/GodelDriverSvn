package com.driver.godel.RefineCode.RefineActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeActivity extends GodelActivity implements View.OnClickListener  {

    Toolbar toolbar;
    ImageView ivBack, ivBarcode;
    TextView tvTitle, tvName, tvId;

    private IntentIntegrator qrScan;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String unid = intent.getStringExtra("unid");

        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qrScan = new IntentIntegrator(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvName = (TextView) findViewById(R.id.tv_unname);
        tvId = (TextView) findViewById(R.id.tv_unid);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBarcode = (ImageView) findViewById(R.id.iv_barcode);

        tvId.setText(unid);
        tvName.setText(name);

        Log.d("QRCODE_LOG",id);

        String encodedid = Global.encodeBase(id);
        Log.d("QRCODE_LOG","encodedid = "+ encodedid);

//        String decodedid = Global.decodeBase(encodedid);
//        Log.d("QRCODE_LOG","decodedid = "+ decodedid);

        QRGEncoder qrgEncoder = new QRGEncoder(encodedid, null, QRGContents.Type.TEXT, 300);
            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.encodeAsBitmap();
                // Setting Bitmap to ImageView
                ivBarcode.setImageBitmap(bitmap);
            } catch (WriterException e) {
//                Log.v("PROFILEAPI", e.toString());
            }
        //Set Title
        tvTitle.setText(getResources().getString(R.string.my_qr_code));
        //Set On Click Listener
        ivBack.setOnClickListener(this);
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
