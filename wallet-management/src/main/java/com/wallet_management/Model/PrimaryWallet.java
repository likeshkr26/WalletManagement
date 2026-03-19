package com.wallet_management.Model;

public class PrimaryWallet {
    
    Integer primary_wallet;
    Integer wallet_id;
    Integer active;

    public Integer getPrimary_wallet() {
        return primary_wallet;
    }

    public void setPrimary_wallet(Integer primary_wallet) {
        this.primary_wallet = primary_wallet;
    }

    public Integer getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }

   

    

    public PrimaryWallet(Integer primary_wallet, Integer wallet_id, Integer active) {
        this.primary_wallet = primary_wallet;
        this.wallet_id = wallet_id;
        this.active = active;
    }

    public PrimaryWallet() {
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    
}
