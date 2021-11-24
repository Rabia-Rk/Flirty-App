package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;

import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.userRelationship;

public class Relationship_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1;
    RadioGroup rg;
    Context context;
    TextView title;
    RadioButton radioButton;

    ListView listView;
    String relationFromPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);

        context = Relationship_A.this;
        tb = (Toolbar) findViewById(R.id.relationship_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.relationship_back_id);
        title = findViewById(R.id.title);

        listView = (ListView) findViewById(R.id.simple_list);

        try{
            Intent intent = getIntent();
            relationFromPre = intent.getStringExtra("relation");

        }catch (Exception b){
            b.printStackTrace();
        }


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice,
                Variables.ARR_LIST_RELATIONSHIP);


        listView.setOnItemClickListener((parent, view, position, id) -> {

            Variables.varRelationship = Variables.ARR_LIST_RELATIONSHIP[position];

            userRelationship.setText("" + Variables.ARR_LIST_RELATIONSHIP[position]);


        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
       try{
            int selectSec = Arrays.asList( Variables.ARR_LIST_RELATIONSHIP).indexOf("" + relationFromPre);
            listView.setItemChecked(selectSec, true);
        }catch (Exception b){
            b.printStackTrace();
        }

        iv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relationship_back_id:
                addListenerOnButton();
                finish();
                break;

            default:
                break;
        }
    }


    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.relationship_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
    }
}
