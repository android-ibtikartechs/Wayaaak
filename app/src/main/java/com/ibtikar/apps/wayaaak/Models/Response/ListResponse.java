package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Product;

import java.util.List;

public class ListResponse {
    String status;
    @SerializedName(value = "Result", alternate = {"Products", "List", "Favourites"})
    List<Product> Products;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }
}
