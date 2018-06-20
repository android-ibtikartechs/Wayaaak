package com.ibtikar.apps.wayaaak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class HomeActivity extends AppCompatActivity {
    Spinner citiesSpin, areaSpin, catSpin;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        citiesSpin = findViewById(R.id.cities_spin);
        areaSpin = findViewById(R.id.area_spin);
        catSpin = findViewById(R.id.cat_spin);
        btnSearch = findViewById(R.id.button_search);

    }
}
