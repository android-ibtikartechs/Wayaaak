package com.ibtikar.apps.wayaaak.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Models.Status;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.Map;

public class Forget_Fragment extends Fragment {
    EditText email;
    VolleySimple volleySimple;
    CardView forget_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_forget, container, false);

        volleySimple = VolleySimple.getInstance(getActivity());
        email = rootview.findViewById(R.id.email_edtx);
        forget_btn = rootview.findViewById(R.id.forget_btn);

        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("")) {
                    forget(email.getText().toString());
                }
            }
        });
        return rootview;
    }

    public void forget(String mail) {
        Map<String, String> map = new HashMap<>();
        map.put("email", mail);
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "forgetpassword", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("OK")) {
                    email.setText("");
                    Toast.makeText(getContext(), "تم ارسال رساله على البريد الخاص بك لاسترداد كلمة السر", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "الحساب غير موجود", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }
}
