package com.ibtikar.apps.wayaaak.Models;

import java.util.List;

public class HomeCategory {
    int id, parentcategory_id;
    String name, image, parentcategory_name;

    List<ListItem> Sublist;

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

    public List<ListItem> getSublist() {
        return Sublist;
    }

    public void setSublist(List<ListItem> sublist) {
        Sublist = sublist;
    }

    public int getParentcategory_id() {
        return parentcategory_id;
    }

    public void setParentcategory_id(int parentcategory_id) {
        this.parentcategory_id = parentcategory_id;
    }

    public String getParentcategory_name() {
        return parentcategory_name;
    }

    public void setParentcategory_name(String parentcategory_name) {
        this.parentcategory_name = parentcategory_name;
    }
}
