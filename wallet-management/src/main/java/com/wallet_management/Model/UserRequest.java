package com.wallet_management.Model;

public class UserRequest {
    
    String operation;
    Integer wallet_id;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }



    public UserRequest(String operation, Integer wallet_id) {
        this.operation = operation;
        this.wallet_id = wallet_id;
    }

    public UserRequest() {
    }

    public Integer getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }

    
}
