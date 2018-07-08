package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Product;
import com.ibtikar.apps.wayaaak.Models.SuggestedProduct;

import java.util.ArrayList;
import java.util.List;

public class SuggestedItemsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("Products")
    @Expose
    private List<SuggestedProduct> products = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<SuggestedProduct> getProducts() {
        return new ArrayList<>(products);
    }

    public void setProducts(List<SuggestedProduct> products) {
        this.products = products;
    }

}
