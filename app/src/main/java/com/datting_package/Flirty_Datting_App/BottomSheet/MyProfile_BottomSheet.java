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
import com.datting_package.Flirty_Datting_App.Activities.EditProfile_A;
import com.datting_package.Flirty_Datting_App.Activities.ShareProfile_A;
import com.datting_package.Flirty_Datting_App.Adapters.ImagesAdp;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.ImagesModel;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;
import java.util.List;

public class MyProfile_BottomSheet extends BottomSheetDialogFragment {
    View view;
    TextView distance, aboutMeTvId, simpleAbout;
    ArrayList<Integer> aboutMePics = new ArrayList<>();
    List<ImagesModel> listProfileImg;
    ArrayList<String> aboutMe = new ArrayList<>();

    String userId;
    RecyclerView rvImagesList;
    ImagesAdp adpImg;
    Context context;
    ImageView editProfile;
    ImageView dialogueCancel, moveLeft, moveRight;
    SimpleDraweeView iv;
    FlowLayout item;
    FrameLayout fl;
    BottomSheetBehavior behavior;
    NearbyUsersModel getNearby;
    TextView reportUser;
    FrameLayout bottomSheet;
    String swipePosString;
    String viewType;
    TextView shareProfile, userName;
    String isBlocked = "0";

    public MyProfile_BottomSheet() {
        //required empty constructor
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_view_my_profile, container, false);
        userId = getArguments().getString("user_id");
        swipePosString = getArguments().getString("current_position");
        viewType = getArguments().getString("view_type");
        context = getContext();
        distance = view.findViewById(R.id.distance);
        aboutMeTvId = view.findViewById(R.id.about_me_tv_id);
        simpleAbout = view.findViewById(R.id.simple_about);
        dialogueCancel = (ImageView) view.findViewById(R.id.left_overlay);
        editProfile = (ImageView) view.findViewById(R.id.right_overlay);
        listProfileImg = new ArrayList<>();
        rvImagesList = view.findViewById(R.id.RV_images_list);
        iv = view.findViewById(R.id.profile_iv_id);
        shareProfile = view.findViewById(R.id.share_profile);
        userName = view.findViewById(R.id.user_name);


        fl = (FrameLayout) view.findViewById(R.id.fl_id);

        editProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfile_A.class)));


        dialogueCancel.setOnClickListener(v -> dismiss());


        fl.setOnClickListener(v -> dismiss());

        reportUser = view.findViewById(R.id.report_user);

        moveLeft = view.findViewById(R.id.move_left);
        moveRight = view.findViewById(R.id.move_right);

        getNearby = (NearbyUsersModel) getArguments().getSerializable("user_near_by");

        reportUser.setOnClickListener(v -> showPopOptions());


        shareProfile.setOnClickListener(v -> {
            String userImage = Functions.getInfo(context, "image1");

            String userName = Functions.getInfo(context, "first_name")
                    + " " + Functions.getInfo(context, "last_name");

            Intent intent = new Intent(getActivity(), ShareProfile_A.class);
            intent.putExtra("name", userName);
            intent.putExtra("image", userImage);

            startActivity(intent);


        });

        userName.setText("" + getNearby.getFirst_name() + ", " + getNearby.getBirthday());

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
        getNearby.getAbout_me();
        aboutMeTvId.setText("" + getNearby.getAbout_me());
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

        rvImagesList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvImagesList.setHasFixedSize(false);
        rvImagesList.setAdapter(adpImg);


        if (getNearby.getImage2().equals("") && getNearby.getImage3().equals("") &&
                getNearby.getImage4().equals("") &&
                getNearby.getImage5().equals("") &&
                getNearby.getImage6().equals("")) {
            // If all images empty
            rvImagesList.setVisibility(View.GONE);
        } else {
            rvImagesList.setVisibility(View.VISIBLE);
        }


        try {
            Uri uri = Uri.parse(getNearby.getImage1());
            iv.setImageURI(uri);  // Attachment
        } catch (Exception v) {
            v.printStackTrace();
        }

        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "living"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_living);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "living"
            ));
        }

        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "children"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_childrens);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "children"
            ));


        }


        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "smoking"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_smoking);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "smoking"
            ));

        }


        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "drinking"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_drinking);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "drinking"
            ));


        }

        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "relationship"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_relationship);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "relationship"
            ));

        }


        if (!SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "sexuality"
        ).equals("")) {
            aboutMePics.add(R.drawable.ic_sexuality);
            aboutMe.add("" + SharedPrefrence.getSocialInfo(context,
                    "" + SharedPrefrence.uLoginDetail,
                    "sexuality"
            ));

        }

        fl = (FrameLayout) view.findViewById(R.id.fl_id);


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


                final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
                rvImagesList.setLayoutManager(layoutManager);


                rvImagesList.setAdapter(adpImg);
                inflateLayout();

                try {
                    Uri uri = Uri.parse(getNearby.getImage1());
                    iv.setImageURI(uri);

                } catch (Exception v9) {
                    v9.printStackTrace();
                }

            }
        });


        return view;
    }


    public void inflateLayout() {

        item = view.findViewById(R.id.inflate_layout);

        for (int i = 0; i < aboutMe.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml
            ImageView image = child.findViewById(R.id.ic_image);
            TextView intrestName = child.findViewById(R.id.intrest_name);
            LinearLayout mainLinearLayout = child.findViewById(R.id.main_linear_layout);

            if (!aboutMe.get(i).equals("0") || !aboutMe.get(i).equals(" ")) {
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

}
