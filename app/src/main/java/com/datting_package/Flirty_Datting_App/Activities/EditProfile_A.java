package com.datting_package.Flirty_Datting_App.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.Adapters.EditProfileAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.datting_package.Flirty_Datting_App.CodeClasses.Variables.userGender;
import static com.datting_package.Flirty_Datting_App.Fragments.Profile_F.mCircleView;

public class EditProfile_A extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    public static List<String> basicInfoTitles = new ArrayList<>();
    public static Activity activity = null;
    public static TextView userLiving, userChildren, userSmoking, userDrinking, userRelationship, userSex;
    Toolbar tb;
    ImageView iv, iv1;
    RecyclerView recyclerView;
    EditProfileAdapter adapter;
    RelativeLayout basicInfo, living, children, smoking, drinking, relation, sex;
    FirebaseStorage storage;
    StorageReference storageReference;
    List<String> listUserImgFromApi = new ArrayList<>();
    EditText aboutMe;
    ImageView icTick;
    Context context;
    TextView tv3IdLiving, tv4IdChildren, tv5IdSmoking, tv6IdDrinking, tv7IdRelationship, tv8IdSexuality;
    TextView tvBasicInfo;
    String imageFilePath;

    // Update Profile_F
    public static void updateProfile(final Context context, final JSONObject parameters) {

        Functions.showLoader(context, false, false);

        ApiRequest.callApi(context, ApiLinks.edit_profile, parameters, (requestType, resp) -> {

                    try {
                        Functions.cancelLoader();
                        JSONObject response = new JSONObject(resp);

                        if (response.getString("code").equals("200")) {

                            JSONArray msgObj = response.getJSONArray("msg");
                            JSONObject userInfoObj = msgObj.getJSONObject(0);

                            Functions.logDMsg( "Profile_F Updated Successfully.");

                            SharedPrefrence.saveString(context,"" + userInfoObj.toString(),
                                    "" + SharedPrefrence.uLoginDetail);

                            activity.finish();

                            mCircleView.setValue(SharedPrefrence.calculateCompleteProfile(context));

                        } else {
                            Functions.cancelLoader();
                        }
                    } catch (Exception b) {
                        Functions.logDMsg("Error " + b.toString());
                        Functions.cancelLoader();
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = EditProfile_A.this;
        activity = EditProfile_A.this;
        tb = (Toolbar) findViewById(R.id.edit_prof_TB_id);
        iv = (ImageView) tb.findViewById(R.id.edit_prof_back_id);
        iv1 = (ImageView) tb.findViewById(R.id.edit_prof_eye_id);
        aboutMe = findViewById(R.id.about_me);
        icTick = findViewById(R.id.ic_tick);
        activity = this;

        Functions.displayFbAd(context);


        final String about = Functions.getInfo(context, "about_me");
        final String basic = Functions.getInfo(context, "first_name") + ", " + Functions.getInfo(context, "age");

        aboutMe.setText("" + about);
        tvBasicInfo = findViewById(R.id.TV_basic_info);
        tvBasicInfo.setText("" + basic);


        userLiving = findViewById(R.id.user_living);
        userChildren = findViewById(R.id.user_children);
        userSmoking = findViewById(R.id.user_smoking);
        userDrinking = findViewById(R.id.user_drinking);
        userRelationship = findViewById(R.id.user_relationship);
        userSex = findViewById(R.id.user_sex);


        String living1 = Functions.getInfo(context, "living");
        String children1 = Functions.getInfo(context, "children");
        String smoking1 = Functions.getInfo(context, "smoking");
        String drinking1 = Functions.getInfo(context, "drinking");
        String relationship1 = Functions.getInfo(context, "relationship");
        String sexuality1 = Functions.getInfo(context, "sexuality");

        if (living1.equals("0") || living1.equals("")) {
            living1 = "";
        }

        if (children1.equals("0") || children1.equals("")) {
            children1 = "";
        }

        if (smoking1.equals("0") || smoking1.equals("")) {
            smoking1 = "";
        }

        if (drinking1.equals("0") || drinking1.equals("")) {
            drinking1 = "";
        }

        if (relationship1.equals("0") || relationship1.equals("")) {
            relationship1 = "";
        }

        if (sexuality1.equals("0") || sexuality1.equals("")) {
            sexuality1 = "";
        }


        userLiving.setText("" + living1);
        userChildren.setText("" + children1);
        userSmoking.setText("" + smoking1);
        userDrinking.setText("" + drinking1);
        userRelationship.setText("" + relationship1);
        userSex.setText("" + sexuality1);


        basicInfo = (RelativeLayout) findViewById(R.id.RL1_id);
        living = (RelativeLayout) findViewById(R.id.RL3_id);
        children = (RelativeLayout) findViewById(R.id.RL4_id);
        smoking = (RelativeLayout) findViewById(R.id.RL5_id);
        drinking = (RelativeLayout) findViewById(R.id.RL6_id);
        relation = (RelativeLayout) findViewById(R.id.RL7_id);
        sex = (RelativeLayout) findViewById(R.id.RL8_id);

        // init TextViews
        tv3IdLiving = findViewById(R.id.TV3_id);
        tv4IdChildren = findViewById(R.id.TV4_id);
        tv5IdSmoking = findViewById(R.id.TV5_id);
        tv6IdDrinking = findViewById(R.id.TV6_id);
        tv7IdRelationship = findViewById(R.id.TV7_id);
        tv8IdSexuality = findViewById(R.id.TV8_id);


        // init Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        recyclerView = findViewById(R.id.edit_prof_RL_id);
        adapter = new EditProfileAdapter(context, activity, listUserImgFromApi, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

                if (view.getId() == R.id.cancel) {
                    removeImage(postion);
                } else if (view.getId() == R.id.RL_add_img) {
                    selectImage();
                }

            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        iv.setOnClickListener(this);
        iv1.setOnClickListener(this);

        basicInfo.setOnClickListener(this);
        living.setOnClickListener(this);
        children.setOnClickListener(this);
        smoking.setOnClickListener(this);
        drinking.setOnClickListener(this);
        relation.setOnClickListener(this);
        sex.setOnClickListener(this);
        icTick.setOnClickListener(this);
        final String userId = Functions.getInfo(context, "fb_id");
        getuserInfo(userId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ic_tick:
                createJsonForAPI();

                break;
            case R.id.edit_prof_back_id:
                mCircleView.setValue(SharedPrefrence.calculateCompleteProfile(context));
                finish();
                break;
            case R.id.edit_prof_eye_id:
                startActivity(new Intent(EditProfile_A.this, Viewprofile_A.class));
                break;
            case R.id.RL1_id:

                Intent myintentGender = new Intent(EditProfile_A.this, BasicInfo_A.class);
                myintentGender.putExtra("gender", "" + userGender); //Optional parameters
                startActivity(myintentGender);

                break;
            case R.id.RL3_id:
                Intent myIntent = new Intent(EditProfile_A.this, Living_A.class);
                myIntent.putExtra("living", "" + userLiving.getText()); //Optional parameters
                startActivity(myIntent);


                break;
            case R.id.RL4_id:
                Intent myintentChild = new Intent(EditProfile_A.this, Children_A.class);
                myintentChild.putExtra("child", "" + userChildren.getText()); //Optional parameters
                startActivity(myintentChild);

                break;
            case R.id.RL5_id:

                Intent myIntentSmoking = new Intent(EditProfile_A.this, Smoking_A.class);
                myIntentSmoking.putExtra("smoking", "" + userSmoking.getText()); //Optional parameters
                startActivity(myIntentSmoking);

                break;
            case R.id.RL6_id:

                Intent myintentDrink = new Intent(EditProfile_A.this, Drinking_A.class);
                myintentDrink.putExtra("drinking", "" + userDrinking.getText()); //Optional parameters
                startActivity(myintentDrink);


                break;
            case R.id.RL7_id:

                Intent myIntentRelation = new Intent(EditProfile_A.this, Relationship_A.class);
                myIntentRelation.putExtra("relation", "" + userRelationship.getText()); //Optional parameters
                startActivity(myIntentRelation);

                break;
            case R.id.RL8_id:

                Intent myintentSexx = new Intent(EditProfile_A.this, Sexuality_A.class);
                myintentSexx.putExtra("sexuality", "" + userSex.getText()); //Optional parameters
                startActivity(myintentSexx);
                break;

            default:
                break;

        }
    }

    public void removeImage(int position) {
        final String user_id = Functions.getInfo(context, "fb_id");
        Functions.deleteImage(context, "" + listUserImgFromApi.get(position), user_id);

        listUserImgFromApi.remove(position);
        adapter.notifyDataSetChanged();

    }

    public void createJsonForAPI() {

        final String user_id = Functions.getInfo(context, "fb_id");


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", "" + user_id);
            parameters.put("about_me", "" + aboutMe.getText().toString().replaceAll("[-+.^:,']", ""));
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

            Functions.logDMsg("parameters  e: " + parameters);
            updateProfile(context, parameters);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void selectImage() {
        try {
            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, context.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_gallery), getString(R.string.cancel)};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle(getString(R.string.select));
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals(getString(R.string.take_photo))) {
                        dialog.dismiss();
                        openCameraIntent();

                    } else if (options[item].equals(getString(R.string.choose_gallery))) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals(getString(R.string.cancel))) {
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

        if (requestCode == PICK_IMAGE_CAMERA) {
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
                            Functions.logDMsg("Angel 180 " + ExifInterface.ORIENTATION_ROTATE_180);
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
                    imageStream = getContentResolver().openInputStream(selectedImage);
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
                Functions.toastMsg(context, "Camera Permission error");
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {


            try {
                Uri selectedImage = data.getData();

                beginCrop(selectedImage);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 123) {

            handleCrop(resultCode, data);
        } else {

            handleCrop(resultCode, data);
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
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(500, 500).start(EditProfile_A.this, 123);
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
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }


    // Upload User Profile_F.
    public void addProfilePic(Uri filePath) {

        Functions.showLoader(context, true, false);


        final String fbId = Functions.getInfo(context, "fb_id");

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {


                    listUserImgFromApi.add(uri.toString());

                    for (int i = 0; i < listUserImgFromApi.size(); i++) {
                        if (listUserImgFromApi.get(i).equals("") || listUserImgFromApi.get(i).equals("0")) {
                            listUserImgFromApi.remove(i);
                        }
                    }

                    if (listUserImgFromApi.size() < 6) {
                        listUserImgFromApi.add("0");
                    }
                    adapter.notifyDataSetChanged();


                    final JSONObject parameters = new JSONObject();
                    try {
                        parameters.put("image_link", uri.toString());
                        parameters.put("fb_id", fbId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ApiRequest.callApi(context, "" + ApiLinks.uploadImages, parameters,
                            (requestType, response) -> {

                                Functions.cancelLoader();

                                try {
                                    JSONObject resp = new JSONObject(response);
                                    JSONArray msgArr = resp.getJSONArray("msg");
                                    msgArr.getJSONObject(0).getString("response");

                                    Functions.getUserInfo(fbId, context);  // Method to save User data into Local Shared Prefrence

                                    Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));
                                } catch (Exception v) {
                                    v.printStackTrace();
                                }


                            }
                    );
                }))
        ;


    }


    public void getuserInfo(final String userId) {

        Functions.showLoader(context, false, false);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(context, ApiLinks.getUserInfo, parameters, (requestType, resp) -> {
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


                            String living = userInfoObj.getString("living");
                            String children = userInfoObj.getString("children");
                            String smoking = userInfoObj.getString("smoking");
                            String drinking = userInfoObj.getString("drinking");
                            String relationship = userInfoObj.getString("relationship");
                            String sexuality = userInfoObj.getString("sexuality");


                            if (living.equals("0") || living.equals("")) {
                                living = "";
                            }

                            if (children.equals("0") || children.equals("")) {
                                children = "";
                            }

                            if (smoking.equals("0") || smoking.equals("")) {
                                smoking = "";
                            }

                            if (drinking.equals("0") || drinking.equals("")) {
                                drinking = "";
                            }

                            if (relationship.equals("0") || relationship.equals("")) {
                                relationship = "";
                            }

                            if (sexuality.equals("0") || sexuality.equals("")) {
                                sexuality = "";
                            }

                            userLiving.setText("" + living);
                            userChildren.setText("" + children);
                            userSmoking.setText("" + smoking);
                            userDrinking.setText("" + drinking);
                            userRelationship.setText("" + relationship);
                            userSex.setText("" + sexuality);

                            aboutMe.setText("" + userInfoObj.getString("about_me"));

                            imageDrawableToURI();
                        }
                    } catch (Exception b) {
                        Functions.cancelLoader();
                        Functions.toastMsg(context, "" + b.toString());
                    }

                }
        );
    }


    public void imageDrawableToURI() {

        if (listUserImgFromApi.size() < 6) {
            listUserImgFromApi.add("0");
        }

        adapter.notifyDataSetChanged();

    }


}
