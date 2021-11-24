package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Adapters.BlockedUserAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.BlockUserModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

public class BlockedUser_A extends AppCompatActivity {

    RecyclerView msgRV;
    BlockedUserAdapter adapter;
    Context context;
    ArrayList<BlockUserModel> blockUserArrayList;
    DatabaseReference rootref;
    ImageView backId;
    int i = 0;
    ProgressBar progressLoader;
    ValueEventListener eventListener;
    Query inboxQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_user);
        context = BlockedUser_A.this;
        progressLoader = findViewById(R.id.loader);

        msgRV = findViewById(R.id.chatlist);

        msgRV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        // For Desc.
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        // For Divider in recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(msgRV.getContext(),
                linearLayoutManager.getOrientation());
        msgRV.addItemDecoration(dividerItemDecoration);

        msgRV.setLayoutManager(linearLayoutManager);

        backId = findViewById(R.id.back_id);


        String userId = Functions.getInfo(context, "fb_id");
        String firstName = Functions.getInfo(context, "first_name");
        String userPic = Functions.getInfo(context, "image1");


        Variables.userId = userId;
        Variables.userName = firstName;
        Variables.userPic = userPic;
        rootref = FirebaseDatabase.getInstance().getReference();
        blockUserArrayList = new ArrayList<>();

        backId.setOnClickListener(v -> {
            finish();
        });


        adapter = new BlockedUserAdapter(context, blockUserArrayList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                BlockUserModel blockUserModel = (BlockUserModel) model;
                showPopOptions(blockUserModel.getId());
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        msgRV.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("BlockUser").child(Variables.userId);
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blockUserArrayList.clear();
                progressLoader.setVisibility(View.GONE);

                if (!dataSnapshot.exists()) {

                    if (blockUserArrayList.size() == 0) {
                        showNorecordText();
                    } else {
                        findViewById(R.id.no_record).setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();


                } else {
                    Functions.logDMsg("dataSnapot : " + dataSnapshot.toString());
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Functions.logDMsg("dataSnapshot1 : " + dataSnapshot1.toString());

                        BlockUserModel model = dataSnapshot1.getValue(BlockUserModel.class);
                        model.setId(dataSnapshot1.getKey());
                        blockUserArrayList.add(model);
                        adapter.notifyDataSetChanged();
                        if (blockUserArrayList.size() == 0) {
                            showNorecordText();
                        } else {
                            findViewById(R.id.no_record).setVisibility(View.GONE);
                        }
                    }
                }

                if (blockUserArrayList.size() == 0) {
                    // If size is Zero
                    showNorecordText();
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

    public void showPopOptions(final String receiverid) {


        final CharSequence[] options = {getString(R.string.unblock), getString(R.string.cancel)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.unblock))) {
                dialog.dismiss();
                unBlockUserFromChat(receiverid);
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();

            }
        });


        builder.show();


    }


    // Method to Block the user From Inbox_F
    public void unBlockUserFromChat(final String Receiverid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("BlockUser").child(Variables.userId).child(Receiverid);
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Functions.toastMsg(context, "User Un Blocked");
                adapter.notifyDataSetChanged();
            }

        });
    }


    public void showNorecordText() {
        findViewById(R.id.no_record).setVisibility(View.VISIBLE);
    }

}
