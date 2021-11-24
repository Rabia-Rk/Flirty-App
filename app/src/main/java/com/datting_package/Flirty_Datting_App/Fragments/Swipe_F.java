package com.datting_package.Flirty_Datting_App.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.appyvet.materialrangebar.RangeBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Activities.MainActivity;
import com.datting_package.Flirty_Datting_App.Activities.SearchPlaces_A;
import com.datting_package.Flirty_Datting_App.Adapters.CardAdapter;
import com.datting_package.Flirty_Datting_App.BottomSheet.SwipeProfile_BottomSheet;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class Swipe_F extends RootFragment {

    public static CardStackView cardStackView;
    public static String tokenOtherUser;
    public static ArrayList<Object> removedUserIdsIndex = new ArrayList<>();
    public static TextView searchPlace;
    public CardAdapter adapter;
    View v;
    String userIdOfSwipe;
    Context context;
    String searchGender, searchFilterBy, searchAgeMin, searchAgeMax, searchDistance;
    List<NearbyUsersModel> nearbyList = new ArrayList<>();
    ProgressBar progressBar;
    ImageView imageView;
    Toolbar tb;
    RelativeLayout findNearbyUser;
    PulsatorLayout pulsator;
    SimpleDraweeView profileimage;
    Button changeSettingBtn;
    String locationString;

    public static void updatedataOnLeftSwipe(Context context, final NearbyUsersModel item) {
        try {
            Functions.countNumClick(context);
            Functions.displayFbAd(context);
        } catch (Exception b) {
            b.printStackTrace();
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
                    mymap.put("match", "false");
                    mymap.put("type", "dislike");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");


                    Map othermap = new HashMap<>();
                    othermap.put("match", "false");
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

    public static void updatedataOnrightSwipe(Context context, final NearbyUsersModel item) {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_swipe, null);
        context = getContext();
        Main_F.toolbar.setVisibility(View.GONE);

        nearbyList.clear();
        progressBar = v.findViewById(R.id.progress_bar);
        pulsator = (PulsatorLayout) v.findViewById(R.id.pulsator);
        findNearbyUser = v.findViewById(R.id.find_nearby_User);
        profileimage = v.findViewById(R.id.profileimage);
        changeSettingBtn = v.findViewById(R.id.change_setting_btn);

        String searchPlace = SharedPrefrence.getString(context, "" + SharedPrefrence.shareUserSearchPlaceName);

        if (searchPlace != null) {

            if (searchPlace.equals("People nearby")) {
                locationString = SharedPrefrence.getString(context, "" + SharedPrefrence.uLatLng);
            } else {
                locationString = SharedPrefrence.getString(context, "" + SharedPrefrence.shareUserSearchPlaceLatLng);
            }

        } else {
            locationString = SharedPrefrence.getString(context, "" + SharedPrefrence.uLatLng);
        }


        tb = (Toolbar) v.findViewById(R.id.toolbar);
        imageView = (ImageView) v.findViewById(R.id.control_IV_id);


        imageView.setOnClickListener(v -> {
            displayFilterDialogue();

        });

        String userId = SharedPrefrence.getString(context, "" + SharedPrefrence.uId);


        getUserInfo(userId);

        changeSettingBtn.setOnClickListener(v -> {

            displayFilterDialogue();

        });


        try {
            String img = Functions.getInfo(context, "image1");
            if (img != null && !img.equals("")) {
                Uri uri = Uri.parse(img);
                profileimage.setImageURI(uri);
            }
        } catch (Exception v) {
            Functions.logDMsg( "" + v.toString());
        }

        cardStackView = (CardStackView) v.findViewById(R.id.swipe_csv_id);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                int orgIndex = cardStackView.getTopIndex() - 1;

                if (orgIndex < adapter.getCount()) {

                    if (direction.equals(SwipeDirection.Left)) {
                        Swipe_F.updatedataOnLeftSwipe(getContext(), adapter.getItem(orgIndex));
                    } else if (direction.equals(SwipeDirection.Right)) {
                        Swipe_F.updatedataOnrightSwipe(getContext(), adapter.getItem(orgIndex));

                    }

                    if (cardStackView.getTopIndex() == adapter.getCount()) {
                        tb.setVisibility(View.VISIBLE);
                        findNearbyUser.setVisibility(View.VISIBLE);
                        pulsator.start();
                    }


                }


            }


            @Override
            public void onCardReversed() {
            }

            @Override
            public void onCardMovedToOrigin() {

            }

            @Override
            public void onCardClicked(int index) {

                NearbyUsersModel getNearby = nearbyList.get(index);


                Bundle bundleUserProfile = new Bundle();
                bundleUserProfile.putString("user_id", "" + userIdOfSwipe);

                bundleUserProfile.putSerializable("user_near_by", getNearby);

                SwipeProfile_BottomSheet viewProfile = new SwipeProfile_BottomSheet();
                viewProfile.setArguments(bundleUserProfile);
                viewProfile.setCancelable(true);
                viewProfile.show(getActivity().getSupportFragmentManager(), viewProfile.getTag());


            }
        });


        initAdp();
        getNearByUsers();


        return v;
    }

    @Override
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {
            Main_F.toolbar.setVisibility(View.GONE);

            if (Variables.isSearchedAtWw == 1) {
                adapter.clear();
                nearbyList.clear();
                adapter.notifyDataSetChanged();

                getNearByUsers();

                Variables.isSearchedAtWw = 0;
            }

        }
    }


    public void getNearByUsers() {

        progressBar.setVisibility(View.VISIBLE);

        String searchPlace = SharedPrefrence.getString(context, SharedPrefrence.shareUserSearchPlaceName);

        if (searchPlace != null) {

            if (searchPlace.equals("People nearby")) {
                locationString = SharedPrefrence.getString(context, SharedPrefrence.uLatLng);

            } else {
                locationString = SharedPrefrence.getString(context, SharedPrefrence.shareUserSearchPlaceLatLng);
            }

        } else {
            locationString = SharedPrefrence.getString(context, SharedPrefrence.uLatLng);
        }

        String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);

        String search = SharedPrefrence.getString(context, SharedPrefrence.shareSearchParams);
        String searchGender = "", searchAgeMin = "", searchAgeMax = "", searchDistance = "";
        try {

            JSONObject searchObj = new JSONObject(search);
            searchGender = searchObj.optString("gender", Variables.defalutGender);
            searchAgeMin = searchObj.optString("age_min", Variables.defalutMinAge);
            searchAgeMax = searchObj.optString("age_max", Variables.defalutMaxAge);
            searchDistance = searchObj.optString("search_distance", Variables.defalutMaxDistance);
        } catch (Exception n) {
            searchGender = Variables.defalutGender;
            searchAgeMin = Variables.defalutMinAge;
            searchAgeMax = Variables.defalutMaxAge;
            searchDistance = Variables.defalutMaxDistance;
            progressBar.setVisibility(View.GONE);
        }


        final JSONObject parameters = new JSONObject();


        try {
            parameters.put("fb_id", userId);
            parameters.put("lat_long", locationString);
            parameters.put("gender", searchGender);
            parameters.put("distance", searchDistance);
            parameters.put("device_token", MainActivity.token);
            parameters.put("device", context.getResources().getString(R.string.device));
            parameters.put("age_min", searchAgeMin);
            parameters.put("age_max", searchAgeMax);
            parameters.put("version", context.getResources().getString(R.string.version));
            if (MainActivity.purductPurchase)
                parameters.put("purchased", "1");
            else
                parameters.put("purchased", "0");

        } catch (JSONException e) {

            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }

        ApiRequest.callApi(
                context,
                "" + ApiLinks.userNearByMe,
                parameters,
                (requestType, resp) -> {

                    progressBar.setVisibility(View.GONE);
                    Functions.logDMsg( "Near from Search \n " + parameters.toString());
                    Functions.logDMsg( "Near from Response  \n " + resp);
                    try {
                        JSONObject response = new JSONObject(resp);

                        JSONArray msgArr = response.getJSONArray("msg");

                        if (msgArr.length() == 0) {

                            findNearbyUser.setVisibility(View.VISIBLE);
                            pulsator.start();


                        } else {

                            adapter.clear();
                            nearbyList.clear();

                            findNearbyUser.setVisibility(View.GONE);

                            for (int i = 0; i < msgArr.length(); i++) {
                                JSONObject userObj = msgArr.getJSONObject(i);
                                userObj.getString("fb_id");
                                userObj.getString("first_name");
                                userObj.getString("last_name");
                                userObj.getString("birthday");
                                userObj.getString("distance");
                                userObj.getString("image1");

                                userObj.getString("image2");
                                userObj.getString("image3");
                                userObj.getString("image4");
                                userObj.getString("image5");
                                userObj.getString("image6");


                                userObj.getString("job_title");
                                userObj.getString("company");
                                userObj.getString("school");
                                userObj.getString("living");
                                userObj.getString("children");
                                userObj.getString("smoking");
                                userObj.getString("drinking");
                                userObj.getString("relationship");
                                userObj.getString("sexuality");
                                userObj.getString("block");


                                NearbyUsersModel nearby = new NearbyUsersModel(
                                        "" + userObj.getString("fb_id"),
                                        "" + userObj.getString("first_name"),
                                        "" + userObj.getString("last_name"),
                                        "" + userObj.getString("birthday"),
                                        "" + userObj.getString("about_me"),
                                        "" + userObj.getString("distance"),
                                        "" + userObj.getString("image1"),
                                        "like",

                                        "" + userObj.getString("job_title"),
                                        "" + userObj.getString("company"),
                                        "" + userObj.getString("school"),
                                        "" + userObj.getString("living"),
                                        "" + userObj.getString("children"),
                                        "" + userObj.getString("smoking"),
                                        "" + userObj.getString("drinking"),
                                        "" + userObj.getString("relationship"),
                                        "" + userObj.getString("sexuality"),
                                        "" + userObj.getString("block"),
                                        "" + userObj.getString("image2"),
                                        "" + userObj.getString("image3"),
                                        "" + userObj.getString("image4"),
                                        "" + userObj.getString("image5"),
                                        "" + userObj.getString("image6")

                                );


                                adapter.add(nearby);
                                nearbyList.add(nearby);

                            }


                            adapter.notifyDataSetChanged();


                        }


                    } catch (Exception b) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );


    }


    public void displayFilterDialogue() {

        final Dialog dialog = new Dialog(getContext());

        final View dialogview = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout, null);

        String handleSearch = SharedPrefrence.getString(context, SharedPrefrence.shareSearchParams);

        String handleSearchGender = "", handleSearchFilterBy = "", handleSearchAgeMin = "", handleSearchAgeMax = "",
                handleSearchDistance = "";

        searchGender = Variables.defalutGender;
        searchFilterBy = Variables.defalutSearchByStatus;
        searchAgeMin = Variables.defalutMinAge;
        searchAgeMax = Variables.defalutMaxAge;
        searchDistance = Variables.defalutMaxDistance;

        String userSearchPlace = SharedPrefrence.getString(context, "" + SharedPrefrence.shareUserSearchPlaceName);


        final TextView filterText = dialogview.findViewById(R.id.filter_text);
        String sourceString = "<b>Filter</b> ";
        filterText.setText(Html.fromHtml(sourceString));

        final TextView tv = (TextView) dialogview.findViewById(R.id.guys_id);
        final TextView tv1 = (TextView) dialogview.findViewById(R.id.girls_id);
        final TextView tv2 = (TextView) dialogview.findViewById(R.id.both_id);

        final TextView tv4 = (TextView) dialogview.findViewById(R.id.all_id);
        final TextView tv5 = (TextView) dialogview.findViewById(R.id.online_id);
        final TextView tv6 = (TextView) dialogview.findViewById(R.id.new_id);

        searchPlace = dialogview.findViewById(R.id.search_place);
        if (userSearchPlace != null) {
            // If it is not Null
            searchPlace.setText("" + userSearchPlace);
        } else {
            searchPlace.setText("People nearby");
        }


        final ImageView iv1 = (ImageView) dialogview.findViewById(R.id.dialog_cross_Id);
        final ImageView iv2 = (ImageView) dialogview.findViewById(R.id.dialog_tick_id);
        RangeBar rangeBar = (RangeBar) dialogview.findViewById(R.id.ww_RB_id);

        RelativeLayout nearByRlId = dialogview.findViewById(R.id.near_by_RL_id);


        nearByRlId.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), SearchPlaces_A.class));

        });


        final TextView textAge = dialogview.findViewById(R.id.text_age);
        final TextView distance = dialogview.findViewById(R.id.distance);
        SeekBar simpleSeekBar = dialogview.findViewById(R.id.simpleSeekBar);
        simpleSeekBar.setProgress(Integer.parseInt(Variables.defalutMaxDistance));
        distance.setText("" + simpleSeekBar.getProgress());
        dialog.setContentView(dialogview);

        if (handleSearch != null) {

            try {

                JSONObject searchObj = new JSONObject(handleSearch);
                handleSearchGender = searchObj.getString("gender");
                handleSearchFilterBy = searchObj.getString("filter_by");
                handleSearchAgeMin = searchObj.getString("age_min");
                handleSearchAgeMax = searchObj.getString("age_max");
                handleSearchDistance = searchObj.getString("search_distance");

                textAge.setText(handleSearchAgeMin + " - " + handleSearchAgeMax);

                rangeBar.setRangePinsByValue(Float.parseFloat(handleSearchAgeMin), Float.parseFloat(handleSearchAgeMax));

            } catch (Exception b) {
                b.printStackTrace();
            }

            searchDistance = handleSearchDistance;
            searchAgeMin = handleSearchAgeMin;
            searchAgeMax = handleSearchAgeMax;
            searchGender = handleSearchGender;
            try {
                if (handleSearchDistance != null) {
                    simpleSeekBar.setProgress(Integer.parseInt(handleSearchDistance));
                    distance.setText(handleSearchDistance);
                } else {
                    distance.setText(Variables.defalutMaxDistance);
                    simpleSeekBar.setProgress(Integer.parseInt(Variables.defalutMaxDistance));
                }

            } catch (Exception b) {
                b.printStackTrace();
            }

            if (handleSearchGender.equals("female")) {
                tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            } else if (handleSearchGender.equals("male")) {
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            } else {

                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            }


            if (handleSearchFilterBy.equals("All")) {

                tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            } else if (handleSearchFilterBy.equals("Online")) {
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            } else {
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            }

        }


        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance.setText("" + progress);
                searchDistance = "" + progress + "";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        try {
            rangeBar.setOnRangeBarChangeListener((rangeBar1, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
                searchAgeMin = leftPinValue;
                searchAgeMax = rightPinValue;
                textAge.setText("" + leftPinValue + " - " + "" + rightPinValue);
            });

        } catch (Exception b) {
            b.printStackTrace();
        }


        tv.setOnClickListener(v -> {
            tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv1.setBackgroundResource(R.drawable.d_gray_border);
            tv2.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv);

        });


        tv1.setOnClickListener(v -> {
            tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv.setBackgroundResource(R.drawable.d_gray_border);
            tv2.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv1);

        });


        tv2.setOnClickListener(v -> {
            tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv.setBackgroundResource(R.drawable.d_gray_border);
            tv1.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv2);

        });


        tv4.setOnClickListener(v -> {
            tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv5.setBackgroundResource(R.drawable.d_gray_border);
            tv6.setBackgroundResource(R.drawable.d_gray_border);
            addAllFilter(tv4);

        });


        tv5.setOnClickListener(v -> {
            tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv4.setBackgroundResource(R.drawable.d_gray_border);
            tv6.setBackgroundResource(R.drawable.d_gray_border);
            addAllFilter(tv5);  //get Value from All Filter

        });


        tv6.setOnClickListener(v -> {
            tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv5.setBackgroundResource(R.drawable.d_gray_border);
            tv4.setBackgroundResource(R.drawable.d_gray_border);
            addAllFilter(tv6);  //get Value from All Filter
        });


        iv1.setOnClickListener(v -> {
            dialog.dismiss();
        });


        iv2.setOnClickListener(v -> {
            try {
                JSONObject searchParams = new JSONObject();
                searchParams.put("gender", "" + searchGender);
                searchParams.put("filter_by", "" + searchFilterBy);
                searchParams.put("age_min", "" + searchAgeMin);
                searchParams.put("age_max", "" + searchAgeMax);
                searchParams.put("search_distance", "" + searchDistance);


                SharedPrefrence.saveString(context, "" + searchParams.toString(),
                        "" + SharedPrefrence.shareSearchParams);

            } catch (Exception b) {
                b.printStackTrace();

            }

            Variables.isSearched = 1;

            getNearByUsers();
            dialog.dismiss();

        });

        dialog.show();


    }

    public void addGender(TextView textView) {
        searchGender = textView.getText().toString();

        if (searchGender.equals("Male")) {
            searchGender = "male";
        } else if (searchGender.equals("Female")) {
            searchGender = "female";
        } else if (searchGender.equals("Both")) {
            searchGender = "all";
        }

    }


    public void addAllFilter(TextView textView) {
        searchFilterBy = textView.getText().toString();
    }


    public void initAdp() {

        adapter = new CardAdapter(context, 0, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

                if (view.getId() == R.id.right_overlay) {
                    swipeRight();
                } else if (view.getId() == R.id.left_overlay) {

                    swipeLeft();
                }
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        }, nearbyList);

        cardStackView.setAdapter(adapter);

    }


    public void swipeLeft() {
        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(400);
        translateY.setStartDelay(400);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet);

    }


    public void swipeRight() {

        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(400);
        translateY.setStartDelay(400);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet);
    }


    public void getUserInfo(String userId) {
        Functions.getUserInfo("" + userId, context);
    }


}
