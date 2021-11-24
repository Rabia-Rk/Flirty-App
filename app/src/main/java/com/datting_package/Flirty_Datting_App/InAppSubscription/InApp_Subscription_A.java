package com.datting_package.Flirty_Datting_App.InAppSubscription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.tabs.TabLayout;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.datting_package.Flirty_Datting_App.Activities.MainActivity;
import com.datting_package.Flirty_Datting_App.Activities.Splashscreen_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InApp_Subscription_A extends RootFragment implements BillingProcessor.IBillingHandler, View.OnClickListener {

    BillingProcessor bp;
    View view;
    Context context;
    Button purchaseBtn;

    TextView goback;

    LinearLayout subLayout1, subLayout2, subLayout3;
    int subscriptionPosition;
    private ViewPager mPager;
    private ArrayList<Integer> imagesArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate
        // the layout for this fragment
        view = inflater.inflate(R.layout.activity_in_app_subscription, container, false);
        context = getContext();


        purchaseBtn = view.findViewById(R.id.purchase_btn);
        purchaseBtn.setOnClickListener(this);


        goback = view.findViewById(R.id.Goback);
        goback.setOnClickListener(this);

        setSlider();


        subLayout1 = view.findViewById(R.id.sub_layout1);
        subLayout2 = view.findViewById(R.id.sub_layout2);
        subLayout3 = view.findViewById(R.id.sub_layout3);


        selectOne(2);

        subLayout1.setOnClickListener(this);
        subLayout2.setOnClickListener(this);
        subLayout3.setOnClickListener(this);

        return view;
    }

    public void selectOne(int position) {
        subscriptionPosition = position;

        subLayout1.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_gray_border));
        subLayout2.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_gray_border));
        subLayout3.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_gray_border));

        if (position == 1) {
            subLayout1.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_blue_border_radius15));

        } else if (position == 2) {
            subLayout2.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_blue_border_radius15));

        } else if (position == 3) {
            subLayout3.setBackground(ContextCompat.getDrawable(context,R.drawable.d_round_blue_border_radius15));

        }


    }

    public void initlizeBilling() {


        Functions.showLoader(context, false, false);

        bp = new BillingProcessor(context, Variables.LICENCEKEY, this);
        bp.initialize();


    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

        // if the user is subcripbe successfully then we will store the data in local and also call the api
        SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.isPuchase, true).commit();
        MainActivity.purductPurchase = true;
        callApiForUpdatePurchase("1");

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        // on billing intialize we will get the data from google
        Functions.cancelLoader();

        if (bp.loadOwnedPurchasesFromGoogle()) {
            // check user is already subscribe or not
            if ((bp.isSubscribed(Variables.PRODUCT_ID) || bp.isSubscribed(Variables.PRODUCT_ID_2))
                    || bp.isSubscribed(Variables.PRODUCT_ID_3)) {
                // if already subscribe then we will change the static variable and goback
                MainActivity.purductPurchase = true;
                callApiForUpdatePurchase("1");
            }

        }
    }

    // when we click the continue btn this method will call
    public void puchaseItem() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
        if (isAvailable) {

            if (subscriptionPosition == 1) {
                bp.subscribe(getActivity(), Variables.PRODUCT_ID);
            } else if (subscriptionPosition == 2) {
                bp.subscribe(getActivity(), Variables.PRODUCT_ID_2);
            } else if (subscriptionPosition == 3) {
                bp.subscribe(getActivity(), Variables.PRODUCT_ID_3);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            Animation anim = MoveAnimation.create(MoveAnimation.UP, enter, 300);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    initlizeBilling();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            return anim;

        } else {
            return MoveAnimation.create(MoveAnimation.DOWN, enter, 300);
        }
    }

    // on destory we will release the billing process
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    public void goback() {

        startActivity(new Intent(getActivity(), Splashscreen_A.class));
        getActivity().finish();

    }

    // when user subscribe the app then this method will call that will store the status of user
    // into the database
    private void callApiForUpdatePurchase(String purchaseValue) {

        JSONObject parameters = new JSONObject();
        try {

            String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);

            parameters.put("fb_id", userId);
            parameters.put("purchased", purchaseValue);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.showLoader(context, false, false);

        ApiRequest.callApi(context, ApiLinks.update_purchase_Status, parameters, new CallBack() {
            @Override
            public void getResponse(String requestType, String response) {
                Functions.cancelLoader();

                SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.isPuchase, true).commit();
                MainActivity.purductPurchase = true;

                goback();

            }
        });


    }

    public void setSlider() {

        imagesArray = new ArrayList<>();
        imagesArray.add(0);
        imagesArray.add(1);
        mPager = (ViewPager) view.findViewById(R.id.image_slider_pager);

        try {
            mPager.setAdapter(new SlidingImageAdapter(getContext(), imagesArray));
        } catch (NullPointerException e) {
            e.getCause();
        }

        mPager.setCurrentItem(0);

        TabLayout indicator = (TabLayout) view.findViewById(R.id.indicator);
        indicator.setupWithViewPager(mPager, true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.purchase_btn:
                puchaseItem();
                break;

            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.sub_layout1:
                selectOne(1);
                break;

            case R.id.sub_layout2:
                selectOne(2);
                break;

            case R.id.sub_layout3:
                selectOne(3);
                break;

            default:
                break;

        }
    }

}
