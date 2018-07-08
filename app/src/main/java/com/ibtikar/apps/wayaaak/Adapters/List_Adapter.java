package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Models.Product;
import com.ibtikar.apps.wayaaak.Models.Status;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.ProductActivity;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.MyViewHolder> {
    Context context;
    List<Product> products;
    FragmentManager fragmentManager;
    public onUpdateListener listener;
    VolleySimple volley;
    User user;

    public List_Adapter(Context context, FragmentManager fragmentManager, List<Product> products) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.products = products;
        this.volley = VolleySimple.getInstance(context);
        user = WayaaakAPP.getUserLoginInfo(context);
    }

    public void registerOnUpdateListener(onUpdateListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (products.get(position).getOprice().equals("0")) {
            holder.price.setText(products.get(position).getPrice() + " EGP");
        } else {
            holder.oprice.setText(products.get(position).getPrice() + " ");
            holder.price.setText(products.get(position).getOprice() + " EGP");
            holder.oprice.setPaintFlags(holder.oprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.title.setText(products.get(position).getName());
        Glide.with(context).load(products.get(position).getImage()).asBitmap().into(holder.photo);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id", products.get(position).getId());
                context.startActivity(intent);
            }
        });

        if (user != null) {
            if (products.get(position).getIsfavourite() != null)
            {
                if (products.get(position).getIsfavourite()) {
                    holder.like.setImageResource(R.drawable.ic_action_liked);
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFromFav(holder.like, position);
                            holder.like.setImageResource(R.drawable.ic_action_unliked);
                            products.get(position).setIsfavourite(false);
                        }
                    });
                } else {
                    holder.like.setImageResource(R.drawable.ic_action_unliked);
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToFav(holder.like, position);
                            holder.like.setImageResource(R.drawable.ic_action_liked);
                            products.get(position).setIsfavourite(true);
                        }
                    });
                }
        }
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addToFav(final ImageView img, int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(products.get(pos).getId()));
        volley.asyncStringPost(WayaaakAPP.BASE_URL + "addtofavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("false")) {
                    img.setImageResource(R.drawable.ic_action_unliked);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void removeFromFav(final ImageView img, final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(products.get(pos).getId()));
        volley.asyncStringPost(WayaaakAPP.BASE_URL + "removefromfavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("OK")) {
                    if (listener != null) {
                        products.remove(pos);
                        notifyDataSetChanged();
                        listener.onUpdate();

                    }
                } else {
                    img.setImageResource(R.drawable.ic_action_liked);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photo, like;
        TextView title, price, oprice;
        CardView container;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.product_img);
            title = itemView.findViewById(R.id.product_title_txt);
            price = itemView.findViewById(R.id.product_price_txt);
            oprice = itemView.findViewById(R.id.product_offer_price_txt);
            container = itemView.findViewById(R.id.product_container);
            like = itemView.findViewById(R.id.product_like_img);
        }
    }

    public interface onUpdateListener {
        void onUpdate();
    }
}
