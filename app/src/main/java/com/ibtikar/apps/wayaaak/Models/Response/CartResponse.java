package com.ibtikar.apps.wayaaak.Models.Response;

import com.ibtikar.apps.wayaaak.Models.Cart;

import java.util.List;

public class CartResponse {
    List<Cart> cartList;

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}
