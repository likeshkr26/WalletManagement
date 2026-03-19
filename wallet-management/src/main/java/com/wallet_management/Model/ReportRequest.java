package com.wallet_management.Model;

public class ReportRequest {
    
    String operation;
    String from;
    String to;
    String resource;
    Integer user_id;
    Integer wallet_id;
    

    public String getFrom() {
        return from;
    }
    public ReportRequest() {
    }
    
    public ReportRequest(String operation, String from, String to) {
        this.operation = operation;
        this.from = from;
        this.to = to;
    }

    public ReportRequest(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public Integer getWallet_id() {
        return wallet_id;
    }
    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }
    public void setTo(String to) {
        this.to = to;
    }
    
    
    

    
}
