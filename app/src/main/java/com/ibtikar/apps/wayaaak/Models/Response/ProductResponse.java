package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Product;

public class ProductResponse {
    String status;
    @SerializedName("Details")
    Product product;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
