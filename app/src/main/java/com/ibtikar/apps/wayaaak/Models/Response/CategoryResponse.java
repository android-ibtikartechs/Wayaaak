package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Category;

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

    public List<Category> getData() {
        return data;
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
