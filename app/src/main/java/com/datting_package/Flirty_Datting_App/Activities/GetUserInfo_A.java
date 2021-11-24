package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GetUserInfo_A extends AppCompatActivity {

    ImageView profileImage;
    TextView firstName, lastName;

    DatabaseReference rootref;


    ImageButton editProfileImage;
    EditText dateofbrithEdit;
    RadioButton maleBtn, femaleBtn;
    byte[] imageByteArray;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);


        rootref = FirebaseDatabase.getInstance().getReference();


        profileImage = findViewById(R.id.profile_image);

        editProfileImage = findViewById(R.id.edit_profile_image);
        editProfileImage.setOnClickListener(v -> selectImage());


        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);


        dateofbrithEdit = findViewById(R.id.dateofbirth_edit);

        maleBtn = findViewById(R.id.male_btn);
        femaleBtn = findViewById(R.id.female_btn);


        dateofbrithEdit.setOnClickListener(v -> {
            Functions.opendatePicker(GetUserInfo_A.this, dateofbrithEdit);

        });


        findViewById(R.id.nextbtn).setOnClickListener(v -> {

            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String dateOfBirth = dateofbrithEdit.getText().toString();

            if (imageByteArray == null) {
                Functions.toastMsg(GetUserInfo_A.this, this.getResources().getString(R.string.select_image));
            } else if (TextUtils.isEmpty(fName)) {
                Functions.toastMsg(GetUserInfo_A.this, this.getResources().getString(R.string.please_enter_first_name));

            } else if (TextUtils.isEmpty(lName)) {
                Functions.toastMsg(GetUserInfo_A.this, this.getResources().getString(R.string.please_enter_last_name));

            } else if (TextUtils.isEmpty(dateOfBirth)) {
                Functions.toastMsg(GetUserInfo_A.this, this.getResources().getString(R.string.please_enter_date_of_birth));
            } else {

                saveInfo();
            }


        });


        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            userId = intent.getExtras().getString("id");
            userId = userId.replace("+", "");
        }
        if (intent.hasExtra("fname")) {
            firstName.setText(intent.getExtras().getString("fname"));
        }

        if (intent.hasExtra("lname")) {
            lastName.setText(intent.getExtras().getString("lname"));
        }


    }


    // open the gallary to select and upload the picture
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    // on select the bottom method will reture the uri of that image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 2) {

                Uri selectedImage = data.getData();
                beginCrop(selectedImage);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                handleCrop(result.getUri());
            }

        }

    }


    private void beginCrop(Uri source) {

        CropImage.activity(source)
                .start(this);
    }


    private void handleCrop(Uri userimageuri) {

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(userimageuri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path = userimageuri.getPath();
        Matrix matrix = new Matrix();
        android.media.ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new android.media.ExifInterface(path);
                int orientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        imageByteArray = out.toByteArray();


        profileImage.setImageBitmap(null);
        profileImage.setImageURI(null);
        profileImage.setImageBitmap(rotatedBitmap);


    }


    // this method is used to store the selected image into database
    public void saveInfo() {
        Functions.showLoader(this, false, false);
        // first we upload image after upload then get the picture url and save the group data in database
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filelocation = storageReference.child("User_image")
                .child(userId + ".jpg");

        filelocation.putBytes(imageByteArray).addOnSuccessListener(taskSnapshot -> filelocation.getDownloadUrl().addOnSuccessListener(uri -> callApiForSignup(userId,
                firstName.getText().toString(),
                lastName.getText().toString(),
                dateofbrithEdit.getText().toString()
                , uri.toString())));


    }


    // this method will store the info of user to  database
    private void callApiForSignup(String userId,
                                  String fName, String lName,
                                  String birthday, String picture) {

        fName = fName.replaceAll("\\W+", "");
        lName = lName.replaceAll("\\W+", "");

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);
            parameters.put("first_name", fName);
            parameters.put("last_name", lName);
            parameters.put("birthday", birthday);

            if (maleBtn.isChecked()) {
                parameters.put("gender", "Male");

            } else {
                parameters.put("gender", "Female");
            }
            parameters.put("image1", picture);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            ApiRequest.callApi(
                    this,
                    ApiLinks.API_SignUp,
                    parameters,
                    new CallBack() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void getResponse(String requestType, String resp) {

                            Functions.cancelLoader();
                            try {
                                JSONObject response = new JSONObject(resp);

                                if (response.getString("code").equals("200")) {

                                    JSONArray msgObj = response.getJSONArray("msg");
                                    JSONObject userInfoObj = msgObj.getJSONObject(0);

                                    SharedPrefrence.saveString(GetUserInfo_A.this, userInfoObj.toString(),
                                            SharedPrefrence.uLoginDetail);


                                    SharedPrefrence.saveString(GetUserInfo_A.this, userInfoObj.optString("fb_id"), SharedPrefrence.uId
                                    );


                                    SharedPrefrence.removeValueOfKey(GetUserInfo_A.this,
                                            SharedPrefrence.shareSocialInfo);


                                    enableLocation();

                                }


                            } catch (Exception b) {
                                Functions.cancelLoader();
                            }
                        }
                    }
            );
        } catch (Exception b) {
            Functions.cancelLoader();
        }

    }


    private void enableLocation() {

        startActivity(new Intent(this, EnableLocation_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finishAffinity();

    }


    public void goback(View view) {
        finish();
    }


}
