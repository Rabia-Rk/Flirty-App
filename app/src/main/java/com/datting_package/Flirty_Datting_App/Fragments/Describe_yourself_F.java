package com.datting_package.Flirty_Datting_App.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A.colorRl;

/**
 * A simple {@link Fragment} subclass.
 */
public class Describe_yourself_F extends RootFragment {

    View view;

    TextView textCounter;
    EditText et;
    Context context;


    public Describe_yourself_F() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_describe_yourself, container, false);
        context = getContext();
        textCounter = (TextView) view.findViewById(R.id.text_counter_id);
        et = (EditText) view.findViewById(R.id.et_id);

        final String about_me = Functions.getInfo(context, "about_me");

        et.setText("" + about_me);


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Variables.varAboutMe = s.toString();
                int len = et.length();
                textCounter.setText(len + "/250");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) EditProfileVp_A.vpRl.getLayoutParams();
        lp1.height = (int) (Variables.height / 2);

        EditProfileVp_A.vpRl.setLayoutParams(lp1);

        EditProfileVp_A.next.setVisibility(View.VISIBLE);

        EditProfileVp_A.next.setOnClickListener(v -> {

            int adaptorPositionTotal = EditProfileVp_A.segmentAdapter.getCount();
            int adpCurrentItemPosition = EditProfileVp_A.vp.getCurrentItem() + 1;

            if (adaptorPositionTotal == adpCurrentItemPosition) {

                EditProfileVp_A.createJsonForAPI(context);
                getActivity().finish();
            } else {
                EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem() + 1);
                EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1) + "/ " + EditProfileVp_A.segmentAdapter.getCount());
                colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.zink2));

                EditProfileVp_A.getFragmentName(adpCurrentItemPosition);

            }
        });


        return view;
    }

}
