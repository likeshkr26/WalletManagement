package com.wallet_management.Model;

public class Wallet {
    
    int wallet_id;
    String name;
    double balance;
    int user_id;
    int active;
    
    public int getWallet_id() {
        return wallet_id;
    }
    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }
    public String getName() {
        return name;
    }
    public Wallet() {
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public Wallet(int wallet_id, String name, double balance, int user_id, int active) {
        this.wallet_id = wallet_id;
        this.name = name;
        this.balance = balance;
        this.user_id = user_id;
        this.active = active;
    }

    

}
