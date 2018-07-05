package com.ibtikar.apps.wayaaak;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Fragment.Cart_Fragment;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.Models.Response.ProductResponse;
import com.ibtikar.apps.wayaaak.Models.Status;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    TextView title, price, sellername, details, cat_name, add_btn;
    ImageView photo, like, cart, back;
    VolleySimple volleySimple;
    Spinner qty_spin;
    User user;
    ProductResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sec);
        volleySimple = VolleySimple.getInstance(this);
        user = WayaaakAPP.getUserLoginInfo(this);

        initView();
        init();
        listener();
    }

    public void initView() {
        cat_name = findViewById(R.id.product_cat_name_txt);
        title = findViewById(R.id.product_title_txt);
        price = findViewById(R.id.product_price_txt);
        sellername = findViewById(R.id.product_seller_name_txt);
        details = findViewById(R.id.product_details_txt);
        photo = findViewById(R.id.product_img);
        like = findViewById(R.id.product_like_img);
        back = findViewById(R.id.toolbar_back_ico);
        cart = findViewById(R.id.product_cart_img);
        add_btn = findViewById(R.id.add_btn);

        qty_spin = findViewById(R.id.qty_spin);
        //qty_spin.setSelection(getIntent().getIntExtra("qty", 0), true);
        //View v = qty_spin.getSelectedView();
        //((TextView) v).setTextColor(Color.WHITE);

        cart.setColorFilter(Color.parseColor("#333333"), PorterDuff.Mode.SRC_ATOP);
        WayaaakAPP.setBadgeCount(this, (LayerDrawable) cart.getDrawable(), String.valueOf(WayaaakAPP.getCartProducts(this).size()));
    }

    public void init() {
        String link = "product_details/" + getIntent().getIntExtra("id", 0);
        if (user != null) link += "/" + user.getId();
        ProgressDialog progressDialog = ProgressDialog.show(this, "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + link, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                response = new Gson().fromJson(s, ProductResponse.class);
                if (response.getStatus().equals("OK")) {
                    cat_name.setText(response.getProduct().getCategory());
                    title.setText(response.getProduct().getName());
                    if (!response.getProduct().getOprice().equals("0"))
                        price.setText(response.getProduct().getPrice() + "ج");
                    else
                        price.setText(response.getProduct().getOprice() + "ج");

                    Glide.with(ProductActivity.this).load(response.getProduct().getImage()).asBitmap().into(photo);
                    sellername.setText(sellername.getText() + response.getProduct().getSellername());
                    details.setText(details.getText() + response.getProduct().getDetails());
                    if (response.getProduct().getIsfavourite() == null) {
                        response.getProduct().setIsfavourite("no");
                        if (response.getProduct().getIsfavourite().equals("yes")) {
                            Toast.makeText(ProductActivity.this, "like", Toast.LENGTH_SHORT).show();
                            like.setImageResource(R.drawable.ic_action_liked);
                            like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //removeFromFav(like, response.getProduct().getId());
                                    like.setImageResource(R.drawable.ic_action_unliked);

                                    response.getProduct().setIsfavourite("no");
                                }
                            });
                        } else {
                            Toast.makeText(ProductActivity.this, "like", Toast.LENGTH_SHORT).show();
                            like.setImageResource(R.drawable.ic_action_unliked);
                            like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //addToFav(like, response.getProduct().getId());
                                    like.setImageResource(R.drawable.ic_action_liked);

                                    response.getProduct().setIsfavourite("yes");
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }

    public void addToFav(final ImageView img, int id) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(id));
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "addtofavourites", map, new VolleySimple.NetworkListener<String>() {
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

    public void removeFromFav(final ImageView img, final int id) {
        Map<String, String> map = new HashMap<>();
        map.put("user", String.valueOf(user.getId()));
        map.put("product", String.valueOf(id));
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "removefromfavourites", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                Status response = new Gson().fromJson(s, Status.class);
                if (!response.getStatus().equals("OK")) {
                    img.setImageResource(R.drawable.ic_action_liked);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void addToCart() {
        Cart cartitem = new Cart();
        cartitem.setId(response.getProduct().getId());
        cartitem.setName(response.getProduct().getName());
        if (response.getProduct().getIsfavourite() != null)
            cartitem.setFavourite(response.getProduct().getIsfavourite().equals("yes"));
        //cartitem.setQty(qty_spin.getSelectedItemPosition());
        cartitem.setPrice(Integer.valueOf(response.getProduct().getOprice().equals("0") ? response.getProduct().getPrice() : response.getProduct().getOprice()));
        cartitem.setPhoto(response.getProduct().getImage());
        cartitem.setTotal(cartitem.getQty() * cartitem.getPrice());
        WayaaakAPP.addToCart(this, cartitem);
        Toast.makeText(this, "تم الاضافة للسلة", Toast.LENGTH_LONG).show();
        //qty_spin.setSelection(0);
        WayaaakAPP.setBadgeCount(this, (LayerDrawable) cart.getDrawable(), String.valueOf(WayaaakAPP.getCartProducts(this).size()));

    }

    public void listener() {
        /*qty_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Change the selected item's text color
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart_Fragment cart_fragment = new Cart_Fragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("FromInnerCart", true);
                cart_fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.product_fragment_container, cart_fragment, "cart_fragment")
                        .addToBackStack("cart_fragment")
                        .commit();
            }
        });
/*
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty_spin.getSelectedItemPosition() != 0) {
                    addToCart();
                } else {
                    Toast.makeText(ProductActivity.this, "اختر الكمية اولا", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.this.onBackPressed();
            }
        });
    }

    public void dymmyClick(View view) {

    }
}
