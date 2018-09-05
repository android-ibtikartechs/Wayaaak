package com.ibtikar.apps.wayaaak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ibtikar.apps.wayaaak.Fragment.Start_Fragment;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.start_fragment_container, new Start_Fragment(), "Start_Fragment")
                .commit();


    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
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
