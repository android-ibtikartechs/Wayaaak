package com.ibtikar.apps.wayaaak;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.SpinerAdapter;
import com.ibtikar.apps.wayaaak.Models.LocationItem;
import com.ibtikar.apps.wayaaak.Models.Response.LocationListResponse;
import com.ibtikar.apps.wayaaak.Tools.SlideNavigation;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.IOException;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private SlidingRootNav slidingRootNav;
    Spinner citiesSpin, areaSpin, catSpin;
    Button btnSearch;
    VolleySimple volleySimple;
    String city, area, categ;
    LinearLayout loutLocation;
    TextView tvCityLocation;
    Toolbar toolbar;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        citiesSpin = findViewById(R.id.cities_spin);
        areaSpin = findViewById(R.id.area_spin);
        catSpin = findViewById(R.id.cat_spin);
        btnSearch = findViewById(R.id.button_search);
        volleySimple = VolleySimple.getInstance(this);
        loutLocation = findViewById(R.id.lout_location);
        tvCityLocation = findViewById(R.id.tv_city);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuLayout(R.layout.screen_navigation)
                .inject();
        SlideNavigation slideNavigation = new SlideNavigation(R.id.main_content);
        slideNavigation.initSlideMenu(HomeActivity.this, getSupportFragmentManager(), slidingRootNav);

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

        initCities(64);
        initCatSpin();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.getStartIntent(HomeActivity.this, categ,city,area));

            }
        });
    }

    public void initCities(int id) {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "cities/" + id, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                final LocationListResponse response = new Gson().fromJson(s, LocationListResponse.class);
                if (response.getStatus().equals("OK")) {
                    LocationItem country1 = new LocationItem();
                    country1.setId(0);
                    country1.setName("المحافظة");
                    response.getData().add(0, country1);
                    SpinerAdapter spinerAdapter = new SpinerAdapter(HomeActivity.this, R.layout.spiner_item_layout, R.id.title, response.getData());
                    citiesSpin.setAdapter(spinerAdapter);
                    citiesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(response.getData().get(position).getName());

                            if (response.getData().get(position).getId() != 0) {
                                city = String.valueOf(response.getData().get(position).getId());
                                if (position == 0) city = null;
                                initDistricts(response.getData().get(position).getId());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    public void initDistricts(int id) {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "districts/" + id, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                final LocationListResponse response = new Gson().fromJson(s, LocationListResponse.class);
                if (response.getStatus().equals("OK")) {
                    LocationItem country1 = new LocationItem();
                    country1.setId(0);
                    country1.setName("الحى/المدينة");
                    response.getData().add(0, country1);
                    SpinerAdapter spinerAdapter = new SpinerAdapter(HomeActivity.this, R.layout.spiner_item_layout, R.id.title, response.getData());
                    areaSpin.setAdapter(spinerAdapter);
                    areaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //System.out.println(response.getData().get(position).getName());
                            Log.d("TAG", "onItemSelected: " + String.valueOf(response.getData().get(position).getId()));

                            if (response.getData().get(position).getId() != 0) {
                                area = String.valueOf(response.getData().get(position).getId());
                                //ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this, "انتظر من فضلك", "", false, false);
                                System.out.println(response.getData().get(position).getId());
                                if (position == 0) categ = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void initCatSpin() {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "categories", new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                final LocationListResponse response = new Gson().fromJson(s, LocationListResponse.class);
                if (response.getStatus().equals("OK")) {
                    LocationItem country1 = new LocationItem();
                    country1.setId(0);
                    country1.setName("الفئة");
                    response.getData().add(0, country1);
                    SpinerAdapter spinerAdapter = new SpinerAdapter(HomeActivity.this, R.layout.spiner_item_layout, R.id.tv_item, response.getData());
                    catSpin.setAdapter(spinerAdapter);
                    catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(response.getData().get(position).getName());

                            if (response.getData().get(position).getId() != 0) {
                                categ = String.valueOf(response.getData().get(position).getId());
                                Log.d("TAG", "onItemSelected: " + categ);
                                //ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this, "انتظر من فضلك", "", false, false);
                                if (position == 0) categ = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
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
                    //getbannerAdWithoutLocation();
                    loutLocation.setVisibility(View.GONE);
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

                            Log.d("location", "onComplete: " + currentLocation.getLatitude() + " , " + currentLocation.getLongitude());
                            LatLng latLng = new  LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //LatLng latLng = new  LatLng(Double.valueOf("27.067145"), Double.valueOf("27.96655106"));
                            getCity(latLng);
                        }
                        else
                            loutLocation.setVisibility(View.GONE);
                            //getbannerAdWithoutLocation();
                    }
                    else {
                        Log.d("TAG", "onComplete: current location is null");
                        //Toast.makeText(this, "unable to find current location", Toast.LENGTH_SHORT).show();
                        //getbannerAdWithoutLocation();
                        loutLocation.setVisibility(View.GONE);
                    }
                }
            });

        } catch (SecurityException e) {
            Log.d("TAG", "getDeviceLocation: SecurityException" + e);
        }

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

                //getbannerAdByLocation(adminArea);
                tvCityLocation.setText(adminArea);
              /*  etMapAddress.setText(address);
                Double latt = addresses.get(0).getLatitude();
                lastLatitude = latt.toString();
                Log.d(TAG, "run: "+"latt = "+ lastLatitude);
                Double longitude = addresses.get(0).getLongitude();
                lastLongitude = longitude.toString(); */
            }
            else
                loutLocation.setVisibility(View.GONE);
                //getbannerAdWithoutLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adminArea;
    }

}
