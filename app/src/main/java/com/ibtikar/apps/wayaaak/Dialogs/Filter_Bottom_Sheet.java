package com.ibtikar.apps.wayaaak.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.CategorySpinerAdapter;
import com.ibtikar.apps.wayaaak.Adapters.SpinerAdapter;
import com.ibtikar.apps.wayaaak.Models.Category;
import com.ibtikar.apps.wayaaak.Models.LocationItem;
import com.ibtikar.apps.wayaaak.Models.Response.CategoryResponse;
import com.ibtikar.apps.wayaaak.Models.Response.LocationListResponse;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.Map;

public class Filter_Bottom_Sheet extends BottomSheetDialogFragment {
    RelativeLayout place_btn, price_btn, catgory_btn;
    LinearLayout place_container, price_container, catgery_container;
    View place_indicator, price_indicator, category_indicator;
    Spinner countries, cities, district, category;
    String country = null, city = null, area = null;
    int cat_id = 0;
    EditText price_from, price_to;
    CheckBox all, offer;
    ImageView close_btn;
    VolleySimple volleySimple;
    TextView clear, submit;
    Map<String, String> dataMap = new HashMap<>();
    onActionListener listener;

    public void setOnActionListner(onActionListener listner) {
        this.listener = listner;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialogsheet, int style) {
        super.setupDialog(dialogsheet, style);
        View contentView;
        contentView = View.inflate(getContext(), R.layout.filter_dialog_fragment, null);
        volleySimple = VolleySimple.getInstance(getContext());
        initView(contentView);
        init();
        listner();
        dialogsheet.setContentView(contentView);
    }

    public void initView(View view) {
        price_btn = view.findViewById(R.id.price_btn);
        price_container = view.findViewById(R.id.price_container);
        price_indicator = view.findViewById(R.id.price_indicator);

        place_btn = view.findViewById(R.id.place_btn);
        place_container = view.findViewById(R.id.place_container);
        place_indicator = view.findViewById(R.id.place_indicator);

        catgory_btn = view.findViewById(R.id.category_btn);
        catgery_container = view.findViewById(R.id.category_container);
        category_indicator = view.findViewById(R.id.category_indicator);

        close_btn = view.findViewById(R.id.filter_close_ico);

        countries = view.findViewById(R.id.country_spin);
        cities = view.findViewById(R.id.cities_spin);
        district = view.findViewById(R.id.district_spin);
        category = view.findViewById(R.id.category_spin);

        clear = view.findViewById(R.id.clear_btn);
        submit = view.findViewById(R.id.submit_btn);

        price_from = view.findViewById(R.id.price_from_edtx);
        price_to = view.findViewById(R.id.price_to_edtx);
        all = view.findViewById(R.id.location_all_ckbox);
        offer = view.findViewById(R.id.offer_ckbox);
    }

    public void listner() {
        price_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price_btn.setBackgroundColor(Color.WHITE);
                place_btn.setBackgroundColor(Color.TRANSPARENT);
                catgory_btn.setBackgroundColor(Color.TRANSPARENT);

                price_indicator.setBackgroundResource(R.color.colorAccent);
                place_indicator.setBackgroundResource(android.R.color.transparent);
                category_indicator.setBackgroundResource(android.R.color.transparent);

                price_container.setVisibility(View.VISIBLE);
                place_container.setVisibility(View.GONE);
                catgery_container.setVisibility(View.GONE);
            }
        });
        place_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_btn.setBackgroundColor(Color.WHITE);
                price_btn.setBackgroundColor(Color.TRANSPARENT);
                catgory_btn.setBackgroundColor(Color.TRANSPARENT);

                place_indicator.setBackgroundResource(R.color.colorAccent);
                price_indicator.setBackgroundResource(android.R.color.transparent);
                category_indicator.setBackgroundResource(android.R.color.transparent);

                place_container.setVisibility(View.VISIBLE);
                price_container.setVisibility(View.GONE);
                catgery_container.setVisibility(View.GONE);
            }
        });
        catgory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catgory_btn.setBackgroundColor(Color.WHITE);
                place_btn.setBackgroundColor(Color.TRANSPARENT);
                price_btn.setBackgroundColor(Color.TRANSPARENT);

                category_indicator.setBackgroundResource(R.color.colorAccent);
                place_indicator.setBackgroundResource(android.R.color.transparent);
                price_indicator.setBackgroundResource(android.R.color.transparent);

                catgery_container.setVisibility(View.VISIBLE);
                place_container.setVisibility(View.GONE);
                price_container.setVisibility(View.GONE);
            }
        });

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataMap.put("all", all.isChecked() ? "1" : "0");
                if (isChecked) {
                    category.setSelection(0);
                    cities.setSelection(0);
                    district.setSelection(0);
                }
            }
        });

        offer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataMap.put("hasoffer", offer.isChecked() ? "1" : "0");
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (!price_from.getText().toString().equals(""))
                    dataMap.put("price_from", price_from.getText().toString());
                if (!price_to.getText().toString().equals(""))
                    dataMap.put("price_to", price_to.getText().toString());
                listener.onSubmit(dataMap);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onReset();
            }
        });

    }

    public void init() {
        initCountry();
        initSlideNav();
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
                                dataMap.put("country", country);
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
                                dataMap.put("city", city);
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
                                area = String.valueOf(response.getData().get(position).getName());
                                if (position == 0) area = null;
                                dataMap.put("area", area);
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

    public void initSlideNav() {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "categories", new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                final CategoryResponse response = new Gson().fromJson(s, CategoryResponse.class);
                if (response.getStatus().equals("OK")) {
                    final Category cat = new Category();
                    cat.setId(0);
                    cat.setName("اختر قسم");
                    response.getData().add(0, cat);
                    CategorySpinerAdapter spinerAdapter = new CategorySpinerAdapter(getContext(), R.layout.spiner_item_layout, R.id.title, response.getData());
                    category.setAdapter(spinerAdapter);
                    category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cat_id = response.getData().get(position).getId();
                            if (position == 0) cat_id = 0;
                            dataMap.put("category", String.valueOf(cat_id));
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

    public interface onActionListener {
        void onSubmit(Map<String, String> map);

        void onReset();
    }
}
