package com.ibtikar.apps.wayaaak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ibtikar.apps.wayaaak.Fragment.checkout.Add_Address_Fragment;

public class Track_Activity extends AppCompatActivity {
    ImageView back_ico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        back_ico = findViewById(R.id.toolbar_back_ico);
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, new Add_Address_Fragment(), "").commit();
        back_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void dymmyClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
