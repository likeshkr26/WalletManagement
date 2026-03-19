package com.wallet_management.Model;

public class Transaction {
    
    int transaction_id;
    int wallet_id;
    double amount;
    String timestamp;
    int type;
    String transfer_id;
    
    public int getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }
    public int getWallet_id() {
        return wallet_id;
    }
    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    
    public Transaction(int transaction_id, int wallet_id, double amount, String timestamp, int type,
            String transfer_id) {
        this.transaction_id = transaction_id;
        this.wallet_id = wallet_id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.transfer_id = transfer_id;
    }
    public Transaction() {
    }
    public String getTransfer_id() {
        return transfer_id;
    }
    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
    }

    
}
