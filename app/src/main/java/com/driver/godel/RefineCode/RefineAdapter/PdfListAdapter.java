package com.driver.godel.RefineCode.RefineAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.godel.ExceptionHandler;
import com.driver.godel.R;


import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by QSYS\nancy.thakkar on 1/7/17.
 */

public class PdfListAdapter extends RecyclerView.Adapter<PdfListAdapter.MyViewHolder>
{

    private static Context c;

    ArrayList<String> fileName;
    ArrayList<String> filepath;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvFileName;
        ConstraintLayout clFullRow;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            clFullRow = (ConstraintLayout) itemView.findViewById(R.id.cl_full_row);

        }
    }

    public PdfListAdapter(Context c, ArrayList<String> fileName, ArrayList<String> filepath)
    {
        this.c = c;
        this.fileName = fileName;
        this.filepath = filepath;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(c));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pdf_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        holder.tvFileName.setText(fileName.get(listPosition));
        holder.clFullRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= 24)
                {
                    try
                    {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                File apkStorage = new File(
                        filepath.get(listPosition));//+ packageId
                try
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(apkStorage), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    c.startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(c, "Nothing found to open file" , Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(c, "Something went wrong" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileName.size();
    }
}