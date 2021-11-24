package com.datting_package.Flirty_Datting_App.LiveStreaming.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Adapters.LiveCommentsAdapter;
import com.datting_package.Flirty_Datting_App.Models.LiveCommentModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.LiveStreaming.stats.LocalStatsData;
import com.datting_package.Flirty_Datting_App.LiveStreaming.stats.RemoteStatsData;
import com.datting_package.Flirty_Datting_App.LiveStreaming.stats.StatsData;
import com.datting_package.Flirty_Datting_App.LiveStreaming.ui.VideoGridContainer;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.Utils.KeyboardHeightObserver;
import com.datting_package.Flirty_Datting_App.Utils.KeyboardHeightProvider;

import java.util.ArrayList;
import java.util.HashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LiveActivity extends RtcBaseActivity implements View.OnClickListener, KeyboardHeightObserver {

    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;
    private ImageView mMuteVideoBtn;

    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;

    DatabaseReference rootref;

    String userId, userName, userPicture;
    int userRole;
    EditText messageEdit;
    private KeyboardHeightProvider keyboardHeightProvider;
    RelativeLayout writeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        rootref= FirebaseDatabase.getInstance().getReference();

        Intent bundle=getIntent();
        if(bundle!=null){
            userId =bundle.getStringExtra("user_id");
            userName =bundle.getStringExtra("user_name");
            userPicture =bundle.getStringExtra("user_picture");
            userRole =bundle.getIntExtra("user_role", io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);

        }

        findViewById(R.id.live_btn_mute_video).setOnClickListener(this);

        if(userRole == io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER){

            rootref.child("LiveUsers").child(userId).keepSynced(true);
            rootref.child("LiveUsers").child(userId).onDisconnect().removeValue();

            addFirebaseNode();
            findViewById(R.id.live_btn_mute_video).setOnClickListener(this);

            if(!SharedPrefrence.getBoolean(this,SharedPrefrence.isPuchase))
              starttimer();

        }else {
            listenerNode();
        }

        TextView liveUserName=findViewById(R.id.live_user_name);
        liveUserName.setText(userName);

        initUI();
        initData();

        messageEdit =findViewById(R.id.message_edit);
        writeLayout =findViewById(R.id.write_layout);
        keyboardHeightProvider = new KeyboardHeightProvider(this);


        findViewById(R.id.cross_btn).setOnClickListener(this::onClick);
        findViewById(R.id.swith_camera_btn).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_mute_audio).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_mute_video).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_beautification).setOnClickListener(this::onClick);
        findViewById(R.id.send_btn).setOnClickListener(this::onClick);

        findViewById(R.id.live_activity).post(() -> keyboardHeightProvider.start());

        getCommentData();
    }

    private void initUI() {
        
        initUserIcon();

       boolean isBroadcaster =  (userRole == Constants.CLIENT_ROLE_BROADCASTER);

        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video);
        mMuteVideoBtn.setActivated(isBroadcaster);

        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio);
        mMuteAudioBtn.setActivated(isBroadcaster);

        ImageView beautyBtn = findViewById(R.id.live_btn_beautification);
        beautyBtn.setActivated(true);
        rtcEngine().setBeautyEffectOptions(beautyBtn.isActivated(),
                com.datting_package.Flirty_Datting_App.LiveStreaming.Constants.DEFAULT_BEAUTY_OPTIONS);

        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());

        rtcEngine().setClientRole(userRole);
        if (isBroadcaster) startBroadcast();
    }

    private void initUserIcon() {
        SimpleDraweeView iconView = findViewById(R.id.live_name_board_icon);
        if(userPicture !=null && !userPicture.equals("")) {
            Uri uri = Uri.parse(userPicture);
            iconView.setImageURI(uri);
        }
    }

    private void initData() {
        mVideoDimension = com.datting_package.Flirty_Datting_App.LiveStreaming.Constants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }

    private void startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);
        mVideoGridContainer.addUserVideoSurface(0, surface, true);
        mMuteAudioBtn.setActivated(true);
    }

    private void stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideo(0, true);
        mMuteAudioBtn.setActivated(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(() -> removeRemoteUser(uid));
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        runOnUiThread(() -> renderRemoteUser(uid));
    }

    private void renderRemoteUser(int uid) {
        SurfaceView surface = prepareRtcVideo(uid, false);
        mVideoGridContainer.addUserVideoSurface(uid, surface, false);
    }

    private void removeRemoteUser(int uid) {
        removeRtcVideo(uid, false);
        mVideoGridContainer.removeUserVideo(uid, false);
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {

        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);

    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);

    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));

    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {

        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);

    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {

        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));

    }

    @Override
    public void finish() {
        super.finish();

        statsManager().clearAllData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(userRole == io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER){
          removeNode();
        }else {
            removeListener();
        }
        keyboardHeightProvider.close();

        removeCommentListener();

        stopTimer();

    }

    public void addFirebaseNode(){

        HashMap map=new HashMap();
        map.put("user_id", userId);
        map.put("user_name", userName);
        map.put("user_picture", userPicture);
        rootref.child("LiveUsers").child(userId).setValue(map);

    }

    public void removeNode(){

        rootref.child("LiveUsers").child(userId).removeValue();

    }


    ValueEventListener valueEventListener;
    public void listenerNode(){

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rootref.child("LiveUsers").child(userId).addValueEventListener(valueEventListener);

    }

    public void removeListener(){

        if(rootref!=null && valueEventListener!=null){
            rootref.child("LiveUsers").child(userId).removeEventListener(valueEventListener);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.cross_btn:
                finish();
                break;

            case R.id.swith_camera_btn:
                rtcEngine().switchCamera();
                break;

            case R.id.live_btn_mute_audio:
                if (!mMuteVideoBtn.isActivated()) return;

                rtcEngine().muteLocalAudioStream(view.isActivated());
                view.setActivated(!view.isActivated());
                break;

            case R.id.live_btn_beautification:
                view.setActivated(!view.isActivated());
                rtcEngine().setBeautyEffectOptions(view.isActivated(),
                        com.datting_package.Flirty_Datting_App.LiveStreaming.Constants.DEFAULT_BEAUTY_OPTIONS);
                break;

            case R.id.live_btn_mute_video:
                if (view.isActivated()) {
                    stopBroadcast();
                }
                else {
                    startBroadcast();
                }
                view.setActivated(!view.isActivated());
                break;


            case R.id.send_btn:
                if(!TextUtils.isEmpty(messageEdit.getText().toString())){
                    addMessages();
                  }
                break;


            default:
                break;

        }

    }


    ArrayList<Object> dataList;
    RecyclerView recyclerView;
    LiveCommentsAdapter adapter;
    public void initAdapter(){

        dataList =new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new LiveCommentsAdapter(this, dataList, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });
        recyclerView.setAdapter(adapter);


    }

    public void addMessages(){

        DatabaseReference dref = rootref.child("LiveUsers").child(userId).child("Chat").push();

        final String key=dref.getKey();
        String myId = SharedPrefrence.getString(this,SharedPrefrence.uId);
        String myName = Functions.getInfo(this,"first_name")
                +" "+Functions.getInfo(this,"last_name");
        String myImage = Functions.getInfo(this,"image1");

        HashMap messageUserMap = new HashMap<>();
        messageUserMap.put("id",key);
        messageUserMap.put("user_id", myId);
        messageUserMap.put("user_name", myName);
        messageUserMap.put("user_picture", myImage);
        messageUserMap.put("comment", messageEdit.getText().toString());

        rootref.child("LiveUsers").child(userId).child("Chat").child(key).setValue(messageUserMap);

        messageEdit.setText(null);

    }

    ChildEventListener childEventListener;
    public void getCommentData(){
        initAdapter();

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LiveCommentModel model = dataSnapshot.getValue(LiveCommentModel.class);
                dataList.add(model);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(dataList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        rootref.child("LiveUsers").child(userId).child("Chat").addChildEventListener(childEventListener);
    }

    public void removeCommentListener(){
        if(rootref!=null && childEventListener!=null)
            rootref.child("LiveUsers").child(userId).child("Chat").removeEventListener(childEventListener);

    }



    CountDownTimer countDownTimer;
    public void starttimer(){
        countDownTimer= new CountDownTimer(Variables.MAX_STREMING_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                int streaming=SharedPrefrence.getInt(LiveActivity.this,SharedPrefrence.streamingUsedTime);
                SharedPrefrence.saveInt(LiveActivity.this,streaming+1000,SharedPrefrence.streamingUsedTime);

                if(streaming>Variables.MAX_STREMING_TIME){
                    finish();
                }

            }

            public void onFinish() {
               finish();
            }
        }.start();
    }

    public void stopTimer(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }


    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(writeLayout.getWidth(), writeLayout.getHeight());
        params.bottomMargin = height;
        writeLayout.setLayoutParams(params);

    }

}
