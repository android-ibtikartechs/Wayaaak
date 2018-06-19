package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.LocationItem;

import java.util.List;

public class LocationListResponse {
    String status;
    @SerializedName("List")
    List<LocationItem> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LocationItem> getData() {
        return data;
    }

    public void setData(List<LocationItem> data) {
        this.data = data;
    }
}
