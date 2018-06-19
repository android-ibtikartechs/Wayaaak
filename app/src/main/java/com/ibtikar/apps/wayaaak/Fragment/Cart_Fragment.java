package com.ibtikar.apps.wayaaak.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.CartAdapter;
import com.ibtikar.apps.wayaaak.Checkout_Activity;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Cart_Fragment extends Fragment {
    RecyclerView cartList;
    CartAdapter cartAdapter;
    VolleySimple volleySimple;
    TextView total, title;
    CardView checkout, total_holder;
    ImageView back_ico, filter_ico;
    RelativeLayout toolbar_holder;
    LinearLayout empty_holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cart, container, false);
        volleySimple = VolleySimple.getInstance(getContext());

        cartList = rootview.findViewById(R.id.cart_list);
        total = rootview.findViewById(R.id.cart_total_txt);
        checkout = rootview.findViewById(R.id.checkout_btn);
        empty_holder = rootview.findViewById(R.id.no_item_holder);
        total_holder = rootview.findViewById(R.id.total_holder);

        toolbar_holder = rootview.findViewById(R.id.toolbar_holder);
        title = toolbar_holder.findViewById(R.id.toolbar_title_txt);
        back_ico = toolbar_holder.findViewById(R.id.toolbar_back_ico);
        filter_ico = toolbar_holder.findViewById(R.id.toolbar_filter_ico);
        if (getArguments() != null) {
            toolbar_holder.setVisibility(View.VISIBLE);
            filter_ico.setVisibility(View.GONE);
            title.setText("مشترياتى");
        } else {
            toolbar_holder.setVisibility(View.GONE);
        }

        init();
        initTotal();
        listener();
        return rootview;
    }

    public void init() {
        cartList.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), WayaaakAPP.getCartProducts(getContext()), new CartAdapter.onUpdateListener() {
            @Override
            public void onUpdate() {
                initTotal();
                if (cartAdapter.getItemCount() == 0) {
                    empty_holder.setVisibility(View.VISIBLE);
                    total_holder.setVisibility(View.GONE);
                } else {
                    total_holder.setVisibility(View.VISIBLE);
                    empty_holder.setVisibility(View.GONE);
                }
            }
        });
        if (cartAdapter.getItemCount() == 0) {
            empty_holder.setVisibility(View.VISIBLE);
            total_holder.setVisibility(View.GONE);
        } else {
            total_holder.setVisibility(View.VISIBLE);
            empty_holder.setVisibility(View.GONE);
        }

        cartList.setAdapter(cartAdapter);
    }

    public void listener() {
        back_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WayaaakAPP.getUserLoginState(getContext()))
                    startActivity(new Intent(getActivity(), Checkout_Activity.class));
                else
                    Toast.makeText(getContext(), "قم بتسجيل الدول اولا", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initTotal() {
        total.setText("EGP " + String.valueOf(WayaaakAPP.getTotalPrice()));
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}

