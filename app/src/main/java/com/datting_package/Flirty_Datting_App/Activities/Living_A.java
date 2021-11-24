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
import android.widget.TextView;

import java.util.Arrays;

import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.EditProfile_A.userLiving;

public class Living_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1;
    TextView  title;
    Context context;
    ListView listView;
    String livingFromPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        context = Living_A.this;
        tb = (Toolbar) findViewById(R.id.living_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.living_back_id);
        title = tb.findViewById(R.id.title);

        try{
            Intent intent = getIntent();
            livingFromPre = intent.getStringExtra("living");

        }catch (Exception b){
            b.printStackTrace();
        }


        iv1.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.simple_list);

        final ArrayAdapter<String> adapter

                = new ArrayAdapter<>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.ARR_LIST_LIVING);


        listView.setOnItemClickListener((parent, view, position, id) ->  {

                Variables.varLiving = Variables.ARR_LIST_LIVING[position];

                userLiving.setText("" + Variables.ARR_LIST_LIVING[position]);


        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
             try{
            int selectSec = Arrays.asList( Variables.ARR_LIST_LIVING).indexOf("" + livingFromPre);
             listView.setItemChecked(selectSec, true);

        }catch (Exception n){
                 n.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.living_back_id:

                finish();
                break;

            default:
                break;

        }
    }
}
