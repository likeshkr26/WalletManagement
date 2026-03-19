package com.wallet_management.Service;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import com.wallet_management.Model.Transaction;
import com.wallet_management.Model.Wallet;
import com.wallet_management.dao.Transactiondao;
import com.wallet_management.dao.Walletdao;
import com.wallet_management.util.DBConnection;

public class TransactionService {
    
    private Transactiondao transactiondao=new Transactiondao();
    private WalletService walletService=new WalletService();
    private Walletdao walletdao=new Walletdao();

    //perform transaction
    public void doTransaction(int wallet_id,int receiver_id,int type,double amount) throws Exception
    {
        Connection con=DBConnection.getConnection();

            if(wallet_id==receiver_id)
            {
                throw new Exception("Sender and receiver id cannot be same");
            }

            Wallet w=walletService.getWalletByID(wallet_id);
            Wallet receiverWallet = null;
            
            if(type==3)
                receiverWallet=walletService.getWalletByID(receiver_id);

            if(w==null)
            {
                throw new Exception("Sender Wallet not found");
            }

            if(type==3 && receiverWallet==null)
            {
                throw new Exception("Receiver Wallet not found");
            }

            if(w.getActive()==0)
            {
                throw new Exception("The sender wallet is inactive");
            }

            if(type==3 && receiverWallet.getActive()==0)
            {
                throw new Exception("The receiver wallet is inactive");
            }

            if(amount<0)
            {
                throw new Exception("Amount should be a positive value");
            }

            if(amount==0)
            {
                throw new Exception("Amount for cannot be 0");
            }

            if(amount>9999999999999.99)
            {
                throw new Exception("Amount should be of 15 digits with last 2 digit as decimal");
            }


        double balance=walletdao.getBalance(con, wallet_id);

        String transferId = UUID.randomUUID().toString();

        if(amount>balance)
        {
            throw new Exception("Amount insufficient");
        }

        //topup
        if(type==1)
        {
            transactiondao.doTransaction(con, wallet_id, balance+amount);
            transactiondao.logTransaction(con, wallet_id, amount, type,transferId);
            return;
        }

        //purchase
        else if(type==2)
        {
            
            transactiondao.doTransaction(con, wallet_id, balance-amount);
            transactiondao.logTransaction(con, wallet_id, amount, type,transferId);
            return;
        }

        //transfer
        else if(type==3)
        {

            transactiondao.doTransaction(con, wallet_id, balance-amount);
            transactiondao.logTransaction(con, wallet_id, amount, type,transferId);

            double receiverBalance=walletdao.getBalance(con, receiver_id);

            transactiondao.doTransaction(con, receiver_id, receiverBalance+amount);
            transactiondao.logTransaction(con, receiver_id, amount, 4,transferId);

            return;
        }

    }

    //get transaction by wallet_id
    public List<Transaction> getTransaction(int wallet_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return transactiondao.getTransaction(con, wallet_id);
    }

    public void deleteTransaction(String transfer_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        Transaction t=getTransactionByTransferId(transfer_id);

        if(t == null)
        {
            throw new Exception("Transaction not found");
        }
            

        int type=t.getType();
        
        double amount=t.getAmount();

        List<Integer> list=getWalletId(transfer_id);

        if(list.size()==0)
        {
            throw new Exception("Transaction not found");
        }

        Wallet w1=walletService.getWalletByID(list.get(0));
        double senderAmount=w1.getBalance();

        if(type==1)
        {
            if(amount>senderAmount)
            {
                throw new Exception("Amount insufficient");
            }

            try{
                transactiondao.updateWallet(con, list.get(0), senderAmount-amount);
                transactiondao.deleteTransaction(con, transfer_id);
            }
            catch(Exception e)
            {
                throw new Exception("Error: "+e.getMessage());
            }
            
            return;
        }
        else
        {
            try{
                transactiondao.updateWallet(con, list.get(0), senderAmount+amount);
            }
            catch(Exception e)
            {
                throw new Exception("Error while updating wallet: "+e.getMessage());
            }
            
        }

        if(list.size()==2)
        {
            Wallet w2=walletService.getWalletByID(list.get(1));
            double receiverAmount=w2.getBalance();

            if(receiverAmount<amount)
            {
                throw new Exception("Amount insufficient to do delete transfer");
            }

            try{
                transactiondao.updateWallet(con, list.get(1), receiverAmount-amount);
            }
            catch(Exception e)
            {
                throw new Exception("Error while updating wallet: "+e.getMessage());
            }
        }
        
        try{
            transactiondao.deleteTransaction(con, transfer_id);
        }
        catch(Exception e)
        {
            throw new Exception("Error while deleting transaction entry: "+e.getMessage());
        }

    }

    public Transaction getTransactionByTransferId(String transfer_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return transactiondao.getTransactionByTransferId(con, transfer_id);
    }

    public List<Integer> getWalletId(String transfer_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return transactiondao.getWalletsByTransferId(con, transfer_id);
    }

    public Transaction getTransactionDetail(int transaction_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        Transaction t=transactiondao.getTransactionDetail(con,transaction_id);

        if(t==null)
        {
            throw new Exception("Transaction not found");
        }

        return t;
    }

}
