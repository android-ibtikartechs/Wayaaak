package com.ibtikar.apps.wayaaak.Models;

import java.util.List;

public class Product {
    int id;
    String image, name, sellerid, sellername, category, intro, details, price, oprice, available_from, available_to,categoryid;
    Boolean isfavourite;
    List<Gallery> Gallery;

    public int getId() {
        return id;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOprice() {
        return oprice;
    }

    public void setOprice(String oprice) {
        this.oprice = oprice;
    }

    public String getAvailable_from() {
        return available_from;
    }

    public void setAvailable_from(String available_from) {
        this.available_from = available_from;
    }

    public String getAvailable_to() {
        return available_to;
    }

    public void setAvailable_to(String available_to) {
        this.available_to = available_to;
    }

    public List<com.ibtikar.apps.wayaaak.Models.Gallery> getGallery() {
        return Gallery;
    }

    public void setGallery(List<com.ibtikar.apps.wayaaak.Models.Gallery> gallery) {
        Gallery = gallery;
    }

    public Boolean getIsfavourite() {
        return isfavourite;
    }

    public void setIsfavourite(Boolean isfavourite) {
        this.isfavourite = isfavourite;
    }
}
