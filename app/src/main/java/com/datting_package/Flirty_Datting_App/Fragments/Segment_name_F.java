package com.datting_package.Flirty_Datting_App.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.segmentedProgressBar;
import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.viewPager;

public class Segment_name_F extends RootFragment {

    public static String signupName;
    View view;
    Button btn;
    EditText text;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_name, null);

        text = view.findViewById(R.id.name_ET_id);
        context = getContext();


        String name = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.shareSocialInfo,
                "name"
        );
        text.setText("" + name);

        btn = (Button) view.findViewById(R.id.continue_btn_id);
        btn.setOnClickListener(v -> {
            signupName = text.getText().toString();

            if (signupName.isEmpty()) {
                text.setError("Please fill this");
            } else {
                viewPager.setCurrentItem(2);
                segmentedProgressBar.setCompletedSegments(2);
            }

        });

        return view;
    }
}
