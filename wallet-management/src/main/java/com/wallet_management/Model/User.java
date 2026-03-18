package com.wallet_management.Model;

public class User {
    
    int user_id;
    String name;
    int primary_wallet;
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrimary_wallet() {
        return primary_wallet;
    }
    public void setPrimary_wallet(int primary_wallet) {
        this.primary_wallet = primary_wallet;
    }
    public User(int user_id, String name, int primary_wallet) {
        this.user_id = user_id;
        this.name = name;
        this.primary_wallet = primary_wallet;
    }
    public User() {
    }

    
}
