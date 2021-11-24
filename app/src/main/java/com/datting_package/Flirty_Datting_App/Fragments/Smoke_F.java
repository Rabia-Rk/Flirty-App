package com.datting_package.Flirty_Datting_App.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A;
import com.datting_package.Flirty_Datting_App.Adapters.ProfileOptionsAdapter;
import com.datting_package.Flirty_Datting_App.Models.ProfileInfoModel;
import com.datting_package.Flirty_Datting_App.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Smoke_F extends RootFragment {

    View view;
    RecyclerView rv;
    ProfileOptionsAdapter adapter;
    ArrayList<ProfileInfoModel> list;

    public Smoke_F() {
        // Required empty public constructor
    }

    Context context;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_smoke, container, false);
        context = getContext();
        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();
        Functions.addValuesToRv(list, Variables.ARR_LIST_SMOKE);



        listView = (ListView) view.findViewById(R.id.simple_list);

        final ArrayAdapter<String> arrayAdapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_SMOKE);


        listView.setOnItemClickListener((parent, view, position, id) ->  {


                Variables.varSmoking = Variables.ARR_LIST_SMOKE[position];

                int adaptorPositionTotal = EditProfileVp_A.segmentAdapter.getCount();
                int adpCurrentItemPosition = EditProfileVp_A.vp.getCurrentItem() + 1;

                EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem() + 1);
                EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1) + "/ " + EditProfileVp_A.segmentAdapter.getCount());
                EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.coloraccent));



                Variables.varSmoking = Variables.ARR_LIST_SMOKE[position];

                if (adaptorPositionTotal == adpCurrentItemPosition) {

                    EditProfileVp_A.createJsonForAPI(context);
                    getActivity().finish();
                } else {

                    EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem() + 1);
                    EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1) + "/ " + EditProfileVp_A.segmentAdapter.getCount());
                    EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.coloraccent));

                    EditProfileVp_A.getFragmentName(adpCurrentItemPosition);

                }
            });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(arrayAdapter);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) EditProfileVp_A.vpRl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        EditProfileVp_A.vpRl.setLayoutParams(lp1);

        EditProfileVp_A.next.setVisibility(View.VISIBLE);


        adapter = new ProfileOptionsAdapter(getContext(), list, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

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






}
