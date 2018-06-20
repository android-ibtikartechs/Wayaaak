package com.ibtikar.apps.wayaaak;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;
import java.util.List;

public class AdActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    ProgressBar countDownProgBar;
    ImageView imBanner;
    TextView btnSkipAd;
    ProgressBar loadProgBar;
    int total = 0;
    private CountDownTimer cdt;
    private boolean skipped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        countDownProgBar = findViewById(R.id.progressBar1);
        imBanner = findViewById(R.id.imageView12);
        btnSkipAd = findViewById(R.id.tv_btn_skip_ad);
        loadProgBar = findViewById(R.id.progressBar2);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            } else {
              //  init();
                getDeviceLocation();
            }
        } else {
          //  init();
            getDeviceLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                  //  init();
                    getDeviceLocation();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    getbannerAdWithoutLocation();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getDeviceLocation() {
        Log.d("TAG", "getDeviceLocation: getting the current device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {

                com.google.android.gms.tasks.Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "onComplete: location found");
                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation!=null ) {
                                //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                                getbannerAdByLocation();
                                Log.d("location", "onComplete: " + currentLocation.getLatitude() + " , " + currentLocation.getLongitude());
                                LatLng latLng = new  LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                getCity(latLng);
                            }
                        }
                        else {
                            Log.d("TAG", "onComplete: current location is null");
                            //Toast.makeText(this, "unable to find current location", Toast.LENGTH_SHORT).show();
                            getbannerAdWithoutLocation();
                        }
                    }
                });

        } catch (SecurityException e) {
            Log.d("TAG", "getDeviceLocation: SecurityException" + e);
        }

    }

    private void getbannerAdByLocation() {
    }

    private void getbannerAdWithoutLocation() {

    }

    private String getCity(LatLng latLng)
    {
        List<Address> addresses;
        String adminArea = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses.size()!=0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                adminArea = addresses.get(0).getAdminArea();
                Log.d("TAG", "AdminArea: " + adminArea);
                Log.d("TAG", "SubAdminArea: " + addresses.get(0).getSubAdminArea());
                Log.d("TAG", "CountryName: " + addresses.get(0).getCountryName());
                Log.d("TAG", "FeatureName: " + addresses.get(0).getFeatureName());
                Log.d("TAG", "Locality: " + addresses.get(0).getLocality());
                Log.d("TAG", "SubLocality: " + addresses.get(0).getSubLocality());
                Log.d("TAG", "PostalCode: " + addresses.get(0).getPostalCode());
                Log.d("TAG", "Premises: " + addresses.get(0).getPremises());
                Log.d("TAG", "Thoroughfare: " + addresses.get(0).getThoroughfare());
                Log.d("TAG", "SubThoroughfare: " + addresses.get(0).getSubThoroughfare());
                Log.d("TAG", "Url: " + addresses.get(0).getUrl());


              /*  etMapAddress.setText(address);
                Double latt = addresses.get(0).getLatitude();
                lastLatitude = latt.toString();
                Log.d(TAG, "run: "+"latt = "+ lastLatitude);
                Double longitude = addresses.get(0).getLongitude();
                lastLongitude = longitude.toString(); */
            }
            else
                getbannerAdWithoutLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adminArea;
    }
}
