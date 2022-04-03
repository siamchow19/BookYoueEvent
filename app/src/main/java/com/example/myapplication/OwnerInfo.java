package com.example.myapplication;

public class OwnerInfo {
    String name, email, ownerId;

    public OwnerInfo() {

    }

    public OwnerInfo(String name, String email, String buyerId) {
        this.name = name;
        this.email = email;
        this.ownerId = buyerId;
    }

    public String getOwnerName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
