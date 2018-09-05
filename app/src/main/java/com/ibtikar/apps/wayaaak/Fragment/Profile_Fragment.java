package com.ibtikar.apps.wayaaak.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class Profile_Fragment extends Fragment {
    TextView name, email, phone, address;
    CircleImageView profIm;
    User userobj;
    public static final int PICK_IMAGE = 1;

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
        profIm = rootview.findViewById(R.id.prof_im);


        if (WayaaakAPP.getUserLoginState(getContext())) {
            userobj = WayaaakAPP.getUserLoginInfo(getContext());
            name.setText(userobj.getName());
            email.setText(userobj.getEmail());
            phone.setText(userobj.getPhone());
            address.setText(userobj.getAddress());
        }


        profIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        return rootview;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                profIm.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

