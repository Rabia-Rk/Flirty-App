package com.datting_package.Flirty_Datting_App.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Adapters.UserLikeAdapter;
import com.datting_package.Flirty_Datting_App.BottomSheet.SwipeProfile_BottomSheet;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.FragmentCallback;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_likes_F extends RootFragment {

    View view;
    Context context;

    ArrayList<NearbyUsersModel> dataList;
    RecyclerView recyclerView;
    UserLikeAdapter adapter;

    String likesCount;
    TextView titleTxt;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    Boolean isViewCreated = false;
    FragmentCallback fragmentCallback;
    Boolean isFromTab = false;
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Functions.toastMsg(getActivity(), "on Move");
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            return super.getSwipeDirs(recyclerView, viewHolder);

        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {


            int position = viewHolder.getAdapterPosition();

            NearbyUsersModel item = dataList.get(position);
            dataList.remove(position);


            if (swipeDir == 8) {
                updatedataOnrightSwipe(item);
            } else if (swipeDir == 4) {
                updatedataOnLeftSwipe(item);
            }

            adapter.notifyItemRemoved(position);
            adapter.notifyItemChanged(position);

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive);


            if (dX < 0.0) {
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.VISIBLE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.GONE);
            } else if (dX > 0.0) {
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.GONE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.VISIBLE);

            } else {
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.GONE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.GONE);
            }
        }
    };

    public User_likes_F() {
        //Required public constructor
    }


    public User_likes_F(FragmentCallback fragmentCallback, Boolean isFromTab) {
        this.fragmentCallback = fragmentCallback;
        this.isFromTab = isFromTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        context = getContext();


        databaseReference = FirebaseDatabase.getInstance().getReference();


        progressBar = view.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            likesCount = bundle.getString("like_count");
        }

        titleTxt = view.findViewById(R.id.title_txt);
        titleTxt.setText(likesCount);

        view.findViewById(R.id.back_btn).setOnClickListener(v -> {

            if (fragmentCallback != null) {
                fragmentCallback.onResponce(null);
            }
            getActivity().onBackPressed();
        });


        dataList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        recyclerView.setHasFixedSize(true);

        adapter = new UserLikeAdapter(context, dataList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                openUserDetail((NearbyUsersModel) model);
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        if (isFromTab) {

            view.findViewById(R.id.toolbar).setVisibility(View.GONE);

            view.findViewById(R.id.cardview).
                    setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

            view.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

            getPeopleNearby();
        }

        isViewCreated = true;
        return view;
    }

    private void getPeopleNearby() {

        progressBar.setVisibility(View.VISIBLE);
        String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(context, ApiLinks.mylikies, parameters, new CallBack() {
            @Override
            public void getResponse(String requestType, String response) {
                progressBar.setVisibility(View.GONE);

                parseUserInfo(response);
            }
        });


    }


    public void parseUserInfo(String loginData) {
        try {
            JSONObject jsonObject = new JSONObject(loginData);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                dataList.clear();
                JSONArray msg = jsonObject.getJSONArray("msg");

                for (int i = 0; i < msg.length(); i++) {
                    JSONObject userdata = msg.getJSONObject(i);
                    NearbyUsersModel item = new NearbyUsersModel();
                    item.setFb_id(userdata.optString("fb_id"));
                    item.setFirst_name(userdata.optString("first_name"));
                    item.setLast_name(userdata.optString("last_name"));
                    item.setJob_title(userdata.optString("job_title"));
                    item.setCompany(userdata.optString("company"));
                    item.setSchool(userdata.optString("school"));
                    item.setBirthday(userdata.optString("birthday"));
                    item.setAbout_me(userdata.optString("about_me"));
                    item.setDistance(userdata.optString("distance"));
                    item.setGender(userdata.optString("gender"));
                    item.setSwipe(userdata.optString("swipe"));

                    item.setImage1(userdata.optString("image1"));
                    item.setImage2(userdata.optString("image2"));
                    item.setImage3(userdata.optString("image3"));
                    item.setImage4(userdata.optString("image4"));
                    item.setImage5(userdata.optString("image5"));
                    item.setImage6(userdata.optString("image6"));


                    dataList.add(item);
                }

                if (dataList.isEmpty()) {
                    view.findViewById(R.id.nodata_found_txt).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.nodata_found_txt).setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            view.findViewById(R.id.nodata_found_txt).setVisibility(View.VISIBLE);

        }


    }


    public void updatedataOnLeftSwipe(final NearbyUsersModel item) {


        try {

            Functions.countNumClick(context);


            Functions.displayFbAd(context);
        } catch (Exception b) {

            Functions.toastMsg(context, "" + b.toString());

        }


        Functions.sendPushNotification(context, item.getFb_id(), "dislike you", "dislike");

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();

        final String user_id = Functions.getInfo(context, "fb_id");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh");
        final String formattedDate = df.format(c);

        rootref.child("Match").child(item.getFb_id()).child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map mymap = new HashMap<>();
                    mymap.put("fragment_match", "false");
                    mymap.put("type", "dislike");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");


                    Map othermap = new HashMap<>();
                    othermap.put("fragment_match", "false");
                    othermap.put("type", "dislike");
                    othermap.put("status", "0");
                    othermap.put("time", formattedDate);
                    othermap.put("name", item.getFirst_name());
                    othermap.put("effect", "false");

                    rootref.child("Match").child(user_id + "/" + item.getFb_id()).updateChildren(mymap);
                    rootref.child("Match").child(item.getFb_id() + "/" + user_id).updateChildren(othermap);

                } else {
                    Map mymap = new HashMap<>();
                    mymap.put("fragment_match", "false");
                    mymap.put("type", "dislike");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");

                    Map othermap = new HashMap<>();
                    othermap.put("fragment_match", "false");
                    othermap.put("type", "dislike");
                    othermap.put("status", "0");
                    othermap.put("time", formattedDate);
                    othermap.put("name", item.getFirst_name());
                    othermap.put("effect", "false");

                    rootref.child("Match").child(user_id + "/" + item.getFb_id()).setValue(mymap);
                    rootref.child("Match").child(item.getFb_id() + "/" + user_id).setValue(othermap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void updatedataOnrightSwipe(final NearbyUsersModel item) {
        try {

            Functions.countNumClick(context);


            Functions.displayFbAd(context);
        } catch (Exception b) {

            b.printStackTrace();
        }


        Functions.sendPushNotification(context, item.getFb_id(), "Like you", "like");

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        final String user_id = Functions.getInfo(context, "fb_id");

        final String user_name = Functions.getInfo(context, "first_name");


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh");
        final String formattedDate = df.format(c);

        Query query = rootref.child("Match").child(item.getFb_id()).child(user_id);
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() || item.getSwipe().equals("like")) {
                    Map mymap = new HashMap<>();
                    mymap.put("fragment_match", "true");
                    mymap.put("type", "like");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");

                    Map othermap = new HashMap<>();
                    othermap.put("fragment_match", "true");
                    othermap.put("type", "like");
                    othermap.put("status", "0");
                    othermap.put("time", formattedDate);
                    othermap.put("name", user_name);
                    othermap.put("effect", "false");

                    rootref.child("Match").child(user_id + "/" + item.getFb_id()).updateChildren(mymap);
                    rootref.child("Match").child(item.getFb_id() + "/" + user_id).updateChildren(othermap);

                } else {
                    Map mymap = new HashMap<>();
                    mymap.put("fragment_match", "false");
                    mymap.put("type", "like");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");

                    Map othermap = new HashMap<>();
                    othermap.put("fragment_match", "false");
                    othermap.put("type", "like");
                    othermap.put("status", "0");
                    othermap.put("time", formattedDate);
                    othermap.put("name", user_name);
                    othermap.put("effect", "false");

                    rootref.child("Match").child(user_id + "/" + item.getFb_id()).setValue(mymap);
                    rootref.child("Match").child(item.getFb_id() + "/" + user_id).setValue(othermap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void openUserDetail(NearbyUsersModel item) {

        Bundle bundleUserProfile = new Bundle();
        bundleUserProfile.putString("user_id", "" + item.getFb_id());

        bundleUserProfile.putSerializable("user_near_by", item);

        SwipeProfile_BottomSheet viewProfile = new SwipeProfile_BottomSheet();
        viewProfile.setArguments(bundleUserProfile);
        viewProfile.setCancelable(true);
        viewProfile.show(getFragmentManager(), viewProfile.getTag());


    }

}
