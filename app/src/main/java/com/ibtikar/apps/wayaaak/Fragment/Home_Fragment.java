package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.Category_Adapter;
import com.ibtikar.apps.wayaaak.Adapters.Slider_Adapter;
import com.ibtikar.apps.wayaaak.Models.Product;
import com.ibtikar.apps.wayaaak.Models.Response.HomeResponse;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Home_Fragment extends Fragment {
    Slider_Adapter sliderPagerAdapter;
    VolleySimple volleySimple;
    private ViewPager images_slider;
    private TabLayout pages_dots;
    RecyclerView categorylist;
    Category_Adapter categoryAdapter;
    int page_position = 0, slider_size = 0;
    Timer timer;
    CountDownTimer cdt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        volleySimple = VolleySimple.getInstance(getContext());
        categorylist = rootview.findViewById(R.id.cat_home_list);
        //categorylist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        categorylist.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        initView(rootview);
        init();

        images_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //cancelSlidingforSomeTime();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootview;
    }

    public void initView(View view) {
        images_slider = view.findViewById(R.id.image_page_slider);
        pages_dots = view.findViewById(R.id.image_page_dots);

        timer = new Timer();
        scheduleSlider();



    }

    public void init() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "frontpage", new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                HomeResponse response = new Gson().fromJson(s, HomeResponse.class);
                if (response.getStatus().equals("OK")) {
                    initSlider(response.getSliderProducts());
                    categoryAdapter = new Category_Adapter(getContext(), getFragmentManager(), response.getFrontCategories());
                    categorylist.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }



    public void initSlider(List<Product> items) {
        sliderPagerAdapter = new Slider_Adapter(getActivity(), items);
        images_slider.setAdapter(sliderPagerAdapter);
        images_slider.setClipToPadding(false);
        images_slider.setPadding(60, 0, 60, 0);
        images_slider.setPageMargin(24);
        pages_dots.setupWithViewPager(images_slider);
        slider_size = items.size();

    }

    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_size) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                images_slider.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 3000);
    }

    private void cancelSlidingforSomeTime()
    {
        timer.cancel();
        if (cdt != null)
            cdt.cancel();
        cdt = new CountDownTimer(5000,30) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                scheduleSlider();
            }
        };
    }


    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    private View.OnTouchListener getButtonTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //cancelSlidingforSomeTime();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //Release logic here
                        return true;
                }

                return false;
            }
        };
    }


}

