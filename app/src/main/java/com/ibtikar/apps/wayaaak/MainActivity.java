package com.ibtikar.apps.wayaaak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ibtikar.apps.wayaaak.Fragment.SearchDialogFragment;
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
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements ListFragment.onUpdateListFragmentListener {
    VolleySimple volleySimple;
    Toolbar toolbar;
    ExpandableListView catnawlist;
    private SlidingRootNav slidingRootNav;
    CardView appBarLayout;
    BottomBar bottomBar;
    BottomBarTab carticon;
    private OnAboutDataReceivedListener mAboutDataListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WayaaakAPP.initCartProducts(this);
        Intent intent = getIntent();

        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarMain);
        setSupportActionBar(toolbar);
        volleySimple = VolleySimple.getInstance(this);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                //.withGravity(isRTL() ? SlideGravity.RIGHT : SlideGravity.LEFT)
                .withGravity(SlideGravity.RIGHT)
                .withMenuLayout(R.layout.screen_navigation)
                .inject();
        SlideNavigation slideNavigation = new SlideNavigation(R.id.main_fragment_container);
        slideNavigation.initSlideMenu(MainActivity.this, getSupportFragmentManager(), slidingRootNav);

        initSlideNav();
        AppRater.app_launched(this);

        bottomBar = findViewById(R.id.bottomBar);

        carticon = bottomBar.getTabWithId(R.id.menu_cart);
        carticon.setBadgeCount(WayaaakAPP.getCartProducts(this).size());




      /*  getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new Home_Fragment(), "Home_Fragment")
                .commit(); */


        catnawlist = findViewById(R.id.catgoryList);

        System.out.println(WayaaakAPP.printHashKey(this));

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
                            bottomBar.selectTabAtPosition(0);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, new Login_Fragment(), "Login_Fragment")
                                    .addToBackStack("")
                                    .commit();
                        }
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

        if (intent.getBooleanExtra(WayaaakAPP.KEY_FLAG_SEARCH,false))
        {
            Search_Fragment search_fragment = new Search_Fragment();


            String cityId = intent.getStringExtra(WayaaakAPP.KEY_CITY_ID);
            String areaId = intent.getStringExtra(WayaaakAPP.KEY_AREA_ID);
            String catId = intent.getStringExtra(WayaaakAPP.KEY_CAT_ID);
          /*  Bundle bundle = new Bundle();
            bundle.putString(WayaaakAPP.KEY_CITY_ID, cityId);
            bundle.putString(WayaaakAPP.KEY_AREA_ID, areaId);
            bundle.putString(WayaaakAPP.KEY_CAT_ID, catId);
            search_fragment.setArguments(bundle);
            //bottomBar.selectTabWithId(R.id.menu_search);
            //bottomBar.setDefaultTab(R.id.menu_search);
            /* getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, search_fragment, "Search_Fragment")
                    .commit(); */

            //bottomBar.setSelectedItemId(R.id.my_menu_item_id);;
            //mAboutDataListener.onDataReceived(catId, cityId, areaId);
            FragmentManager fm = getSupportFragmentManager();
            SearchDialogFragment searchDialogFragment = new SearchDialogFragment();


            Bundle bundle1 = new Bundle();
            bundle1.putString(WayaaakAPP.KEY_CITY_ID, cityId);
            bundle1.putString(WayaaakAPP.KEY_AREA_ID, areaId);
            bundle1.putString(WayaaakAPP.KEY_CAT_ID, catId);
            searchDialogFragment.setArguments(bundle1);
            searchDialogFragment.show(fm, "search_dialog");
        }


    }

    private boolean isRTL() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                FragmentManager fm = getSupportFragmentManager();
                SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
                searchDialogFragment.show(fm, "search_dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //  actionBarDrawerToggle.syncState();
    }

    public static Intent getStartIntent(Context context, String catlId, String cityId, String areaId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(WayaaakAPP.KEY_FLAG_SEARCH, true);
        intent.putExtra(WayaaakAPP.KEY_CAT_ID, catlId);
        intent.putExtra(WayaaakAPP.KEY_CITY_ID, cityId);
        intent.putExtra(WayaaakAPP.KEY_AREA_ID, areaId);
        Log.d("TAG", "getStartIntent: " + "cityId" + cityId + "areaId" + areaId + "catId" + catlId);
        return intent;
    }

    public static Intent getStartIntentTransition(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
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
        carticon.setBadgeCount(WayaaakAPP.getCartProducts(this).size());
        //super.onBackPressed();
       slidingRootNav.isMenuOpened();
        if (slidingRootNav.isMenuOpened()) {
            slidingRootNav.closeMenu();
        }




        else if ((!WayaaakAPP.getUserLoginState(MainActivity.this)) && (bottomBar.getCurrentTabPosition() == 2 || bottomBar.getCurrentTabPosition() == 3)) {
          /*  if (bottomBar.getCurrentTabPosition() == 2 || bottomBar.getCurrentTabPosition() == 4) {
                bottomBar.selectTabAtPosition(0);

            }*/
            super.onBackPressed();
               bottomBar.selectTabAtPosition(0);
        }
        else if (bottomBar.getCurrentTabPosition() != 0) {
            bottomBar.selectTabAtPosition(0);
        }


        else {
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
                ArrayList<Category> lastList = new ArrayList<>();
                /*for (int i = 0; i < response.getData().size(); i++) {
                    ListItem item = new ListItem();
                    item.setId(response.getData().get(i).getId());
                    item.setName("الكل");
                    response.getData().get(i).getSub_list().add(item);
                }*/

                for (Category c: response.getData())
                {
                    lastList.add(c);
                }
                Log.d("TAG", "onResponse: "+s);
                if (response.getStatus().equals("OK")) {
                    final SliderNavAdapter sliderNavAdapter = new SliderNavAdapter(MainActivity.this, getSupportFragmentManager(), lastList);
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
                            listFragment.setCustomButtonListner(MainActivity.this);
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

    @Override
    public void onOpenListFragment() {
        //appBarLayout.setCardElevation(0);
    }


    public interface OnAboutDataReceivedListener {
        void onDataReceived(String catlId, String cityId, String areaId);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }
}
