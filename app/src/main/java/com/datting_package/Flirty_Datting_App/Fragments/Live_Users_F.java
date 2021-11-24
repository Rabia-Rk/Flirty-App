package com.datting_package.Flirty_Datting_App.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.datting_package.Flirty_Datting_App.Adapters.LiveUserAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.LiveStreaming.activities.StreamingMain_A;
import com.datting_package.Flirty_Datting_App.Models.LiveUserModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

import io.agora.rtc.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Live_Users_F extends RootFragment implements View.OnClickListener {


    View view;
    Context context;
    ArrayList<LiveUserModel> dataList;
    RecyclerView recyclerView;
    LiveUserAdapter adapter;

    DatabaseReference rootref;

    TextView noDataFound;
    ChildEventListener valueEventListener;


    public Live_Users_F() {
        // Required empty public constructor
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_live_users, container, false);
        context = getContext();
        rootref = FirebaseDatabase.getInstance().getReference();

        dataList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        recyclerView.setHasFixedSize(true);

        adapter = new LiveUserAdapter(context, dataList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                LiveUserModel liveUserModel = (LiveUserModel) model;
                openHugmeLive(liveUserModel.getUser_id(),
                        liveUserModel.getUser_name(), liveUserModel.getUser_picture(), Constants.CLIENT_ROLE_AUDIENCE);
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        view.findViewById(R.id.go_live_layout).setOnClickListener(this::onClick);

        getData();

        noDataFound = view.findViewById(R.id.no_data_found);
        return view;
    }

    public void getData() {

        valueEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LiveUserModel model = dataSnapshot.getValue(LiveUserModel.class);
                dataList.add(model);
                adapter.notifyDataSetChanged();
                noDataFound.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                LiveUserModel model = dataSnapshot.getValue(LiveUserModel.class);

                for (int i = 0; i < dataList.size(); i++) {
                    if (model.getUser_id().equals(dataList.get(i).getUser_id())) {
                        dataList.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();

                if (dataList.isEmpty()) {
                    noDataFound.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        rootref.child("LiveUsers").addChildEventListener(valueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rootref != null && valueEventListener != null)
            rootref.child("LiveUsers").removeEventListener(valueEventListener);
    }

    public void openHugmeLive(String userId, String userName, String userImage, int role) {

        if (checkPermissions()) {

            Intent intent = new Intent(getActivity(), StreamingMain_A.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("user_name", userName);
            intent.putExtra("user_picture", userImage);
            intent.putExtra("user_role", role);
            startActivity(intent);

        }
    }

    public boolean checkPermissions() {

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, permissions)) {
            requestPermissions(permissions, 2);
        } else {

            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_live_layout:
                String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);
                String userName = Functions.getInfo(context, "first_name")
                        + " " + Functions.getInfo(context, "last_name");
                String userImage = Functions.getInfo(context, "image1");

                openHugmeLive(userId, userName, userImage, Constants.CLIENT_ROLE_BROADCASTER);
                break;

            default:
                break;
        }
    }
}
