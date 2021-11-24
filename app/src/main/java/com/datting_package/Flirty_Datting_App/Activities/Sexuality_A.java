package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import java.util.Arrays;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.userSex;

public class Sexuality_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1;
    Context context;
    TextView title;
    String sexxFromPre;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sexuality);
        context = Sexuality_A.this;
        tb = (Toolbar) findViewById(R.id.sex_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.sex_back_id);
        title = findViewById(R.id.title);

        listView = (ListView) findViewById(R.id.simple_list);

        try {
            Intent intent = getIntent();
            sexxFromPre = intent.getStringExtra("sexuality"); //if it's a string you stored.

        } catch (Exception b) {
            b.printStackTrace();
        }


        final ArrayAdapter<String> adapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_SEXUALITY);

        listView.setOnItemClickListener((parent, view, position, id) -> {


            Variables.varSexuality = Variables.ARR_LIST_SEXUALITY[position];

            userSex.setText("" + Variables.ARR_LIST_SEXUALITY[position]);


        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        try {
            int selectSec = Arrays.asList(Variables.ARR_LIST_SEXUALITY).indexOf("" + sexxFromPre);
            listView.setItemChecked(selectSec, true);
        } catch (Exception m) {
            m.printStackTrace();
        }


        iv1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex_back_id:
                finish();
                break;

            default:
                break;
        }
    }
}
