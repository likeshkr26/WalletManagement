package com.wallet_management.Model;

public class DoTransaction {
    
    Integer type;
    Double amount;
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public DoTransaction(Integer type, Double amount) {
        this.type = type;
        this.amount = amount;
    }
    public DoTransaction() {
    }

    
}
