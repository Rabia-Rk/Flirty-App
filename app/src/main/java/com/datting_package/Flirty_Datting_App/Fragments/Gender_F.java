package com.datting_package.Flirty_Datting_App.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A;
import com.datting_package.Flirty_Datting_App.Adapters.ProfileOptionsAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.ProfileInfoModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Gender_F extends RootFragment {

    View view;
    RecyclerView rv;
    ProfileOptionsAdapter adapter;
    ArrayList<ProfileInfoModel> list;
    Context context;
    ListView listView;

    public Gender_F() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gender, container, false);
        context = getContext();
        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();
        list.add(new ProfileInfoModel("Male"));
        list.add(new ProfileInfoModel("Female"));
        list.add(new ProfileInfoModel("I dont want to disclose"));
        list.add(new ProfileInfoModel("No Way"));
        list.add(new ProfileInfoModel("Ask Me"));

        listView = (ListView) view.findViewById(R.id.simple_list);

        final ArrayAdapter<String> adapter1

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_GENDER);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Variables.userGender = Variables.ARR_LIST_GENDER[position];

            int adaptorPositionTotal = EditProfileVp_A.segmentAdapter.getCount();
            int adpCurrentItemPosition = EditProfileVp_A.vp.getCurrentItem() + 1;

            if (adaptorPositionTotal == adpCurrentItemPosition) {
                // Calling API
                EditProfileVp_A.createJsonForAPI(context);
                getActivity().finish();
            } else {
                // If pager element is not last

                EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem() + 1);
                EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1) + "/ " + EditProfileVp_A.segmentAdapter.getCount());
                EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.pink));

                EditProfileVp_A.getFragmentName(adpCurrentItemPosition);

            }

        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter1);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) EditProfileVp_A.vpRl.getLayoutParams();
        lp1.height = (int) (Variables.height / 2);

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
