package com.ibtikar.apps.wayaaak.Fragment.checkout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.SpinerAdapter;
import com.ibtikar.apps.wayaaak.Models.AddressBook;
import com.ibtikar.apps.wayaaak.Models.LocationItem;
import com.ibtikar.apps.wayaaak.Models.Response.LocationListResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Address_Fragment extends Fragment implements OnMapReadyCallback {
    TextView contiune, new_Address, list_Address;
    Spinner countries, cities, district;
    String country = null, city = null, area = null;
    VolleySimple volleySimple;
    User user;


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    EditText name, phone, search_txt;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    Marker marker;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult result) {
            super.onLocationResult(result);
            //mCurrentLocation = locationResult.getLastLocation();
            mCurrentLocation = result.getLocations().get(0);
            if (marker != null) {
                marker.remove();
            }
/*            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude())).title("Your Selection")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
*/
            moveCamera(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                    DEFAULT_ZOOM);
            System.out.println("location ok");

            //   mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }

        @Override
        public void onLocationAvailability(LocationAvailability availability) {
            //boolean isLocation = availability.isLocationAvailable();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_add_address, container, false);
        volleySimple = VolleySimple.getInstance(getContext());
        user = WayaaakAPP.getUserLoginInfo(getContext());
        getLocationPermission();
        initView(rootview);
        listener();
        initCountry();


        return rootview;
    }


    public void initView(View view) {
        new_Address = view.findViewById(R.id.address_new_txt);
        list_Address = view.findViewById(R.id.address_list_txt);
        contiune = view.findViewById(R.id.continue_btn);
        countries = view.findViewById(R.id.country_spin);
        cities = view.findViewById(R.id.cities_spin);
        district = view.findViewById(R.id.area_spin);
        search_txt = view.findViewById(R.id.input_search);
        phone = view.findViewById(R.id.phone_edtx);
        name = view.findViewById(R.id.name_edtx);

        final NestedScrollView mainScrollView = view.findViewById(R.id.scrollView);
        (view.findViewById(R.id.transparent_image)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });


    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void moveCamera(LatLng latLng, float zoom) {
        //   Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    private void geoLocate(ProgressDialog progressDialog, String word) {
        Log.d("MAp", "geoLocate: geolocating");

        String searchString = word;

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM);
            System.out.println("location ok");
            if (marker != null) {
                marker.remove();
            }
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),
                    address.getLongitude())).title(address.getAddressLine(0))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            Log.d("MAp", "geoLocate: found a location: " + address.toString());
            //  Toast.makeText(getContext(), address.toString(), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "No result for " + word, Toast.LENGTH_SHORT).show();

        }
        progressDialog.dismiss();
    }

    private void getDeviceLocation() {
        // Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(getContext());

        mLocationRequest = new LocationRequest();
        //  mLocationRequest.setInterval(10000);
        //  mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setNumUpdates(1);
        // mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();

        try {
            if (mLocationPermissionsGranted) {

                Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
                locationResponse.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        // Log.e("Response", "Successful acquisition of location information!!");
                        //
                        System.out.println("setting ok");
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }
                });

                locationResponse.addOnFailureListener(getActivity(), new

                        OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                int statusCode = ((ApiException) e).getStatusCode();
                                switch (statusCode) {
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        e.printStackTrace();
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        e.printStackTrace();
                                        //  Log.e("onFailure", errorMessage);
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            // Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        // Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        //  Log.d(TAG, "onMapReady: map is ready");
        googleMap = gMap;

        if (mLocationPermissionsGranted) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            getDeviceLocation();

            listener();

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                    temp.setLatitude(point.latitude);
                    temp.setLongitude(point.longitude);
                    mCurrentLocation = temp;

                    //remove previously placed Marker
                    if (marker != null) {
                        marker.remove();
                    }

                    //place marker where user just clicked
                    Toast.makeText(getContext(), "location selected..", Toast.LENGTH_SHORT).show();
                    marker = googleMap.addMarker(new MarkerOptions().position(point).title("Your Selection")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    //    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //   googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            });
        }
    }

    private void getLocationPermission() {
        //  Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void listener() {
        list_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, new List_Address_Fragment(), "").commit();
            }
        });

        contiune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !phone.getText().toString().equals("") && area != null) {
                    if (mCurrentLocation != null) {
                        addAddress();
                    } else {
                        Toast.makeText(getContext(), "أختر موقعك من الخريطة", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all boxes", Toast.LENGTH_LONG).show();
                }

            }
        });
        search_txt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
                    geoLocate(progressDialog, search_txt.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search_txt.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public void initCountry() {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "countries", new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                final LocationListResponse response = new Gson().fromJson(s, LocationListResponse.class);
                if (response.getStatus().equals("OK")) {
                    LocationItem country1 = new LocationItem();
                    country1.setId(0);
                    country1.setName("الدولة");
                    response.getData().add(0, country1);
                    SpinerAdapter spinerAdapter = new SpinerAdapter(getContext(), R.layout.spiner_item_layout, R.id.title, response.getData());
                    countries.setAdapter(spinerAdapter);
                    countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(response.getData().get(position).getName());

                            if (response.getData().get(position).getId() != 0) {
                                country = String.valueOf(response.getData().get(position).getName());
                                if (position == 0) country = null;
                                initCities(response.getData().get(position).getId());
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
                    SpinerAdapter spinerAdapter = new SpinerAdapter(getContext(), R.layout.spiner_item_layout, R.id.title, response.getData());
                    cities.setAdapter(spinerAdapter);
                    cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(response.getData().get(position).getName());

                            if (response.getData().get(position).getId() != 0) {
                                city = String.valueOf(response.getData().get(position).getName());
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
                    SpinerAdapter spinerAdapter = new SpinerAdapter(getContext(), R.layout.spiner_item_layout, R.id.title, response.getData());
                    district.setAdapter(spinerAdapter);
                    district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(response.getData().get(position).getName());

                            if (response.getData().get(position).getId() != 0) {
                                area = String.valueOf(response.getData().get(position).getId());
                                ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
                                geoLocate(progressDialog, response.getData().get(position).getName());
                                if (position == 0) area = null;
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

    public void addAddress() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("district", area);
        map.put("longtude", String.valueOf(mCurrentLocation.getLongitude()));
        map.put("latitude", String.valueOf(mCurrentLocation.getLatitude()));
        map.put("postalcode", "11111");
        map.put("name", name.getText().toString());
        map.put("mobile", phone.getText().toString());
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "addaddressbook", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                AddressBook addressBook = new Gson().fromJson(s, AddressBook.class);
                Payment_Fragment payment_fragment = new Payment_Fragment();
                payment_fragment.setAddress(addressBook);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.checkout_fragment_container, payment_fragment, "payment_fragment")
                        .addToBackStack("payment_fragment")
                        .commit();

            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            //        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    //  Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}
