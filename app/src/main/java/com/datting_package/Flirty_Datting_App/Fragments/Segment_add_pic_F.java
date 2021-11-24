package com.datting_package.Flirty_Datting_App.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.Activities.EnableLocation_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.datting_package.Flirty_Datting_App.Fragments.Segment_dob_F.dobComplete;
import static com.datting_package.Flirty_Datting_App.Fragments.Segment_gender_F.gender;
import static com.datting_package.Flirty_Datting_App.Fragments.Segment_name_F.signupName;

public class Segment_add_pic_F extends RootFragment {

    private final int pickImageCamera = 1, pickImageGallery = 2;
    View v;
    TextView textView;
    Button btn;
    ImageView iv;
    RelativeLayout addPicRl;
    Context context;
    Uri imagUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imgUrl;
    String imageFilePath;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_add_pic, null);

        context = getContext();

        textView = (TextView) v.findViewById(R.id.add_pic_title_id);
        String text = "Picture time! <br> Choose your photo";
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        addPicRl = (RelativeLayout) v.findViewById(R.id.add_pic_rl_id);

        textView.setText(Html.fromHtml(text));

        imgUrl = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.shareSocialInfo,
                "img_url"
        );


        ImageView profilePic = v.findViewById(R.id.profile_pic);

        try {
            Picasso.get().load(imgUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(profilePic);

        } catch (Exception b) {
            b.printStackTrace();

        }


        btn = (Button) v.findViewById(R.id.add_pic_btn_id);
        btn.setOnClickListener(v -> {
            if (imagUri != null) {

                uploadImageToFirebase(imagUri);

            } else {
                uploadImageToFirebase(Uri.parse(imgUrl));
            }


        });

        iv = (ImageView) v.findViewById(R.id.add_pic_plus_id);
        addPicRl.setOnClickListener(v -> {
            selectImage();

        });


        return v;
    }

    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
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
                        startActivityForResult(pickPhoto, pickImageGallery);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pickImageCamera) {
            try {

                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                beginCrop(selectedImage);


                Functions.logDMsg("Path " + imageFilePath);
                Functions.logDMsg( "" + imageFilePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == pickImageGallery) {

            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                bitmap.getWidth();
                bitmap.getHeight();
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

    private void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Functions.logDMsg( "" + ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, pickImageCamera);
            }
        }
    }

    private File createImageFile() throws IOException {
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
        Crop.of(source, destination).asSquare().withMaxSize(500, 500).start(context, getCurrentFragment(), 123);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri userimageuri = Crop.getOutput(result);

            InputStream imageStream = null;
            try {
                imageStream = context.getContentResolver().openInputStream(userimageuri);
            } catch (FileNotFoundException e) {
                Functions.toastMsg(context, "" + e.toString());
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
            imagUri = userimageuri;


            ImageView profilePic = v.findViewById(R.id.profile_pic);
            profilePic.setImageBitmap(imagebitmap);


        } else if (resultCode == Crop.RESULT_ERROR) {
            Functions.toastMsg(context, "" + Crop.getError(result).getMessage());
        } else {
            Functions.toastMsg(context, "" + Crop.getError(result).getMessage());
        }
    }


    public void uploadImageToFirebase(Uri filePath) {

        Functions.showLoader(context, false, false);

        final String user_id = SharedPrefrence.getSocialInfo(context, SharedPrefrence.shareSocialInfo,
                "user_id");

        if (filePath.toString().startsWith("http")) {

            JSONObject parameters = new JSONObject();
            try {
                parameters.put("fb_id", user_id);
                parameters.put("first_name", signupName);
                parameters.put("last_name", "");
                parameters.put("birthday", dobComplete);
                parameters.put("gender", "" + gender);
                parameters.put("image1", filePath);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Calling API for SignUp
            apiCalling(ApiLinks.API_SignUp, context, parameters);


        } else {

            StorageReference ref = storageReference.child("images/" + user_id);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {


                        JSONObject parameters = new JSONObject();
                        try {
                            parameters.put("fb_id", user_id);
                            parameters.put("first_name", signupName);
                            parameters.put("last_name", "");
                            parameters.put("birthday", dobComplete);
                            parameters.put("gender", "" + gender);
                            parameters.put("image1", uri.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        apiCalling(ApiLinks.API_SignUp, context, parameters);

                        Functions.cancelLoader();

                    }));


        }

    }


    public Fragment getCurrentFragment() {

        return getActivity().getSupportFragmentManager().findFragmentById(R.id.view_pager_id);

    }


    public void apiCalling(String url, final Context context, JSONObject parameters) {
        try {

            ApiRequest.callApi(this.context, url, parameters, (requestType, resp) -> {
                        try {
                            JSONObject response = new JSONObject(resp);

                            if (response.getString("code").equals("200")) {

                                JSONArray msgObj = response.getJSONArray("msg");
                                JSONObject userInfoObj = msgObj.getJSONObject(0);


                                SharedPrefrence.saveString(Segment_add_pic_F.this.context, "" + userInfoObj.toString(),
                                        "" + SharedPrefrence.uLoginDetail);

                                SharedPrefrence.saveString(Segment_add_pic_F.this.context, userInfoObj.optString("fb_id", "0"), SharedPrefrence.uId);


                                SharedPrefrence.removeValueOfKey(Segment_add_pic_F.this.context,
                                        "" + SharedPrefrence.shareSocialInfo);


                                Functions.cancelLoader();


                                openEnableLocation();


                            }

                            Functions.cancelLoader();

                        } catch (Exception b) {
                            Functions.cancelLoader();
                        }
                    }
            );
        } catch (Exception b) {
            Functions.cancelLoader();
        }

    }


    public void openEnableLocation() {
        getActivity().startActivity(new Intent(context, EnableLocation_A.class));
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        getActivity().finish();
    }


}
