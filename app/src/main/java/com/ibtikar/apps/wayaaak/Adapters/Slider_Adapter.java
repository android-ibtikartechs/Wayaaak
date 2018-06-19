package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibtikar.apps.wayaaak.Models.Product;
import com.ibtikar.apps.wayaaak.ProductActivity;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

/**
 * Created by Hosam Azzam on 06/11/2017.
 */

public class Slider_Adapter extends PagerAdapter {
    Context context;
    List<Product> products;
    private LayoutInflater layoutInflater;

    public Slider_Adapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slider_home_layout, container, false);
        ImageView im_slider = view.findViewById(R.id.im_slider);
        Glide.with(context).load(products.get(position).getImage()).asBitmap().into(im_slider);
        //im_slider.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView title = view.findViewById(R.id.im_title);
        title.setText(products.get(position).getName() + "\n" + products.get(position).getPrice() + " EGP");
        container.addView(view);

        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id", products.get(position).getId());
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return products.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}