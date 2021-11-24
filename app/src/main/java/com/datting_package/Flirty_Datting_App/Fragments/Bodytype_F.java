package com.datting_package.Flirty_Datting_App.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A;
import com.datting_package.Flirty_Datting_App.Adapters.ProfileOptionsAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.ProfileInfoModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bodytype_F extends RootFragment {

    View view;
    RecyclerView rv;
    ProfileOptionsAdapter adapter;
    ArrayList<ProfileInfoModel> list;


    public Bodytype_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bodytype, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();
        Functions.addValuesToRv(list, Variables.ARR_LIST_SMOKE);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) EditProfileVp_A.vpRl.getLayoutParams();
        lp1.height = (int) (Variables.height / 2);

        EditProfileVp_A.vpRl.setLayoutParams(lp1);

        EditProfileVp_A.next.setVisibility(View.VISIBLE);


        adapter = new ProfileOptionsAdapter(getContext(), list, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem() + 1);
                EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1) + "/11");
                EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));

                methodHidelinearlayout(EditProfileVp_A.livingLl, EditProfileVp_A.descLl, EditProfileVp_A.happyLl,
                        EditProfileVp_A.genderLl, EditProfileVp_A.kidsLl, EditProfileVp_A.bodytypeLl, EditProfileVp_A.drinkLl,
                        EditProfileVp_A.smokeLl, EditProfileVp_A.profqsLl, EditProfileVp_A.relationLl, EditProfileVp_A.missingLl);

            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);

        return view;
    }


    private void methodHidelinearlayout(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4,
                                        LinearLayout ll5, LinearLayout ll6, LinearLayout ll7, LinearLayout ll8,
                                        LinearLayout ll9, LinearLayout ll10, LinearLayout ll11) {

        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.GONE);
        ll6.setVisibility(View.GONE);
        ll7.setVisibility(View.GONE);
        ll8.setVisibility(View.GONE);
        ll9.setVisibility(View.GONE);
        ll10.setVisibility(View.GONE);
        ll11.setVisibility(View.GONE);

    }

}
