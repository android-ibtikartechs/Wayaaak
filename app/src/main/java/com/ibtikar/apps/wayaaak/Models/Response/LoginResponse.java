package com.ibtikar.apps.wayaaak.Models.Response;

import com.ibtikar.apps.wayaaak.Models.User;

public class LoginResponse {
    String status;
    User user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
