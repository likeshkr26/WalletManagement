package com.wallet_management.Model;

public class TopRequest {
    
    String from;
    String to;
    String operation;
    public String getFrom() {
        return from;
    }
    public TopRequest() {
    }
    public TopRequest(String from, String to, String operation) {
        this.from = from;
        this.to = to;
        this.operation = operation;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }

    
}
