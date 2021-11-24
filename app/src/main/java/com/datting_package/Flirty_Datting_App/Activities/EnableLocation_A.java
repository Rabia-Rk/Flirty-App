package com.datting_package.Flirty_Datting_App.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.R;

public class EnableLocation_A extends AppCompatActivity {
    private static int updateInterval = 3000;
    private static int fatestinterval = 3000;
    private static int displacement = 0;
    Context context;
    Button enableLocationBtn;
    IOSDialog iosDialog;
    String userInfo;
    LocationManager locationManager;
    FirebaseStorage storage;
    StorageReference storageReference;
    LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);

        context = EnableLocation_A.this;
        userInfo = SharedPrefrence.getString(context, "" + SharedPrefrence.uLoginDetail);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();

        enableLocationBtn = findViewById(R.id.enable_location_btn);
        enableLocationBtn.setOnClickListener(v -> {
            gpsstatus();

        });

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
                    Functions.toastMsg(context,context.getResources().getString(R.string.permission_granted));
                    gpsstatus();
                } else {
                    Functions.toastMsg(context,context.getResources().getString(R.string.please_grant_permission));
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            iosDialog.cancel();
            getLocationPermission();
            return;
        }

        createLocationRequest();

    }

    public void goNext(Location location) {

        if (location != null) {

            location.getLatitude();
            location.getLongitude();
            String locationString = "" + location.getLatitude() + ", " + location.getLongitude();

            SharedPrefrence.saveString(context, "" + locationString,
                    "" + SharedPrefrence.uLatLng);

            stopLocationUpdates();

            if (userInfo != null) {

                startActivity(new Intent(context, MainActivity.class));
                finish();

            } else {
                startActivity(new Intent(context, Login_A.class));
                finish();
            }


        }

    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(updateInterval);
        mLocationRequest.setFastestInterval(fatestinterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(displacement);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        break;
                    }
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
        super.onDestroy();
    }
}
