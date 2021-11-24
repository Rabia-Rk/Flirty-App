package com.datting_package.Flirty_Datting_App.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.Main_F;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    public static String userId;
    public static String token;
    public static boolean purductPurchase = false;
    Main_F fragment;
    long mBackPressed;
    Context context;
    BillingProcessor billingProcessor;
    DatabaseReference rootref;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        SharedPrefrence.initShare(this);

        MobileAds.initialize(this);

        Functions.displayFbAd(context);

        rootref = FirebaseDatabase.getInstance().getReference();

        userId = SharedPrefrence.getString(context, SharedPrefrence.uId);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    token = task.getResult();
                });

        if (token == null || (token.equals("") || token.equals("null")))
            token = SharedPrefrence.getString(this, SharedPrefrence.uDeviceToken);

        billingProcessor = new BillingProcessor(this, Variables.LICENCEKEY, this);
        billingProcessor.initialize();


        fragment = new Main_F();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.RL_id, fragment).commit();


        Functions.registerConnectivity(this, (requestType, response) -> {
            if (response.equalsIgnoreCase("disconnected")) {
                snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet. Connect to wifi or cellular network.", Snackbar.LENGTH_INDEFINITE);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                snackbar.show();
            } else {

                if (snackbar != null)
                    snackbar.dismiss();
            }
        });


        setLanguageLocal();


    }

    public void setLanguageLocal() {

        String language = SharedPrefrence.getString(this, SharedPrefrence.selectedLanguage);
        Locale myLocale = new Locale(Locale.getDefault().getLanguage());
        if (language != null && language.equalsIgnoreCase(getString(R.string.english))) {
            myLocale = new Locale("en");

        } else if (language != null && language.equalsIgnoreCase(getString(R.string.arabic))) {
            myLocale = new Locale("ar");
        }

        if (myLocale.getLanguage().equalsIgnoreCase("en") || myLocale.getLanguage().equalsIgnoreCase("ar")) {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = new Configuration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            onConfigurationChanged(conf);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Functions.unRegisterConnectivity(this);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (!fragment.onBackPressed()) {
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Functions.toastMsg(this, "Tap Again To Exit");
                    mBackPressed = System.currentTimeMillis();

                }
            } else {
                super.onBackPressed();

            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            rootref.child("Users").child(userId).child("token").setValue(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.isPuchase, true).commit();
        purductPurchase = true;
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

        if (billingProcessor.loadOwnedPurchasesFromGoogle()) {
            if ((billingProcessor.isSubscribed(Variables.PRODUCT_ID) ||
                    billingProcessor.isSubscribed(Variables.PRODUCT_ID_2)) || billingProcessor.isSubscribed(Variables.PRODUCT_ID_3)) {

                SharedPrefrence.saveBoolean(this, true, SharedPrefrence.isPuchase);
                purductPurchase = true;
                billingProcessor.release();
                callApiForUpdatePurchase("1");
            } else {
                SharedPrefrence.saveBoolean(this, false, SharedPrefrence.isPuchase);
                purductPurchase = false;
                callApiForUpdatePurchase("0");
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            for (Fragment fragment1 : fragment.getChildFragmentManager().getFragments()) {
                fragment1.onActivityResult(requestCode, resultCode, data);
            }
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void callApiForUpdatePurchase(String purchaseValue) {
        String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("fb_id", userId);
            parameters.put("purchased", purchaseValue);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(this, ApiLinks.update_purchase_Status, parameters, null);


    }


}
