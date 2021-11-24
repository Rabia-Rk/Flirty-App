package com.datting_package.Flirty_Datting_App.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.datting_package.Flirty_Datting_App.Adapters.VPAdapter;
import com.datting_package.Flirty_Datting_App.Chat.Inbox_F;
import com.datting_package.Flirty_Datting_App.Activities.EditProfile_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.Activities.Setting_A;
import com.datting_package.Flirty_Datting_App.Utils.Custom_ViewPager;


public class Main_F extends RootFragment {

    View v;
    public static Toolbar toolbar;
    public  TabLayout tabLayout;
    public  Custom_ViewPager viewPager;
    VPAdapter adp;
    ImageView iv2, iv3;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, null);
        context = getContext();
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) v.findViewById(R.id.tablayout_id);
        viewPager = v.findViewById(R.id.main_f_vp_id);



        iv2 = (ImageView) v.findViewById(R.id.main_f_setting_id);
        iv3 = (ImageView) v.findViewById(R.id.main_f_edit_id);


        toolbar.setVisibility(View.GONE);
        adp = new VPAdapter(getChildFragmentManager());
        adp.addFragment(new World_wide_native_ads_F(), "");
        adp.addFragment(new Live_Users_F(), "");
        adp.addFragment(new Swipe_F(), "");
        adp.addFragment(new Inbox_F(), "");
        adp.addFragment(new Profile_F(), "");

        viewPager.setAdapter(adp);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setPagingEnabled(false);

        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.CTI_IV_id);
        imageView1.setImageResource(R.drawable.ic_world_gray);
        tabLayout.getTabAt(0).setCustomView(view1);

        final View view5 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView5 = (ImageView) view5.findViewById(R.id.CTI_IV_id);
        imageView5.setImageResource(R.drawable.ic_live_black);
        tabLayout.getTabAt(1).setCustomView(view5);


        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView22 = (ImageView) view2.findViewById(R.id.CTI_IV_id);
        imageView22.setImageResource(R.drawable.ic_card_color);
        tabLayout.getTabAt(2).setCustomView(view2);

        final View view3 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.CTI_IV_id);
        imageView3.setImageResource(R.drawable.ic_chat_gray);
        tabLayout.getTabAt(3).setCustomView(view3);

        final View view4 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView4 = (ImageView) view4.findViewById(R.id.CTI_IV_id);
        imageView4.setImageResource(R.drawable.ic_profile_gray);
        tabLayout.getTabAt(4).setCustomView(view4);


        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.CTI_IV_id);
                switch (tab.getPosition()){
                    case 0:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_world_color));
                        Main_F.toolbar.setVisibility(View.GONE);
                        break;

                    case 1:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_live_color));
                        Main_F.toolbar.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_card_color));
                        Main_F.toolbar.setVisibility(View.GONE);

                        break;

                    case 3:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_chat_color));
                        Main_F.toolbar.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_profile_color));
                        iv2.setVisibility(View.VISIBLE);
                        iv3.setVisibility(View.VISIBLE);
                        Main_F.toolbar.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;

                }
                tab.setCustomView(view);

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.CTI_IV_id);

                switch (tab.getPosition()){
                    case 0:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_world_gray));
                        break;

                    case 1:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_live_black));
                        break;
                    case 2:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_card_gray));
                        break;

                    case 3:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_chat_gray));
                        break;

                    case 4:
                        image.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_profile_gray));
                        iv2.setVisibility(View.GONE);
                        iv3.setVisibility(View.GONE);
                        break;

                    default:
                        break;

                }

                tab.setCustomView(view);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });






        tabLayout.getTabAt(2).select();





        iv2.setOnClickListener(v-> {
                startActivity(new Intent(getActivity(), Setting_A.class));

        });




        iv3.setOnClickListener(v ->  {
                startActivity(new Intent(getActivity(), EditProfile_A.class));

        });



        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
    }


    @Override
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {

            Main_F.toolbar.setVisibility(View.GONE);

        }
    }


}
