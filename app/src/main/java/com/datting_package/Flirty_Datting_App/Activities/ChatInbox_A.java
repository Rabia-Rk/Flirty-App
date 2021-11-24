package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Chat.InboxAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.InboxModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

public class ChatInbox_A extends AppCompatActivity {
    RecyclerView recyclerView;
    InboxAdapter adapter;
    Context context;
    ArrayList<InboxModel> dataList;
    DatabaseReference rootref;
    Toolbar toolbar;
    ImageView backId;

    ProgressBar progressLoader;
    TextView noRecord;
    // on start we get the all inbox data from database
    ValueEventListener eventListener;
    Query inboxQuery;

    public void changeColorDynmic() {
        try {
            toolbar = findViewById(R.id.tb_id);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inbox);


        context = ChatInbox_A.this;
        progressLoader = findViewById(R.id.loader);

        recyclerView = findViewById(R.id.chatlist);
        noRecord = findViewById(R.id.no_record);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(linearLayoutManager);

        backId = findViewById(R.id.back_id);


        String userId = Functions.getInfo(context, "fb_id");
        String firstName = Functions.getInfo(context, "first_name");
        String userPic = Functions.getInfo(context, "image1");


        Variables.userId = userId;
        Variables.userName = firstName;
        Variables.userPic = userPic;
        rootref = FirebaseDatabase.getInstance().getReference();
        dataList = new ArrayList<>();

        backId.setOnClickListener(v -> {
            finish();
        });


        adapter = new InboxAdapter(context, dataList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

                InboxModel inboxModel = (InboxModel) model;


                chatFragment(inboxModel.getRid(), inboxModel.getName(), inboxModel.getPic(), inboxModel.getBlock());
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        changeColorDynmic();

    }

    @Override
    public void onStart() {
        super.onStart();


        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("Inbox").child(Variables.userId).orderByChild("timestamp");
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                progressLoader.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InboxModel model = ds.getValue(InboxModel.class);
                        dataList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }

                if (dataList.size() == 0) {
                    findViewById(R.id.no_record).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.no_record).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        inboxQuery.removeEventListener(eventListener);

    }

    public void chatFragment(String receiverid, String name, String receiverPic, String isBlock) {

        // Open New Activity
        Intent myIntent = new Intent(ChatInbox_A.this, Chat_A.class);
        myIntent.putExtra("receiver_id", receiverid);
        myIntent.putExtra("receiver_name", name);
        myIntent.putExtra("receiver_pic", receiverPic);
        myIntent.putExtra("is_block", isBlock);
        myIntent.putExtra("match_api_run", "0");
        startActivity(myIntent);


    }


}
