package com.ibtikar.apps.wayaaak.Fragment.checkout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.MiniCartAdapter;
import com.ibtikar.apps.wayaaak.Fragment.ConfirmOrderDialog;
import com.ibtikar.apps.wayaaak.Models.AddressBook;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.Models.Response.OrderResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Confirm_Fragment extends Fragment implements ConfirmOrderDialog.ConfirmFragmentButtonsListener {
    TextView contiune, address_info_txt, total_txt, subtotal_txt, charge_txt;
    RecyclerView cartList;
    MiniCartAdapter cartAdapter;
    AddressBook address;
    int deliver_option = 1;
    String note = "";
    VolleySimple volleySimple;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_confirm, container, false);
        volleySimple = VolleySimple.getInstance(getContext());
        user = WayaaakAPP.getUserLoginInfo(getContext());
        initView(rootview);
        listener();
        initTotal();
        return rootview;
    }

    public void initView(View view) {
        address_info_txt = view.findViewById(R.id.address_info_txt);
        contiune = view.findViewById(R.id.continue_btn);
        total_txt = view.findViewById(R.id.total_txt);
        subtotal_txt = view.findViewById(R.id.subtotal_txt);
        charge_txt = view.findViewById(R.id.charge_txt);

        cartList = view.findViewById(R.id.cart_list);
        cartList.setNestedScrollingEnabled(false);
        cartList.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList.setAdapter(new MiniCartAdapter(getContext(), WayaaakAPP.getCartProducts(getContext())));

        if (!note.equals("")) note += "\n";
        address_info_txt.setText(address.getAddress() + "\n" + "ملاحظات : "  +note);
    }

    public void listener() {
        contiune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addorder();
            }
        });
/*
        card_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
            }
        });
        */
    }


    public void setdata(AddressBook address, int option, String note, boolean isCash) {
        this.address = address;
        this.deliver_option = option;
        this.note = note;
    }

    public void initTotal() {
        subtotal_txt.setText(WayaaakAPP.getTotalPrice() + " EGP");
        charge_txt.setText(address.getFees() + "EGP");
        double tot = WayaaakAPP.getTotalPrice() + Integer.valueOf(address.getFees());
        total_txt.setText((tot + getOptionDouble(deliver_option)) + " EGP");
    }

    public String getOptionString(int option) {
        if (option == 1) return "مجانى";
        if (option == 2) return "2-3 يوم";
        if (option == 3) return "اليوم التالى";
        return "";
    }

    public double getOptionDouble(int option) {
        if (option == 1) return 0;
        if (option == 2) return 49;
        if (option == 3) return 149;
        return 0;
    }

    public void addorder() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        Map<String, String> map = new HashMap<>();
        //map.put("bdate", "");
        //map.put("btime", "");
        map.put("bcharge", String.valueOf(deliver_option));
        //map.put("bcomment", note);
        map.put("buser", String.valueOf(user.getId()));
        map.put("baddress", String.valueOf(address.getId()));
        int size = WayaaakAPP.getCartProducts(getContext()).size();
        List<Cart> items = WayaaakAPP.getCartProducts(getContext());
        for (int i = 0; i < size; i++) {
            map.put("mybookproducts[" + items.get(i).getId() + "]", items.get(i).getQty() + "");
        }
        Log.d("TAG", "addorder: " + tolString(map));

        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "addbook", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "onResponse: "+ s);
                OrderResponse response = new Gson().fromJson(s, OrderResponse.class);
                if (response.getStatus().equals("OK")) {
                    //Toast.makeText(getContext(), "تم ارسال طلبك", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getChildFragmentManager();
                    ConfirmOrderDialog confirmDialogFragment = new ConfirmOrderDialog();
                    confirmDialogFragment.setButtonListener(Confirm_Fragment.this);
                    confirmDialogFragment.show(fm, "search_dialog");
                    //getActivity().finish();
                    WayaaakAPP.clearCartList(getContext());
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }, progressDialog);
    }

    public String tolString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }

    @Override
    public void onContinueButtonClickLisener() {
        getActivity().finish();
    }
}
