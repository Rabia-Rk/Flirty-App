package com.datting_package.Flirty_Datting_App.Utils;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.datting_package.Flirty_Datting_App.LiveStreaming.Constants;
import com.datting_package.Flirty_Datting_App.LiveStreaming.rtc.AgoraEventHandler;
import com.datting_package.Flirty_Datting_App.LiveStreaming.rtc.EngineConfig;
import com.datting_package.Flirty_Datting_App.LiveStreaming.rtc.EventHandler;
import com.datting_package.Flirty_Datting_App.LiveStreaming.stats.StatsManager;
import com.datting_package.Flirty_Datting_App.LiveStreaming.utils.FileUtil;
import com.datting_package.Flirty_Datting_App.LiveStreaming.utils.PrefManager;
import com.datting_package.Flirty_Datting_App.R;

import io.agora.rtc.RtcEngine;

public class HugMeApplication extends Application {

    private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fresco.initialize(this);
        AudienceNetworkAds.initialize(this);

        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }


        initConfig();
    }


    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RtcEngine.destroy();
    }


}
