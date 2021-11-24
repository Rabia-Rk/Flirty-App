package com.datting_package.Flirty_Datting_App.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.datting_package.Flirty_Datting_App.BottomSheet.MyProfile_BottomSheet;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;

import org.json.JSONObject;

public class Account_A extends AppCompatActivity {

    Toolbar tb;
    ImageView iv;
    TextView deleteAccount;
    Context context;
    Button accountSignoutId;
    CheckBox accountCBId;


    TextView profileUsernameId;
    SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        deleteAccount = findViewById(R.id.delete_account);
        context = Account_A.this;

        tb = (Toolbar) findViewById(R.id.account_TB_id);
        iv = (ImageView) tb.findViewById(R.id.account_back_id);
        accountCBId = findViewById(R.id.account_CB_id);

        deleteAccount.setOnClickListener(v -> delAccount());


        profileUsernameId = findViewById(R.id.username_txt);
        draweeView = findViewById(R.id.userimage_img);


        String img = Functions.getInfo(context, "image1");
        String firstName = Functions.getInfo(context, "first_name") + " ," + Functions.getInfo(context, "age");
        profileUsernameId.setText("" + firstName);
        try {
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            draweeView.getHierarchy().setPlaceholderImage(R.drawable.ic_user_icon);
            draweeView.getHierarchy().setFailureImage(R.drawable.ic_user_icon);
            draweeView.getHierarchy().setRoundingParams(roundingParams);
            draweeView.setController(controller);
        } catch (Exception v) {
            Functions.logDMsg( "" + v.toString());
        }


        draweeView.setOnClickListener(v -> addUserRecord());


        String isHide = SharedPrefrence.getString(context, "" + SharedPrefrence.shareProfileHideOrNot);

        if (isHide != null) {

            if (isHide.equals("1")) {
                accountCBId.setChecked(true);
            } else {
                accountCBId.setChecked(false);
            }

        }


        accountCBId.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            boolean varBoolSoundOn = (isChecked) ? true : false;
            String userId = Functions.getInfo(context, "fb_id");
            if (varBoolSoundOn == true) {

                SharedPrefrence.saveString(context, "1", "" + SharedPrefrence.shareProfileHideOrNot);

                Functions.showOrHideProfile(userId, "1", context);

            } else {

                SharedPrefrence.saveString(context, "0", "" + SharedPrefrence.shareProfileHideOrNot);

                Functions.showOrHideProfile(userId, "0", context);

            }

        });

        accountSignoutId = findViewById(R.id.account_signout_id);
        accountSignoutId.setOnClickListener(v -> SharedPrefrence.logoutUser(context));

        iv.setOnClickListener(v -> finish());

    }

    public void delAccount() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(getResources().getString(R.string.alert));
        builder1.setMessage(R.string.are_you_sure_delete_account);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.yes,
                (dialog, id) -> {
                    String userId = SharedPrefrence.getString(context, SharedPrefrence.uId);
                    Functions.deleteAccount(context, "" + userId);
                });

        builder1.setNegativeButton(
                R.string.no,
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();


    } // End Del Account_A


    public void addUserRecord() {
        // Method to add Logged in Record
        String info = SharedPrefrence.getString(context, SharedPrefrence.uLoginDetail);

        try {
            JSONObject userObj = new JSONObject(info);
            NearbyUsersModel nearby = new NearbyUsersModel(
                    "" + userObj.getString("fb_id"),
                    "" + userObj.getString("first_name"),
                    "" + userObj.getString("last_name"),
                    "" + userObj.getString("age"),
                    "" + userObj.getString("about_me"),
                    "no",
                    "" + userObj.getString("image1"),
                    "like",

                    "Job",
                    "" + userObj.getString("company"),
                    "" + userObj.getString("school"),
                    "" + userObj.getString("living"),
                    "" + userObj.getString("children"),
                    "" + userObj.getString("smoking"),
                    "" + userObj.getString("drinking"),
                    "" + userObj.getString("relationship"),
                    "" + userObj.getString("sexuality"),
                    "",
                    "" + userObj.getString("image2"),
                    "" + userObj.getString("image3"),
                    "" + userObj.getString("image4"),
                    "" + userObj.getString("image5"),
                    "" + userObj.getString("image6")

            );

            Bundle bundleUserProfile = new Bundle();
            bundleUserProfile.putString("user_id", "" + userObj.getString("fb_id"));
            bundleUserProfile.putString("current_position", "");
            bundleUserProfile.putSerializable("user_near_by", nearby);

            MyProfile_BottomSheet viewProfile = new MyProfile_BottomSheet();
            viewProfile.setArguments(bundleUserProfile);

            viewProfile.show(getSupportFragmentManager(), viewProfile.getTag());


        } catch (Exception b) {
            Functions.toastMsg(context, "Err " + b.toString());
        }

    }


}
