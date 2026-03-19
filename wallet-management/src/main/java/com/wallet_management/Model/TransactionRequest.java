package com.wallet_management.Model;

public class TransactionRequest {
    
    Integer wallet_id;
    Integer type;
    Double amount;
    Integer receiver_id;
    String transaction_unique_id;
    Integer transaction_id;

    public Integer getWallet_id() {
        return wallet_id;
    }
    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }
    public TransactionRequest() {
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getReceiver_id() {
        return receiver_id;
    }
    public Integer getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }
    public String getTransaction_unique_id() {
        return transaction_unique_id;
    }
    public void setTransaction_unique_id(String transaction_unique_id) {
        this.transaction_unique_id = transaction_unique_id;
    }
    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    
}
