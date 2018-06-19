package com.ibtikar.apps.wayaaak.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Adapters.Slider_Start_Adapter;
import com.ibtikar.apps.wayaaak.MainActivity;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.ArrayList;

/**
 * Created by hosam azzam on 20/02/2018.
 */

public class Start_Fragment extends Fragment {
    ArrayList<Integer> slider_image_list = new ArrayList<>();
    private ViewPager images_slider;
    private LinearLayout pages_dots;
    private TextView[] dots;
    Slider_Start_Adapter sliderStartAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_start, container, false);
        images_slider = rootview.findViewById(R.id.page_slider);
        pages_dots = rootview.findViewById(R.id.image_page_dots);

        initSlider();
        addBottomDots(0);

        rootview.findViewById(R.id.con_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WayaaakAPP.setFirstOpen(getContext());
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return rootview;
    }

    public void initSlider() {
        addBottomDots(0);

        slider_image_list = new ArrayList<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        slider_image_list.add(R.drawable.splash_bg1);
        slider_image_list.add(R.drawable.splash_bg2);
        slider_image_list.add(R.drawable.splash_bg3);


        sliderStartAdapter = new Slider_Start_Adapter(getActivity(), slider_image_list);
        images_slider.setAdapter(sliderStartAdapter);
        images_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        pages_dots.removeAllViews();
        pages_dots.setPadding(0, 0, 0, 20);
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#ffffff"));
            pages_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#0b94c8"));
    }
}
