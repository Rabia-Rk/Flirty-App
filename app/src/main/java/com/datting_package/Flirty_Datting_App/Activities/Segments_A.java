package com.datting_package.Flirty_Datting_App.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.datting_package.Flirty_Datting_App.Adapters.SegmentAdapter;
import com.datting_package.Flirty_Datting_App.Fragments.Segment_add_pic_F;
import com.datting_package.Flirty_Datting_App.Fragments.Segment_dob_F;
import com.datting_package.Flirty_Datting_App.Fragments.Segment_email_F;
import com.datting_package.Flirty_Datting_App.Fragments.Segment_gender_F;
import com.datting_package.Flirty_Datting_App.Fragments.Segment_name_F;
import com.datting_package.Flirty_Datting_App.R;

public class Segments_A extends AppCompatActivity {

    public static SegmentedProgressBar segmentedProgressBar;
    public static ViewPager viewPager;
    SegmentAdapter segmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segments);

        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segment_bar_id);
        viewPager = (ViewPager) findViewById(R.id.view_pager_id);
        viewPager.setOffscreenPageLimit(5);


        segmentAdapter = new SegmentAdapter(getSupportFragmentManager());
        segmentAdapter.addFragment(new Segment_gender_F(), "");
        segmentAdapter.addFragment(new Segment_name_F(), "");
        segmentAdapter.addFragment(new Segment_dob_F(), "");
        segmentAdapter.addFragment(new Segment_email_F(), "");
        segmentAdapter.addFragment(new Segment_add_pic_F(), "");

        viewPager.setAdapter(segmentAdapter);
        segmentedProgressBar.setCompletedSegments(1);

        viewPager.setOnTouchListener((v, event) -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem());
            return true;
        });

    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }

    }

}
