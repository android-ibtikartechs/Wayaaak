package com.ibtikar.apps.wayaaak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.SuggestedProductsAdapter;
import com.ibtikar.apps.wayaaak.Fragment.Cart_Fragment;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.Models.Response.ProductResponse;
import com.ibtikar.apps.wayaaak.Models.Response.SuggestedItemsResponse;
import com.ibtikar.apps.wayaaak.Models.Status;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends BaseActivity implements SuggestedProductsAdapter.ContainerClickListener {
    TextView title, price, sellername, cat_name, add_btn, oPrice;
    WebView details;
    ImageView photo, like, cart, back, btnIncrease, btnDecrease;
    ConstraintLayout loutFooter;
    VolleySimple volleySimple;
    Spinner qty_spin;
    User user;
    EditText etQuantity;
    boolean liked;
    ProductResponse response;


    RecyclerView rvSuggestedList;
    SuggestedProductsAdapter categoryAdapter;

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
        oPrice = findViewById(R.id.product_offer_price_txt);
        sellername = findViewById(R.id.product_seller_name_txt);
        details = findViewById(R.id.product_details_txt);
        photo = findViewById(R.id.product_img);
        like = findViewById(R.id.product_like_img);
        back = findViewById(R.id.toolbar_back_ico);
        cart = findViewById(R.id.product_cart_img);
        add_btn = findViewById(R.id.add_btn);
        loutFooter = findViewById(R.id.lout_footer);
        qty_spin = findViewById(R.id.qty_spin);
        rvSuggestedList = findViewById(R.id.rv_products);
        etQuantity = findViewById(R.id.et_quantity);
        btnIncrease = findViewById(R.id.im_btn_increase_quantity);
        btnDecrease = findViewById(R.id.im_btn_decrease_quantity);
        rvSuggestedList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
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


                    if(response.getProduct().getOprice().isEmpty() || response.getProduct().getOprice().equals("0"))
                    {
                        oPrice.setText(response.getProduct().getPrice());
                        price.setVisibility(View.GONE);
                    }
                    else
                    {
                        price.setText(response.getProduct().getPrice() );
                        Log.d("TAG", "onBindViewHolder: " + response.getProduct().getOprice());
                        //} else {
                        oPrice.setText(response.getProduct().getOprice()  );
                        // holder.price.setText(products.get(position).getOprice()  );
                        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }


                    if (!response.getProduct().getOprice().equals("0"))
                        price.setText(response.getProduct().getPrice() + "ج");
                    else
                        price.setText(response.getProduct().getOprice() + "ج");

                    Glide.with(ProductActivity.this).load(response.getProduct().getImage()).asBitmap().into(photo);
                    sellername.setText(sellername.getText() + response.getProduct().getSellername());
                    //details.setText(details.getText() + response.getProduct().getDetails());


                    details.setWebViewClient(new WebViewClient() {

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // TODO Auto-generated method stub

                            return false;
                        }
                    });
                    WebSettings webSettings2 = details.getSettings();
                    webSettings2.setJavaScriptEnabled(true);
                    details.loadDataWithBaseURL("", buildHtml(response.getProduct().getDetails()), "text/html", "utf-8", "");



                    if (response.getProduct().getIsfavourite() != null) {
                        if (response.getProduct().getIsfavourite()) {
                            like.setImageResource(R.drawable.ic_action_liked);
                            liked = true;
                        } else {
                            like.setImageResource(R.drawable.ic_action_unliked);
                            liked = false;
                        }
                    }

                    like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (liked)
                            {
                                removeFromFav(like, response.getProduct().getId());
                                like.setImageResource(R.drawable.ic_action_unliked);
                                response.getProduct().setIsfavourite(false);
                                liked = false;
                            }
                            else
                            {
                                addToFav(like, response.getProduct().getId());
                                like.setImageResource(R.drawable.ic_action_liked);
                                response.getProduct().setIsfavourite(true);
                                liked = true;
                            }
                        }
                    });


                    getSuggestedProducts(response.getProduct().getCategoryid());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }


    private void  getSuggestedProducts(String catName)
    {
        Map<String, String> map = new HashMap<>();
        map.put("category", catName);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "categoryproducts_rand", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                SuggestedItemsResponse response = new Gson().fromJson(s, SuggestedItemsResponse.class);
                if (response.isStatus())
                {
                    categoryAdapter = new SuggestedProductsAdapter(ProductActivity.this,response.getProducts());
                    categoryAdapter.setCustomButtonListner(ProductActivity.this);
                    rvSuggestedList.setAdapter(categoryAdapter);
                }

                loutFooter.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Exception e) {

            }
        });


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
                like.setImageResource(R.drawable.ic_action_unliked);
                response.getProduct().setIsfavourite(false);
                liked = false;
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
                like.setImageResource(R.drawable.ic_action_liked);
                response.getProduct().setIsfavourite(true);
                liked = true;
            }
        });
    }






    public void addToCart() {
        Cart cartitem = new Cart();
        cartitem.setId(response.getProduct().getId());
        cartitem.setName(response.getProduct().getName());
        if (response.getProduct().getIsfavourite() != null)
            cartitem.setFavourite(response.getProduct().getIsfavourite().equals("yes"));
        cartitem.setQty(Integer.valueOf(etQuantity.getText().toString()));
        cartitem.setPrice(Integer.valueOf(response.getProduct().getOprice().equals("0")  ? response.getProduct().getPrice() : response.getProduct().getOprice()));
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

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Integer.valueOf(etQuantity.getText().toString()) >= 10)
                {
                    etQuantity.setText("10");
                    Toast.makeText(ProductActivity.this, "الحد الأقصى للكمية 10 وحدات", Toast.LENGTH_SHORT).show();
                }


                else if (!(etQuantity.getText().toString().isEmpty() || etQuantity.getText().toString().equals("0")) ) {
                    addToCart();
                }


                else {
                    Toast.makeText(ProductActivity.this, "اختر الكمية اولا", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQuantity.getText().toString().isEmpty())
                    etQuantity.setText("1");
                else if (!etQuantity.getText().toString().equals("0"))
                    etQuantity.setText(String.valueOf(Integer.valueOf(etQuantity.getText().toString())-1));

            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQuantity.getText().toString().isEmpty())
                    etQuantity.setText("1");
                else if (Integer.valueOf(etQuantity.getText().toString()) >= 10)
                {
                    etQuantity.setText("10");
                    Toast.makeText(ProductActivity.this, "الحد الأقصى للكمية 10 وحدات", Toast.LENGTH_SHORT).show();
                }
                else
                    etQuantity.setText(String.valueOf(Integer.valueOf(etQuantity.getText().toString())+1));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.this.onBackPressed();
            }
        });
    }

    public void dymmyClick(View view) {
    }

    private String buildHtml (String text)
    {
        String resultHtml = "<html dir=\"rtl\" lang=\"ar\">\n" +
                "  <head>\n" +
                "    <link rel=\"stylesheet\"\n" +
                "          href=\"https://fonts.googleapis.com/css?family=Cairo\">\n" +
                "    <style>\n" +
                "      body {\n" +
                "        font-family: 'Cairo', sans-serif;\n" +

                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body bgcolor=\"#ffffff\">\n" +
                "    <div>" + decodeBase64(text) + "</div>\n" +
                "  </body>\n" +
                "</html>";
        return resultHtml;
    }

    private String decodeBase64(String coded){
      /*  byte[] valueDecoded= new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }*/
        byte[] data = Base64.decode(coded, Base64.DEFAULT);

        String decoded = null;
        try {
            decoded = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return decoded;
    }

    @Override
    public void onItemClickListener(String id, String title) {
        finish();
        Log.d("", "suggested product id: " + id);
        Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
        intent.putExtra("id", Integer.valueOf(id));
        startActivity(intent);
    }
}
