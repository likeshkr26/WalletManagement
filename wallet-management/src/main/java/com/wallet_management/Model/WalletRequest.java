package com.wallet_management.Model;

public class WalletRequest {
    
    Integer active;
    Integer user_id;

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public WalletRequest() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public WalletRequest(Integer active, Integer user_id) {
        this.active = active;
        this.user_id = user_id;
    }

    

    
}
