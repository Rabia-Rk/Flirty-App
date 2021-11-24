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
import android.widget.LinearLayout;
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
public class Living_F extends RootFragment {

    View view;
    RecyclerView rv;
    ProfileOptionsAdapter adapter;
    ArrayList<ProfileInfoModel> list;

    public Living_F() {
        // Required empty public constructor
    }


    Context context;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gender, container, false);
        context = getContext();
        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();

        Functions.addValuesToRv(list, Variables.ARR_LIST_LIVING);

        listView = (ListView) view.findViewById(R.id.simple_list);

        final ArrayAdapter<String> arrayAdapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_LIVING);


        listView.setOnItemClickListener((parent, view1, position, id) ->  {



                Variables.varLiving = Variables.ARR_LIST_LIVING[position];

                int adaptorPositionTotal = EditProfileVp_A.segmentAdapter.getCount();
                int adpCurrentItemPosition = EditProfileVp_A.vp.getCurrentItem()+1;

                Variables.varLiving = Variables.ARR_LIST_LIVING[position];

                if(adaptorPositionTotal == adpCurrentItemPosition){

                    EditProfileVp_A.createJsonForAPI(context);
                    getActivity().finish();
                }else{
                    EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem()+1);
                    EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1)+"/ " + EditProfileVp_A.segmentAdapter.getCount());
                    EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.zink2));

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
                int adaptorPositionTotal = EditProfileVp_A.segmentAdapter.getCount();
                int adpCurrentItemPosition = EditProfileVp_A.vp.getCurrentItem()+1;

                //Living_F Get Values from List
                ProfileInfoModel item = list.get(postion);

                Variables.varLiving = item.getText();

                if(adaptorPositionTotal == adpCurrentItemPosition){

                    EditProfileVp_A.createJsonForAPI(context);
                    getActivity().finish();
                }else{
                    EditProfileVp_A.vp.setCurrentItem(EditProfileVp_A.vp.getCurrentItem()+1);
                    EditProfileVp_A.fragCounter.setText((EditProfileVp_A.vp.getCurrentItem() + 1)+"/ " + EditProfileVp_A.segmentAdapter.getCount());
                    EditProfileVp_A.colorRl.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.zink2));

                    methodHidelinearlayout(EditProfileVp_A.kidsLl, EditProfileVp_A.descLl, EditProfileVp_A.happyLl,
                            EditProfileVp_A.genderLl, EditProfileVp_A.livingLl, EditProfileVp_A.relationLl, EditProfileVp_A.drinkLl,
                            EditProfileVp_A.smokeLl, EditProfileVp_A.profqsLl, EditProfileVp_A.bodytypeLl, EditProfileVp_A.missingLl);

                }

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
                                        LinearLayout ll9, LinearLayout ll10, LinearLayout ll11){

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
