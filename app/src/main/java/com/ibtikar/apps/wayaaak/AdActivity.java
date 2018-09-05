package com.ibtikar.apps.wayaaak;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AdActivity extends BaseActivity {
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
    float density;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        handler = new Handler(Looper.getMainLooper());
        countDownProgBar = findViewById(R.id.progressBar1);
        imBanner = findViewById(R.id.imageView12);
        btnSkipAd = findViewById(R.id.tv_btn_skip_ad);
        loadProgBar = findViewById(R.id.progressBar2);
        density = getResources().getDisplayMetrics().density;

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


        btnSkipAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cdt !=null)
                    cdt.cancel();
                skipped = true;
                startActivity(new Intent(AdActivity.this,HomeActivity.class));
                finish();
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

                                Log.d("location", "onComplete: " + currentLocation.getLatitude() + " , " + currentLocation.getLongitude());
                                //LatLng latLng = new  LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                LatLng latLng = new  LatLng(Double.valueOf("27.067145"), Double.valueOf("27.96655106"));
                                getCity(latLng);
                            }
                            else
                                getbannerAdWithoutLocation();
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

    private void getbannerAdByLocation(String city) {

    /*    setBannerView("http://www.mobileswall.com/wp-content/uploads/2015/12/640-christmas-xmas-gifts-presents-l.jpg");
        startCountDown();
        hideProgressBar(); */


        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(buildUrl(city))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                startCountDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String stringResponse = response.body().string();
                try {
                    JSONObject jsnmainObject = new JSONObject(stringResponse);
                    String imagUrl = null;
                    if (jsnmainObject.getString("status").equals("OK"))
                    {
                        if (density>=1.0 && density<1.5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img2");

                        else if (density>=1.5 && density<2)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img3");

                        else if (density>=2 && density<3)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img4");

                        else if (density>=3 && density<4)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img5");

                        else if (density>=4 && density<5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img5");
                        else if (density>=5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img1");

                        hideProgressBar();
                        setBannerView(imagUrl);
                        startCountDown();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getbannerAdWithoutLocation() {
       /* setBannerView("https://i.pinimg.com/originals/f1/fe/01/f1fe01192e3dd7a7739bb7ced416bcd4.jpg");
        startCountDown();
        hideProgressBar();
*/


        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(buildUrl(null))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                startCountDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String stringResponse = response.body().string();
                try {
                    JSONObject jsnmainObject = new JSONObject(stringResponse);
                    String imagUrl = null;
                    if (jsnmainObject.getString("status").equals("OK"))
                    {
                        if (density>=1.0 && density<1.5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img2");

                        else if (density>=1.5 && density<2)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img3");

                        else if (density>=2 && density<3)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img4");

                        else if (density>=3 && density<4)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img5");

                        else if (density>=4 && density<5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img5");
                        else if (density>=5)
                            imagUrl = jsnmainObject.getJSONObject("details").getString("img1");

                        hideProgressBar();
                        setBannerView(imagUrl);
                        startCountDown();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String buildUrl(String location) {
        if (location == null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(WayaaakAPP.URL_AUOTHORITY)
                    .appendPath("mobile")
                    .appendPath("mybanner");
            String url = builder.build().toString();
            return url;
        }
        else {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(WayaaakAPP.URL_AUOTHORITY)
                    .appendPath("mobile")
                    .appendPath("mybanner")
                    .appendPath(location);
            String url = builder.build().toString();
            return url;
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

                getbannerAdByLocation(adminArea);

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

    public void startCountDown() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                countDownProgBar.setProgress(0);


                final int oneMin= 5 * 1000; // 1 minute in milli seconds

                // CountDownTimer starts with 1 minutes and every onTick is 1 second
                cdt = new CountDownTimer(oneMin, 30) {

                    public void onTick(final long millisUntilFinished) {

                        total++;
                        // handler.post(new Runnable() {
                        //     @Override
                        //   public void run() {
                        countDownProgBar.setProgress((int)total*100/(oneMin/30));
                        //   }
                        // });


                    }

                    public void onFinish() {
                        // DO something when 1 minute is up
                        total++;
                        // handler.post(new Runnable() {
                        // @Override
                        // public void run() {
                        countDownProgBar.setProgress(100);
                        // }
                        // });
                        if (!skipped) {
                            startActivity(new Intent(AdActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                }.start();

            }
        });
    }

    public void setBannerView(final String imgUrl) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (imgUrl.equals("")|| imgUrl == null)
                {}
                else
                    Glide.with(AdActivity.this)
                            .load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imBanner);
            }
        });
    }
    public void hideProgressBar() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadProgBar!=null)
                    loadProgBar.setVisibility(View.GONE);
            }
        });
    }
}
