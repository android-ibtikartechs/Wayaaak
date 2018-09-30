package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.List_Adapter;
import com.ibtikar.apps.wayaaak.Models.Response.ListResponse;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Favourite_Fragment extends Fragment {
    VolleySimple volleySimple;
    RecyclerView result_list;
    List_Adapter listAdapter;
    LinearLayout empty_holder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_favourite, container, false);
        volleySimple = VolleySimple.getInstance(getContext());

        initView(rootview);
        init(WayaaakAPP.getUserLoginInfo(getContext()).getId());
        return rootview;
    }

    public void initView(View view) {
        result_list = view.findViewById(R.id.result_list);
        result_list.setLayoutManager(new GridLayoutManager(getContext(), 2));
        empty_holder = view.findViewById(R.id.no_item_holder);

    }


    public void init(int id) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "userfavourites/" + id, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("res", "onResponse: " + s);
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
                    result_list.setAdapter(listAdapter);
                    listAdapter.registerOnUpdateListener(new List_Adapter.onUpdateListener() {
                        @Override
                        public void onUpdateLikeStatus(int pos, boolean status) {
                            Toast.makeText(getContext(), "تم الحذف", Toast.LENGTH_SHORT).show();
                            if (listAdapter.getItemCount() != 0)
                                empty_holder.setVisibility(View.GONE);
                            else {
                                empty_holder.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onUpdatePriceView(int position, String price, String oPrice) {

                        }
                    });
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

