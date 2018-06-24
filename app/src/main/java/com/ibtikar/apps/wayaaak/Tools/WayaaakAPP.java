package com.ibtikar.apps.wayaaak.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.ibtikar.apps.wayaaak.Models.Cart;
import com.ibtikar.apps.wayaaak.Models.Response.CartResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hosam Azzam on 2/22/2018.
 */

public class WayaaakAPP {

    public static String BASE_URL = "http://wayaaak.ibtikarprojects.com/mobile/";

    public static boolean USER_ISLOGIN = false;
    public static List<Cart> cartProducts = new ArrayList<>();
    public static String KEY_CITY_ID = "cityId";
    public static String KEY_AREA_ID = "areaId";
    public static String KEY_CAT_ID = "catId";
    public static String KEY_FLAG_SEARCH = "key_flag_search";
    public static String URL_AUOTHORITY = "wayaaak.ibtikarprojects.com";

    public static void changeLang(Context context, String lang) { // func to change lang
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Language", lang);
        editor.apply();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void registerUserLogin(Context context, User userModule) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserObject", new Gson().toJson(userModule));
        editor.apply();
    }

    public static void unRegisterUserLogin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserObject", null);
        editor.apply();
        clearCartList(context);
    }

    public static Boolean getUserLoginState(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userobject = preferences.getString("UserObject", "");
        USER_ISLOGIN = !userobject.equals("");
        return USER_ISLOGIN;
    }

    public static Boolean isFirstOpen(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int open = preferences.getInt("firstOpen", 0);
        return open == 0;
    }

    public static void setFirstOpen(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("firstOpen", 1);
        editor.apply();
    }

    public static User getUserLoginInfo(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userobject = preferences.getString("UserObject", null);
        if (userobject != null) {
            try {
                return new Gson().fromJson(userobject, User.class);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    // get hash key for facebook
    public static String printHashKey(Context ctx) {
        // Add code to print out the key hash
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return "SHA-1 generation: " + Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "SHA-1 generation: the key count not be generated: NameNotFoundException thrown";
        } catch (NoSuchAlgorithmException e) {
            return "SHA-1 generation: the key count not be generated: NoSuchAlgorithmException thrown";
        }

        return "SHA-1 generation: epic failed";
    }

    public static int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenWidthDP(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return (int) (displaymetrics.widthPixels / activity.getResources().getDisplayMetrics().density);

    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static void addToCart(Context context, Cart item) {
        boolean found = false;
        for (int i = 0; i < cartProducts.size(); i++) {
            if (cartProducts.get(i).getId() == item.getId()) {
                cartProducts.get(i).setQty(item.getQty());
                cartProducts.get(i).setTotal(cartProducts.get(i).getQty() * cartProducts.get(i).getPrice());
                found = true;
                break;
            }
        }
        if (!found) {
            cartProducts.add(item);
        }
        CartResponse response = new CartResponse();
        response.setCartList(cartProducts);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartProducts", new Gson().toJson(response));
        editor.apply();
    }

    public static void removeFromCart(Context context, Cart item) {
        for (int i = 0; i < cartProducts.size(); i++) {
            if (cartProducts.get(i).getId() == item.getId()) {
                cartProducts.remove(i);
                break;
            }
        }
        CartResponse response = new CartResponse();
        response.setCartList(cartProducts);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartProducts", new Gson().toJson(response));
        editor.apply();
    }

    public static List<Cart> getCartProducts(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString("cartProducts", null);
        List<Cart> cartlist = new ArrayList<>();
        if (result != null)
            cartProducts = new Gson().fromJson(result, CartResponse.class).getCartList();
        return result == null ? cartlist : cartProducts;
    }

    public static void initCartProducts(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString("cartProducts", null);
        if (result != null)
            cartProducts = new Gson().fromJson(result, CartResponse.class).getCartList();
    }

    public static void clearCartList(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartProducts", null);
        editor.apply();
        cartProducts = new ArrayList<>();
    }

    public static double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < cartProducts.size(); i++) {
            total += cartProducts.get(i).getTotal();
        }
        return total;
    }

}
