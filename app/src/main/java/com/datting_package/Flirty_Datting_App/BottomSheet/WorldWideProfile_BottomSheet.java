package com.datting_package.Flirty_Datting_App.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nex3z.flowlayout.FlowLayout;
import com.datting_package.Flirty_Datting_App.Activities.Chat_A;
import com.datting_package.Flirty_Datting_App.Activities.ShareProfile_A;
import com.datting_package.Flirty_Datting_App.Adapters.ImagesAdp;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.World_wide_native_ads_F;
import com.datting_package.Flirty_Datting_App.Models.ImagesModel;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;
import java.util.List;

import static com.datting_package.Flirty_Datting_App.Fragments.World_wide_native_ads_F.nearbyList;
import static com.datting_package.Flirty_Datting_App.Fragments.World_wide_native_ads_F.recyclerView;

public class WorldWideProfile_BottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;

    TextView distance, aboutMeTvId, simpleAbout;

    List<ImagesModel> listProfileImg;
    ArrayList<String> aboutMe = new ArrayList<>();
    String userId;
    RecyclerView rvImagesList;
    ImagesAdp adpImg;
    Context context;
    ImageView like;
    ImageView moveLeft, moveRight;
    SimpleDraweeView iv;
    FlowLayout item;
    BottomSheetBehavior behavior;
    NearbyUsersModel getNearby;
    FrameLayout bottomSheet;
    String swipePosString;
    int swipePosition;
    String viewType;
    ArrayList<Integer> aboutMePics = new ArrayList<>();
    TextView userName;
    String isBlocked = "0";

    public WorldWideProfile_BottomSheet() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_view_profile_ww, container, false);
        context = getContext();

        userId = getArguments().getString("user_id");
        swipePosString = getArguments().getString("current_position");
        viewType = getArguments().getString("view_type");


        swipePosition = World_wide_native_ads_F.swipePosition;


        userName = view.findViewById(R.id.user_name);


        distance = view.findViewById(R.id.distance);
        aboutMeTvId = view.findViewById(R.id.about_me_tv_id);
        simpleAbout = view.findViewById(R.id.simple_about);

        like = (ImageView) view.findViewById(R.id.right_overlay);
        listProfileImg = new ArrayList<>();
        rvImagesList = view.findViewById(R.id.RV_images_list);
        iv = view.findViewById(R.id.profile_iv_id);


        moveLeft = view.findViewById(R.id.move_left);
        moveRight = view.findViewById(R.id.move_right);

        getNearby = (NearbyUsersModel) getArguments().getSerializable("user_near_by");


        view.findViewById(R.id.chat_btn).setOnClickListener(this);
        view.findViewById(R.id.fl_id).setOnClickListener(this::onClick);
        view.findViewById(R.id.report_user).setOnClickListener(this::onClick);
        view.findViewById(R.id.share_profile).setOnClickListener(this::onClick);


        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.nestedscrollview_id);

        if (scroller != null) {

            scroller.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                if (scrollY > oldScrollY) {
                    Log.i("", "Scroll DOWN");
                    userName.setVisibility(View.GONE);


                }
                if (scrollY < oldScrollY) {
                    Log.i("", "Scroll UP");
                }


                if (scrollY == 0) {
                    userName.setVisibility(View.VISIBLE);
                    Log.i("", "TOP SCROLL");
                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                    Log.i("", "BOTTOM SCROLL");
                }
            });
        }


        fillData(getNearby);


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);

                dialog.getWindow()
                        .findViewById(R.id.design_bottom_sheet)
                        .setBackgroundResource(android.R.color.transparent);

                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_DRAGGING) {
                            recyclerView.smoothScrollToPosition(swipePosition);
                            item.removeAllViews();
                            dismiss();
                        }

                        if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            dismiss();
                            recyclerView.smoothScrollToPosition(swipePosition);
                            item.removeAllViews();
                        }

                        if (i == BottomSheetBehavior.STATE_HIDDEN) {
                            recyclerView.smoothScrollToPosition(swipePosition);
                            item.removeAllViews();
                            dismiss();
                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });

                like.setOnClickListener(v -> {
                    item.removeAllViews();
                    try {
                        nearbyList.remove(swipePosition);
                        World_wide_native_ads_F.adapter.notifyItemRemoved(swipePosition);
                        World_wide_native_ads_F.adapter.notifyItemRangeChanged(swipePosition, nearbyList.size());
                        World_wide_native_ads_F.adapter.notifyDataSetChanged();

                    } catch (Exception b) {
                        Functions.toastMsg(context, "Removed Error " + b.toString());
                    }


                    int listSize = nearbyList.size() - 1;
                    if (swipePosition == listSize) {
                        swipePosition = 0;
                    }
                    swipePosition = swipePosition + 1;
                    if (nearbyList.get(swipePosition) instanceof NearbyUsersModel) {

                        try {
                            getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);
                        } catch (Exception b) {
                            b.printStackTrace();
                        }

                    } else {
                        swipePosition = swipePosition + 1;

                        getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);
                    }

                    World_wide_native_ads_F.updatedataOnrightSwipe(getContext(), getNearby);

                    fillData(getNearby);

                });


                moveRight.setOnClickListener(v -> {

                    item.removeAllViews();
                    int listSize = nearbyList.size() - 1;
                    if (swipePosition == listSize) {
                        swipePosition = 0;
                    }

                    swipePosition = swipePosition + 1;


                    if (nearbyList.get(swipePosition) instanceof NearbyUsersModel) {

                        try {
                            getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);
                        } catch (Exception b) {
                            b.printStackTrace();
                        }

                    } else {
                        swipePosition = swipePosition + 2;

                        try {
                            getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);
                        } catch (Exception b) {
                            b.printStackTrace();
                        }
                    }


                    fillData(getNearby);


                });


                moveLeft.setOnClickListener(v -> {

                    item.removeAllViews();

                    int listSize = nearbyList.size() - 1;


                    if (swipePosition == 0) {
                        swipePosition = listSize;
                    } else {
                        swipePosition = swipePosition - 1;
                    }

                    if (nearbyList.get(swipePosition) instanceof NearbyUsersModel) {

                        try {
                            getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);
                        } catch (Exception b) {
                            b.printStackTrace();
                        }

                    } else {
                        if (swipePosition == 0) {
                            swipePosition = listSize;
                        } else {
                            swipePosition = swipePosition - 1;
                        }

                        getNearby = (NearbyUsersModel) nearbyList.get(swipePosition);

                    }

                    fillData(getNearby);

                });

            }
        });


        return view;
    }


    public void fillData(NearbyUsersModel getNearby) {

        aboutMeTvId.setText("" + getNearby.getAbout_me());
        distance.setText("" + getNearby.getDistance());
        userName.setText("" + getNearby.getFirst_name() + ", " + getNearby.getBirthday() + " \n " + getNearby.getDistance());

        distance.setText("" + getNearby.getDistance());
        if (getNearby.getAbout_me().equals("")) {
            simpleAbout.setVisibility(View.GONE);
            aboutMeTvId.setVisibility(View.GONE);
        } else {
            simpleAbout.setVisibility(View.VISIBLE);
            aboutMeTvId.setVisibility(View.VISIBLE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutMeTvId.setText(Html.fromHtml(getNearby.getAbout_me(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            aboutMeTvId.setText(Html.fromHtml(getNearby.getAbout_me()));
        }


        if (!getNearby.getChildren().equals("")) {
            aboutMe.add(getNearby.getChildren());
            aboutMePics.add(R.drawable.ic_childrens);

        }

        if (!getNearby.getLiving().equals("")) {
            aboutMe.add(getNearby.getLiving());
            aboutMePics.add(R.drawable.ic_living);
        }

        if (!getNearby.getRelationship().equals("")) {
            aboutMe.add(getNearby.getRelationship());
            aboutMePics.add(R.drawable.ic_relationship);

        }

        if (!getNearby.getSchool().equals("")) {
            aboutMe.add(getNearby.getSchool());
            aboutMePics.add(R.drawable.ic_living);

        }

        if (!getNearby.getChildren().equals("")) {
            aboutMe.add(getNearby.getChildren());
            aboutMePics.add(R.drawable.ic_childrens);
        }

        if (!getNearby.getSexuality().equals("")) {
            aboutMe.add(getNearby.getSexuality());
            aboutMePics.add(R.drawable.ic_sexuality);
        }

        if (!getNearby.getSmoking().equals("")) {
            aboutMe.add(getNearby.getSmoking());
            aboutMePics.add(R.drawable.ic_smoking);

        }


        listProfileImg = new ArrayList<>();

        if (!getNearby.getImage2().equals("") && !getNearby.getImage2().equals("0")) {
            ImagesModel add2 = new ImagesModel("" + getNearby.getImage2());
            listProfileImg.add(add2);

        }

        if (!getNearby.getImage3().equals("") && !getNearby.getImage3().equals("0")) {
            ImagesModel add3 = new ImagesModel("" + getNearby.getImage3());
            listProfileImg.add(add3);
        }

        if (!getNearby.getImage4().equals("") && !getNearby.getImage4().equals("0")) {
            ImagesModel add4 = new ImagesModel("" + getNearby.getImage4());
            listProfileImg.add(add4);
        }

        if (!getNearby.getImage5().equals("") && !getNearby.getImage5().equals("0")) {
            ImagesModel add5 = new ImagesModel("" + getNearby.getImage5());
            listProfileImg.add(add5);
        }

        if (!getNearby.getImage6().equals("") && !getNearby.getImage6().equals("0")) {
            ImagesModel add6 = new ImagesModel("" + getNearby.getImage6());
            listProfileImg.add(add6);
        }


        adpImg = new ImagesAdp(context, listProfileImg, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        rvImagesList.setLayoutManager(layoutManager);


        rvImagesList.setAdapter(adpImg);

        try {
            Uri uri = Uri.parse(getNearby.getImage1());
            iv.setImageURI(uri);  // Attachment

        } catch (Exception v9) {
            v9.printStackTrace();
        }
        inflateLayout();

    }

    // End Method to inflate Layout
    public void inflateLayout() {

        item = view.findViewById(R.id.inflate_layout);
        for (int i = 0; i < aboutMe.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml

            TextView intrestName = child.findViewById(R.id.intrest_name);
            ImageView image = child.findViewById(R.id.ic_image);
            LinearLayout mainLinearLayout = child.findViewById(R.id.main_linear_layout);

            if (!aboutMe.get(i).equals("0") && !aboutMe.get(i).equals(" ")) {
                intrestName.setText("" + aboutMe.get(i));
                image.setImageResource(aboutMePics.get(i));
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(Variables.marginLeft, Variables.marginTop, Variables.marginRight, Variables.marginBottom);
                mainLinearLayout.setLayoutParams(buttonLayoutParams);
                item.addView(child);
            }

        }


        aboutMe.clear();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.chat_btn:
                Intent myIntent = new Intent(context, Chat_A.class);
                myIntent.putExtra("receiver_id", getNearby.getFb_id());
                myIntent.putExtra("receiver_name", getNearby.getFirst_name() + " " + getNearby.getLast_name());
                myIntent.putExtra("receiver_pic", getNearby.getImage1());
                myIntent.putExtra("is_block", "0");
                myIntent.putExtra("match_api_run", "0");
                context.startActivity(myIntent);
                break;

            case R.id.fl_id:
                try {
                    recyclerView.smoothScrollToPosition(swipePosition);
                } catch (Exception b) {
                    b.printStackTrace();

                }
                dismiss();
                break;


            case R.id.report_user:
                showPopOptions();
                break;


            case R.id.share_profile:
                String userImage = getNearby.getImage1();

                String userName = getNearby.getFirst_name()
                        + " " + getNearby.getLast_name();

                Intent intent = new Intent(getActivity(), ShareProfile_A.class);
                intent.putExtra("name", userName);
                intent.putExtra("image", userImage);

                startActivity(intent);
                break;

            default:
                break;
        }
    }


    public void showPopOptions() {
        Functions.getUserIsBlockOrNot(getNearby.getFb_id(), (requestType, response) -> isBlocked = response);

        final CharSequence[] options;

        if (isBlocked.equals("0")) {
            options = new CharSequence[]{getString(R.string.block), getString(R.string.report), getString(R.string.cancel)};
        } else {
            options = new CharSequence[]{getString(R.string.unblock), getString(R.string.report), getString(R.string.cancel)};
        }

        Functions.showOptions(context, options, (requestType, response) -> {
            if (response.equals(getString(R.string.report))) {
                Functions.reportUser(context, "" + getNearby.getFb_id());
            } else if (response.equals(getString(R.string.block))) {
                Functions.blockUser(context, getNearby.getFb_id(), getNearby.getFirst_name(), getNearby.getImage1());
            } else if (response.equals(getString(R.string.unblock))) {
                Functions.unblockUser(context, getNearby.getFb_id());
            }
        });

    }

}
