package com.ibtikar.apps.wayaaak;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.SpinerAdapter;
import com.ibtikar.apps.wayaaak.Models.LocationItem;
import com.ibtikar.apps.wayaaak.Models.Response.LocationListResponse;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

public class HomeActivity extends AppCompatActivity {
    Spinner citiesSpin, areaSpin, catSpin;
    Button btnSearch;
    VolleySimple volleySimple;
    String city, area, categ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        citiesSpin = findViewById(R.id.cities_spin);
        areaSpin = findViewById(R.id.area_spin);
        catSpin = findViewById(R.id.cat_spin);
        btnSearch = findViewById(R.id.button_search);
        volleySimple = VolleySimple.getInstance(this);
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
                    SpinerAdapter spinerAdapter = new SpinerAdapter(HomeActivity.this, R.layout.spiner_item_layout, R.id.title, response.getData());
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



}
