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
    public void doTransaction(int wallet_id,int receiver,int type,double amount) throws Exception
    {
        Connection con=DBConnection.getConnection();

        double balance=walletdao.getBalance(con, wallet_id);

        String transferId = UUID.randomUUID().toString();

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
            if(amount>balance)
            {
                throw new Exception("Amount insufficient");
            }
            transactiondao.doTransaction(con, wallet_id, balance-amount);
            transactiondao.logTransaction(con, wallet_id, amount, type,transferId);
            return;
        }

        //transfer
        else if(type==3)
        {
            if(amount>balance)
            {
                throw new Exception("Amount insufficient");
            }

            transactiondao.doTransaction(con, wallet_id, balance-amount);
            transactiondao.logTransaction(con, wallet_id, amount, type,transferId);

            double receiverBalance=walletdao.getBalance(con, receiver);

            transactiondao.doTransaction(con, receiver, receiverBalance+amount);
            transactiondao.logTransaction(con, receiver, amount, 4,transferId);

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
            transactiondao.updateWallet(con, list.get(0), senderAmount-amount);
        }
        else
        {
            transactiondao.updateWallet(con, list.get(0), senderAmount+amount);
        }

        if(list.size()==2)
        {
            Wallet w2=walletService.getWalletByID(list.get(1));
            double receiverAmount=w2.getBalance();

            if(receiverAmount<amount)
            {
                throw new Exception("Amount insufficient to do transfer");
            }
            transactiondao.updateWallet(con, list.get(1), receiverAmount-amount);
        }
        
        transactiondao.deleteTransaction(con, transfer_id);
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

}
