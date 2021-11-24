package com.datting_package.Flirty_Datting_App.Boost;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Boost_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;
    CircularProgressBar circularProgressBar;
    long timeGone;
    CountDownTimer timer;


    public Boost_F() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();

        if (!checkISBoostOn()) {
            view = inflater.inflate(R.layout.fragment_boost, container, false);

            view.findViewById(R.id.boost_btn).setOnClickListener(this);

        } else {

            view = inflater.inflate(R.layout.fragment_boost_on, container, false);
            circularProgressBar = view.findViewById(R.id.circularProgressBar);
            view.findViewById(R.id.okay_btn).setOnClickListener(this);
            setProgress();
        }


        view.findViewById(R.id.transparent_layout).setOnClickListener(this);

        return view;
    }

    public boolean checkISBoostOn() {

        long requesttime = Long.parseLong(SharedPrefrence.pref.getString(SharedPrefrence.boostOnTime, "0"));
        long currenttime = System.currentTimeMillis();

        timeGone = (currenttime - requesttime);

        if (requesttime == 0) {

            return false;
        } else if (timeGone < Variables.BOOST_TIME) {

            return true;

        } else {

            return false;

        }


    }

    public void setProgress() {
        long requesttime = Long.parseLong(SharedPrefrence.pref.getString(SharedPrefrence.boostOnTime, "0"));
        long currenttime = System.currentTimeMillis();

        timeGone = (currenttime - requesttime);


        startTimer();
    }

    public void startTimer() {
        long timeLeft = Variables.BOOST_TIME - timeGone;
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                long millis = l;

                String timeString = Functions.convertSeconds((int) (millis / 1000));
                TextView textView = view.findViewById(R.id.remaining_txt);
                textView.setText(timeString + " Remaining");

                float progress = ((l * 100) / Variables.BOOST_TIME);
                circularProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {

                stopTimer();
            }
        };

        timer.start();
    }

    public void stopTimer() {

        if (timer != null)
            timer.cancel();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.boost_btn:
                callApiForBoostProfile();
                break;

            case R.id.transparent_layout:
                getActivity().onBackPressed();
                break;

            case R.id.okay_btn:
                getActivity().onBackPressed();
                break;

            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void callApiForBoostProfile() {

        String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);
            parameters.put("mins", "30");
            parameters.put("promoted", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.showLoader(context, false, false);
        ApiRequest.callApi(context, ApiLinks.boostProfile, parameters, (requestType, response) -> {
            Functions.cancelLoader();

            try {
                JSONObject jsonObject = new JSONObject(response);

                long min = System.currentTimeMillis();

                SharedPrefrence.pref.edit().putString(SharedPrefrence.boostOnTime, "" + min).commit();

                getActivity().onBackPressed();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }


}
