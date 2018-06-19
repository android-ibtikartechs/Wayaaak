package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.List_Adapter;
import com.ibtikar.apps.wayaaak.Dialogs.Filter_Bottom_Sheet;
import com.ibtikar.apps.wayaaak.Models.Response.ListResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Search_Fragment extends Fragment {
    VolleySimple volleySimple;
    RecyclerView result_list;
    List_Adapter listAdapter;
    LinearLayout empty_holder;
    EditText search_txt;
    ImageView search_ico, filter_ico;
    Map<String, String> filtermap = new HashMap<>();
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_search, container, false);
        volleySimple = VolleySimple.getInstance(getContext());
        if (WayaaakAPP.getUserLoginState(getContext())) {
            user = WayaaakAPP.getUserLoginInfo(getContext());
            filtermap.put("user", String.valueOf(user.getId()));
        }
        initView(rootview);
        init();
        listener();
        return rootview;
    }

    public void initView(View view) {
        result_list = view.findViewById(R.id.result_list);
        search_ico = view.findViewById(R.id.search_ico);
        filter_ico = view.findViewById(R.id.filter_ico);
        search_txt = view.findViewById(R.id.search_txt);
        empty_holder = view.findViewById(R.id.no_item_holder);
        result_list.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    public void listener() {
        search_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtermap.put("keyword", search_txt.getText().toString());
                search(filtermap);
            }
        });

        filter_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_ico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Filter_Bottom_Sheet filter_bottom_sheet = new Filter_Bottom_Sheet();
                        filter_bottom_sheet.show(getFragmentManager(), filter_bottom_sheet.getTag());
                        filter_bottom_sheet.setOnActionListner(new Filter_Bottom_Sheet.onActionListener() {
                            @Override
                            public void onSubmit(Map<String, String> map) {
                                System.out.println(map);
                                filtermap.putAll(map);
                                init();
                            }

                            @Override
                            public void onReset() {
                                filtermap.clear();
                                if (WayaaakAPP.getUserLoginState(getContext())) {
                                    filtermap.put("user", String.valueOf(user.getId()));
                                }
                                init();
                            }
                        });
                    }
                });
            }
        });
    }

    public void init() {
        String link = "allproducts";
        if (user != null) link += "/" + user.getId();
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + link, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
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

    public void search(Map<String, String> map) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "search", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
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
}

