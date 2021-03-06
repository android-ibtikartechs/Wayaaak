package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.ibtikar.apps.wayaaak.MainActivity;
import com.ibtikar.apps.wayaaak.Models.Response.ListResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.MultipartBody.FORM;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Search_Fragment extends Fragment implements MainActivity.OnAboutDataReceivedListener {
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
        //init();
        listener();
        if (getArguments() != null) {
          /*  String cityId = getArguments().getString(WayaaakAPP.KEY_CITY_ID);
            String catId = getArguments().getString(WayaaakAPP.KEY_CAT_ID);
            String areaId = getArguments().getString(WayaaakAPP.KEY_AREA_ID);

            FragmentManager fm = getChildFragmentManager();
            SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
            searchDialogFragment.show(fm, "search_dialog"); */
            //onDataReceived(catId,cityId,areaId);
        }
        else
            init();
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

                Log.d("TAG", "onResponse: " + s);
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


    private void search2(String catlId, String cityId, String areaId)
    {
        if (cityId == null)
            cityId = "";
        if (areaId == null)
            areaId = "";
        if (catlId == null)
            catlId = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody body;
        body = new MultipartBody.Builder()
                .setType(FORM)
                .addFormDataPart("category", catlId)
                .addFormDataPart("city", cityId)
                .addFormDataPart("area",areaId).build();


        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(WayaaakAPP.BASE_URL + "search")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                ListResponse response1 = new Gson().fromJson(response.body().string(), ListResponse.class);
                if (response1.getStatus().equals("OK")) {
                    //Log.d("TAG", "onResponse: " + response.body().string());

                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response1.getProducts());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result_list.setAdapter(listAdapter);

                            if (listAdapter.getItemCount() != 0) empty_holder.setVisibility(View.GONE);
                            else {
                                empty_holder.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    public void onDataReceived(String catlId, String cityId, String areaId) {
        if (catlId == null)
            catlId = "";

        Map<String, String> data = new HashMap<String, String>();
        data.put("category", catlId);
        data.put("city", cityId);
        data.put("area", areaId);
        data.put("keyword", "بل");
        data.put("price_from",null);
        data.put("price_to",null);
        data.put("hasoffer",null);
        //search(data);
        search2(catlId,cityId,areaId);
    }
}

