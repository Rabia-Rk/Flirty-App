package com.datting_package.Flirty_Datting_App.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.ybs.countrypicker.CountryPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Login_Phone_A extends AppCompatActivity {

    EditText phoneText, digit1, digit2, digit3, digit4, digit5, digit6;
    TextView countrytxt, countrycodetxt, sendtotxt;
    RelativeLayout selectCountry;
    ViewFlipper viewFlipper;
    String phoneNumber;
    String countryIsoCode = "US";
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        fbAuth = FirebaseAuth.getInstance();

        fbAuth.setLanguageCode("en");


        phoneText = findViewById(R.id.phonetxt);

        selectCountry = findViewById(R.id.select_country);
        selectCountry.setOnClickListener(v -> opencountry());

        countrytxt = findViewById(R.id.countrytxt);
        countrycodetxt = findViewById(R.id.countrycodetxt);

        sendtotxt = findViewById(R.id.sendtotxt);

        viewFlipper = findViewById(R.id.viewfillper);
        ccp = findViewById(R.id.ccp);

        countrycodetxt.setText(ccp.getDefaultCountryCodeWithPlus());
        countrytxt.setText(ccp.getDefaultCountryName());
        ccp.registerPhoneNumberTextView(phoneText);
        phoneText.setFocusable(true);
        phoneText.requestFocus();
        Functions.showKeyboard(Login_Phone_A.this);
        codefill();

    }

    //message code fill in edittext and change focus in android
    public void codefill() {

        digit1 = findViewById(R.id.digitone);
        digit2 = findViewById(R.id.digittwo);
        digit3 = findViewById(R.id.digitthree);
        digit4 = findViewById(R.id.digitfour);
        digit5 = findViewById(R.id.digitfive);
        digit6 = findViewById(R.id.digitsix);

        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit1.getText().toString().length() == 0) {
                    digit2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit2.getText().toString().length() == 0) {
                    digit3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit3.getText().toString().length() == 0) {
                    digit4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit4.getText().toString().length() == 0) {
                    digit5.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit5.getText().toString().length() == 0) {
                    digit6.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressLint("WrongConstant")
    public void opencountry() {

        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.setListener((name, code, dialCode, flagDrawableResID) -> {
            // Implement your code here
            countrytxt.setText(name);
            countrycodetxt.setText(dialCode);
            Functions.logDMsg("phoneNumber code : "+code);
            Functions.logDMsg("phoneNumber dialCode : "+dialCode);
            ccp.resetToDefaultCountry();
            ccp.setDefaultCountryUsingNameCode("" + code);
            ccp.setCountryForNameCode("" + code);
            picker.dismiss();
            countryIsoCode = code;
        });
        picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
        picker.show(getSupportFragmentManager(), "Select Country");

    }

    public void nextbtn(View view) {
        if (TextUtils.isEmpty(phoneText.getText().toString())) {
            phoneText.setError("" + getResources().getString(R.string.cant_empty));
            phoneText.requestFocus();
            return;
        }
        if (!ccp.isValid()) {
            phoneText.setError("" + getResources().getString(R.string.invalid_phone_no));
            phoneText.requestFocus();
            return;
        }
        phoneNumber = phoneText.getText().toString();
        if (phoneNumber.charAt(0) == '0') {
            phoneNumber = phoneNumber.substring(1);
        }
        phoneNumber = phoneNumber.replace("+", "");
        phoneNumber = phoneNumber.replace(ccp.getSelectedCountryCode(), "");
        phoneNumber = ccp.getSelectedCountryCodeWithPlus() + phoneNumber;
        phoneNumber = phoneNumber.replace(" ", "");
        phoneNumber = phoneNumber.replace("(", "");
        phoneNumber = phoneNumber.replace(")", "");
        phoneNumber = phoneNumber.replace("-", "");

        sendNumberTofirebase(phoneNumber);

    }


    public void sendNumberTofirebase(String phoneNumber) {
        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {
        Functions.showLoader(this, false, true);
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        Functions.cancelLoader();
                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Functions.cancelLoader();
                        Functions.logDMsg("phoneNumber  e: "+e.getMessage());
                        Functions.logDMsg("phoneNumber  e.getCause: "+e.getCause());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                        Functions.cancelLoader();

                        phoneVerificationId = verificationId;
                        resendToken = token;
                        sendtotxt.setText("Send to ( " + phoneNumber + " )");
                        viewFlipper.setInAnimation(Login_Phone_A.this, R.anim.in_from_right);
                        viewFlipper.setOutAnimation(Login_Phone_A.this, R.anim.out_to_left);
                        viewFlipper.setDisplayedChild(1);

                    }
                };
    }


    public void verifyCode(View view) {
        String code = "" + digit1.getText().toString() + digit2.getText().toString() + digit3.getText().toString() + digit4.getText().toString() + digit5.getText().toString() + digit6.getText().toString();
        if (!code.equals("")) {
            Functions.showLoader(this, false, true);
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            Functions.toastMsg(this, "Enter the Correct varification Code ");
        }


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // get the user info to know that user is already login or not
                        getUserInfo();

                    } else {
                        if (task.getException() instanceof
                                FirebaseAuthInvalidCredentialsException) {

                            Functions.cancelLoader();
                        }
                    }
                });
    }


    public void resendCode(View view) {

        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }


    public void goback1(View view) {
        finish();
    }

    public void goback(View view) {
        viewFlipper.setInAnimation(Login_Phone_A.this, R.anim.in_from_left);
        viewFlipper.setOutAnimation(Login_Phone_A.this, R.anim.out_to_right);
        viewFlipper.setDisplayedChild(0);
    }


    private void getUserInfo() {
        final String phone_no = phoneNumber.replace("+", "");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", phone_no);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(
                this,
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

                                SharedPrefrence.saveString(Login_Phone_A.this, userInfoObj.toString(),
                                        SharedPrefrence.uLoginDetail);


                                SharedPrefrence.saveString(Login_Phone_A.this, phone_no,
                                        SharedPrefrence.uId);

                                SharedPrefrence.removeValueOfKey(Login_Phone_A.this,
                                        SharedPrefrence.shareSocialInfo);

                                enableLocation();


                            } else {

                                Intent intent = new Intent(Login_Phone_A.this, GetUserInfo_A.class);
                                intent.putExtra("id", phone_no);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                finish();

                            }

                        } catch (Exception b) {
                            Functions.toastMsg(Login_Phone_A.this, "" + b.toString());
                        }

                    }
                }
        );


    }


    private void enableLocation() {
        // will move the user for enable location screen
        startActivity(new Intent(this, EnableLocation_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finishAffinity();

    }


}
