package com.wallet_management.Model;

public class User {
    
    Integer user_id;
    String name;
    Integer primary_wallet;
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrimary_wallet() {
        return primary_wallet;
    }
    public void setPrimary_wallet(Integer primary_wallet) {
        this.primary_wallet = primary_wallet;
    }
    public User(Integer user_id, String name, Integer primary_wallet) {
        this.user_id = user_id;
        this.name = name;
        this.primary_wallet = primary_wallet;
    }
    public User() {
    }

    
}
