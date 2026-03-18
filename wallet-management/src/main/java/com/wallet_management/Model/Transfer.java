package com.wallet_management.Model;

public class Transfer {
    
    Integer from;
    Integer to;
    Double amount;
    public Integer getFrom() {
        return from;
    }
    public void setFrom(Integer from) {
        this.from = from;
    }
    public Integer getTo() {
        return to;
    }
    public void setTo(Integer to) {
        this.to = to;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Transfer(Integer from, Integer to, Double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
    public Transfer() {
    }

    
    
}
