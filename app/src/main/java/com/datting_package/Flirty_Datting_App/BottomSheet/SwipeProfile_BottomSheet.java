package com.datting_package.Flirty_Datting_App.BottomSheet;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
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
import com.datting_package.Flirty_Datting_App.Activities.ShareProfile_A;
import com.datting_package.Flirty_Datting_App.Adapters.ImagesAdp;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.Swipe_F;
import com.datting_package.Flirty_Datting_App.Models.ImagesModel;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SwipeProfile_BottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    View view;
    TextView distance, aboutMeTvId, simpleAbout;

    List<ImagesModel> listProfileImg;
    ArrayList<String> aboutMe = new ArrayList<>();

    String userId;
    RecyclerView rvImagesList;
    ImagesAdp adpImg;
    Context context;
    ImageView like;
    ImageView dislike, moveLeft, moveRight;
    SimpleDraweeView iv;
    FlowLayout item;


    NearbyUsersModel getNearby;

    TextView reportUser;

    FrameLayout bottomSheet;
    BottomSheetBehavior behavior;
    ArrayList<Integer> aboutMePics = new ArrayList<>();
    TextView userName;
    String isBlocked = "0";

    public SwipeProfile_BottomSheet() {
        //Required public constructor
    }

    @Override
    public Dialog
    onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setContentView(R.layout.bottom_view_profile);


        try {
            Field mBehaviorField = bottomSheetDialog.getClass().getDeclaredField("mBehavior");
            mBehaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) mBehaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_view_profile, container, false);
        userId = getArguments().getString("user_id");


        context = getContext();

        try {
            getNearby = (NearbyUsersModel) getArguments().getSerializable("user_near_by");
        } catch (Exception b) {
            b.printStackTrace();
        }


        userName = view.findViewById(R.id.user_name);
        distance = view.findViewById(R.id.distance);
        aboutMeTvId = view.findViewById(R.id.about_me_tv_id);
        simpleAbout = view.findViewById(R.id.simple_about);
        dislike = (ImageView) view.findViewById(R.id.left_overlay);
        like = (ImageView) view.findViewById(R.id.right_overlay);
        listProfileImg = new ArrayList<>();
        rvImagesList = view.findViewById(R.id.RV_images_list);
        iv = view.findViewById(R.id.profile_iv_id);


        reportUser = view.findViewById(R.id.report_user);

        moveLeft = view.findViewById(R.id.move_left);
        moveRight = view.findViewById(R.id.move_right);


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

                dialog
                        .getWindow()
                        .findViewById(R.id.design_bottom_sheet)
                        .setBackgroundResource(android.R.color.transparent);

                dialog.getWindow().findViewById(R.id.design_bottom_sheet).setOnClickListener(v -> dismiss());


                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {

                        if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            behavior.setHideable(true);
                            dismiss();
                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });


                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        dismiss();
                        swipeRight();
                    }
                });


                dislike.setOnClickListener(v -> {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                    dismiss();
                    swipeLeft();
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

    public void inflateLayout() {


        item = view.findViewById(R.id.inflate_layout);//where you want to add/inflate a view as a child

        for (int i = 0; i < aboutMe.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml
            TextView intrestName = child.findViewById(R.id.intrest_name);
            ImageView image = child.findViewById(R.id.ic_image);
            LinearLayout mainLinearLayout = child.findViewById(R.id.main_linear_layout);


            if (aboutMe.get(i).equals("0") || aboutMe.get(i).equals(" ")) {
                // If empty
            } else {
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

            case R.id.fl_id:
                try {
                    dismiss();
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


    public void swipeRight() {
        // Method to Swip Right like ANimation

        View target = Swipe_F.cardStackView.getTopView();
        View targetOverlay = Swipe_F.cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 20f));
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

        Swipe_F.cardStackView.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet);


        Swipe_F.updatedataOnrightSwipe(getContext(), getNearby);


        Functions.logDMsg("Image " + getNearby.getImage1());


    }


    public void swipeLeft() {

        View target = Swipe_F.cardStackView.getTopView();
        View targetOverlay = Swipe_F.cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -20f));
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

        Swipe_F.cardStackView.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet);

        Swipe_F.updatedataOnLeftSwipe(getContext(), getNearby);

    }
}
