package com.ibtikar.apps.wayaaak.Models;

import java.util.List;

public class Category {
    int id;
    String name, image;
    List<ListItem> sub_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ListItem> getSub_list() {
        return sub_list;
    }

    public void setSub_list(List<ListItem> sub_list) {
        this.sub_list = sub_list;
    }
}
