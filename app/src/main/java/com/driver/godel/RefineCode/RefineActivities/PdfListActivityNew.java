package com.driver.godel.RefineCode.RefineActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineAdapter.PdfListAdapter;

import java.io.File;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PdfListActivityNew extends AppCompatActivity implements View.OnClickListener
{
    TextView tvNoPackageDownload;
    ArrayList<String> fileName = new ArrayList<String>();
    ArrayList<String> filepath = new ArrayList<String>();
    RecyclerView rvFiles;
    private String folderName = "Godel_Driver_PDF";

    //Toolbar
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        rvFiles = (RecyclerView) findViewById(R.id.rv_files);
        tvNoPackageDownload = (TextView)findViewById(R.id.tv_no_package_download);
        rvFiles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvFiles.setItemAnimator(null);
        tvNoPackageDownload.setVisibility(GONE);

        //Initialization
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        ivBack = (ImageView)findViewById(R.id.iv_back);

        tvTitle.setText(getResources().getString(R.string.downloadedlabelstitle));

        //Set On Click Listener
        ivBack.setOnClickListener(this);

        //Access File
        accessFiles();
    }


    private void accessFiles()
    {
        fileName.clear();
        filepath.clear();

        String path = Environment.getExternalStorageDirectory() + "/" + folderName;
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0)
        {
            for (int i = 0; i < files.length; i++)
            {
                fileName.add(files[i].getName());
                filepath.add(files[i].getPath());
            }
            PdfListAdapter adapter = new PdfListAdapter(PdfListActivityNew.this, fileName, filepath);
            rvFiles.setAdapter(adapter);
            tvNoPackageDownload.setVisibility(GONE);
            rvFiles.setVisibility(VISIBLE);
        }
        else
        {
            tvNoPackageDownload.setVisibility(VISIBLE);
            rvFiles.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_back:

                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
