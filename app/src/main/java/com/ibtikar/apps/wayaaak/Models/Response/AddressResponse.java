package com.ibtikar.apps.wayaaak.Models.Response;

import com.ibtikar.apps.wayaaak.Models.AddressBook;

import java.util.List;

public class AddressResponse {
    String status;
    List<AddressBook> Addressbook;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AddressBook> getAddressbook() {
        return Addressbook;
    }

    public void setAddressbook(List<AddressBook> addressbook) {
        this.Addressbook = addressbook;
    }
}
