package com.ibtikar.apps.wayaaak.Models.Response;

import com.google.gson.annotations.SerializedName;
import com.ibtikar.apps.wayaaak.Models.Order;

public class OrderResponse {
    @SerializedName("details")
    Order order;
    String status;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
