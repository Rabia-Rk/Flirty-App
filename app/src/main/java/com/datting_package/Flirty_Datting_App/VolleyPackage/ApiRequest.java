package com.datting_package.Flirty_Datting_App.VolleyPackage;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ApiRequest {


    public static void callApi(final Context context, final String API_link,
                               final JSONObject jsonObject, final CallBack callBack) {


        Functions.logDMsg( API_link);
        if (jsonObject != null)
            Functions.logDMsg( jsonObject.toString());

        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObj = new JsonObjectRequest(API_link, jsonObject, response -> {
                Functions.logDMsg( response.toString());

                if (callBack != null)
                    callBack.getResponse("Post", response.toString());
            }, error -> {
                if (callBack != null)
                    callBack.getResponse("Post", error.toString());

            });

            queue.add(jsonObj);
            jsonObj.setRetryPolicy(new DefaultRetryPolicy(
                    Variables.MY_API_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void sendNotification(final Context context, final JSONObject jsonObject, final CallBack callback) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", jsonObject,
                response -> {



                    if (callback != null)
                        callback.getResponse("Post", response.toString());
                }, error -> {

            if (callback != null)
                callback.getResponse("Post", error.toString());

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Authorization", "key=" + context.getResources().getString(R.string.firebase_server_key));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        if (Functions.isConnectedToInternet(context)) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjReq);
        }
    }


}
