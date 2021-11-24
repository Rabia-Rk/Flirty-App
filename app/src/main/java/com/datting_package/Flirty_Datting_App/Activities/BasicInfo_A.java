package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.CodeClasses.Variables.userGender;

public class BasicInfo_A extends AppCompatActivity {

    Toolbar tb;
    ImageView iv;
    TextView nameId, birthdayId;
    Context context;
    RadioButton radioFemaleRBId, radioMaleRbId;
    String userGenderFromPrePage;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        context = BasicInfo_A.this;

        String firstName = Functions.getInfo(context, "first_name");
        String birthday = Functions.getInfo(context, "birthday");
        String gender = Functions.getInfo(context, "gender");

        try {
            Intent intent = getIntent();
            userGenderFromPrePage = intent.getStringExtra("gender");

        } catch (Exception b) {
            b.printStackTrace();
        }

        radioGroup = findViewById(R.id.basic_info_RG_id);
        birthdayId = findViewById(R.id.birthday_id);
        radioFemaleRBId = findViewById(R.id.female_RB_id);
        radioMaleRbId = findViewById(R.id.male_RB_id);
        userGender = gender;
        if (gender.equals("male")) {
            // If gender is male
            radioMaleRbId.setChecked(true);
        } else if (gender.equals("female")) {
            radioFemaleRBId.setChecked(true);
        }

        if (userGenderFromPrePage != null && !userGenderFromPrePage.isEmpty()) {
            // If not empty
            if (userGenderFromPrePage.equalsIgnoreCase("male")) {
                // If gender is mAle
                radioMaleRbId.setChecked(true);
            } else if (userGenderFromPrePage.equalsIgnoreCase("female")) {
                radioFemaleRBId.setChecked(true);
            }
        }

        tb = (Toolbar) findViewById(R.id.basic_info_TB_id);
        iv = (ImageView) tb.findViewById(R.id.basic_info_back_id);
        nameId = findViewById(R.id.name_id);
        nameId.setText(getString(R.string.name)+"\n"+ firstName);
        birthdayId.setText(getString(R.string.birthday)+"\n"+ birthday);

        iv.setOnClickListener((View.OnClickListener) v -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            userGender = radioButton.getTag().toString();
            finish();
        });
    }
}
