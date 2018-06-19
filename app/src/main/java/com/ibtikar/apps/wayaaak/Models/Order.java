package com.ibtikar.apps.wayaaak.Models;

import java.util.List;

public class Order {
    int id, userid, sellerid, chargetype;
    String username, userpic, sellername, sellerpic, date, time, comment, created_at;

    AddressBook address;
    List<DealItem> Booking_deals;
    List<ProductItem> Booking_products;
    Cost cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public int getChargetype() {
        return chargetype;
    }

    public void setChargetype(int chargetype) {
        this.chargetype = chargetype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSellerpic() {
        return sellerpic;
    }

    public void setSellerpic(String sellerpic) {
        this.sellerpic = sellerpic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public AddressBook getAddress() {
        return address;
    }

    public void setAddress(AddressBook address) {
        this.address = address;
    }

    public List<DealItem> getBooking_deals() {
        return Booking_deals;
    }

    public void setBooking_deals(List<DealItem> booking_deals) {
        Booking_deals = booking_deals;
    }

    public List<ProductItem> getBooking_products() {
        return Booking_products;
    }

    public void setBooking_products(List<ProductItem> booking_products) {
        Booking_products = booking_products;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}
