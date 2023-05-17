package com.driver.godel.RefineCode.RefineFragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.godel.R;
import com.driver.godel.RefineCode.RefineUtils.Global;

/**
 * Created by Ajay2.Sharma on 7/6/2018.
 */

public class NewRequestsFragment extends GodelDriverFragment {
    View view;
    private CountDownTimer countdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_request_new, container, false);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        Countdown();
    }

    private void NewRequestHit() {
        Countdown();
    }

    private void Countdown() {
        countdown = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                NewRequestHit();
            }
        }.start();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            NewRequestHit();
        }

    }
}
