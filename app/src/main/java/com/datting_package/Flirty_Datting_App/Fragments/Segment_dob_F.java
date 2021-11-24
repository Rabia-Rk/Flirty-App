package com.datting_package.Flirty_Datting_App.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.segmentedProgressBar;
import static com.datting_package.Flirty_Datting_App.Activities.Segments_A.viewPager;

public class Segment_dob_F extends RootFragment {

    public static String birthDay, birthMonth, birthdayYear, dobComplete;
    View view;
    Button btn;
    EditText editText, editText1, editText2, editText3, editText4, editText5, editText6, editText7;
    TextView dobTitleId;
    Context context;
    RelativeLayout dobRlId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_dob, null);
        context = getContext();
        editText = (EditText) view.findViewById(R.id.day_ET1_id);
        editText1 = (EditText) view.findViewById(R.id.day_ET2_id);
        editText2 = (EditText) view.findViewById(R.id.month_ET1_id);
        editText3 = (EditText) view.findViewById(R.id.month_ET2_id);
        editText4 = (EditText) view.findViewById(R.id.year_ET1_id);
        editText5 = (EditText) view.findViewById(R.id.year_ET2_id);
        editText6 = (EditText) view.findViewById(R.id.year_ET3_id);
        editText7 = (EditText) view.findViewById(R.id.year_ET4_id);
        dobTitleId = view.findViewById(R.id.dob_title_id);
        btn = (Button) view.findViewById(R.id.dob_btn_id);

        dobRlId = view.findViewById(R.id.dob_RL_id);

        dobTitleId.setText("Hey, " + SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.shareSocialInfo,
                "name"
                ) + " !, Whats your birthday?"

        );

        btn.setOnClickListener(v -> {

            birthDay = editText.getText().toString() + "" + editText1.getText().toString();
            birthMonth = editText2.getText().toString() + "" + editText3.getText().toString();
            birthdayYear = editText4.getText().toString() + "" + editText5.getText().toString() + "" + editText6.getText().toString() + "" + editText7.getText().toString();
            dobComplete = birthDay + "/" + birthMonth + "/" + birthdayYear;


            int age = Functions.getAge(dobComplete);

            if (age > Variables.minAge) {
                viewPager.setCurrentItem(3);
                segmentedProgressBar.setCompletedSegments(3);

            } else {
                Functions.alertDialogue(getContext(), "Info", "You are not under 18");
            }


        });

        TextWatcher tw1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int digitEt1 = Integer.parseInt(s.toString());
                    if (digitEt1 > 3) {

                        editText.setText("");
                        editText.requestFocus();

                    } else {
                        if (s.length() == 1) {
                            editText1.requestFocus();
                        }

                    }


                } catch (Exception b) {
                    b.printStackTrace();
                } // End Catch Statement


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw3 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    int digitEt1 = Integer.parseInt(s.toString());
                    if (digitEt1 > 1) {
                        editText2.setText("");
                        editText2.requestFocus();

                    } else {
                        if (s.length() == 1) {
                            editText3.requestFocus();
                        }

                    }


                } catch (Exception b) {
                    b.printStackTrace();
                } // End Catch Statement


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw4 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    handleValidation(editText3, editText2, editText4);

                } catch (Exception b) {
                    b.printStackTrace();
                } // End Catch Statement

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw5 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw6 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw7 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher tw8 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(tw1);
        editText1.addTextChangedListener(tw2);
        editText2.addTextChangedListener(tw3);
        editText3.addTextChangedListener(tw4);
        editText4.addTextChangedListener(tw5);
        editText5.addTextChangedListener(tw6);
        editText6.addTextChangedListener(tw7);
        editText7.addTextChangedListener(tw8);

        editText.setSelectAllOnFocus(true);
        editText1.setSelectAllOnFocus(true);
        editText2.setSelectAllOnFocus(true);
        editText3.setSelectAllOnFocus(true);
        editText4.setSelectAllOnFocus(true);
        editText5.setSelectAllOnFocus(true);
        editText6.setSelectAllOnFocus(true);
        editText7.setSelectAllOnFocus(true);


        delInSoftKey();
        return view;

    }

    public void delInSoftKey() {

        editText.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                editText.requestFocus();

            }
            return false;

        });


        editText1.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E2");

                editText.requestFocus();
            }
            return false;

        });

        editText2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E3");

                editText1.requestFocus();

            }
            return false;

        });

        editText3.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E4");

                editText2.requestFocus();
            }
            return false;

        });

        editText4.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E5");
                editText3.requestFocus();

            }
            return false;

        });

        editText5.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E6");
                editText4.requestFocus();

            }
            return false;

        });

        editText6.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E7");
                editText5.requestFocus();

            }
            return false;

        });

        editText7.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                Log.e("IME_TEST", "DEL KEY E8");
                editText6.requestFocus();

            }
            return false;

        });


    } // End Delete

    public void handleValidation(EditText etCurrent, EditText etPrevious, EditText etFocusAble) {
        // Handle Validation
        int digitEtCurr = Integer.parseInt(etCurrent.getText().toString());
        int digitEtPre = Integer.parseInt(etPrevious.getText().toString());


        if (digitEtPre == 0) {
            etFocusAble.requestFocus();


        } else {
            if (digitEtCurr > 2) {

                etCurrent.setText("");
                etCurrent.requestFocus();

            } else {
                if (etCurrent.getText().toString().length() == 1) {
                    etFocusAble.requestFocus();
                }

            }
        }


    }


}
