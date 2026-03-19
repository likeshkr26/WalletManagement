package com.wallet_management.Model;

public class TransferRequest {
    
    String transfer_id;

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
    }

    public TransferRequest(String transfer_id) {
        this.transfer_id = transfer_id;
    }

    public TransferRequest() {
    }

    
}
