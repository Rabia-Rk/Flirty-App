package com.datting_package.Flirty_Datting_App.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import java.util.Locale;


public class Splashscreen_A extends AppCompatActivity {

    private static int updateInterval = 1000;
    private static int fatestInterval = 1000;
    private static int displacement = 0;
    ImageView iv;
    String userInfo;
    Context context;
    LocationManager locationManager;
    Handler handler;
    Runnable runnable;
    LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        context = Splashscreen_A.this;
        iv = (ImageView) findViewById(R.id.splashscreen_id);
        Variables.varTabChange = 0;


        Variables.height = getResources().getDisplayMetrics().heightPixels;
        Variables.width = getResources().getDisplayMetrics().widthPixels;

        userInfo = SharedPrefrence.getString(context, SharedPrefrence.uLoginDetail);


        handler = new Handler();
        runnable = () -> {
            if (userInfo != null) {

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(context, EnableLocation_A.class));

                } else {


                    final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Functions.toastMsg(context, "GPS is disabled! ");
                        startActivity(new Intent(context, EnableLocation_A.class));
                        finish();
                    } else {

                        gpsstatus();

                    }
                }
            } else {
                startActivity(new Intent(context, Login_A.class));
                finish();
            }
        };
        handler.postDelayed(runnable, 3000);


        if (getIntent().getExtras() != null) {

            final Handler handler1 = new Handler();
            handler1.postDelayed(() -> {
                if (getIntent().getExtras() != null) {
                    String icon = "", recId = "";
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);

                        if (key.equals("receiverid")) {
                            recId = getIntent().getExtras().getString("senderid");
                        } else if (key.equals("icon")) {
                            icon = getIntent().getExtras().getString("icon");
                        }

                        if (value != null) {

                            if (value.equals("messege")) {

                                Intent myIntent = new Intent(context, Chat_A.class);
                                myIntent.putExtra("receiver_id", "" + recId);
                                myIntent.putExtra("receiver_name", "");
                                myIntent.putExtra("receiver_pic", "" + icon);
                                myIntent.putExtra("match_api_run", "0");
                                startActivity(myIntent);


                            }

                        }


                    }
                }

            }, 3000);

        }

        setLanguageLocal();

    }

    public void setLanguageLocal() {

        String language = SharedPrefrence.getString(this, SharedPrefrence.selectedLanguage);
        Locale myLocale = new Locale(Locale.getDefault().getLanguage());
        if (language != null && language.equalsIgnoreCase(getString(R.string.english))) {
            myLocale = new Locale("en");

        } else if (language != null && language.equalsIgnoreCase(getString(R.string.arabic))) {
            myLocale = new Locale("ar");
        }

        if (myLocale.getLanguage().equalsIgnoreCase("en") || myLocale.getLanguage().equalsIgnoreCase("ar")) {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = new Configuration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            onConfigurationChanged(conf);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {
        try {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } catch (Exception b) {
            b.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Functions.toastMsg(context, "Granted");
                    gpsstatus();
                } else {
                    Functions.toastMsg(context, "Please Grant permission");
                }
                break;

            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gpsstatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
        } else {
            getCurrentlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            gpsstatus();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }

        createLocationRequest();

    }


    public void goNext(Location location) {

        if (location != null) {

            location.getLatitude();
            location.getLongitude();
            String locationString = location.getLatitude() + ", " + location.getLongitude();
            String searchPlace = SharedPrefrence.getString(context, "" + SharedPrefrence.shareUserSearchPlaceName);
            if (searchPlace != null) {

                if (searchPlace.equals("People nearby")) {

                    SharedPrefrence.saveString(context, locationString, SharedPrefrence.uLatLng);

                }

            } else {
                SharedPrefrence.saveString(context, locationString, SharedPrefrence.uLatLng);

            }

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(updateInterval);
        mLocationRequest.setFastestInterval(fatestInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(displacement);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        goNext(location);
                        stopLocationUpdates();

                        break;
                    }
                }
                if (userInfo != null) {

                    Intent intent1 = new Intent(context, MainActivity.class);
                    startActivity(intent1);
                    finish();

                }

            }
        };


        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback
                , Looper.myLooper());

    }


    protected void stopLocationUpdates() {
        if (mFusedLocationClient != null && locationCallback != null)
            mFusedLocationClient.removeLocationUpdates(locationCallback);
    }


    @Override
    public void onDestroy() {
        stopLocationUpdates();

        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);

        super.onDestroy();
    }

}
