package com.datting_package.Flirty_Datting_App.LiveStreaming.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;


public class StreamingMain_A extends BaseActivity {
    private static final String TAG = StreamingMain_A.class.getSimpleName();
    private static final int MIN_INPUT_METHOD_HEIGHT = 200;
    private static final int ANIM_DURATION = 200;

    // Permission request code of any integer value
    private static final int PERMISSION_REQ_CODE = 1 << 4;
    String userId, userName, userPicture;
    int userRole;
    private String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Rect mVisibleRect = new Rect();
    private int mLastVisibleHeight = 0;
    private RelativeLayout mBodyLayout;
    private int mBodyDefaultMarginTop;
    private EditText mTopicEdit;
    private TextView mStartBtn;
    private ImageView mLogo;
    private Animator.AnimatorListener mLogoAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            // Do nothing
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mLogo.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mLogo.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            // Do nothing
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mStartBtn.setEnabled(!TextUtils.isEmpty(editable));
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutObserverListener =
            () -> checkInputMethodWindowState();

    private void checkInputMethodWindowState() {
        getWindow().getDecorView().getRootView().getWindowVisibleDisplayFrame(mVisibleRect);
        int visibleHeight = mVisibleRect.bottom - mVisibleRect.top;
        if (visibleHeight == mLastVisibleHeight) return;

        boolean inputShown = mDisplayMetrics.heightPixels - visibleHeight > MIN_INPUT_METHOD_HEIGHT;
        mLastVisibleHeight = visibleHeight;

        if (inputShown) {
            if (mLogo.getVisibility() == View.VISIBLE) {
                mBodyLayout.animate().translationYBy(-mLogo.getMeasuredHeight())
                        .setDuration(ANIM_DURATION).setListener(null).start();
                mLogo.setVisibility(View.INVISIBLE);
            }
        } else if (mLogo.getVisibility() != View.VISIBLE) {
            mBodyLayout.animate().translationYBy(mLogo.getMeasuredHeight())
                    .setDuration(ANIM_DURATION).setListener(mLogoAnimListener).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_streaming);

        Intent bundle = getIntent();
        if (bundle != null) {
            userId = bundle.getStringExtra("user_id");
            userName = bundle.getStringExtra("user_name");
            userPicture = bundle.getStringExtra("user_picture");
            userRole = bundle.getIntExtra("user_role", io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);

        }
        initUI();
    }

    private void initUI() {
        mBodyLayout = findViewById(R.id.middle_layout);
        mLogo = findViewById(R.id.main_logo);

        mTopicEdit = findViewById(R.id.topic_edit);
        mTopicEdit.addTextChangedListener(mTextWatcher);


        mStartBtn = findViewById(R.id.start_broadcast_button);
        if (TextUtils.isEmpty(mTopicEdit.getText())) mStartBtn.setEnabled(false);

        mTopicEdit.setText(userName);
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        adjustViewPositions();
    }

    private void adjustViewPositions() {
        // Setting btn move downward away the status bar
        ImageView settingBtn = findViewById(R.id.setting_button);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) settingBtn.getLayoutParams();
        param.topMargin += mStatusBarHeight;
        settingBtn.setLayoutParams(param);

        // Logo is 0.48 times the screen width
        param = (RelativeLayout.LayoutParams) mLogo.getLayoutParams();
        int size = (int) (mDisplayMetrics.widthPixels * 0.48);
        param.width = size;
        param.height = size;
        mLogo.setLayoutParams(param);

        // Bottom margin of the main body should be two times it's top margin.
        param = (RelativeLayout.LayoutParams) mBodyLayout.getLayoutParams();
        param.topMargin = (mDisplayMetrics.heightPixels -
                mBodyLayout.getMeasuredHeight() - mStatusBarHeight) / 3;
        mBodyLayout.setLayoutParams(param);
        mBodyDefaultMarginTop = param.topMargin;

        // The width of the start button is roughly 0.72
        // times the width of the screen
        mStartBtn = findViewById(R.id.start_broadcast_button);
        param = (RelativeLayout.LayoutParams) mStartBtn.getLayoutParams();
        param.width = (int) (mDisplayMetrics.widthPixels * 0.72);
        mStartBtn.setLayoutParams(param);
    }

    public void onSettingClicked(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void onStartBroadcastClicked(View view) {
        checkPermission();
    }

    private void checkPermission() {
        boolean granted = true;
        for (String per : permissions) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            resetLayoutAndForward();
        } else {
            requestPermissions();
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                resetLayoutAndForward();
            } else {
                toastNeedPermissions();
            }
        }
    }

    private void resetLayoutAndForward() {
        closeImeDialogIfNeeded();
        gotoRoleActivity();
    }

    private void closeImeDialogIfNeeded() {
        InputMethodManager manager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mTopicEdit.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void gotoRoleActivity() {

        Intent intent = new Intent();
        intent.putExtra("user_id", userId);
        intent.putExtra("user_name", userName);
        intent.putExtra("user_picture", userPicture);
        intent.putExtra("user_role", userRole);
        intent.putExtra(com.datting_package.Flirty_Datting_App.LiveStreaming.Constants.KEY_CLIENT_ROLE, userRole);
        intent.setClass(StreamingMain_A.this, LiveActivity.class);

        if (userRole == io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER && !SharedPrefrence.getBoolean(this, SharedPrefrence.isPuchase)) {

            if (Variables.STREAMING_LIMIT) {
                int stremingTime = SharedPrefrence.getInt(StreamingMain_A.this, SharedPrefrence.streamingUsedTime);
                if (stremingTime < Variables.MAX_STREMING_TIME)
                    Functions.showAlert(this, "Alert", "For demo purpose we only allow to " + Variables.MAX_STREMING_TIME / 1000 + "s live streaming", new CallBack() {
                        @Override
                        public void getResponse(String requestType, String response) {

                            config().setChannelName(userId);
                            startActivity(intent);
                            finish();
                        }
                    });
                else
                    Functions.showAlert(this, "Alert", "You have reached out of your streaming limit.", null);
            } else {
                config().setChannelName(userId);
                startActivity(intent);
                finish();
            }


        } else {
            config().setChannelName(userId);
            startActivity(intent);
            finish();
        }

    }

    private void toastNeedPermissions() {
        Functions.toastMsg(this, this.getResources().getString(R.string.need_necessary_permissions));
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUI();
        registerLayoutObserverForSoftKeyboard();
    }

    private void resetUI() {
        resetLogo();
        closeImeDialogIfNeeded();
    }

    private void resetLogo() {
        mLogo.setVisibility(View.VISIBLE);
        mBodyLayout.setY(mBodyDefaultMarginTop);
    }

    private void registerLayoutObserverForSoftKeyboard() {
        View view = getWindow().getDecorView().getRootView();
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(mLayoutObserverListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLayoutObserverForSoftKeyboard();
    }

    private void removeLayoutObserverForSoftKeyboard() {
        View view = getWindow().getDecorView().getRootView();
        view.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutObserverListener);
    }

}
