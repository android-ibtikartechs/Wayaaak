package com.ibtikar.apps.wayaaak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.SliderNavAdapter;
import com.ibtikar.apps.wayaaak.Fragment.Cart_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Discount_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Home_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.ListFragment;
import com.ibtikar.apps.wayaaak.Fragment.Login_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Profile_Fragment;
import com.ibtikar.apps.wayaaak.Fragment.Search_Fragment;
import com.ibtikar.apps.wayaaak.Models.Category;
import com.ibtikar.apps.wayaaak.Models.ListItem;
import com.ibtikar.apps.wayaaak.Models.Response.CategoryResponse;
import com.ibtikar.apps.wayaaak.Tools.AppRater;
import com.ibtikar.apps.wayaaak.Tools.SlideNavigation;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    VolleySimple volleySimple;
    Toolbar toolbar;
    ExpandableListView catnawlist;
    private SlidingRootNav slidingRootNav;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WayaaakAPP.initCartProducts(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        volleySimple = VolleySimple.getInstance(this);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuLayout(R.layout.screen_navigation)
                .inject();
        SlideNavigation slideNavigation = new SlideNavigation(R.id.main_fragment_container);
        slideNavigation.initSlideMenu(MainActivity.this, getSupportFragmentManager(), slidingRootNav);

        initSlideNav();
        AppRater.app_launched(this);

        bottomBar = findViewById(R.id.bottomBar);
        BottomBarTab carticon = bottomBar.getTabWithId(R.id.menu_cart);
        carticon.setBadgeCount(WayaaakAPP.getCartProducts(this).size());

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.menu_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new Home_Fragment(), "Home_Fragment")
                                .commit();
                        break;
                    case R.id.menu_discount:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new Discount_Fragment(), "Discount_Fragment")
                                .commit();
                        break;
                    case R.id.menu_cart:
                        if (WayaaakAPP.getUserLoginState(MainActivity.this)) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_fragment_container, new Cart_Fragment(), "Cart_Fragment")
                                    .commit();
                        } else {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                                    .addToBackStack("")
                                    .commit();
                        }
                        break;
                    case R.id.menu_search:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, new Search_Fragment(), "Search_Fragment")
                                .commit();
                        break;
                    case R.id.menu_profile:
                        if (WayaaakAPP.getUserLoginState(MainActivity.this)) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_fragment_container, new Profile_Fragment(), "Profile_Fragment")
                                    .commit();
                        } else {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                                    .addToBackStack("")
                                    .commit();
                        }
                        break;
                }
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new Home_Fragment(), "Home_Fragment")
                .commit();


        catnawlist = findViewById(R.id.catgoryList);

        System.out.println(WayaaakAPP.printHashKey(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //  actionBarDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("enter");
        if (bottomBar != null) {
            BottomBarTab carticon = bottomBar.getTabWithId(R.id.menu_cart);
            carticon.setBadgeCount(WayaaakAPP.getCartProducts(this).size());
            if (WayaaakAPP.getCartProducts(this).size() == 0) bottomBar.selectTabAtPosition(0);
        }
    }

    @Override
    public void onBackPressed() {
        slidingRootNav.isMenuOpened();
        if (slidingRootNav.isMenuOpened()) {
            slidingRootNav.closeMenu();
        } else if (!WayaaakAPP.getUserLoginState(MainActivity.this)) {
            if (bottomBar.getCurrentTabPosition() == 2 || bottomBar.getCurrentTabPosition() == 4) {
                bottomBar.selectTabAtPosition(0);
            }
        } else if (bottomBar.getCurrentTabPosition() != 0) {
            bottomBar.selectTabAtPosition(0);
        } else {
            super.onBackPressed();
        }
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

    public void initSlideNav() {
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "categories", new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                CategoryResponse response = new Gson().fromJson(s, CategoryResponse.class);
                if (response.getStatus().equals("OK")) {
                    final SliderNavAdapter sliderNavAdapter = new SliderNavAdapter(MainActivity.this, getSupportFragmentManager(), response.getData());
                    catnawlist.setAdapter(sliderNavAdapter);
                    setListViewHeight(catnawlist, sliderNavAdapter, -1);
                    catnawlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            slidingRootNav.closeMenu();
                            Bundle bundle = new Bundle();
                            long cid = sliderNavAdapter.getChildId(groupPosition, childPosition);
                            bundle.putInt("id", Integer.valueOf(String.valueOf(cid)));
                            bundle.putString("title", ((Category) sliderNavAdapter.getGroup(groupPosition)).getName());
                            ListFragment listFragment = new ListFragment();
                            listFragment.setArguments(bundle);
                            listFragment.setSub_list((List<ListItem>) sliderNavAdapter.getChild(groupPosition, childPosition));
                            getSupportFragmentManager().beginTransaction().add(R.id.main_content, listFragment, "").addToBackStack("").commit();

                            return false;
                        }
                    });
                    catnawlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            setListViewHeight(catnawlist, sliderNavAdapter, groupPosition);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void dymmyClick(View view) {

    }

    public void setListViewHeight(ExpandableListView listView, SliderNavAdapter sliderNavAdapter, int group) {

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < sliderNavAdapter.getGroupCount(); i++) {
            View groupItem = sliderNavAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += group != -1 ? groupItem.getMeasuredHeight() : WayaaakAPP.pxFromDp(MainActivity.this, 40); //40 is group height in dp

            if (group != -1)
                if (((listView.isGroupExpanded(i)) && (i != group))
                        || ((!listView.isGroupExpanded(i)) && (i == group))) {
                    for (int j = 0; j < sliderNavAdapter.getChildrenCount(i); j++) {
                        View listItem = sliderNavAdapter.getChildView(i, j, false, null,
                                listView);
                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                        totalHeight += listItem.getMeasuredHeight();

                    }
                }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        System.out.println(totalHeight);
        int height = totalHeight + (listView.getDividerHeight() * (sliderNavAdapter.getGroupCount() - 1));
        System.out.println(height);
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
