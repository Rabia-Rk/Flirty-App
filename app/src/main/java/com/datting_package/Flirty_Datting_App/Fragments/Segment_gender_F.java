package com.datting_package.Flirty_Datting_App.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.segmentedProgressBar;
import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.viewPager;

public class Segment_gender_F extends RootFragment {

    View view;
    Context context;
    TextView male,female;
    public static String gender;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_gender, null);
        context = getContext();

        male = (TextView) view.findViewById(R.id.male_TV_id);
        female = (TextView) view.findViewById(R.id.female_TV_id);

        male.setOnClickListener(v-> {
                gender = male.getText().toString();
                moveNext();

        });


        female.setOnClickListener(v ->  {

                gender = female.getText().toString();
                moveNext();
        });

        return view;
    }


    public void moveNext() {

        viewPager.setCurrentItem(1);
        segmentedProgressBar.setCompletedSegments(1);

    }
}
