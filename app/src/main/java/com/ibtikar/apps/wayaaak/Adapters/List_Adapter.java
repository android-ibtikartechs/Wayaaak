package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
       // if (products.get(position).getOprice().equals("0")) {

  /*      holder.price.setText(products.get(position).getPrice());
        holder.oprice.setText(products.get(position).getOprice() );*/

        if(products.get(position).getOprice().isEmpty() || products.get(position).getOprice().equals("0"))
        {
            holder.oprice.setText(products.get(position).getPrice());
            holder.oprice.setTextColor(context.getResources().getColor(R.color.colorAccent));
            //holder.price.setVisibility(View.GONE);
        }
        else
        {
            holder.price.setTextColor(context.getResources().getColor(R.color.colorPrice));
            holder.price.setText(products.get(position).getPrice());
            Log.d("TAG", "onBindViewHolder: " + products.get(position).getOprice());
            //} else {
            holder.oprice.setText(products.get(position).getOprice());
            // holder.price.setText(products.get(position).getOprice()  );
            holder.oprice.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

       // }
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

        Log.d("TAG", "onBindViewHolder: " + products.get(position).getIsfavourite());

        if (products.get(position).getIsfavourite())
            holder.like.setImageResource(R.drawable.ic_action_liked);
        else
            holder.like.setImageResource(R.drawable.ic_action_unliked);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.get(position).getIsfavourite() != null && user != null)
                {
                    if (products.get(position).getIsfavourite()) {
                        listener.onUpdateLikeStatus(position, false);
                        removeFromFav(holder.like, position);
                        //holder.like.setImageResource(R.drawable.ic_action_unliked);
                        products.get(position).setIsfavourite(false);
                    } else {
                        listener.onUpdateLikeStatus(position, true);
                        addToFav(holder.like, position);
                        //holder.like.setImageResource(R.drawable.ic_action_liked);
                        products.get(position).setIsfavourite(true);
                    }
                }
                else
                    Toast.makeText(context, "برجاء التسجيل/تسجيل الدخول لتتمكن من استخدام سلة الأمنيات", Toast.LENGTH_SHORT).show();

            }
        });
/*
            if (products.get(position).getIsfavourite() != null)
            {
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user != null) {
                            if (products.get(position).getIsfavourite()) {
                                removeFromFav(holder.like, position);
                                holder.like.setImageResource(R.drawable.ic_action_unliked);
                                products.get(position).setIsfavourite(false);
                            } else {
                                addToFav(holder.like, position);
                                holder.like.setImageResource(R.drawable.ic_action_liked);
                                products.get(position).setIsfavourite(true);
                            }
                        }


                    }
                });
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
                    /*holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToFav(holder.like, position);
                            holder.like.setImageResource(R.drawable.ic_action_liked);
                            products.get(position).setIsfavourite(true);
                        }
                    });
                }
        }

        else
                Toast.makeText(context, "برجاء التسجيل/تسجيل الدخول لتتمكن من استخدام سلة الأمنيات", Toast.LENGTH_SHORT).show();
*/
        //listener.onUpdatePriceView(position, products.get(position).getPrice(), products.get(position).getOprice());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addToFav(final ImageView img, final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(products.get(pos).getId()));
        volley.asyncStringPost(WayaaakAPP.BASE_URL + "addtofavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "add to fav " + s);
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("false")) {
                    listener.onUpdateLikeStatus(pos, false);
                }
                else
                    listener.onUpdateLikeStatus(pos, true);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onUpdateLikeStatus(pos, false);
                Toast.makeText(context, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
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
                Log.d("TAG", "remove from fav " + s);
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("OK")) {
                    if (listener != null) {
                        listener.onUpdateLikeStatus(pos,false);

                    }
                } else {
                    listener.onUpdateLikeStatus(pos,true);

                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onUpdateLikeStatus(pos,true);
                Toast.makeText(context, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
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
        void onUpdateLikeStatus(int position, boolean status);
        void onUpdatePriceView(int position, String price, String oPrice);
    }

    public void setCustomButtonListner(onUpdateListener listener) {
        this.listener = listener;
    }
}
