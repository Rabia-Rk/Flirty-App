package com.datting_package.Flirty_Datting_App.CodeClasses;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Fragments.Swipe_F;
import com.datting_package.Flirty_Datting_App.Models.BlockUserModel;
import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.Models.ProfileInfoModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.Utils.TimeAgo2;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.DOWNLOAD_SERVICE;

public class Functions {

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    public static Dialog dialog;
    static BroadcastReceiver broadcastReceiver;
    static IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    public static void logDMsg(String msg) {
        if(!Variables.isSecureinfo){
            Log.d(Variables.tag, msg);
        }
    }

    public static void toastMsg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

    //Show KEYBOARD
    public static void showKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }


    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static void alertDialogue(final Context context, String title, String msg) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("" + title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());


        AlertDialog alert11 = builder1.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            alert11.show();

        } else {
            alert11.show();
        }


    }

    // this is the delete message diloge which will show after long press in chat message
    public static void showOptions(Context context, CharSequence[] options, final CallBack callback) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, (dialog, item) -> callback.getResponse("resp", "" + options[item]));

        builder.show();

    }


    public static void displayFbAd(final Context context) {

        final String TAG = "Main";
        final com.facebook.ads.InterstitialAd interstitialAd;
        interstitialAd = new com.facebook.ads.InterstitialAd(context, "YOUR_PLACEMENT_ID");
        int num_click_post = SharedPrefrence.getInt(context, SharedPrefrence.uClick);

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
                SharedPrefrence.saveInt(context, 0, SharedPrefrence.uClick);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                SharedPrefrence.saveInt(context, 0, SharedPrefrence.uClick);

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage() + " Code " + adError.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed

                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback

            }
        });

        interstitialAd.loadAd();

        if (num_click_post == Variables.varNumClickToShowAds) {
            interstitialAd.loadAd();


        }

    }

    public static int countNumClick(Context context) {

        int numClickPost = SharedPrefrence.getInt(context, SharedPrefrence.uClick);

        SharedPrefrence.saveInt(context, numClickPost + 1, SharedPrefrence.uClick);

        if (numClickPost >= Variables.varNumClickToShowAds) {
            numClickPost = 1;
            SharedPrefrence.saveInt(context, numClickPost, SharedPrefrence.uClick);

        }

        return numClickPost;

    }

    public static int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        return age;
    }

    public static Drawable getRoundRect() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                25, 25, 25, 25,
                0, 25, 25, 25
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);

        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }

    public static String parseDateToddMMyyyy(String time) {

        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static String getTimeAgoOrg(String dateTime) {

        TimeAgo2 timeAgo2 = new TimeAgo2();
        String myFinalValue = timeAgo2.covertTimeToText(dateTime);

        return myFinalValue;

    }

    public static long downloadDataForChat(Context context, ChatModel item) {

        long downloadReference;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.getPic_url()));

        request.setTitle(item.getSender_name());

        if (item.getType().equals("video")) {
            request.setDescription("Downloading Video");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Hugme/" + item.getChat_id() + ".mp4")));
        } else if (item.getType().equals("audio")) {
            request.setDescription("Downloading Audio");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Hugme/" + item.getChat_id() + ".mp3")));
        } else if (item.getType().equals("pdf")) {

            request.setDescription("Downloading Pdf Document");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Hugme/" + item.getChat_id() + ".pdf")));

        }

        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static void deleteAccount(final Context context, String fbId) {
        Functions.showLoader(context, false, false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fbId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(context,
                "" + ApiLinks.deleteAccount, parameters, (requestType, response) -> {
                    Functions.cancelLoader();
                    try {
                        SharedPrefrence.logoutUser(context);
                        JSONObject response1 = new JSONObject(response);
                        JSONArray msgArr = response1.getJSONArray("msg");
                        Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));

                    } catch (Exception b) {
                        b.printStackTrace();
                    }


                }


        );


    }

    public static void deleteImage(final Context context, String imageLink, final String fb_id) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("image_link", imageLink);
            parameters.put("fb_id", fb_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(context, "" + ApiLinks.deleteImages, parameters, (requestType, response) -> {
                    try {
                        JSONObject response1 = new JSONObject(response);
                        JSONArray msgArr = response1.getJSONArray("msg");
                        Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));

                        getUserInfo(fb_id, context);

                    } catch (Exception b) {
                        b.printStackTrace();
                    }


                }


        );


    }

    public static void reportUser(final Context context, String fbId) {

        Functions.showLoader(context, false, false);

        String myId = Functions.getInfo(context, "fb_id");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fbId);
            parameters.put("my_id", myId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(context,
                "" + ApiLinks.flat_user, parameters, (requestType, response) -> {
                    Functions.cancelLoader();

                    try {
                        JSONObject response1 = new JSONObject(response);
                        JSONArray msgArr = response1.getJSONArray("msg");
                        Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));

                    } catch (Exception b) {
                        b.printStackTrace();
                    }


                }


        );

    }


    public static void blockUser(final Context context, String fbId, String firstName, String imgURl) {

        Functions.showLoader(context, false, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("BlockUser").child(Variables.userId);
        BlockUserModel inboxModel = new BlockUserModel();
        inboxModel.setImgUrl(imgURl);
        inboxModel.setId(fbId);
        inboxModel.setName(firstName);
        databaseReference.child(fbId).setValue(inboxModel).addOnCompleteListener(task -> {
            Functions.cancelLoader();
            if (task.isComplete()) {
                Functions.toastMsg(context, "User blocked");
            }
        });

    }

    public static void unblockUser(final Context context, String fbId) {

        Functions.showLoader(context, false, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("BlockUser").child(Variables.userId).child(fbId);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Functions.cancelLoader();
                if (task.isSuccessful()) {
                    Functions.toastMsg(context, "User unBlocked");
                }

            }
        });

    }


    public static void getUserIsBlockOrNot(String receiverid, final CallBack callback) {
        ValueEventListener eventListener;
        Query inboxQuery;
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();

        inboxQuery = rootref.child("BlockUser").child(Variables.userId).child(receiverid);
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.getResponse("resp", "0");

                } else {
                    callback.getResponse("resp", "1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);

    }


    public static void apiFirstchat(final Context context, String fbId, String effectedId) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fbId);
            parameters.put("effected_id", effectedId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(
                context,
                "" + ApiLinks.firstchat,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String response) {


                    }
                }


        );


    }

    public static String getInfo(Context context, String valWant) {
        String getVal = "";

        getVal = SharedPrefrence.getSocialInfo(context,
                "" + SharedPrefrence.uLoginDetail,
                valWant
        );


        if (valWant.equals("fb_id")) {
            SharedPrefrence.saveString(context, "" + getVal, "" + SharedPrefrence.uId);
        }


        return getVal;


    }

    public static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID));
    }

    public static void addValuesToRv(ArrayList<ProfileInfoModel> list, String[] arrVal) {

        for (int i = 0; i < arrVal.length; i++) {
            list.add(new ProfileInfoModel("" + arrVal[i]));
        } // End For Loop


    }

    public static void showBannerAd(Context context, RelativeLayout layout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.banner_ad_unit_id));


        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rightParams.setMargins(20, 20, 20, 0);
        adView.setLayoutParams(rightParams); //causes layout updat

        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);


    }

    public static void showLoader(Context context, boolean outsideTouch, boolean cancleable) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_loading_layout);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background_10));

        CamomileSpinner loader = dialog.findViewById(R.id.loader);
        loader.start();

        if (!outsideTouch)
            dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            dialog.setCancelable(false);

        dialog.show();

    }

    public static void cancelLoader() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog.cancel();
            }

        } catch (Exception b) {
            b.printStackTrace();
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void getUserInfo(String userId, final Context context) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(
                context,
                ApiLinks.getUserInfo,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String resp) {
                        try {


                            JSONObject response = new JSONObject(resp);


                            if (response.getString("code").equals("200")) {


                                JSONArray msgObj = response.getJSONArray("msg");
                                JSONObject userInfoObj = msgObj.getJSONObject(0);

                                SharedPrefrence.saveString(context, "" + userInfoObj.toString(),
                                        "" + SharedPrefrence.uLoginDetail);
                            }

                        } catch (Exception b) {
                            b.printStackTrace();
                        }


                    }
                }
        );

    }

    public static void showOrHideProfile(String userId, String status, final Context context) {

        Functions.showLoader(context, false, false);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", userId);
            parameters.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(
                context,
                ApiLinks.show_or_hide_profile,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String resp) {
                        try {


                            Functions.cancelLoader();

                            JSONObject response = new JSONObject(resp);


                            if (response.getString("code").equals("200")) {

                                JSONArray msgArr = response.getJSONArray("msg");
                                Functions.toastMsg(context, "" + msgArr.getJSONObject(0).getString("response"));


                            }

                        } catch (Exception b) {
                            b.printStackTrace();
                        }


                    }
                }
        );

    }

    public static void sendPushNotification(final Context context, final String receverid, final String message, final String noti_type) {


        Query inboxQuery;
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();


        inboxQuery = rootref.child("Users").child(receverid);
        inboxQuery.keepSynced(true);


        inboxQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Swipe_F.tokenOtherUser = ds.getValue().toString();

                        JSONObject notimap = new JSONObject();
                        try {
                            notimap.put("title", Functions.getInfo(context, "first_name"));
                            notimap.put("message", message);
                            notimap.put("icon", Functions.getInfo(context, "image1"));
                            notimap.put("tokon", Swipe_F.tokenOtherUser);
                            notimap.put("senderid", "" + Functions.getInfo(context, "fb_id"));
                            notimap.put("receiverid", receverid);
                            notimap.put("action_type", "" + noti_type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ApiRequest.callApi(
                                context,
                                ApiLinks.sendPushNotification,
                                notimap,
                                new CallBack() {
                                    @Override
                                    public void getResponse(String requestType, String resp) {
                                        try {

                                            Functions.cancelLoader();

                                        } catch (Exception b) {
                                            b.printStackTrace();
                                        }


                                    }
                                }
                        );

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (noti_type.equals("message")) {


            JSONObject notimap = new JSONObject();
            try {
                notimap.put("title", Functions.getInfo(context, "first_name"));
                notimap.put("message", message);
                notimap.put("icon", Functions.getInfo(context, "image1"));
                notimap.put("tokon", Swipe_F.tokenOtherUser);
                notimap.put("senderid", "" + Functions.getInfo(context, "fb_id"));
                notimap.put("receiverid", receverid);
                notimap.put("action_type", "" + noti_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ApiRequest.callApi(
                    context,
                    ApiLinks.sendPushNotification,
                    notimap,
                    new CallBack() {
                        @Override
                        public void getResponse(String requestType, String resp) {
                            try {


                                Functions.cancelLoader();

                            } catch (Exception b) {
                                b.printStackTrace();
                            }


                        }
                    }
            );

        }


    }

    public static void openBrowser(final Context context, String url) {

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)

    }

    public static void showlistdialog(Activity activity, String title, final String[] itemList, final TextView textView) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);

        builder.setSingleChoiceItems(itemList, 0, (dialog, item) -> {

            String selection = itemList[item];
            textView.setText(selection);

            dialog.dismiss();
        });

        AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();

    }

    public static void opendatePicker(Context context, final EditText editText) {
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        String dateString = editText.getText().toString();
        if (dateString.equals("")) {
            dateString = "01/01/2000";
        }
        String[] parts = dateString.split("/");

        DatePickerDialog mdiDialog = new DatePickerDialog(context, R.style.datepicker_style, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            Date chosenDate = cal.getTime();
            editText.setText(format.format(chosenDate));

        }, Integer.parseInt(parts[2]), Integer.parseInt(parts[0]) - 1, Integer.parseInt(parts[1]));
        mdiDialog.show();
    }

    public static String getRandomString(int n) {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());

            sb.append(alphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String convertSeconds(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? String.valueOf(h) + " " + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + " " + "min") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + " " + "sec");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
    }

    public static void showAlert(Context context, String title, String description, CallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton("OK", (dialog, id) -> {
            if (callBack != null)
                callBack.getResponse("alert", "OK");
        });
        builder.create();
        builder.show();

    }

    public static void unRegisterConnectivity(Context mContext) {
        if (mContext != null && broadcastReceiver != null)
            mContext.unregisterReceiver(broadcastReceiver);
    }

    public static void registerConnectivity(Context context, final CallBack callback) {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnectedToInternet(context)) {
                    callback.getResponse("alert", "connected");
                } else {
                    callback.getResponse("alert", "disconnected");
                }
            }
        };

        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static Boolean isConnectedToInternet(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network nw = connectivityManager.getActiveNetwork();
                if (nw == null) return false;
                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
                return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e("NetworkChange", e.getMessage());
            return false;
        }
    }


}
