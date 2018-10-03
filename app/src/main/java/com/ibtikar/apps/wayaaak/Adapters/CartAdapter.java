package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.Models.Status;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.ProductActivity;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;
    VolleySimple volley;
    User user;
    onUpdateListener listener;


    public CartAdapter(Context context, List<Cart> carts, onUpdateListener listener) {
        this.context = context;
        this.cartList = carts;
        this.volley = VolleySimple.getInstance(context);
        user = WayaaakAPP.getUserLoginInfo(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.total.setText(cartList.get(position).getTotal() + " EGP");
        holder.price.setText("(" + cartList.get(position).getPrice() + "X" + cartList.get(position).getQty() + ")");
        holder.qty.setSelection(cartList.get(position).getQty() - 1);
        holder.qty.setEnabled(false);
        holder.title.setText(cartList.get(position).getName());
        Glide.with(context).load(cartList.get(position).getPhoto()).asBitmap().into(holder.photo);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("حذف من السلة");
                builder.setMessage("هل انت متاكد انك تريد حذف " + cartList.get(position).getName() + " ؟");

                builder.setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WayaaakAPP.removeFromCart(context, cartList.get(position));
                        notifyDataSetChanged();
                        listener.onUpdate();

                    }

                });
                builder.setNegativeButton("تراجع", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdatePrice(position);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id", cartList.get(position).getId());
                intent.putExtra("qty", cartList.get(position).getQty());
                context.startActivity(intent);
            }
        });

        if (cartList.get(position).isFavourite())
            holder.like.setImageResource(R.drawable.ic_action_liked);
        else
            holder.like.setImageResource(R.drawable.ic_action_unliked);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                {
                    if (cartList.get(position).isFavourite()) {
                        listener.onUpdateLike(position, false);
                        removeFromFav(holder.like, position);
                        //holder.like.setImageResource(R.drawable.ic_action_unliked);
                        cartList.get(position).setFavourite(false);
                    } else {
                        listener.onUpdateLike(position, true);
                        addToFav(holder.like, position);
                        //holder.like.setImageResource(R.drawable.ic_action_liked);
                        cartList.get(position).setFavourite(true);
                    }
                }
                else
                    Toast.makeText(context, "برجاء التسجيل/تسجيل الدخول لتتمكن من استخدام سلة الأمنيات", Toast.LENGTH_SHORT).show();

            }
        });

       /* if (user != null) {
            if (cartList.get(position).isFavourite()) {
                holder.like.setImageResource(R.drawable.ic_action_liked);
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFav(holder.like, position);
                        holder.like.setImageResource(R.drawable.ic_action_unliked);
                        cartList.get(position).setFavourite(false);
                    }
                });
            } else {
                holder.like.setImageResource(R.drawable.ic_action_unliked);
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToFav(holder.like, position);
                        holder.like.setImageResource(R.drawable.ic_action_liked);
                        cartList.get(position).setFavourite(true);
                    }
                });
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void addToFav(final ImageView img, final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(cartList.get(pos).getId()));
        volley.asyncStringPost(WayaaakAPP.BASE_URL + "addtofavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "add to fav " + s);
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("false")) {
                    listener.onUpdateLike(pos, false);
                }
                else
                    listener.onUpdateLike(pos, true);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onUpdateLike(pos, false);
                Toast.makeText(context, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeFromFav(final ImageView img, final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(cartList.get(pos).getId()));
        volley.asyncStringPost(WayaaakAPP.BASE_URL + "removefromfavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "remove from fav " + s);
                Status response = new Gson().fromJson(s, Status.class);
                if (response.getStatus().equals("OK")) {
                    if (listener != null) {
                        listener.onUpdateLike(pos,false);

                    }
                } else {
                    listener.onUpdateLike(pos,true);

                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onUpdateLike(pos,true);
                Toast.makeText(context, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView photo, like;
        TextView title, price, total, remove, edit;
        Spinner qty;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.cart_container);
            photo = itemView.findViewById(R.id.photo_img);
            remove = itemView.findViewById(R.id.remove_tvbtn);
            edit = itemView.findViewById(R.id.edit_tvbtn);
            like = itemView.findViewById(R.id.like_img);
            title = itemView.findViewById(R.id.title_txt);
            price = itemView.findViewById(R.id.price_txt);
            qty = itemView.findViewById(R.id.qty_spin);
            total = itemView.findViewById(R.id.total_txt);
        }
    }

    public interface onUpdateListener {
        void onUpdate();
        void onUpdateLike(int pos, boolean status);
        void onUpdatePrice(int pos);
    }
    public void setCustomButtonListner(onUpdateListener listener) {
        this.listener = listener;
    }
}
