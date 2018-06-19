package com.ibtikar.apps.wayaaak.Fragment.checkout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.Address_List_Adapter;
import com.ibtikar.apps.wayaaak.Models.AddressBook;
import com.ibtikar.apps.wayaaak.Models.Response.AddressResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

public class List_Address_Fragment extends Fragment {
    TextView contiune, new_Address, list_Address;
    RecyclerView list;
    Address_List_Adapter addressListAdapter;
    VolleySimple volleySimple;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_list_address, container, false);
        user = WayaaakAPP.getUserLoginInfo(getContext());
        volleySimple = VolleySimple.getInstance(getContext());
        initView(rootview);
        listener();
        init();
        return rootview;
    }

    public void initView(View view) {
        new_Address = view.findViewById(R.id.address_new_txt);
        list_Address = view.findViewById(R.id.address_list_txt);
        contiune = view.findViewById(R.id.continue_btn);
        list = view.findViewById(R.id.list_address_recycler);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void listener() {
        new_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, new Add_Address_Fragment(), "").commit();
            }
        });

    }

    public void init() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + "addressbook/" + user.getId(), new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                AddressResponse response = new Gson().fromJson(s, AddressResponse.class);
                if (response.getStatus().equals("OK")) {
                    addressListAdapter = new Address_List_Adapter(getContext(), response.getAddressbook(), new Address_List_Adapter.onClickListener() {
                        @Override
                        public void onClick(AddressBook address) {
                            Payment_Fragment payment_fragment = new Payment_Fragment();
                            payment_fragment.setAddress(address);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.checkout_fragment_container, payment_fragment, "payment_fragment")
                                    .addToBackStack("payment_fragment")
                                    .commit();

                        }
                    });
                    list.setAdapter(addressListAdapter);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }
}
