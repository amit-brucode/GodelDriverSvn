package com.driver.godel.RefineCode;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.driver.godel.ExceptionHandler;

/**
 * Created by Ajay2.Sharma on 24-Oct-17.
 */

public class GodelFragmentActivity extends FragmentActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }
}
