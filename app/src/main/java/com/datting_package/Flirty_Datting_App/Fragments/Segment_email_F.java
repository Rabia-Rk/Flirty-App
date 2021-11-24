package com.datting_package.Flirty_Datting_App.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;
import com.ybs.countrypicker.CountryPicker;

import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.segmentedProgressBar;
import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.viewPager;

public class Segment_email_F extends RootFragment {

    public static String signupEmail, stringPhoneNum;
    View v;
    Button btn;
    LinearLayout linearLayout, phoneLLId;
    EditText editText, numberVerifyETId, passwordETId;
    TextView emailTV, phoneTV, countryTV, emailTitleId;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_email, null);

        context = getContext();
        emailTV = (TextView) v.findViewById(R.id.email_TV_id);
        phoneTV = (TextView) v.findViewById(R.id.phone_TV_id);
        countryTV = (TextView) v.findViewById(R.id.phone_number_id);
        phoneLLId = v.findViewById(R.id.phone_LL_id);
        editText = (EditText) v.findViewById(R.id.email_ET_id);
        linearLayout = (LinearLayout) v.findViewById(R.id.inner_LL1_id);
        numberVerifyETId = v.findViewById(R.id.Number_verify_ET_id);
        emailTitleId = v.findViewById(R.id.email_title_id);
        passwordETId = v.findViewById(R.id.password_ET_id);


        String email = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.shareSocialInfo,
                "email"
        );
        editText.setText("" + email);


        emailTV.setOnClickListener(v -> {
            phoneTV.setVisibility(View.VISIBLE);
            emailTV.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            phoneLLId.setVisibility(View.VISIBLE);
            emailTitleId.setText("Whats your Phone Number?");

        });

        phoneTV.setOnClickListener(v -> {
            emailTV.setVisibility(View.VISIBLE);
            phoneTV.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            phoneLLId.setVisibility(View.GONE);

            emailTitleId.setText("Whats your Email?");
        });


        countryTV.setOnClickListener(v -> countryPicker());

        btn = (Button) v.findViewById(R.id.email_btn_id);
        btn.setOnClickListener(v -> {
            signupEmail = editText.getText().toString();
            stringPhoneNum = countryTV.getText().toString() + numberVerifyETId.getText().toString();

            if (phoneLLId.getVisibility() == View.VISIBLE) {

                if (stringPhoneNum.isEmpty()) {

                    numberVerifyETId.setError("Please fill this ");
                } else {
                    viewPager.setCurrentItem(5);
                    segmentedProgressBar.setCompletedSegments(5);
                }


            } else {


                if (signupEmail.isEmpty()) {
                    editText.setError("Please fill this ");
                } else if (!Functions.isEmailValid(signupEmail)) {
                    editText.setError("Please enter a valid Email");
                } else {
                    viewPager.setCurrentItem(5);
                    segmentedProgressBar.setCompletedSegments(6);
                }
            }

        });

        return v;
    }

    @SuppressLint("WrongConstant")
    private void countryPicker() {

        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.setListener((name, code, dialCode, flagDrawableResID) -> {
            countryTV.setText(dialCode);
            picker.dismiss();
        });

        picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
        picker.show(getChildFragmentManager(), "Select Country");

    }
}
