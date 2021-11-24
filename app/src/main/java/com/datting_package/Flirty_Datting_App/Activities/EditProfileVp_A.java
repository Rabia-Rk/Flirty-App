package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.datting_package.Flirty_Datting_App.Adapters.SegmentAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.Describe_yourself_F;
import com.datting_package.Flirty_Datting_App.Fragments.Drink_F;
import com.datting_package.Flirty_Datting_App.Fragments.Gender_F;
import com.datting_package.Flirty_Datting_App.Fragments.Kids_F;
import com.datting_package.Flirty_Datting_App.Fragments.Living_F;
import com.datting_package.Flirty_Datting_App.Fragments.Relationship_F;
import com.datting_package.Flirty_Datting_App.Fragments.Smoke_F;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.updateProfile;
import static com.datting_package.Flirty_Datting_App.CodeClasses.Variables.userGender;

public class EditProfileVp_A extends AppCompatActivity implements View.OnClickListener {

    public static ViewPager vp;
    public static SegmentAdapter segmentAdapter;

    public static RelativeLayout colorRl, vpRl;

    public static LinearLayout backLl, descLl, happyLl, genderLl, relationLl, bodytypeLl, livingLl, kidsLl, smokeLl, drinkLl, profqsLl, missingLl, updateProfileLL;

    public static ImageView next, previous;
    public static TextView fragCounter;


    Context context;
    List<String> listUserImgFromApi = new ArrayList<>();

    public static void methodHidelinearlayout(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4,
                                              LinearLayout ll5, LinearLayout ll6, LinearLayout ll7, LinearLayout ll8,
                                              LinearLayout ll9, LinearLayout ll10, LinearLayout ll11) {

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

    public static String getFragmentName(int position) {
        int totalPages = segmentAdapter.getCount();

        String fullFragmentName = segmentAdapter.getItem(position).toString();
        int index = fullFragmentName.indexOf("{");

        String fragmentName = fullFragmentName.substring(0, index);
        position = position + 1;

        if (fragmentName.equals("" + Variables.FRAG_ABOUT)) {
            methodHidelinearlayout(descLl, happyLl, relationLl, kidsLl, livingLl, drinkLl, smokeLl, profqsLl, genderLl, bodytypeLl, missingLl);
        } else if (fragmentName.equals("" + Variables.FRAG_DRINK)) {
            methodHidelinearlayout(drinkLl, descLl, relationLl, kidsLl, livingLl, happyLl, smokeLl, profqsLl, genderLl, bodytypeLl, missingLl);

        } else if (fragmentName.equals("" + Variables.FRAG_KIDS)) {
            methodHidelinearlayout(kidsLl, descLl, relationLl, drinkLl, livingLl, happyLl, smokeLl, profqsLl, genderLl, bodytypeLl, missingLl);

        } else if (fragmentName.equals("" + Variables.FRAG_LIVING)) {
            methodHidelinearlayout(livingLl, descLl, relationLl, drinkLl, kidsLl, happyLl, smokeLl, profqsLl, genderLl, bodytypeLl, missingLl);

        } else if (fragmentName.equals("" + Variables.FRAG_RELATION)) {

            methodHidelinearlayout(relationLl, descLl, livingLl, drinkLl, kidsLl, happyLl, smokeLl, profqsLl, genderLl, bodytypeLl, missingLl);

        } else if (fragmentName.equals("" + Variables.FRAG_SMOKE)) {

            methodHidelinearlayout(smokeLl, descLl, livingLl, drinkLl, kidsLl, happyLl, relationLl, profqsLl, genderLl, bodytypeLl, missingLl);

        } else if (fragmentName.equals("" + Variables.FRAG_GENDER)) {
            methodHidelinearlayout(genderLl, descLl, livingLl, drinkLl, kidsLl, happyLl, relationLl, profqsLl, smokeLl, bodytypeLl, missingLl);

        }

        if (totalPages == position) {

            next.setVisibility(View.GONE);

        }


        return fragmentName;
    }

    public static void createJsonForAPI(Context context) {
        final String user_id = Functions.getInfo(context, "fb_id");

        Variables.varSexuality = Functions.getInfo(context, "sexuality");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", "" + user_id);
            parameters.put("about_me", "" + Variables.varAboutMe.replaceAll("[-+.^:,']", ""));
            parameters.put("job_title", "job_title");
            parameters.put("company", "company");
            parameters.put("school", "school");
            parameters.put("living", "" + Variables.varLiving.replaceAll("[-+.^:,']", ""));
            parameters.put("children", "" + Variables.varChildren.replaceAll("[-+.^:,']", ""));
            parameters.put("smoking", "" + Variables.varSmoking.replaceAll("[-+.^:,']", ""));
            parameters.put("drinking", "" + Variables.varDrinking.replaceAll("[-+.^:,']", ""));
            parameters.put("relationship", "" + Variables.varRelationship.replaceAll("[-+.^:,']", ""));
            parameters.put("sexuality", "" + Variables.varSexuality.replaceAll("[-+.^:,']", ""));

            parameters.put("image1", "" + Functions.getInfo(context, "image1"));
            parameters.put("image2", "" + Functions.getInfo(context, "image2"));
            parameters.put("image3", "" + Functions.getInfo(context, "image3"));
            parameters.put("image4", "" + Functions.getInfo(context, "image4"));
            parameters.put("image5", "" + Functions.getInfo(context, "image5"));
            parameters.put("image6", "" + Functions.getInfo(context, "image6"));

            parameters.put("gender", "" + userGender.replaceAll("[-+.^:,']", ""));
            parameters.put("birthday", "" + Functions.getInfo(context, "birthday"));


            updateProfile(context, parameters);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        setContentView(R.layout.activity_edit_profile_vp);

        context = EditProfileVp_A.this;


        vp = (ViewPager) findViewById(R.id.vp_id);

        colorRl = (RelativeLayout) findViewById(R.id.color_rl_id);
        vpRl = (RelativeLayout) findViewById(R.id.vp_rl_id);

        backLl = (LinearLayout) findViewById(R.id.back_ll_id);
        descLl = (LinearLayout) findViewById(R.id.desc_ll_id);
        happyLl = (LinearLayout) findViewById(R.id.happy_ll_id);
        genderLl = (LinearLayout) findViewById(R.id.gender_ll_id);
        relationLl = (LinearLayout) findViewById(R.id.relationship_ll_id);
        bodytypeLl = (LinearLayout) findViewById(R.id.bodytype_ll_id);
        livingLl = (LinearLayout) findViewById(R.id.house_ll_id);
        kidsLl = (LinearLayout) findViewById(R.id.kids_ll_id);
        smokeLl = (LinearLayout) findViewById(R.id.smoking_ll_id);
        drinkLl = (LinearLayout) findViewById(R.id.drink_ll_id);
        profqsLl = (LinearLayout) findViewById(R.id.profqs_ll_id);
        missingLl = (LinearLayout) findViewById(R.id.missing_ll_id);

        updateProfileLL = (LinearLayout) findViewById(R.id.update_profile_LL);

        next = (ImageView) findViewById(R.id.next_id);
        previous = (ImageView) findViewById(R.id.previous_id);

        fragCounter = (TextView) findViewById(R.id.fragment_counter_id);


        segmentAdapter = new SegmentAdapter(getSupportFragmentManager());

        if (!checkValues("about_me")) {
            segmentAdapter.addFragment(new Describe_yourself_F(), "");
        }

        if (!checkValues("gender")) {
            segmentAdapter.addFragment(new Gender_F(), "");
        }


        if (!checkValues("relationship")) {
            segmentAdapter.addFragment(new Relationship_F(), "");

        }

        if (!checkValues("living")) {
            segmentAdapter.addFragment(new Living_F(), "");

        }

        if (!checkValues("children")) {
            segmentAdapter.addFragment(new Kids_F(), "");

        }

        if (!checkValues("smoking")) {
            segmentAdapter.addFragment(new Smoke_F(), "");

        }


        if (!checkValues("drinking")) {
            segmentAdapter.addFragment(new Drink_F(), "");

        }


        if (checkValues("drinking") && checkValues("smoking") && checkValues("children") && checkValues("living")
                && checkValues("relationship") && checkValues("gender") && checkValues("about_me")
        ) {

            imagesCheck();
        }


        String userId = Functions.getInfo(context, "fb_id");
        getuserInfo(userId);


        vp.setAdapter(segmentAdapter);
        fragCounter.setText((vp.getCurrentItem() + 1) + "/" + segmentAdapter.getCount());

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) colorRl.getLayoutParams();
        lp.height = (int) (Variables.height / 2);

        colorRl.setLayoutParams(lp);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) vpRl.getLayoutParams();
        lp1.height = (int) (Variables.height / 2);

        vpRl.setLayoutParams(lp1);

        vp.setCurrentItem(0);
        vp.setOnTouchListener((v, event) -> {
            vp.setCurrentItem(vp.getCurrentItem());
            return true;
        });

        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        backLl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_ll_id:
                finish();
                break;


            case R.id.next_id:

                int position = vp.getCurrentItem() + 1;

                vp.setCurrentItem(vp.getCurrentItem() + 1);

                fragCounter.setText((vp.getCurrentItem() + 1) + "/" + segmentAdapter.getCount());

                if (position == segmentAdapter.getCount()) {
                    next.setVisibility(View.GONE);
                }

                getFragmentName(vp.getCurrentItem());
                switch (vp.getCurrentItem()) {
                    case 0:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.coloraccent));
                        break;

                    case 1:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.red));

                        break;

                    case 2:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));

                        break;

                    case 3:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));

                        break;

                    case 4:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));

                        break;

                    case 5:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.green));

                        break;

                    case 6:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.zink));

                        break;

                    case 7:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.light_purple));

                        break;

                    case 8:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.coloraccent));

                        break;

                    case 9:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.zink2));
                        break;

                    case 10:
                        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) vpRl.getLayoutParams();
                        lp1.height = (int) (Variables.height / 1.1);

                        vpRl.setLayoutParams(lp1);

                        methodHidelinearlayout(missingLl, drinkLl, smokeLl, kidsLl, livingLl, bodytypeLl, relationLl, genderLl, happyLl, descLl, profqsLl);
                        break;

                    default:
                        break;
                }

                if (vp.getCurrentItem() != 10)
                    getFragmentName(vp.getCurrentItem());

                next.setVisibility(View.VISIBLE);
                break;

            case R.id.previous_id:
                vp.setCurrentItem(vp.getCurrentItem() - 1);

                switch (vp.getCurrentItem()) {
                    case 0:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.coloraccent));
                        break;

                    case 1:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                        break;

                    case 2:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                        break;

                    case 3:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));

                        break;

                    case 4:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));

                        break;

                    case 5:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                        break;

                    case 6:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.zink));

                        break;

                    case 7:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.light_purple));

                        break;

                    case 8:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.coloraccent));

                        break;

                    case 9:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.zink2));

                        break;

                    case 10:
                        colorRl.setBackgroundColor(ContextCompat.getColor(context, R.color.coloraccent));
                        break;

                    default:
                        break;
                }

                getFragmentName(vp.getCurrentItem());
                next.setVisibility(View.VISIBLE);

                fragCounter.setText((vp.getCurrentItem() + 1) + "/" + segmentAdapter.getCount());
                break;

            default:
                break;
        }
    }

    // Method if value present or not
    public boolean checkValues(String valWant) {

        boolean isPresent;
        String children = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                "" + valWant
        );
        if (children.equals("") || children.equals("0")) {

            isPresent = false;
        } else {

            isPresent = true;
        }


        return isPresent;
    }

    public void imagesCheck() {


        if (checkValues("living")) {
            startActivity(new Intent(context, EditProfile_A.class));
            finish();

        }


    }

    public void getuserInfo(final String userId) {

        Functions.showLoader(context, false, false);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiRequest.callApi(
                context,
                ApiLinks.getUserInfo,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String resp) {
                        try {

                            Functions.cancelLoader();


                            JSONObject response = new JSONObject(resp);


                            if (response.getString("code").equals("200")) {


                                JSONArray msgObj = response.getJSONArray("msg");
                                JSONObject userInfoObj = msgObj.getJSONObject(0);

                                SharedPrefrence.saveString(context, "" + userInfoObj.toString(),
                                        "" + SharedPrefrence.uLoginDetail);


                                Variables.varSexuality = userInfoObj.getString("about_me");
                                Variables.varLiving = userInfoObj.getString("living");
                                Variables.varChildren = userInfoObj.getString("children");
                                Variables.varSmoking = userInfoObj.getString("smoking");
                                Variables.varDrinking = userInfoObj.getString("drinking");
                                Variables.varRelationship = userInfoObj.getString("relationship");
                                Variables.varSexuality = userInfoObj.getString("sexuality");
                                Variables.varAboutMe = userInfoObj.getString("about_me");
                                userGender = userInfoObj.getString("gender");

                                /* Get User Images */


                                if (!userInfoObj.getString("image1").equals("") && !userInfoObj.getString("image1").equals("0")) {

                                    listUserImgFromApi.add(userInfoObj.getString("image1"));
                                }


                                if (!userInfoObj.getString("image2").equals("") && !userInfoObj.getString("image2").equals("0")) {
                                    listUserImgFromApi.add(userInfoObj.getString("image2"));

                                }

                                if (!userInfoObj.getString("image3").equals("") && !userInfoObj.getString("image3").equals("0")) {
                                    listUserImgFromApi.add(userInfoObj.getString("image3"));

                                }

                                if (!userInfoObj.getString("image4").equals("") && !userInfoObj.getString("image4").equals("0")) {
                                    listUserImgFromApi.add(userInfoObj.getString("image4"));
                                }

                                if (!userInfoObj.getString("image5").equals("") && !userInfoObj.getString("image5").equals("0")) {
                                    listUserImgFromApi.add(userInfoObj.getString("image5"));

                                }

                                if (!userInfoObj.getString("image6").equals("") && !userInfoObj.getString("image6").equals("0")) {
                                    listUserImgFromApi.add(userInfoObj.getString("image6"));

                                }


                            }

                        } catch (Exception b) {
                            Functions.cancelLoader();
                            Functions.toastMsg(context, "" + b.toString());
                        }

                    }
                }
        );

    }

}
