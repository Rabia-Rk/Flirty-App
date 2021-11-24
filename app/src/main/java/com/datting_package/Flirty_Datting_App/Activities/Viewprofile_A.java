package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import com.datting_package.Flirty_Datting_App.Adapters.ViewPagerAdpNew;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.CodeClasses.VerticalViewPager;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import me.relex.circleindicator.CircleIndicator;

public class Viewprofile_A extends AppCompatActivity {

    VerticalViewPager vp;
    ViewPagerAdpNew adp;
    CircleIndicator ci;
    CoordinatorLayout rl;
    BottomSheetBehavior behavior ;
    ImageView icCancel;
    Context context;
    TextView nameAndAge, aboutMeText;

    List<String> listProfileImg;
    Button logout;
    FlowLayout item;
    ArrayList<String> aboutMe = new ArrayList<>();
    ArrayList<Integer> aboutMePics = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        context = Viewprofile_A.this;
        rl = (CoordinatorLayout) findViewById(R.id.view_prof_rl_id);
        listProfileImg = new ArrayList<>();
        aboutMeText = findViewById(R.id.about_me);

        String firstName = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "first_name"
        );

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(v-> {
                SharedPrefrence.logoutUser(context);

        });

        String age = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "age"
        );

        String image1 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image1"
        );

        String image2 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image2"
        );

        String image3 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image3"
        );

        String image4 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image4"
        );

        String image5 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image5"
        );

        String image6 = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "image6"
        );


        if(image1.equals("") || image1.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image1);

        }


        if(image2.equals("") || image2.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image2);
        }

        if(image3.equals("") || image3.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image3);
        }

        if(image4.equals("") || image4.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image4);
        }

        if(image5.equals("") || image5.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image5);
        }

        if(image6.equals("") || image6.equals("0")){
            // If Empty
        }else{
            // If not Empty
            listProfileImg.add(image6);
        }

        nameAndAge = findViewById(R.id.name_and_age);
        nameAndAge.setText("" + firstName + ", " + age);
        aboutMeText.setText("" + SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "about_me"
        ));

        // Basic Profile_F add into ListArray





        if( SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "living"
        ).equals("")){
             // If EMpty

        }else{
            // Not Empty
            aboutMePics.add(R.drawable.ic_living);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "living"
            ));
        }

        if(SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "children"
        ).equals("")){
            // If EMpty

        }else{
            // If not Empty
            aboutMePics.add(R.drawable.ic_childrens);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "children"
            ));

        }


        if(SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "smoking"
        ).equals("")){
            /// If EMpty

        }else{
            // If nOt EMpty
            aboutMePics.add(R.drawable.ic_smoking);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "smoking"
            ));

        }


        if(SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "drinking"
        ).equals("")){
             // If EMpty

        }else{
            // If Not EMpty
            aboutMePics.add(R.drawable.ic_drinking);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "drinking"
            ));

        }

        if(SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "relationship"
        ).equals("")){
             // If EMpty

        }else{
            // If Not Empty
            aboutMePics.add(R.drawable.ic_relationship);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "relationship"
            ));
        }


        if(SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "sexuality"
        ).equals("")){
            // If Empty

        }else{
            // If Not EMpty
            aboutMePics.add(R.drawable.ic_sexuality);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "sexuality"
            ));
        }


        inflateLayout();



        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (320 * scale + 0.5f);

        icCancel = findViewById(R.id.ic_cancel);

        icCancel.setOnClickListener(v-> {
                finish();

        });


        CoordinatorLayout.LayoutParams relBtn = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels);
        rl.setLayoutParams(relBtn);

        vp = (VerticalViewPager) findViewById(R.id.view_prof_vp_id);
        adp = new ViewPagerAdpNew(getApplicationContext(), listProfileImg);
        ci = (CircleIndicator) findViewById(R.id.view_prof_ci_id);

        vp.setAdapter(adp);
        ci.setViewPager(vp);

        final View bottomsheet = findViewById(R.id.bottom_sheet);

        behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }

    // End Method to inflate Layout
    public void inflateLayout(){
        // Method to inflate Layout

        item = findViewById(R.id.inflate_layout);//where you want to add/inflate a view as a child

        for(int i = 0; i< aboutMe.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml
            ImageView image = child.findViewById(R.id.ic_image);
            TextView intrestName = child.findViewById(R.id.intrest_name);
            LinearLayout mainLinearLayout = child.findViewById(R.id.main_linear_layout);
            try{
                aboutMePics.get(i);
                image.setImageResource(aboutMePics.get(i));
            }catch (Exception b){
                b.printStackTrace();
            }


            if(!aboutMe.get(i).equals("0") && !aboutMe.get(i).equals(" ")){
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(Variables.marginLeft, Variables.marginTop, Variables.marginRight, Variables.marginBottom);
                mainLinearLayout.setLayoutParams(buttonLayoutParams);

                intrestName.setText("" + aboutMe.get(i));
                item.addView(child);
            }

        }

        aboutMe.clear();

    }// End Method to inflate Layout

}
