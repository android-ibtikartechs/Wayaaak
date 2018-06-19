package com.ibtikar.apps.wayaaak.Models.Response;

import com.ibtikar.apps.wayaaak.Models.HomeCategory;
import com.ibtikar.apps.wayaaak.Models.Product;

import java.util.List;

public class HomeResponse {
    String status;
    List<Product> SliderProducts;
    List<HomeCategory> FrontCategories;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getSliderProducts() {
        return SliderProducts;
    }

    public void setSliderProducts(List<Product> sliderProducts) {
        SliderProducts = sliderProducts;
    }

    public List<HomeCategory> getFrontCategories() {
        return FrontCategories;
    }

    public void setFrontCategories(List<HomeCategory> frontCategories) {
        FrontCategories = frontCategories;
    }
}
