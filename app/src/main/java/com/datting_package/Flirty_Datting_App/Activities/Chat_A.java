package com.datting_package.Flirty_Datting_App.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.Adapters.GifAdapter;
import com.datting_package.Flirty_Datting_App.Adapters.MsgAdapter;
import com.datting_package.Flirty_Datting_App.Chat.Videos.Chat_Send_file_Service;
import com.datting_package.Flirty_Datting_App.Chat.Videos.Play_Video_F;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.See_Full_Image_F;
import com.datting_package.Flirty_Datting_App.Models.BlockUserModel;
import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VideoCalling.VideoActivity;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.datting_package.Flirty_Datting_App.Chat.Inbox_F.listMyMatch;
import static com.datting_package.Flirty_Datting_App.Chat.Inbox_F.matchAdapter;

public class Chat_A extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    public static SharedPreferences download_pref;
    public static String token = "null";
    public static String uploading_image_id = "none";
    final ArrayList<String> urlList = new ArrayList<>();
    private final int PICK_IMAGE_GALLERY = 2;
    MediaType mediaType;
    EditText msg;
    RecyclerView recyclerView;
    ImageView iv;
    Context context;
    MsgAdapter adapter;
    String receiverName = "Rec Name";
    String receiverPic = "Image";
    String isMatchApiRun, isBlock;
    String removePositionInMyMatch;
    Query queryGetchat;
    DatabaseReference rootref;
    String receiverid = "890";
    ProgressBar pBar;
    ProgressDialog pd;
    String blockingText;
    View view;
    TextView tUserRecName;
    Toolbar header;
    LinearLayout gifLayout;
    ImageButton uploadStikerBtn, uploadGifBtn;
    ImageView sendBtn;
    SimpleDraweeView userimage;
    TextView blocked;
    ValueEventListener valueEventListener;
    ChildEventListener eventListener;
    String imageFilePath;
    // this is related with the list of Gifs that is show in the list below
    GifAdapter gifAdapter;
    RecyclerView gipsList;
    GPHApi client;
    private List<ChatModel> mChats = new ArrayList<>();
    private DatabaseReference adduserToInbox;
    private DatabaseReference mchatRefReteriving;

    public void changeColorDynmic() {
        try {
            header = findViewById(R.id.tb_id);
        } catch (Exception v) {
            v.printStackTrace();
        }

    }

    public void hideSendingView() {
        uploadGifBtn.setVisibility(View.GONE);
        uploadStikerBtn.setVisibility(View.GONE);
        ImageButton uploadimagebtn = findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.GONE);
        sendBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_msg_send_purple));
    }

    public void showSendingView() {

        uploadGifBtn.setVisibility(View.VISIBLE);
        uploadStikerBtn.setVisibility(View.VISIBLE);
        ImageButton uploadimagebtn = findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.GONE);
        sendBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_msg_send_gray));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = Chat_A.this;

        view = findViewById(R.id.Chat_F);
        sendBtn = findViewById(R.id.send_btn);

        userimage = findViewById(R.id.userimage);

        blocked = findViewById(R.id.blocked);

        pd = new ProgressDialog(context);
        pd.setMessage(getResources().getString(R.string.loading));
        pd.setCancelable(false);


        findViewById(R.id.ic_more).setOnClickListener(this::onClick);

        gifLayout = findViewById(R.id.gif_layout);

        recyclerView = findViewById(R.id.chatlist);

        tUserRecName = findViewById(R.id.user_name);

        pBar = findViewById(R.id.progress_bar);

        uploadGifBtn = findViewById(R.id.upload_gif_btn);
        uploadStikerBtn = findViewById(R.id.upload_stiker_btn);

        uploadGifBtn.setOnClickListener(this::onClick);
        uploadStikerBtn.setOnClickListener(this::onClick);


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String userId = Functions.getInfo(context, "fb_id");
        String firstName = Functions.getInfo(context, "first_name") + " " + Functions.getInfo(context, "last_name");
        String userPic = Functions.getInfo(context, "image1");


        Variables.userId = userId;
        Variables.userName = firstName;
        Variables.userPic = userPic;

        Intent intent = getIntent();
        receiverid = intent.getStringExtra("receiver_id");
        receiverName = intent.getStringExtra("receiver_name");
        receiverPic = intent.getStringExtra("receiver_pic");

        try {

            isMatchApiRun = intent.getStringExtra("match_api_run");
            removePositionInMyMatch = intent.getStringExtra("position_to_remove");
        } catch (Exception b) {
            b.printStackTrace();
        }

        if (receiverName.length() > 14) {

            receiverName = receiverName.substring(0, 14) + " ...";
        }

        tUserRecName.setText("" + receiverName);


        displayUserImage(receiverPic, receiverName);

        getLatestUserProfile(receiverid);


        findViewById(R.id.select_camera_2).setOnClickListener(this::onClick);
        findViewById(R.id.uploadimagebtn).setOnClickListener(this::onClick);
        download_pref = context.getSharedPreferences(Variables.downloadPref, Context.MODE_PRIVATE);


        rootref = FirebaseDatabase.getInstance().getReference();
        adduserToInbox = FirebaseDatabase.getInstance().getReference();

        iv = (ImageView) findViewById(R.id.back_id);

        view.findViewById(R.id.back_id).setOnClickListener(this::onClick);

        msg = findViewById(R.id.msgedittext);

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (msg.getText().toString().length() == 0) {

                    sendBtn.setEnabled(false);
                    showSendingView();
                } else if (msg.getText().toString().length() > 0) {
                    hideSendingView();
                    sendBtn.setEnabled(true);
                }
            }
        });


        sendBtn.setOnClickListener(this);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Chat_A.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MsgAdapter(mChats, Variables.userId, context, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {
                ChatModel chatModel = (ChatModel) model;


                if (chatModel.getType().equals("image"))
                    openfullsizeImage(chatModel);

                if (chatModel.getType().equals("video")) {

                    File fullpath = new File(Environment.getExternalStorageDirectory() + "/Hugme/" + chatModel.chat_id + ".mp4");
                    if (fullpath.exists()) {
                        openVideo(fullpath.getAbsolutePath());
                    } else {
                        if (download_pref.getString(chatModel.getChat_id(), "").equals("")) {
                            long downlodid = Functions.downloadDataForChat(context, chatModel);
                            download_pref.edit().putString(chatModel.getChat_id(), "" + downlodid).commit();
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        view.findViewById(R.id.video_call_btn).setOnClickListener(this);
        view.findViewById(R.id.voice_call_btn).setOnClickListener(this::onClick);

        changeColorDynmic();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.video_call_btn:
                if (!checkPermissionForCameraAndMicrophone()) {

                    requestPermissionForCameraAndMicrophone();
                } else {
                    if (MainActivity.purductPurchase || !Variables.CALLING_LIMIT)
                        openCalling("video_call");
                    else {
                        int video_call_time = SharedPrefrence.getInt(Chat_A.this, SharedPrefrence.videoCallingUsedTime);
                        if (video_call_time < Variables.MAX_VIDEO_CALLING_TIME)
                            Functions.showAlert(this, "Alert", "For demo purpose we only allow to " + Functions.convertSeconds(Variables.MAX_VIDEO_CALLING_TIME / 1000) + " for video Call", new CallBack() {
                                @Override
                                public void getResponse(String requestType, String response) {
                                    openCalling("video_call");
                                }
                            });
                        else
                            Functions.showAlert(Chat_A.this, getString(R.string.alert), getString(R.string.video_reach_limit), null);
                    }

                }
                break;

            case R.id.voice_call_btn:
                if (!checkPermissionForCameraAndMicrophone()) {

                    requestPermissionForCameraAndMicrophone();
                } else {

                    if (MainActivity.purductPurchase || !Variables.CALLING_LIMIT)
                        openCalling("voice_call");

                    else {
                        int video_call_time = SharedPrefrence.getInt(Chat_A.this, SharedPrefrence.voiceCallingUsedTime);
                        if (video_call_time < Variables.MAX_VOICE_CALLING_TIME)
                            Functions.showAlert(this, "Alert", "For demo purpose we only allow to " + Functions.convertSeconds(Variables.MAX_VOICE_CALLING_TIME / 1000) + " for voice Call", new CallBack() {
                                @Override
                                public void getResponse(String requestType, String response) {

                                    openCalling("voice_call");

                                }
                            });
                        else
                            Functions.showAlert(Chat_A.this, getString(R.string.alert), getString(R.string.voice_reah_limit), null);


                    }

                }
                break;


            case R.id.send_btn:

                if (msg.getText().toString().length() > 0) {

                    SendMessage(msg.getText().toString());
                    msg.setText("");
                    showSendingView();

                }


                break;

            case R.id.back_id:
                finish();
                break;

            case R.id.uploadimagebtn:
                selectfile();
                break;

            case R.id.select_camera_2:
                selectfile();
                break;

            case R.id.upload_stiker_btn:
                msg.setHint(R.string.search_sticker);
                mediaType = MediaType.sticker;
                if (gifLayout.getVisibility() != View.VISIBLE) {
                    slideUp();
                }
                getGipy();
                break;

            case R.id.upload_gif_btn:
                msg.setHint(R.string.search_gifs);
                mediaType = MediaType.gif;
                if (gifLayout.getVisibility() != View.VISIBLE) {
                    slideUp();
                }
                getGipy();
                break;

            case R.id.ic_more:
                showDialogue();
                break;

            default:
                break;

        }

    }

    public void openCalling(String type) {
        Intent intent2 = new Intent(Chat_A.this, VideoActivity.class);
        intent2.putExtra("id", receiverid);
        intent2.putExtra("name", receiverName);
        intent2.putExtra("image", receiverPic);
        intent2.putExtra("status", VideoActivity.Call_Send);
        intent2.putExtra("call_type", type);
        intent2.putExtra("roomname", Functions.getRandomString(10));
        startActivity(intent2);
    }

    @Override
    public void onStart() {
        super.onStart();

        Variables.openedChatId = Variables.userId;

        getUserIsBlockOrNot();

        mChats.clear();
        mchatRefReteriving = FirebaseDatabase.getInstance().getReference();
        queryGetchat = mchatRefReteriving.child("chat").child(Variables.userId + "-" + receiverid);

        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    dataSnapshot.getChildrenCount();
                    ChatModel model = dataSnapshot.getValue(ChatModel.class);
                    mChats.add(model);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(mChats.size() - 1);

                } catch (Exception ex) {
                    Log.e("", ex.getMessage());

                }
                changeStatus();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    try {
                        ChatModel model = dataSnapshot.getValue(ChatModel.class);

                        for (int i = mChats.size() - 1; i >= 0; i--) {
                            if (mChats.get(i).getTimestamp().equals(dataSnapshot.child("timestamp").getValue())) {
                                mChats.remove(i);
                                mChats.add(i, model);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", databaseError.getMessage());
            }
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(Variables.userId + "-" + receiverid)) {

                    pBar.setVisibility(View.GONE);
                    mchatRefReteriving.removeEventListener(valueEventListener);
                } else {
                    pBar.setVisibility(View.GONE);
                    mchatRefReteriving.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        queryGetchat.limitToLast(20).addChildEventListener(eventListener);

        mchatRefReteriving.child("chat").addValueEventListener(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queryGetchat != null && eventListener != null)
            queryGetchat.removeEventListener(eventListener);
    }

    public void changeStatus() {

        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(receiverid + "-" + Variables.userId).orderByChild("status").equalTo("0");
        final Query query2 = reference.child("chat").child(Variables.userId + "-" + receiverid).orderByChild("status").equalTo("0");

        final DatabaseReference inbox_change_status_1 = reference.child("Inbox").child(Variables.userId + "/" + receiverid);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if (!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.userId)) {
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time", Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if (!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.userId)) {
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time", Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child("rid").getValue().equals(receiverid)) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("status", "1");
                    inbox_change_status_1.updateChildren(result);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void SendMessage(final String message) {

        // API RUN when send first msg
        matchAPIRunAtFirstMsg();


        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        final String current_user_ref = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + Variables.userId;

        DatabaseReference reference = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String pushid = reference.getKey();
        final HashMap message_user_map = new HashMap<>();
        message_user_map.put("chat_id", pushid);
        message_user_map.put("sender_id", Variables.userId);
        message_user_map.put("receiver_id", receiverid);
        message_user_map.put("sender_name", Variables.userName);

        message_user_map.put("rec_img", "" + receiverPic);  // Rec Pic
        message_user_map.put("pic_url", "");
        message_user_map.put("lat", "");
        message_user_map.put("long", "");

        message_user_map.put("text", message);
        message_user_map.put("type", "text");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(current_user_ref + "/" + pushid, message_user_map);
        user_map.put(chat_user_ref + "/" + pushid, message_user_map);

        rootref.updateChildren(user_map, (databaseError, databaseReference) -> addInbox(formattedDate, message));
    }

    // this method will upload the image in chhat
    public void uploadImage(ByteArrayOutputStream byteArrayOutputStream) {

        //  API RUN when send first msg
        matchAPIRunAtFirstMsg();

        byte[] data = byteArrayOutputStream.toByteArray();

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        DatabaseReference dref = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String key = dref.getKey();
        uploading_image_id = key;
        final String current_user_ref = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chat_user_ref = "chat" + "/" + receiverid + "-" + Variables.userId;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", receiverid);
        my_dummi_pic_map.put("sender_id", Variables.userId);
        my_dummi_pic_map.put("sender_name", Variables.userName);
        my_dummi_pic_map.put("chat_id", key);

        my_dummi_pic_map.put("rec_img", "");
        my_dummi_pic_map.put("pic_url", "none");
        my_dummi_pic_map.put("lat", "");
        my_dummi_pic_map.put("long", "");

        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type", "image");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);

        final StorageReference imagepath = reference.child("images").child(key + ".jpg");
        reference.child("images").child(key + ".jpg").putBytes(data).addOnSuccessListener(taskSnapshot -> imagepath.getDownloadUrl().addOnSuccessListener(uri -> {


            uploading_image_id = "none";

            HashMap message_user_map = new HashMap<>();
            message_user_map.put("receiver_id", receiverid);
            message_user_map.put("sender_id", Variables.userId);
            message_user_map.put("sender_name", Variables.userName);
            message_user_map.put("chat_id", key);

            message_user_map.put("rec_img", "");
            message_user_map.put("pic_url", uri.toString());
            message_user_map.put("lat", "");
            message_user_map.put("long", "");

            message_user_map.put("text", "");
            message_user_map.put("type", "image");
            message_user_map.put("status", "0");
            message_user_map.put("time", "");
            message_user_map.put("timestamp", formattedDate);

            HashMap user_map = new HashMap<>();

            user_map.put(current_user_ref + "/" + key, message_user_map);
            user_map.put(chat_user_ref + "/" + key, message_user_map);

            rootref.updateChildren(user_map, (databaseError, databaseReference) -> addInbox(formattedDate, "send an image"));

        }));
    }

    public void addInbox(String formattedDate, String message) {

        String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
        String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;


        HashMap<String, String> sendermap = new HashMap<>();
        sendermap.put("rid", Variables.userId);
        sendermap.put("name", Variables.userName);
        sendermap.put("msg", message);
        sendermap.put("pic", Variables.userPic);
        sendermap.put("timestamp", formattedDate);
        sendermap.put("date", formattedDate);

        sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
        sendermap.put("status", "0");
        sendermap.put("block", "0");
        sendermap.put("read", "0");
        sendermap.put("like", "0");

        HashMap<String, String> receivermap = new HashMap<>();
        receivermap.put("rid", receiverid);
        receivermap.put("name", receiverName);
        receivermap.put("msg", message);
        receivermap.put("pic", receiverPic);
        receivermap.put("timestamp", formattedDate);
        receivermap.put("date", formattedDate);

        receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
        receivermap.put("status", "1");
        receivermap.put("block", "0");
        receivermap.put("read", "0");
        receivermap.put("like", "0");


        HashMap both_user_map = new HashMap<>();
        both_user_map.put(inbox_sender_ref, receivermap);
        both_user_map.put(inbox_receiver_ref, sendermap);

        adduserToInbox.updateChildren(both_user_map).addOnCompleteListener((OnCompleteListener<Void>) task -> Functions.sendPushNotification(context, "" + receiverid, "send a picture ", "messege"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCamrapermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, Variables.PERMISSION_CAMERA_CODE);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkReadStoragepermission() {
        if (ContextCompat.checkSelfPermission(Chat_A.this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Variables.PERMISSION_READ_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkWriteStoragepermission() {
        if (ContextCompat.checkSelfPermission(Chat_A.this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Variables.PERMISSION_WRITE_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Variables.PERMISSION_CAMERA_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Functions.toastMsg(context, "Tap again");
            } else {
                Functions.toastMsg(context, "camera permission denied");
            }
        }

        if (requestCode == Variables.PERMISSION_READ_DATA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Functions.toastMsg(context, "Tap again");
            }
        }

        if (requestCode == Variables.PERMISSION_WRITE_DATA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Functions.toastMsg(context, "Tap again");
            }
        }


        if (requestCode == Variables.PERMISSION_RECORDING_AUDIO) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Functions.toastMsg(context, "Tap again");
            }
        }


    }

    private void selectfile() {

        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_gallery), getString(R.string.select_video), getString(R.string.cancel)};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle(R.string.select);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {

                    if (checkCamrapermission() && checkWriteStoragepermission())
                        openCameraIntent();


                } else if (options[item].equals(getString(R.string.choose_gallery))) {

                    if (checkReadStoragepermission()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals(getString(R.string.select_video))) {
                    if (checkReadStoragepermission()) {
                        chooseVideo();
                    }
                } else if (options[item].equals(getString(R.string.cancel))) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Functions.logDMsg("" + ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_REQUEST);
            }
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Functions.logDMsg( imageFilePath);

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private void chooseVideo() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, Variables.VEDIO_REQUEST_CODE);
    }

    //on image select activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Functions.logDMsg("In Camera 1");
        if (resultCode == RESULT_OK) {
            Functions.logDMsg("In Camera 2");
            if (requestCode == CAMERA_REQUEST) {
                Functions.logDMsg("In Camera 3");
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
                Functions.logDMsg( imageFilePath);
                InputStream imageStream = null;
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                Functions.logDMsg("ok " + selectedImage);
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Functions.logDMsg("" + e.toString());
                }

                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);


                uploadImage(baos);


            } else if (requestCode == PICK_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                uploadImage(baos);

            } else if (requestCode == Variables.VEDIO_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                Chat_Send_file_Service mService = new Chat_Send_file_Service();

                // API RUN when send first msg
                matchAPIRunAtFirstMsg();

                if (!Functions.isMyServiceRunning(context, mService.getClass())) {
                    Intent mServiceIntent = new Intent(context.getApplicationContext(), mService.getClass());
                    mServiceIntent.setAction("startservice");
                    mServiceIntent.putExtra("uri", "" + selectedImageUri);
                    mServiceIntent.putExtra("type", "video");

                    mServiceIntent.putExtra("sender_id", Variables.userId);
                    mServiceIntent.putExtra("sender_name", Variables.userName);
                    mServiceIntent.putExtra("sender_pic", Variables.userPic);

                    mServiceIntent.putExtra("receiver_id", receiverid);
                    mServiceIntent.putExtra("receiver_name", receiverName);
                    mServiceIntent.putExtra("receiver_pic", receiverPic);

                    mServiceIntent.putExtra("token", token);

                    context.startService(mServiceIntent);
                } else {
                    Functions.toastMsg(context, "Please wait video already in uploading progress");
                }

            }

        }

    }

    //this method will get the big size of image in private chat
    public void openfullsizeImage(ChatModel item) {
        See_Full_Image_F seeFullImageF = new See_Full_Image_F();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putSerializable("image_url", item.getPic_url());
        args.putSerializable("chat_id", item.getChat_id());
        seeFullImageF.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, seeFullImageF).commit();

    }

    //this method will get the big size of image in private chat
    public void openVideo(String path) {
        Play_Video_F playVideoF = new Play_Video_F();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("path", path);
        playVideoF.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, playVideoF).commit();

    }

    public void getLatestUserProfile(String userId) {

        try {
            JSONObject userDataObjs = new JSONObject();
            userDataObjs.put("fb_id", userId);

            ApiRequest.callApi(
                    Chat_A.this,
                    ApiLinks.getUserInfo,
                    userDataObjs,
                    new CallBack() {
                        @Override
                        public void getResponse(String requestType, String resp) {

                            try {

                                JSONObject response = new JSONObject(resp);

                                if (response.getString("code").equals("200")) {

                                    JSONArray msgObj = response.getJSONArray("msg");
                                    JSONObject userInfoObj = msgObj.getJSONObject(0);

                                    receiverPic = userInfoObj.getString("image1");
                                    receiverName = userInfoObj.getString("first_name") + " " + userInfoObj.getString("last_name");
                                    // Method to display user image
                                    displayUserImage(receiverPic, receiverName);

                                }

                            } catch (Exception b) {
                                Functions.logDMsg("" + b.toString());

                            }
                        }
                    }
            );

        } catch (Exception b) {
            Functions.logDMsg("" + b.toString());

        }


    }  // End Method to get user Updated Profile_F.

    // Method to display top Menu Button
    public void showDialogue() {
        // Method to display dialogue for Top Menu
        final CharSequence[] options = {getString(R.string.unmatch_user), "" + blockingText, getString(R.string.cancel)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.select));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.unmatch_user))) {
                dialog.dismiss();
                // if user click on Un Match
                // Calling An API..
                unMatchUserAPI();
            } else if (options[item].equals("" + blockingText)) {
                dialog.dismiss();
                // If User Click On Block user
                blockUserFromChat();
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });

        builder.show();

    } // End Method to show Top Menu

    // Method to Un Block the user
    public void unMatchUserAPI() {
        // Method to Un Block the user

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.userId);
            parameters.put("other_id", receiverid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Calling An API

        ApiRequest.callApi(
                context,
                "" + ApiLinks.unMatch,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String response) {
                        progressDialog.hide();

                        try {
                            listMyMatch.remove(Integer.parseInt(removePositionInMyMatch));

                            matchAdapter.notifyItemRemoved(Integer.parseInt(removePositionInMyMatch));
                            matchAdapter.notifyItemRangeChanged(Integer.parseInt(removePositionInMyMatch), listMyMatch.size());
                            matchAdapter.notifyDataSetChanged();

                        } catch (Exception b) {
                            b.printStackTrace();
                        }

                        try {

                            JSONObject response1 = new JSONObject(response);
                            JSONArray msgArr = response1.getJSONArray("msg");
                            Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));

                            finish();

                        } catch (Exception b) {
                            b.printStackTrace();
                        }
                    }
                }
        );
    } // End Metho to un blok user

    // Method to Block the user From Inbox_F
    public void blockUserFromChat() {
        if (isBlock.equals("0")) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference = databaseReference.child("BlockUser").child(Variables.userId);
            BlockUserModel inboxModel = new BlockUserModel();
            inboxModel.setImgUrl(receiverPic);
            inboxModel.setId(receiverid);
            inboxModel.setName(receiverName);
            databaseReference.child(receiverid).setValue(inboxModel).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Functions.toastMsg(context, "User blocked");
                    isBlock = "1";
                    blockingText = "Un Block User";
                    getUserIsBlockOrNot();
                }
            });
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference = databaseReference.child("BlockUser").child(Variables.userId).child(receiverid);
            databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Functions.toastMsg(context, "User unBlocked");
                        isBlock = "0";
                        blockingText = "Block User";
                        getUserIsBlockOrNot();
                    }

                }
            });

        }


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference inbox_change_status_1 = reference.child("Inbox").child(Variables.userId + "/" + receiverid);
        final DatabaseReference inbox_change_status_2 = reference.child("Inbox").child(receiverid + "/" + Variables.userId);

        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("rid").getValue().equals(receiverid)) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("block", "" + isBlock);
                        inbox_change_status_1.updateChildren(result);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("rid").getValue().equals(receiverid)) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("block", "" + isBlock);
                        inbox_change_status_2.updateChildren(result);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }// End Method to Block the User

    // slide the view from below itself to the current position
    public void slideUp() {
        uploadGifBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_gif));
        gifLayout.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(700);
        gifLayout.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown() {
        msg.setHint(getString(R.string.message));
        msg.setText("");
        uploadGifBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_gif));
        gifLayout.setVisibility(View.GONE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(700);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gifLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gifLayout.startAnimation(animate);

    }

    public void getGipy() {
        if (client == null)
            client = new GPHApiClient(context.getResources().getString(R.string.gif_api_key));

        urlList.clear();
        gipsList = findViewById(R.id.gif_recylerview);

        ImageButton hideGifLayout = findViewById(R.id.hide_gif_layout);
        hideGifLayout.setOnClickListener(v -> slideDown());


        gipsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        gifAdapter = new GifAdapter(context, urlList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

                String s = (String) model;

                sendGif(s);
                slideDown();
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });
        gipsList.setAdapter(gifAdapter);

        client.trending(mediaType, null, null, null, (result, e) -> {
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.getData() != null) {
                    for (Media gif : result.getData()) {

                        urlList.add(gif.getId());
                    }
                    gifAdapter.notifyDataSetChanged();

                } else {
                    Log.e("giphy error", "No results found");
                }
            }
        });
    }


    public void searchGif(String search) {
        /// Gif Search
        client.search(search, mediaType, null, null, null, null, (result, e) -> {
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.getData() != null) {
                    urlList.clear();
                    for (Media gif : result.getData()) {
                        urlList.add(gif.getId());
                        gifAdapter.notifyDataSetChanged();
                    }
                    gipsList.smoothScrollToPosition(0);

                } else {
                    Log.e("giphy error", "No results found");
                }
            }
        });
    }


    // this method will upload the image in chhat
    public void sendGif(String url) {
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        //  API RUN when send first msg
        matchAPIRunAtFirstMsg();

        DatabaseReference dref = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String key = dref.getKey();

        String current_user_ref = "chat" + "/" + Variables.userId + "-" + receiverid;
        String chat_user_ref = "chat" + "/" + receiverid + "-" + Variables.userId;

        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", receiverid);
        message_user_map.put("sender_id", Variables.userId);
        message_user_map.put("sender_name", Variables.userName);
        message_user_map.put("chat_id", key);


        message_user_map.put("rec_img", "");
        message_user_map.put("pic_url", url);
        message_user_map.put("lat", "");
        message_user_map.put("long", "");

        message_user_map.put("text", "");
        message_user_map.put("type", "gif");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("timestamp", formattedDate);

        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, (databaseError, databaseReference) -> addInbox(formattedDate, "Send an gif"));
    }


    public void displayUserImage(String imgLink, String receverName) {

        try {
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(imgLink))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            userimage.getHierarchy().setPlaceholderImage(R.drawable.ic_account_gray);
            userimage.getHierarchy().setFailureImage(R.drawable.ic_account_gray);
            userimage.getHierarchy().setRoundingParams(roundingParams);
            userimage.setController(controller);

            tUserRecName.setText("" + receverName);


        } catch (Exception v) {
            v.printStackTrace();
        }


    }


    public void matchAPIRunAtFirstMsg() {

        if (isMatchApiRun != null) {

            if (isMatchApiRun.equals("1")) {

                Functions.apiFirstchat(context, Variables.userId, receiverid);


                try {
                    listMyMatch.remove(Integer.parseInt(removePositionInMyMatch));

                    matchAdapter.notifyItemRemoved(Integer.parseInt(removePositionInMyMatch));
                    matchAdapter.notifyItemRangeChanged(Integer.parseInt(removePositionInMyMatch), listMyMatch.size());
                    matchAdapter.notifyDataSetChanged();

                } catch (Exception b) {
                    b.printStackTrace();
                }

            }


        }

    }


    public void getUserIsBlockOrNot() {
        ValueEventListener eventListener;
        Query inboxQuery;
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.writechatlayout).setVisibility(View.GONE);
        inboxQuery = rootref.child("BlockUser").child(Variables.userId).child(receiverid);
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    isBlock = "0";
                    blockingText = "Block User";
                    blocked.setVisibility(View.GONE);
                    findViewById(R.id.writechatlayout).setVisibility(View.VISIBLE);

                } else {
                    isBlock = "1";
                    blocked.setVisibility(View.VISIBLE);
                    blockingText = "Un Block User";
                    findViewById(R.id.writechatlayout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);

    }


    private boolean checkPermissionForCameraAndMicrophone() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int resultCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            int resultMic = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
            return resultCamera == PackageManager.PERMISSION_GRANTED &&
                    resultMic == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }

    }

    private void requestPermissionForCameraAndMicrophone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                    shouldShowRequestPermissionRationale(
                            Manifest.permission.RECORD_AUDIO)) {

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                        Variables.CAMERA_MIC_PERMISSION_REQUEST_CODE);
            }
        }
    }


}
