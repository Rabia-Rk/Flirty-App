package com.datting_package.Flirty_Datting_App.CodeClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.datting_package.Flirty_Datting_App.Activities.Splashscreen_A;

import org.json.JSONObject;

import static com.datting_package.Flirty_Datting_App.CodeClasses.Variables.per1;

public class SharedPrefrence {

    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;
    public static String prefName = "Flirty_Datting_App";

    public static String uId = "fb_id_social";
    public static String uLatLng = "user_lat_lng";
    public static String selectedLanguage = "selected_language";

    public static String uClick = "click";
    public static String uDeviceToken = "device_token";
    public static String uLoginDetail = "user_info";

    public static String shareSearchParams = "search_params";

    public static String shareUserSearchPlaceName = "search_place_name";
    public static String shareUserSearchPlaceLatLng = "search_place_lat_lng";

    public static String shareFilterInboxKey = "inbox_filter";
    public static String shareProfileHideOrNot = "profile_hide";
    public static String shareSocialInfo = "social_info";

    public static String isPuchase = "is_puchase";

    public static String boostOnTime = "Boost_On_Time";


    public static String streamingUsedTime = "streaming_used_time";
    public static String videoCallingUsedTime = "video_calling_used_time";
    public static String voiceCallingUsedTime = "voice_calling_used_time";


    public static void initShare(Context context) {
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static void saveString(Context context, String value, String dataKey) {
        if (pref == null || editor == null)
            initShare(context);


        editor.putString(dataKey, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        initShare(context);
        return pref.getString(key, null);

    }

    public static void saveInt(Context context, int value, String dataKey) {
        if (pref == null || editor == null)
            initShare(context);


        editor.putInt(dataKey, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        initShare(context);
        return pref.getInt(key, 0);

    }


    public static void saveBoolean(Context context, boolean value, String dataKey) {
        if (pref == null || editor == null)
            initShare(context);


        editor.putBoolean(dataKey, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        initShare(context);
        return pref.getBoolean(key, false);
    }


    // Clear Key
    public static void removeValueOfKey(Context context, String key) {
        initShare(context);
        pref.edit().remove(key).commit();
    }


    public static void logoutUser(Context context) {
        if (pref == null || editor == null)
            initShare(context);


        pref.edit().remove(uLoginDetail).commit();
        pref.edit().remove(shareSocialInfo).commit();
        pref.edit().remove(uLatLng).commit();
        pref.edit().remove(shareSearchParams).commit();
        pref.edit().remove(shareProfileHideOrNot).commit();
        pref.edit().remove(shareFilterInboxKey).commit();

        Variables.varFilledValNew = 0;
        per1 = 0;
        Intent intent = new Intent(context, Splashscreen_A.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    public static String getSocialInfo(Context context, String key, String whatValWant) {

        String info = SharedPrefrence.getString(context, key);
        String val = "";

        try {
            JSONObject infoObj = new JSONObject(info);
            val = infoObj.getString("" + whatValWant);
        } catch (Exception b) {

        }


        return val;
    }

    public static int calculateCompleteProfile(Context context) {

        String info = SharedPrefrence.getString(context, SharedPrefrence.uLoginDetail);

        Functions.logDMsg("user info : " + info);

        Variables.varFilledValNew = 0;
        per1 = 0;
        int totalVal = 19;


        try {
            JSONObject infoObj = new JSONObject(info);

            infoObj.getString("about_me");
            infoObj.getString("job_title");
            infoObj.getString("gender");
            infoObj.getString("birthday");
            infoObj.getString("age");
            infoObj.getString("company");
            infoObj.getString("school");
            infoObj.getString("living");
            infoObj.getString("children");
            infoObj.getString("smoking");
            infoObj.getString("drinking");
            infoObj.getString("relationship");
            infoObj.getString("sexuality");
            infoObj.getString("image1");
            infoObj.getString("image2");
            infoObj.getString("image3");
            infoObj.getString("image4");
            infoObj.getString("image5");
            infoObj.getString("image6");


            if (!TextUtils.isEmpty(infoObj.getString("job_title")) && !infoObj.getString("job_title").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("age")) && !infoObj.getString("age").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("about_me")) && !infoObj.getString("about_me").equals("0")) {

                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }


            if (!TextUtils.isEmpty(infoObj.getString("gender")) && !infoObj.getString("gender").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("birthday")) && !infoObj.getString("birthday").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("company")) && !infoObj.getString("company").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("school")) && !infoObj.getString("school").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("living")) && !infoObj.getString("living").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }
            if (!TextUtils.isEmpty(infoObj.getString("children")) && !infoObj.getString("children").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }


            if (!TextUtils.isEmpty(infoObj.getString("smoking")) && !infoObj.getString("smoking").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("drinking")) && !infoObj.getString("drinking").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("relationship")) && !infoObj.getString("relationship").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("sexuality")) && !infoObj.getString("sexuality").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }
            if (!TextUtils.isEmpty(infoObj.getString("image1")) && !infoObj.getString("image1").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("image2")) && !infoObj.getString("image2").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }
            if (!TextUtils.isEmpty(infoObj.getString("image3")) && !infoObj.getString("image3").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }


            if (!TextUtils.isEmpty(infoObj.getString("image4")) && !infoObj.getString("image4").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            if (!TextUtils.isEmpty(infoObj.getString("image5")) && !infoObj.getString("image5").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }
            if (!TextUtils.isEmpty(infoObj.getString("image6")) && !infoObj.getString("image6").equals("0")) {
                Variables.varFilledValNew = Variables.varFilledValNew + 1;
            }

            float percantage1 = ((float) Variables.varFilledValNew / (float) totalVal) * 100;

            Functions.logDMsg("user info varFilledValNew : " + Variables.varFilledValNew);
            Functions.logDMsg("user info percantage1 : " + percantage1);

            per1 = Integer.parseInt("" + (int) percantage1);
            Functions.logDMsg("user info per1 : " + per1);

        } catch (Exception b) {
            b.printStackTrace();
        }


        return per1;
    }


}
