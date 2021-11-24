package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import java.util.Arrays;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.userDrinking;

public class Drinking_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1;
    RadioGroup rg;
    Context context;
    TextView title;
    RadioButton radioButton;
    String drinkFromPre;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking);

        context = Drinking_A.this;
        title = findViewById(R.id.title);

        tb = findViewById(R.id.drinking_TB_id);
        iv1 = tb.findViewById(R.id.drinking_back_id);
        listView = findViewById(R.id.simple_list);

        try {
            Intent intent = getIntent();
            drinkFromPre = intent.getStringExtra("drinking"); //if it's a string you stored.

        } catch (Exception b) {
            b.printStackTrace();
        }

        final ArrayAdapter<String> adapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_DRINK);


        listView.setOnItemClickListener((parent, view, position, id) -> {

            Variables.varDrinking = Variables.ARR_LIST_DRINK[position];

            userDrinking.setText("" + Variables.ARR_LIST_DRINK[position]);

        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        try {
            int selectSec = Arrays.asList(Variables.ARR_LIST_DRINK).indexOf("" + drinkFromPre);
            listView.setItemChecked(selectSec, true);
        } catch (Exception o) {
            o.printStackTrace();
        }

        iv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drinking_back_id:
                addListenerOnButton();
                finish();
                break;

            default:
                break;
        }
    }

    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.drinking_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

    }


}
