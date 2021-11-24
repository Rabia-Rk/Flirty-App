package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.R;

public class ShareProfile_A extends AppCompatActivity implements View.OnClickListener {


    SimpleDraweeView userimageImg;
    TextView tvusername;

    RelativeLayout profileCardLayout;
    String userImage, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profile);


        profileCardLayout = findViewById(R.id.profile_card_layout);
        tvusername = findViewById(R.id.username);
        userimageImg = findViewById(R.id.userimage_img);

        Intent intent = getIntent();

        if (intent != null) {

            userImage = intent.getStringExtra("image");
            userName = intent.getStringExtra("name");


            tvusername.setText(userName);
            if (userImage != null && !userImage.equals("")) {
                Uri uri = Uri.parse(userImage);
                userimageImg.setImageURI(uri);
            }


        }

        findViewById(R.id.share_profile_btn).setOnClickListener(this::onClick);
        findViewById(R.id.back_btn).setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_profile_btn:
                share();
                break;

            case R.id.back_btn:
                finish();
                break;

            default:
                break;
        }
    }


    private void share() {
        try {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            Uri bitmapUri = changeLayoutBitmapImage(profileCardLayout);
            intent.setType("image/*");
            intent.setType("text/plain");
            String link = "Check this person of name:" + userName + "\n\n"
                    + "https://play.google.com/store/apps/details?id="
                    + getPackageName();
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public Uri changeLayoutBitmapImage(RelativeLayout view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        return bitmapUri;

    }


}
