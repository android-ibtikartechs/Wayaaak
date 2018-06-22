package com.ibtikar.apps.wayaaak.Tools;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Fragment.Favourite_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Login_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Profile_Fragment;
import com.ibtikar.apps.wayaaak.MainActivity;
import com.ibtikar.apps.wayaaak.R;
import com.yarolegovich.slidingrootnav.SlidingRootNav;


/**
 * Created by hosamazzam on 22/02/17.
 */

public class SlideNavigation {
    int fragmnetholder;
    TextView wishes, track, profile, exit, home;


    public SlideNavigation(int fragmnetholder) {
        this.fragmnetholder = fragmnetholder;
    }


    public void initSlideMenu(final Activity activity, final FragmentManager fragmentManager, final SlidingRootNav slidingRootNav) {
        wishes = activity.findViewById(R.id.wishes_nav_txt);
        profile = activity.findViewById(R.id.profile_nav_txt);
        track = activity.findViewById(R.id.track_nav_txt);
        exit = activity.findViewById(R.id.exit_nav_txt);
        home = activity.findViewById(R.id.home_nav_txt);
        if (!WayaaakAPP.getUserLoginState(activity)) {
            exit.setText("تسجيل الدخول");
        }
        if (activity instanceof MainActivity)
            home.setVisibility(View.GONE);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();
                activity.startActivity(MainActivity.getStartIntentTransition(activity));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                if (WayaaakAPP.getUserLoginState(activity)) {
                    fragmentManager
                            .beginTransaction()
                            .replace(fragmnetholder, new Profile_Fragment(), "Profile_Fragment")
                            .addToBackStack("Profile_Fragment")
                            .commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                            .addToBackStack("Login_Fragment")
                            .commit();
                }
            }
        });

        wishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                if (WayaaakAPP.getUserLoginState(activity)) {
                    fragmentManager
                            .beginTransaction()
                            .replace(fragmnetholder, new Favourite_Fragment(), "Favourite_Fragment")
                            .addToBackStack("Favourite_Fragment")
                            .commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                            .addToBackStack("Login_Fragment")
                            .commit();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exit.getText().toString().equals("تسجيل الخروج")) {
                    WayaaakAPP.unRegisterUserLogin(activity);
                    activity.finish();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                } else {
                    slidingRootNav.closeMenu();
                    fragmentManager
                            .beginTransaction()
                            .add(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                            .addToBackStack("Login_Fragment")
                            .commit();
                }
            }
        });
    }


}
