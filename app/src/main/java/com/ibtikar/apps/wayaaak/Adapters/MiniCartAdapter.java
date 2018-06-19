package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

public class MiniCartAdapter extends RecyclerView.Adapter<MiniCartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;

    public MiniCartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.cartList = carts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mini_cart_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.total.setText(cartList.get(position).getTotal() + " EGP");
        holder.price.setText("(" + cartList.get(position).getPrice() + "X" + cartList.get(position).getQty() + ")");
        holder.qty.setText("Qty : " + cartList.get(position).getQty() + "");
        holder.title.setText(cartList.get(position).getName());
        Glide.with(context).load(cartList.get(position).getPhoto()).asBitmap().into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView photo, like;
        TextView title, price, total, remove, edit, qty;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.cart_container);
            photo = itemView.findViewById(R.id.photo_img);
            remove = itemView.findViewById(R.id.remove_tvbtn);
            edit = itemView.findViewById(R.id.edit_tvbtn);
            like = itemView.findViewById(R.id.like_img);
            title = itemView.findViewById(R.id.title_txt);
            price = itemView.findViewById(R.id.price_txt);
            qty = itemView.findViewById(R.id.qty_txt);
            total = itemView.findViewById(R.id.total_txt);
        }
    }
}
