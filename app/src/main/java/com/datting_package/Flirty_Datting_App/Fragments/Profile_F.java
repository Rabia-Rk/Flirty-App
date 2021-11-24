package com.datting_package.Flirty_Datting_App.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.Activities.EditProfileVp_A;
import com.datting_package.Flirty_Datting_App.Activities.EditProfile_A;
import com.datting_package.Flirty_Datting_App.Activities.MainActivity;
import com.datting_package.Flirty_Datting_App.Activities.Viewprofile_A;
import com.datting_package.Flirty_Datting_App.Boost.Boost_F;
import com.datting_package.Flirty_Datting_App.BottomSheet.MyProfile_BottomSheet;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.InAppSubscription.InApp_Subscription_A;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Profile_F extends Fragment implements View.OnClickListener {

    public static CircleProgressView mCircleView;
    private final int pickimagecamera = 1, pickimagegallery = 2;
    View v;
    SimpleDraweeView iv;
    ImageView iv1;
    TextView profTv;
    RelativeLayout profRl;
    CardView getMoreAten;
    RelativeLayout popularityRl, creditsActivatePopularity;
    LinearLayout creditPremiumLl;
    Context context;
    FirebaseStorage storage;
    StorageReference storageReference;
    RelativeLayout profileRLId;
    SimpleDraweeView userimageImg;
    TextView usernameTxt;
    TextView completeText, subscribeTxt;
    String userImage;
    String userName;
    String imageFilePath;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null);

        context = getContext();
        iv = v.findViewById(R.id.userimage_img);
        iv.setClipToOutline(true);
        profileRLId = v.findViewById(R.id.profile_RL_id);
        completeText = v.findViewById(R.id.complete_text);

        usernameTxt = v.findViewById(R.id.username_txt);
        Main_F.toolbar.setVisibility(View.GONE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userimageImg = v.findViewById(R.id.userimage_img);
        userimageImg.setOnClickListener(this::onClick);


        profileRLId.setOnClickListener(this::onClick);


        setUserData();

        profRl = (RelativeLayout) v.findViewById(R.id.profile_RL_id);
        popularityRl = (RelativeLayout) v.findViewById(R.id.boost_layout);
        creditsActivatePopularity = (RelativeLayout) v.findViewById(R.id.credits_activate_popularity_rl_id);

        profTv = (TextView) v.findViewById(R.id.see_profile);

        creditPremiumLl = (LinearLayout) v.findViewById(R.id.credtis_premium_ll_id);

        getMoreAten = (CardView) v.findViewById(R.id.get_more_atten_cv_id);

        iv1 = (ImageView) v.findViewById(R.id.add_photo_img_id);
        iv1.setOnClickListener(this::onClick);


        mCircleView = (CircleProgressView) v.findViewById(R.id.cpv_id);
        mCircleView.setMaxValue(100);
        mCircleView.setDirection(Direction.CW);
        mCircleView.setUnit("%");
        mCircleView.setUnitVisible(true);
        mCircleView.setMinimumWidth(42);
        mCircleView.setMinimumHeight(42);
        mCircleView.setTextSize(30); // text size set, auto text size off
        mCircleView.setUnitSize(22); // if i set the text size i also have to set the unit size
        mCircleView.setUnitScale(1f);
        mCircleView.setTextScale(1f);
        mCircleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mCircleView.setTextColor(ContextCompat.getColor(getActivity(), R.color.purple));
        mCircleView.setUnitColor(ContextCompat.getColor(getActivity(), R.color.purple));
        mCircleView.setBarColor(ContextCompat.getColor(getActivity(), R.color.purple));
        mCircleView.setBarWidth(6);

        mCircleView.setRimColor(ContextCompat.getColor(getActivity(), R.color.off_white));
        mCircleView.setRimWidth(6);

        mCircleView.setInnerContourSize(0);
        mCircleView.setOuterContourSize(0);

        getMoreAten.setOnClickListener(this);
        profTv.setOnClickListener(this);
        usernameTxt.setOnClickListener(this);


        v.findViewById(R.id.premium_layout).setOnClickListener(this);
        v.findViewById(R.id.boost_layout).setOnClickListener(this);

        subscribeTxt = v.findViewById(R.id.subscribe_txt);
        if (MainActivity.purductPurchase)
            subscribeTxt.setText("Activated");


        return v;
    }

    public void setUserData() {
        userImage = Functions.getInfo(context, "image1");

        userName = Functions.getInfo(context, "first_name")
                + " " + Functions.getInfo(context, "last_name");
        usernameTxt.setText(userName + ", " + Functions.getInfo(context, "age"));

        if (userImage != null && !userImage.equals("")) {
            Uri uri = Uri.parse(userImage);
            iv.setImageURI(uri);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_more_atten_cv_id:

                if (SharedPrefrence.calculateCompleteProfile(context) == 100) {
                    startActivity(new Intent(getActivity(), EditProfile_A.class));

                } else {

                    startActivity(new Intent(context, EditProfileVp_A.class));

                }
                break;

            case R.id.see_profile:
                startActivity(new Intent(getActivity(), Viewprofile_A.class));
                break;
            case R.id.username_txt:
                startActivity(new Intent(getActivity(), Viewprofile_A.class));
                break;

            case R.id.premium_layout:
                if (MainActivity.purductPurchase) {
                    Functions.toastMsg(context, "Already Subscribed");
                } else
                    openSubscriptionView();
                break;

            case R.id.boost_layout:
                if (MainActivity.purductPurchase)
                    openBoost();
                else
                    openSubscriptionView();
                break;

            case R.id.add_photo_img_id:
                selectImage();
                break;

            case R.id.profile_RL_id:
                startActivity(new Intent(getActivity(), EditProfile_A.class));
                break;

            case R.id.userimage_img:
                addUserRecord();
                break;

            default:
                break;
        }
    }

    public void openSubscriptionView() {

        InApp_Subscription_A inAppSubscriptionA = new InApp_Subscription_A();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_F, inAppSubscriptionA)
                .addToBackStack(null)
                .commit();

    }

    public void openBoost() {
        Boost_F matchF = new Boost_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.Main_F, matchF).commit();

    }

    @Override
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {
            if (SharedPrefrence.calculateCompleteProfile(context) == 100) {
                completeText.setText("Get more attention - \nProfile completed.");
            }

            mCircleView.setValue(SharedPrefrence.calculateCompleteProfile(context));
        }
    }

    public void addUserRecord() {
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

            viewProfile.show(getActivity().getSupportFragmentManager(), viewProfile.getTag());


        } catch (Exception b) {
            Functions.toastMsg(context, "Err " + b.toString());
        }


    }

    private void selectImage() {
        try {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, context.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Select Option");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        openCameraIntent();

                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, pickimagegallery);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });


                builder.show();
            } else
                Functions.toastMsg(context, "Camera Permission error");
        } catch (Exception e) {
            Functions.toastMsg(context, "Camera Permission error");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == pickimagecamera) {
                try {

                    Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                    // New Code
                    Matrix matrix = new Matrix();
                    try {
                        ExifInterface exif = new ExifInterface(imageFilePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Functions.logDMsg("Angel " + orientation);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                Functions.logDMsg("Angel 90 " + ExifInterface.ORIENTATION_ROTATE_90);
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                Functions.logDMsg( "Angel 180 " + ExifInterface.ORIENTATION_ROTATE_180);
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                matrix.postRotate(0);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);


                    Uri selectedImage1 = Functions.getImageUri(context, rotatedBitmap);
                    beginCrop(selectedImage1);


                    Functions.logDMsg("Path " + imageFilePath);
                    Functions.logDMsg("" + imageFilePath);


                } catch (Exception e) {
                    Functions.toastMsg(context, "Camera Error " + e.toString());
                    e.printStackTrace();
                }
            } else if (requestCode == pickimagegallery) {


                try {
                    Uri selectedImage = data.getData();

                    beginCrop(selectedImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 123) {

                handleCrop(resultCode, data);
            }

        }
    }

    public File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(500, 500).start(getActivity(), 123);
    }


    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            Uri userimageuri = Crop.getOutput(result);


            addProfilePic(userimageuri);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Functions.toastMsg(context, "You cancel This");
        } else {
            Functions.toastMsg(context, "You cancel This");
        }
    }


    public void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, pickimagecamera);
            }
        }
    }


    // Upload User Profile_F.
    public void addProfilePic(Uri filePath) {

        Functions.showLoader(context, true, false);

        final String fb_id = Functions.getInfo(context, "fb_id");

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {

                    Functions.logDMsg("Img Url " + uri.toString());


                    final JSONObject parameters = new JSONObject();
                    try {
                        parameters.put("image_link", uri.toString());
                        parameters.put("fb_id", fb_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ApiRequest.callApi(
                            context,
                            "" + ApiLinks.changeProfilePicture,
                            parameters,
                            (requestType, response) -> {

                                Functions.cancelLoader();
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    if (resp.getString("code").equals("200")) {


                                        JSONArray msgObj = resp.getJSONArray("msg");
                                        JSONObject userInfoObj = msgObj.getJSONObject(0);

                                        SharedPrefrence.saveString(context, "" + userInfoObj.toString(),
                                                "" + SharedPrefrence.uLoginDetail);

                                        setUserData();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                    );
                }))
                .addOnFailureListener(e -> {
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());

                });


    }


}
