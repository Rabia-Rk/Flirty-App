package com.datting_package.Flirty_Datting_App.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class See_Full_Image_F extends RootFragment {

    View view;
    Context context;
    ImageButton savebtn, sharebtn, closeGallery;


    ImageView singleImage;

    String imageUrl, chatId;

    ProgressBar progressBar;

    ProgressDialog progressDialog;

    DownloadRequest prDownloader;

    File direct;
    File fullpath;
    int width, height;


    public See_Full_Image_F() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_see_full_image, container, false);
        context = getContext();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        imageUrl = getArguments().getString("image_url");
        chatId = getArguments().getString("chat_id");


        closeGallery = view.findViewById(R.id.close_gallery);
        closeGallery.setOnClickListener(v -> {

            getActivity().onBackPressed();

        });


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");

        PRDownloader.initialize(getActivity().getApplicationContext());

        fullpath = new File(Environment.getExternalStorageDirectory() + "/Hugme/" + chatId + ".jpg");


        savebtn = view.findViewById(R.id.savebtn);
        if (fullpath.exists()) {
            savebtn.setVisibility(View.GONE);
        }


        direct = new File(Environment.getExternalStorageDirectory() + "/Hugme/");

        prDownloader = PRDownloader.download(imageUrl, direct.getPath(), chatId + ".jpg")
                .build();


        savebtn.setOnClickListener(v -> {
            savepicture(false);

        });


        progressBar = view.findViewById(R.id.p_bar);

        singleImage = view.findViewById(R.id.single_image);


        if (fullpath.exists()) {
            Uri uri = Uri.parse(fullpath.getAbsolutePath());
            singleImage.setImageURI(uri);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).placeholder(R.drawable.image_placeholder)
                    .into(singleImage, new Callback() {
                        @Override
                        public void onSuccess() {

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);

                        }

                    });
        }

        sharebtn = view.findViewById(R.id.sharebtn);
        sharebtn.setOnClickListener(v -> {
            sharePicture();

        });

        return view;
    }


    public void sharePicture() {
        if (checkstoragepermision()) {
            Uri bitmapuri;
            if (fullpath.exists()) {
                bitmapuri = Uri.parse(fullpath.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapuri);
                startActivity(Intent.createChooser(intent, ""));
            } else {
                savepicture(true);
            }

        }
    }


    public void savepicture(final boolean isfromshare) {
        if (checkstoragepermision()) {

            final File direct = new File(Environment.getExternalStorageDirectory() + "/DCIM/Binder/");
            progressDialog.show();
            prDownloader.start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.parse(direct.getPath() + chatId + ".jpg"));
                    context.sendBroadcast(intent);
                    progressDialog.dismiss();
                    if (isfromshare) {
                        sharePicture();
                    } else {
                        Functions.toastMsg(context, "Image Saved.");
                    }
                }

                @Override
                public void onError(Error error) {
                    progressDialog.dismiss();
                    Functions.toastMsg(context, "Error");
                }

            });

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Functions.toastMsg(context, "Click Again");
        }
    }

    public boolean checkstoragepermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;

            } else {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {

            return true;
        }
    }

}
