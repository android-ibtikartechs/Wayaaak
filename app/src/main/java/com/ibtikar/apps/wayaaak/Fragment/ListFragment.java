package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.List_Adapter;
import com.ibtikar.apps.wayaaak.Dialogs.Filter_Bottom_Sheet;
import com.ibtikar.apps.wayaaak.Models.ListItem;
import com.ibtikar.apps.wayaaak.Models.Response.ListResponse;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment implements List_Adapter.onUpdateListener {
    VolleySimple volleySimple;
    RecyclerView result_list;
    TabLayout sub_tabs;
    TextView title;
    ImageView back_ico, filter_ico;
    List<ListItem> subList;
    List_Adapter listAdapter;
    Map<String, String> filtermap = new HashMap<>();
    LinearLayout empty_holder;
    int ID;


    public void setSub_list(List<ListItem> subList) {
        this.subList = subList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_list, container, false);
        volleySimple = VolleySimple.getInstance(getContext());
        if (WayaaakAPP.getUserLoginState(getContext())) {
            filtermap.put("user", String.valueOf(WayaaakAPP.getUserLoginInfo(getContext()).getId()));
        }
        initView(rootview);
        listener();
        ID = getArguments().getInt("id", -1);
        init(ID);
        System.out.println("id " + getArguments().getInt("id", -1));
        return rootview;
    }

    public void initView(View view) {
        result_list = view.findViewById(R.id.result_list);
        sub_tabs = view.findViewById(R.id.sub_cat_tab_list);
        title = view.findViewById(R.id.toolbar_title_txt);
        back_ico = view.findViewById(R.id.toolbar_back_ico);
        filter_ico = view.findViewById(R.id.toolbar_filter_ico);

        empty_holder = view.findViewById(R.id.no_item_holder);

        title.setText(getArguments().getString("title", ""));

        result_list.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (subList.size() > 1) {
            sub_tabs.setVisibility(View.VISIBLE);
            for (int i = 0; i < subList.size(); i++) {
                sub_tabs.addTab(sub_tabs.newTab().setText(subList.get(i).getName()));
                if (getArguments().getInt("id", -1) == subList.get(i).getId()) {
                    sub_tabs.getTabAt(i).select();
                }
            }

        }

    }

    public void listener() {
        back_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        filter_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Filter_Bottom_Sheet filter_bottom_sheet = new Filter_Bottom_Sheet();
                filter_bottom_sheet.show(getFragmentManager(), filter_bottom_sheet.getTag());
                filter_bottom_sheet.setOnActionListner(new Filter_Bottom_Sheet.onActionListener() {
                    @Override
                    public void onSubmit(Map<String, String> map) {
                        System.out.println(map);
                        filtermap.putAll(map);
                        init(ID);
                    }

                    @Override
                    public void onReset() {
                        filtermap.clear();
                        if (WayaaakAPP.getUserLoginState(getContext())) {
                            filtermap.put("user", String.valueOf(WayaaakAPP.getUserLoginInfo(getContext()).getId()));
                        }
                        init(ID);
                    }
                });
                */
                FragmentManager fm = getChildFragmentManager();
                SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
                searchDialogFragment.show(fm, "search_dialog");
            }
        });

        sub_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                init(subList.get(tab.getPosition()).getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void init(int catid) {
        filtermap.put("category", String.valueOf(catid));
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "categoryproducts", filtermap, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
                    listAdapter.setCustomButtonListner(ListFragment.this);
                    result_list.setAdapter(listAdapter);
                    if (listAdapter.getItemCount() != 0) empty_holder.setVisibility(View.GONE);
                    else {
                        empty_holder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }

    @Override
    public void onUpdateLikeStatus(int position, boolean status) {
        if (status)
            ((ImageView)result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_like_img)).setImageResource(R.drawable.ic_action_liked);
        else
            ((ImageView)result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_like_img)).setImageResource(R.drawable.ic_action_unliked);

    }

    @Override
    public void onUpdatePriceView(int position, String price, String oPrice) {
        if (result_list.findViewHolderForLayoutPosition(position) != null) {
            if (oPrice.isEmpty() || oPrice.equals("0")) {
                ((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_offer_price_txt)).setText(price);
                ((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_price_txt)).setVisibility(View.GONE);
            } else {
                ((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_price_txt)).setText(price);
                //Log.d("TAG", "onBindViewHolder: " + products.get(position).getOprice());
                //} else {
                ((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_offer_price_txt)).setText(oPrice);
                // holder.price.setText(products.get(position).getOprice()  );
                ((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_price_txt)).setPaintFlags(((TextView) result_list.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.product_price_txt)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
