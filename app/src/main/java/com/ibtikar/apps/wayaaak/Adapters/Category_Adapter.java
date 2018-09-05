package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibtikar.apps.wayaaak.Fragment.ListFragment;
import com.ibtikar.apps.wayaaak.Models.HomeCategory;
import com.ibtikar.apps.wayaaak.Models.ListItem;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.NewViewHolder> {
    Context context;
    List<HomeCategory> categories;
    FragmentManager fragmentManager;

    public Category_Adapter(Context context, FragmentManager fragmentManager, List<HomeCategory> categories) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.categories = categories;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_home_category, parent, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, final int position) {
        //if (position % 3 == 0) {
            holder.big_container.setVisibility(View.VISIBLE);
           // holder.small_container.setVisibility(View.GONE);
            holder.big_txt.setText(categories.get(position).getParentcategory_name() + " - " + categories.get(position).getName());
            Glide.with(context).load(categories.get(position).getImage()).asBitmap().into(holder.big_img);
        /*} else {
            holder.small_container.setVisibility(View.VISIBLE);
            holder.big_container.setVisibility(View.GONE);
            holder.small_txt.setText(categories.get(position).getParentcategory_name() + " - " + categories.get(position).getName());
            Glide.with(context).load(categories.get(position).getImage()).asBitmap().into(holder.small_img);
        } */

        holder.big_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(position);
            }
        });
    /*    holder.small_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(position);
            }
        });*/

    }

    public void open(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", categories.get(pos).getId());
        bundle.putString("title", categories.get(pos).getParentcategory_name());
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        List<ListItem> items = categories.get(pos).getSublist();
        /*ListItem item = new ListItem();
        item.setId(categories.get(pos).getParentcategory_id());
        item.setName("الكل");
        if (items.get(items.size() - 1).getId() != item.getId())
            items.add(item);*/
        listFragment.setSub_list(items);
        fragmentManager.beginTransaction().add(R.id.main_content, listFragment, "").addToBackStack("").commit();

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout big_container, small_container;
        TextView big_txt, small_txt;
        ImageView big_img, small_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            big_container = itemView.findViewById(R.id.cat_home_big_container);
            big_img = itemView.findViewById(R.id.cat_home_big_img);
            big_txt = itemView.findViewById(R.id.cat_home_big_txt);
            small_container = itemView.findViewById(R.id.cat_home_small_container);
            small_img = itemView.findViewById(R.id.cat_home_small_img);
            small_txt = itemView.findViewById(R.id.cat_home_small_txt);
        }
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout big_container;
        TextView big_txt;
        ImageView big_img;

        public NewViewHolder(View itemView) {
            super(itemView);
            big_container = itemView.findViewById(R.id.cat_home_big_container);
            big_img = itemView.findViewById(R.id.cat_home_big_img);
            big_txt = itemView.findViewById(R.id.cat_home_big_txt);
        }
    }
}
