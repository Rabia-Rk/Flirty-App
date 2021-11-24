package com.datting_package.Flirty_Datting_App.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;

import java.util.Locale;

import static com.datting_package.Flirty_Datting_App.CodeClasses.Variables.userGender;

public class Setting_A extends AppCompatActivity implements View.OnClickListener {

    TextView textAppVersion, languageTxt;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = Setting_A.this;
        textAppVersion = findViewById(R.id.t_app_verson_1);

        Functions.displayFbAd(context);


        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            textAppVersion.setText(getResources().getString(R.string.version_txt) + ": " + info.versionName);  // Dynamica Version name


        } catch (Exception b) {
            Functions.toastMsg(context, " " + b.toString());
        }

        languageTxt = findViewById(R.id.language_txt);

        String language = Locale.getDefault().getDisplayLanguage();
        if (SharedPrefrence.getString(Setting_A.this, SharedPrefrence.selectedLanguage) != null)
            language = SharedPrefrence.getString(Setting_A.this, SharedPrefrence.selectedLanguage);

        if (language.equalsIgnoreCase(context.getResources().getString(R.string.english)) || language.equalsIgnoreCase(context.getResources().getString(R.string.arabic)))
            languageTxt.setText(language);
        else
            languageTxt.setText(context.getResources().getString(R.string.english));


        findViewById(R.id.setting_blocked_id).setOnClickListener(this::onClick);

        findViewById(R.id.setting_about_id).setOnClickListener(this::onClick);

        findViewById(R.id.Chat_Inbox).setOnClickListener(this::onClick);

        findViewById(R.id.setting_back_id).setOnClickListener(this::onClick);

        findViewById(R.id.setting_basic_id).setOnClickListener(this::onClick);

        findViewById(R.id.setting_account_id).setOnClickListener(this::onClick);

        findViewById(R.id.setting_account_pref_id).setOnClickListener(this::onClick);

        findViewById(R.id.language_layout).setOnClickListener(this::onClick);

        findViewById(R.id.setting_help_id).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.setting_blocked_id:
                Intent myIntentGender = new Intent(Setting_A.this, BlockedUser_A.class);
                startActivity(myIntentGender);
                break;

            case R.id.setting_help_id:
                try {
                    Functions.openBrowser(context, ApiLinks.App_Privacy_Policy_new);
                } catch (Exception vq) {
                    vq.printStackTrace();
                }
                break;

            case R.id.setting_about_id:
                try {
                    Functions.openBrowser(context, ApiLinks.App_Privacy_Policy_new);
                } catch (Exception vq) {
                    vq.printStackTrace();
                }
                break;

            case R.id.Chat_Inbox:
                startActivity(new Intent(Setting_A.this, ChatInbox_A.class));
                break;

            case R.id.setting_back_id:
                finish();
                break;

            case R.id.setting_basic_id:
                Intent intent = new Intent(Setting_A.this, BasicInfo_A.class);
                intent.putExtra("gender", "" + userGender);
                startActivity(intent);

                break;

            case R.id.setting_account_id:
                startActivity(new Intent(Setting_A.this, Account_A.class));
                break;

            case R.id.setting_account_pref_id:
                try {
                    Functions.openBrowser(context, ApiLinks.App_Privacy_Policy_new);
                } catch (Exception vq) {
                    vq.printStackTrace();
                }
                break;


            case R.id.language_layout:

                final CharSequence[] options = {context.getResources().getString(R.string.english), context.getResources().getString(R.string.arabic)};
                Functions.showOptions(context, options, (requestType, response) -> {

                    SharedPrefrence.saveString(Setting_A.this, response, SharedPrefrence.selectedLanguage);

                    startActivity(new Intent(Setting_A.this, MainActivity.class));

                });

                break;

            default:
                break;
        }
    }
}
