package com.ibtikar.apps.wayaaak.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Profile_Fragment extends Fragment {
    TextView name, email, phone, address;
    User userobj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        name = rootview.findViewById(R.id.name_txt);
        email = rootview.findViewById(R.id.email_txt);
        phone = rootview.findViewById(R.id.phone_txt);
        address = rootview.findViewById(R.id.address_txt);

        if (WayaaakAPP.getUserLoginState(getContext())) {
            userobj = WayaaakAPP.getUserLoginInfo(getContext());
            name.setText(userobj.getName());
            email.setText(userobj.getEmail());
            phone.setText(userobj.getPhone());
            address.setText(userobj.getAddress());
        }


        return rootview;
    }
}

