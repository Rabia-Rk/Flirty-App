package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import java.util.Arrays;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.userChildren;

public class Children_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1;
    Context context;
    TextView title;

    ListView listView;
    String childFromPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        context = Children_A.this;
        tb = (Toolbar) findViewById(R.id.children_TB_id);
        iv1 = (ImageView) findViewById(R.id.children_back_id);
        title = findViewById(R.id.title);
        iv1.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.simple_list);

        try {
            Intent intent = getIntent();
            childFromPre = intent.getStringExtra("child"); //if it's a string you stored.

        } catch (Exception b) {
            b.printStackTrace();
        }


        final ArrayAdapter<String> adapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_CHILDREN);


        listView.setOnItemClickListener((parent, view, position, id) -> {

            Variables.varChildren = Variables.ARR_LIST_CHILDREN[position];

            userChildren.setText("" + Variables.ARR_LIST_CHILDREN[position]);


        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        try {
            int selectSec = Arrays.asList(Variables.ARR_LIST_CHILDREN).indexOf("" + childFromPre);
            listView.setItemChecked(selectSec, true);

        } catch (Exception b) {
            b.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.children_back_id:
                finish();
                break;

            default:
                break;
        }
    }

    public void addRadioButtons(int number) {

        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText("Radio " + rdbtn.getId());
                ll.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.children_RG_id)).addView(ll);
        }
    }


}
