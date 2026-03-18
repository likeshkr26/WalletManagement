package com.wallet_management.Service;

import java.sql.Connection;
import java.util.List;

import com.wallet_management.Model.Transaction;
import com.wallet_management.dao.Transactiondao;
import com.wallet_management.dao.Walletdao;
import com.wallet_management.util.DBConnection;

public class TransactionService {
    
    private Transactiondao transactiondao=new Transactiondao();
    private Walletdao walletdao=new Walletdao();

    //perform transaction
    public void doTransaction(int wallet_id,int receiver,int type,double amount) throws Exception
    {
        Connection con=DBConnection.getConnection();

        double balance=walletdao.getBalance(con, wallet_id);

        //topup
        if(type==1)
        {
            transactiondao.doTransaction(con, wallet_id, balance+amount);
            transactiondao.logTransaction(con, wallet_id, amount, type);
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
            transactiondao.logTransaction(con, wallet_id, amount, type);
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
            transactiondao.logTransaction(con, wallet_id, amount, type);

            double receiverBalance=walletdao.getBalance(con, receiver);

            transactiondao.doTransaction(con, receiver, receiverBalance+amount);
            transactiondao.logTransaction(con, receiver, amount, 4);

            return;
        }

    }

    //get transaction by wallet_id
    public List<Transaction> getTransaction(int wallet_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return transactiondao.getTransaction(con, wallet_id);
    }

}
