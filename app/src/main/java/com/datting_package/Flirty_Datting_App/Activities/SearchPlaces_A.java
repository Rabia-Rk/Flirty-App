package com.datting_package.Flirty_Datting_App.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.datting_package.Flirty_Datting_App.Adapters.PlaceAutocompleteAdapter;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.Models.SavedAddressModel;
import com.datting_package.Flirty_Datting_App.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import static com.datting_package.Flirty_Datting_App.Fragments.Swipe_F.searchPlace;

public class SearchPlaces_A extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, View.OnClickListener {

    Context context;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;
    EditText mSearchEdittext;
    Button closePlaces;
    ImageView mClear;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
        context = SearchPlaces_A.this;

        closePlaces = findViewById(R.id.cancel_places);
        closePlaces.setOnClickListener(view -> finish());

        initViews();
    }

    private void initViews() {


        findViewById(R.id.people_nearby_layout).setOnClickListener(this::onClick);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText) findViewById(R.id.search_et);
        mClear = (ImageView) findViewById(R.id.clear);
        mClear.setOnClickListener(this);


        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.item_placesearch, null);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));


        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count > 0) {
                        mClear.setVisibility(View.VISIBLE);
                        if (mAdapter != null) {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    } else {
                        mClear.setVisibility(View.GONE);
                    }
                    if (mAdapter != null && !s.toString().equals("")) {
                        mAdapter.getFilter().filter(s.toString());
                    }


                } catch (Exception b) {
                    Functions.toastMsg(context, "Writing " + b.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.people_nearby_layout) {
            saveNearbyLocation();
        } else if (v == mClear) {
            mSearchEdittext.setText("");
            if (mAdapter != null) {
                mAdapter.clearList();
            }

        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {


        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);

                SharedPrefrence.saveString(context, "" + mResultList.get(position).description, SharedPrefrence.shareUserSearchPlaceName);

                try {
                    searchPlace.setText("" + mResultList.get(position).description);
                } catch (Exception b) {
                    b.printStackTrace();
                }

                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("https://maps.googleapis.com/maps/api/place/details/json?placeid=");
                    stringBuilder.append(URLEncoder.encode(placeId, "utf8"));
                    stringBuilder.append("&key=");
                    stringBuilder.append(getResources().getString(R.string.google_developer_api_key));

                    RequestQueue rq = Volley.newRequestQueue(this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, stringBuilder.toString(), null, jsonResults -> {
                                String respo = jsonResults.toString();


                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(jsonResults.toString());

                                    JSONObject result = jsonObj.getJSONObject("result");
                                    JSONObject geometry = result.getJSONObject("geometry");
                                    JSONObject location = geometry.getJSONObject("location");


                                    Intent data = new Intent();
                                    data.putExtra("lat", String.valueOf(location.opt("lat")));
                                    data.putExtra("lng", String.valueOf(location.opt("lng")));
                                    setResult(RESULT_OK, data);
                                    finish();

                                    String locationString = location.opt("lat") + ", " + location.opt("lng");

                                    SharedPrefrence.saveString(context, locationString, SharedPrefrence.shareUserSearchPlaceLatLng);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }, error -> {
                                // Handle error
                            });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    rq.getCache().clear();
                    rq.add(jsonObjectRequest);


                } catch (Exception b) {
                    b.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onSavedPlaceClick(ArrayList<SavedAddressModel> mResultList, int position) {
        if (mResultList != null) {
            try {
                Intent data = new Intent();
                data.putExtra("lat", String.valueOf(mResultList.get(position).getLatitude()));
                data.putExtra("lng", String.valueOf(mResultList.get(position).getLongitude()));
                setResult(SearchPlaces_A.RESULT_OK, data);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void saveNearbyLocation() {
        try {
            searchPlace.setText("People Nearby");
        } catch (Exception b) {
            b.printStackTrace();
        }
        SharedPrefrence.removeValueOfKey(this, SharedPrefrence.shareUserSearchPlaceLatLng);
        SharedPrefrence.removeValueOfKey(this, SharedPrefrence.shareUserSearchPlaceName);
        finish();
    }

}
