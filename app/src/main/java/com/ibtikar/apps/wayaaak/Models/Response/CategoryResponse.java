package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Category;
import com.ibtikar.apps.wayaaak.Models.ListItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponse {
    String status;
    @SerializedName("List")
    List<Category> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addDataAll(){
        for (int i = 0; i < data.size(); i++) {
            ListItem item = new ListItem();
            item.setId(data.get(i).getId());
            item.setName("الكل");
            data.get(i).getSub_list().add(item);
        }
    }

    public ArrayList<Category> getData() {
        return new ArrayList<>(data);
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public int getCount() {
        int size = data.size();
        for (int i = 0; i < data.size(); i++) {
            size += data.get(i).getSub_list().size();
        }
        return size;
    }

    public List<String> getMain_List() {
        List<String> main = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            main.add(data.get(i).getName());
        }
        return main;
    }
}
