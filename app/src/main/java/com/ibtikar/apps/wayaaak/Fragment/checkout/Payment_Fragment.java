package com.ibtikar.apps.wayaaak.Fragment.checkout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ibtikar.apps.wayaaak.Models.AddressBook;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

public class Payment_Fragment extends Fragment {
    TextView contiune, address_info_txt, total;
    LinearLayout one_btn, two_btn;
    int deliver_option = 1;
    TextView one_cost, one_time, two_cost, two_time, free_btn;
    EditText note_edtx;
    AddressBook address;
    boolean IsCash = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_payment, container, false);

        initView(rootview);
        listener();
        initTotal(0);
        return rootview;
    }

    public void initView(View view) {
        contiune = view.findViewById(R.id.continue_btn);
        one_btn = view.findViewById(R.id.option_one_btn);
        two_btn = view.findViewById(R.id.option_two_btn);
        free_btn = view.findViewById(R.id.option_free_btn);
        one_cost = view.findViewById(R.id.option_one_cost_txt);
        one_time = view.findViewById(R.id.option_one_time_txt);
        two_cost = view.findViewById(R.id.option_two_cost_txt);
        two_time = view.findViewById(R.id.option_two_time_txt);
        address_info_txt = view.findViewById(R.id.address_info_txt);
        note_edtx = view.findViewById(R.id.note_txt);
        total = view.findViewById(R.id.total_txt);
        address_info_txt.setText(address.getAddress() + "\n" + "الشحن : " + getOptionString(deliver_option));
    }

    public void listener() {
        contiune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (deliver_option != -1) {
                    Confirm_Fragment confirm_fragment = new Confirm_Fragment();
                    confirm_fragment.setdata(address, deliver_option, note_edtx.getText().toString(), IsCash);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.checkout_fragment_container, confirm_fragment, "confirm_fragment")
                            .addToBackStack("confirm_fragment")
                            .commit();

            //    } else {
             //       Toast.makeText(getContext(), "اختر نوع التوصيل", Toast.LENGTH_SHORT).show();
             //   }

            }
        });

        free_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                free_btn.setTextColor(Color.parseColor("#00acec"));
                one_time.setTextColor(Color.parseColor("#717171"));
                two_time.setTextColor(Color.parseColor("#717171"));
                two_cost.setTextColor(Color.parseColor("#000000"));
                one_cost.setTextColor(Color.parseColor("#000000"));
                deliver_option = 1;
                address_info_txt.setText(address.getAddress() + "\n" + "الشحن : " + getOptionString(deliver_option));
                initTotal(0);
            }
        });

        one_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_time.setTextColor(Color.parseColor("#00acec"));
                one_cost.setTextColor(Color.parseColor("#00acec"));
                two_time.setTextColor(Color.parseColor("#717171"));
                two_cost.setTextColor(Color.parseColor("#000000"));
                free_btn.setTextColor(Color.parseColor("#717171"));
                deliver_option = 2;
                address_info_txt.setText(address.getAddress() + "\n" + "الشحن : " + getOptionString(deliver_option));
                initTotal(49);
            }
        });

        two_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two_time.setTextColor(Color.parseColor("#00acec"));
                two_cost.setTextColor(Color.parseColor("#00acec"));
                one_time.setTextColor(Color.parseColor("#717171"));
                one_cost.setTextColor(Color.parseColor("#000000"));
                free_btn.setTextColor(Color.parseColor("#717171"));
                deliver_option = 3;
                address_info_txt.setText(address.getAddress() + "\n" + "الشحن : " + getOptionString(deliver_option));
                initTotal(149);
            }
        });
    }


    public String getOptionString(int option) {
        if (option == 1) return "مجانى";
        if (option == 2) return "2-3 يوم";
        if (option == 3) return "اليوم التالى";
        return "";
    }

    public void initTotal(double fees) {
        total.setText(String.valueOf(WayaaakAPP.getTotalPrice() + fees) + " EGP");
    }


    public void setAddress(AddressBook address) {
        this.address = address;
    }
}
