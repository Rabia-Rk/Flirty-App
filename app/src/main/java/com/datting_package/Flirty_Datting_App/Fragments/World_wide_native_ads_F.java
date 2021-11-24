package com.datting_package.Flirty_Datting_App.Fragments;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.appyvet.materialrangebar.RangeBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Activities.MainActivity;
import com.datting_package.Flirty_Datting_App.Activities.SearchPlaces_A;
import com.datting_package.Flirty_Datting_App.Adapters.WorldWideAdapter;
import com.datting_package.Flirty_Datting_App.BottomSheet.WorldWideProfile_BottomSheet;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.Utils.SpacesItemDecoration;
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
import java.util.List;
import java.util.Map;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class World_wide_native_ads_F extends RootFragment {

    public static RecyclerView recyclerView;
    public static WorldWideAdapter adapter;
    public static List<Object> nearbyList = new ArrayList<>();
    public static int numberofads;
    public static int swipePosition, viewType;
    public static TextView searchPlaceWw;
    View v;
    Context context;
    RelativeLayout findNearbyUser;
    PulsatorLayout pulsator;
    ProgressBar progressBar;
    String userIdOfSwipe;
    ImageView imageView;
    String searchGender, searchFilterBy, searchAgeMin, searchAgeMax, searchDistance;
    TextView distance;
    Toolbar toolbar;
    SimpleDraweeView profileimage;
    Button changeSettingBtn;
    String locationString;
    private AdLoader adLoader;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    public  World_wide_native_ads_F()  {
        //required public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_world_wide, null);

        context = getContext();


        progressBar = v.findViewById(R.id.progress_bar);


        pulsator = (PulsatorLayout) v.findViewById(R.id.pulsator);
        findNearbyUser = v.findViewById(R.id.find_nearby_User);
        profileimage = v.findViewById(R.id.profileimage);

        changeSettingBtn = v.findViewById(R.id.change_setting_btn);
        changeSettingBtn.setOnClickListener(v -> displayFilterDialogue());


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


        try {
            String img = Functions.getInfo(context, "image1");

            if (img != null && !img.equals("")) {
                Uri uri = Uri.parse(img);
                profileimage.setImageURI(uri);
            }


        } catch (Exception b) {
            Functions.logDMsg("" + b.toString());
        }

        MobileAds.initialize(context,
                getString(R.string.admob_app_id));

        adLoader = new AdLoader.Builder(context, context.getResources().getString(R.string.admob_app_id))
                .forUnifiedNativeAd(unifiedNativeAd -> {
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();


        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        imageView = (ImageView) v.findViewById(R.id.control_IV_id);
        imageView.setOnClickListener(v -> {
            displayFilterDialogue();

        });

        distance = v.findViewById(R.id.distance);


        recyclerView = (RecyclerView) v.findViewById(R.id.world_wide_RV_id);


        getNearByUsers();


        return v;

    }


    public static void updatedataOnrightSwipe(Context context, final NearbyUsersModel item) {

        Functions.sendPushNotification(context, item.getFb_id(), "Messege", "like");

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
                    mymap.put("match", "true");
                    mymap.put("type", "like");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");

                    Map othermap = new HashMap<>();
                    othermap.put("match", "true");
                    othermap.put("type", "like");
                    othermap.put("status", "0");
                    othermap.put("time", formattedDate);
                    othermap.put("name", user_name);
                    othermap.put("effect", "false");

                    rootref.child("Match").child(user_id + "/" + item.getFb_id()).updateChildren(mymap);
                    rootref.child("Match").child(item.getFb_id() + "/" + user_id).updateChildren(othermap);

                } else {
                    Map mymap = new HashMap<>();
                    mymap.put("match", "false");
                    mymap.put("type", "like");
                    mymap.put("status", "0");
                    mymap.put("time", formattedDate);
                    mymap.put("name", item.getFirst_name());
                    mymap.put("effect", "true");

                    Map othermap = new HashMap<>();
                    othermap.put("match", "false");
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

    @Override
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {

            ArrayList<NearbyUsersModel> tempList = new ArrayList<>();
            for (int i = 0; i < nearbyList.size(); i++) {

                if (nearbyList.get(i) instanceof NearbyUsersModel) {
                    NearbyUsersModel item = (NearbyUsersModel) nearbyList.get(i);
                    if (Swipe_F.removedUserIdsIndex.contains(item.getFb_id())) {
                        tempList.add(item);

                    }
                }
            }

            for (int i = 0; i < tempList.size(); i++) {
                nearbyList.remove(tempList.get(i));
            }

            if (adapter != null)
                adapter.notifyDataSetChanged();


            if (Variables.isSearched == 1) {

                nearbyList.clear();
                String handleSearch = SharedPrefrence.getString(context, "" + SharedPrefrence.shareSearchParams);

                if (handleSearch != null) {

                    getNearByUsers();

                    Variables.isSearched = 0;

                }
            }
        }
    }

    public void getNearByUsers() {

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
        }

        Functions.logDMsg("parms at wordwide : "+ parameters.toString());
        progressBar.setVisibility(View.VISIBLE);

        ApiRequest.callApi(
                context,
                ApiLinks.userNearByMe,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String resp) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject response = new JSONObject(resp);

                            handleResponse(response);


                        } catch (Exception b) {
                            Functions.toastMsg(context, "Err " + b.toString());
                        }


                    }
                }
        );


    }

    public void handleResponse(JSONObject response) {
        try {
            JSONArray msgArr = response.getJSONArray("msg");
            if (msgArr.length() == 0) {
                findNearbyUser.setVisibility(View.VISIBLE);
                pulsator.start();


            } else {
                findNearbyUser.setVisibility(View.GONE);

                for (int i = 0; i < msgArr.length(); i++) {
                    JSONObject userObj = msgArr.getJSONObject(i);
                    userObj.getString("fb_id");
                    userObj.getString("first_name");
                    userObj.getString("last_name");
                    userObj.getString("birthday");
                    userObj.getString("distance");
                    userObj.getString("image1");

                    NearbyUsersModel nearby = new NearbyUsersModel(
                            "" + userObj.getString("fb_id"),
                            "" + userObj.getString("first_name"),
                            "" + userObj.getString("last_name"),
                            "" + userObj.getString("birthday"),
                            "" + userObj.getString("about_me"),
                            "" + userObj.getString("distance"),
                            "" + userObj.getString("image1"),
                            "" + userObj.getString("swipe"),
                            "",
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

                    nearbyList.add(nearby);
                }

//                loadNativeAds();

            }

        } catch (Exception b) {
            Functions.toastMsg(context, "Error " + b.toString());
        }

    }

    private void loadNativeAds() {

        numberofads = nearbyList.size() / Variables.adDisplayAfter;

        AdLoader.Builder builder = new AdLoader.Builder(context, getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                unifiedNativeAd -> {

                    mNativeAds.add(unifiedNativeAd);
                    if (!adLoader.isLoading()) {
                        insertAdsInMenuItems();
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError errorCode) {
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        adLoader.loadAds(new AdRequest.Builder().build(), numberofads);
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int index = Variables.adDisplayAfter;
        while (index < nearbyList.size()) {

            for (UnifiedNativeAd ad : mNativeAds) {
                nearbyList.add(index, ad);
                index = index + Variables.adDisplayAfter + 1;
                if (index > nearbyList.size()) {
                    setAdapter();
                    return;
                }

            }

            setAdapter();
        }

    }

    public void setAdapter() {

        adapter = new WorldWideAdapter(new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                viewType = adapter.getItemViewType(postion);

                NearbyUsersModel getNearby = (NearbyUsersModel) nearbyList.get(postion);
                getNearby.getImage1();

                Bundle bundleUserProfile = new Bundle();
                bundleUserProfile.putString("user_id", "" + userIdOfSwipe);
                bundleUserProfile.putString("current_position", "" + swipePosition);
                bundleUserProfile.putString("view_type", "" + viewType);

                bundleUserProfile.putSerializable("user_near_by", getNearby);

                WorldWideProfile_BottomSheet viewProfile = new WorldWideProfile_BottomSheet();
                viewProfile.setArguments(bundleUserProfile);

                viewProfile.show(getActivity().getSupportFragmentManager(), viewProfile.getTag());

                swipePosition = postion;

            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }

        }, nearbyList, context);


        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SpacesItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.base_16), context.getResources().getDimensionPixelOffset(R.dimen.base_90)));

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

    }

    public void displayFilterDialogue() {

        final Dialog dialog = new Dialog(getContext());

        final View dialogview = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout, null);
        String handleSearch = SharedPrefrence.getString(context, "" + SharedPrefrence.shareSearchParams);

        String handleSearchGender = "", handleSearchFilterBy = "", handleSearchAgeMin = "", handleSearchAgeMax = "",
                handleSearchDistance = "";
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

        final ImageView iv1 = (ImageView) dialogview.findViewById(R.id.dialog_cross_Id);
        final ImageView iv2 = (ImageView) dialogview.findViewById(R.id.dialog_tick_id);
        RangeBar rangeBar = (RangeBar) dialogview.findViewById(R.id.ww_RB_id);

        final TextView textAge = dialogview.findViewById(R.id.text_age);
        final TextView distance = dialogview.findViewById(R.id.distance);
        SeekBar simpleSeekBar = dialogview.findViewById(R.id.simpleSeekBar);
        simpleSeekBar.setProgress(Integer.parseInt(Variables.defalutMaxDistance));
        distance.setText("" + simpleSeekBar.getProgress());
        dialog.setContentView(dialogview);

        RelativeLayout nearByRLId = dialogview.findViewById(R.id.near_by_RL_id);
        searchPlaceWw = dialogview.findViewById(R.id.search_place);
        if (userSearchPlace != null) {
            searchPlaceWw.setText("" + userSearchPlace);
        } else {
            searchPlaceWw.setText("People nearby");
        }


        nearByRLId.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchPlaces_A.class)));


        if (handleSearch != null) {

            try {

                JSONObject searchObj = new JSONObject(handleSearch);
                handleSearchGender = searchObj.optString("gender");
                handleSearchFilterBy = searchObj.optString("filter_by");
                handleSearchAgeMin = searchObj.optString("age_min");
                handleSearchAgeMax = searchObj.optString("age_max");
                handleSearchDistance = searchObj.optString("search_distance");
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
                    distance.setText("" + handleSearchDistance);
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


        } else {

            searchGender = Variables.defalutGender;
            searchFilterBy = Variables.defalutSearchByStatus;
            searchAgeMin = Variables.defalutMinAge;
            searchAgeMax = Variables.defalutMaxAge;
            searchDistance = Variables.defalutMaxDistance;
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
                Functions.logDMsg("Left " + leftPinValue + " Right " + rightPinValue);
            });

        } catch (Exception b) {

            Functions.logDMsg("" + b.toString());
        }


        tv.setOnClickListener(v -> {
            tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv1.setBackgroundResource(R.drawable.d_gray_border);
            tv2.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv);  // get Value from gender

        });


        tv1.setOnClickListener(v -> {
            tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv.setBackgroundResource(R.drawable.d_gray_border);
            tv2.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv1);  //get Value from gender

        });


        tv2.setOnClickListener(v -> {
            tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv.setBackgroundResource(R.drawable.d_gray_border);
            tv1.setBackgroundResource(R.drawable.d_gray_border);
            addGender(tv2);  //get Value from gender

        });


        tv4.setOnClickListener(v -> {
            tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            tv5.setBackgroundResource(R.drawable.d_gray_border);
            tv6.setBackgroundResource(R.drawable.d_gray_border);
            addAllFilter(tv4);  //get Value from All Filter

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
            addAllFilter(tv6);

        });


        iv1.setOnClickListener(v -> {
            dialog.dismiss();

        });


        iv2.setOnClickListener(v -> {
            mNativeAds.clear();
            nearbyList.clear();
            try {
                adapter.notifyDataSetChanged();
            } catch (Exception b) {
                b.printStackTrace();
            }

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

            Variables.isSearchedAtWw = 1;
            handleSearchParams();

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

    public void handleSearchParams() {

        String handleSearch = SharedPrefrence.getString(context, "" + SharedPrefrence.shareSearchParams);
        String handleSearchGender = "", handleSearchAgeMin = "", handleSearchAgeMax = "",
                handleSearchDistance = "";

        if (handleSearch != null) {

            try {

                JSONObject searchObj = new JSONObject(handleSearch);
                handleSearchGender = searchObj.getString("gender");
                handleSearchAgeMin = searchObj.getString("age_min");
                handleSearchAgeMax = searchObj.getString("age_max");
                handleSearchDistance = searchObj.getString("search_distance");
            } catch (Exception n) {
                Functions.logDMsg("Obj Error " + n.toString());
            }

            searchDistance = handleSearchDistance;
            searchAgeMin = handleSearchAgeMin;
            searchAgeMax = handleSearchAgeMax;
            searchGender = handleSearchGender;


        }

        try {
            getNearByUsers();

        } catch (Exception b) {
            Functions.logDMsg("" + b.toString());
        }


    }


}
