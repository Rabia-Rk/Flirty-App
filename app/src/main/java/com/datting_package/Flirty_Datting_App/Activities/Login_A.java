package com.datting_package.Flirty_Datting_App.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.snackbar.Snackbar;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.Utils.FontHelper;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login_A extends AppCompatActivity {

    private static final int RC_SIGN_IN = 007;
    private static final String EMAIL = "email";
    TextView male, female;
    LoginButton loginButton;
    String name, email, userId, password, imgUrl;
    FrameLayout gmailFrameLayout2, fbFramelayout1;
    Context context;
    Snackbar snackbar;
    private CallbackManager callbackManager;
    private AccessToken mAccessToken;
    private GoogleSignInClient googleSignInClient;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        context = Login_A.this;

        FontHelper.applyFont(getApplicationContext(), findViewById(R.id.Main_frame_Layout), Variables.verdana);

        male = (TextView) findViewById(R.id.male_TV_id);
        female = (TextView) findViewById(R.id.female_TV_id);

        male.setOnClickListener(v -> startActivity(new Intent(Login_A.this, Segments_A.class)));


        findViewById(R.id.phone_login_layout).setOnClickListener(v -> {

            if (checkPermissions()) {
                startActivity(new Intent(Login_A.this, Login_Phone_A.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


        female.setOnClickListener(v -> startActivity(new Intent(Login_A.this, Segments_A.class)));


        fbFramelayout1 = findViewById(R.id.FrameLayout1);
        gmailFrameLayout2 = findViewById(R.id.FrameLayout2);

        googleSignIn();

        fbFramelayout1.setOnClickListener(v -> {
            if (checkPermissions()) {
                loginButton.performClick();
            }
        });

        gmailFrameLayout2.setOnClickListener(v -> {
            if (checkPermissions()) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

        loginButton = findViewById(R.id.login_details_fb_iV_id);
        loginButton.setPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAccessToken = loginResult.getAccessToken();
                getUserProfile(mAccessToken);

            }

            @Override
            public void onCancel() {
                Functions.toastMsg(Login_A.this, "You cancel this.");
            }

            @Override
            public void onError(FacebookException error) {
                Functions.toastMsg(Login_A.this, "err " + error.toString());

            }
        });

        Functions.registerConnectivity(this, (requestType, response) -> {
            if (response.equalsIgnoreCase("disconnected")) {
                snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet. Connect to wifi or cellular network.", Snackbar.LENGTH_INDEFINITE);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(ContextCompat.getColor(Login_A.this, R.color.red));
                snackbar.show();
            } else {
                if (snackbar != null)
                    snackbar.dismiss();
            }
        });

        printKeyHash();


    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    try {
                        String fbUserId = object.getString("id");
                        userId = fbUserId;
                        String imgurl = "https://graph.facebook.com/" + object.getString("id") + "/picture?width=500";
                        String fbName = object.getString("name");
                        String fbEmail = object.getString("email");



                        JSONObject fbDataJson = new JSONObject();
                        fbDataJson.put("name", fbName);
                        fbDataJson.put("email", fbEmail);
                        fbDataJson.put("user_id", fbUserId);
                        fbDataJson.put("img_url", imgurl);

                        SharedPrefrence.saveString(context, fbDataJson.toString(),
                                "" + SharedPrefrence.shareSocialInfo);

                        getuserInfo(userId, fbDataJson);


                        LoginManager.getInstance().logOut();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Functions.toastMsg(Login_A.this, "Error " + e.toString());
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void googleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            try {
                name = acct.getDisplayName();
                userId = acct.getId();
                if (acct.getPhotoUrl() != null) {
                    imgUrl = acct.getPhotoUrl().toString();
                } else {
                    imgUrl = "";
                }

                email = acct.getEmail();
                name = acct.getDisplayName();
                password = acct.getId();

                try {
                    JSONObject msgsJson = new JSONObject();
                    msgsJson.put("name", name);
                    msgsJson.put("email", email);
                    msgsJson.put("user_id", userId);
                    msgsJson.put("img_url", imgUrl);


                    // Check if user ALready member or not
                    getuserInfo(userId, msgsJson);


                } catch (Exception b) {
                    b.printStackTrace();
                }


            } catch (Exception v) {
                Functions.toastMsg(Login_A.this, "" + v.toString());

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

    public boolean checkPermissions() {

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 2);
            }
        } else {

            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                return;

            }
            default:
                break;


        }
    }


    public void getuserInfo(final String user_id, final JSONObject msgsJson) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(
                context,
                ApiLinks.getUserInfo,
                parameters,
                new CallBack() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void getResponse(String requestType, String resp) {
                        try {
                            JSONObject response = new JSONObject(resp);
                            if (response.getString("code").equals("200")) {


                                JSONArray msgObj = response.getJSONArray("msg");
                                JSONObject userInfoObj = msgObj.getJSONObject(0);

                                SharedPrefrence.saveString(context, userInfoObj.toString(),
                                        SharedPrefrence.uLoginDetail);


                                SharedPrefrence.saveString(context, user_id, SharedPrefrence.uId);

                                SharedPrefrence.removeValueOfKey(context, SharedPrefrence.shareSocialInfo);

                                openEnableLocation();


                            } else {
                                SharedPrefrence.saveString(context, msgsJson.toString(), SharedPrefrence.shareSocialInfo);
                                startActivity(new Intent(Login_A.this, Segments_A.class));
                                Functions.unRegisterConnectivity(Login_A.this);
                            }
                        } catch (Exception b) {
                            Functions.toastMsg(context, "" + b.toString());
                        }

                    }
                }
        );
    }

    public void openEnableLocation() {
        context.startActivity(new Intent(context, EnableLocation_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        Functions.unRegisterConnectivity(Login_A.this);
        finish();
    }


    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
