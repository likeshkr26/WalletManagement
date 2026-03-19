package com.wallet_management.Model;

public class UserRequest {
    
    String operation;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public UserRequest(String operation) {
        this.operation = operation;
    }

    public UserRequest() {
    }

    
}
