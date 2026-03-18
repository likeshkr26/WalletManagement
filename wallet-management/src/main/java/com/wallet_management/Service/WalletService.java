package com.wallet_management.Service;

import java.sql.Connection;

import com.wallet_management.Model.Wallet;
import com.wallet_management.dao.Transactiondao;
import com.wallet_management.dao.Walletdao;
import com.wallet_management.util.DBConnection;

public class WalletService {

    private Walletdao wallet=new Walletdao();
    private Transactiondao transaction=new Transactiondao();


    public void createWallet(String name,int user_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        wallet.createWallet(con, name, user_id);

    }

    public Wallet getWalletByID(int wallet_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return wallet.getWalletByID(con, wallet_id);
    }








    public void topup(int wallet_id, double amount) throws Exception{

        Connection con=DBConnection.getConnection();
        
        try{
            double Balance=wallet.getBalance(con,wallet_id);
            double newBalance=Balance+amount;

            wallet.updateBalance(con,wallet_id,newBalance);
            transaction.logTransaction(con,wallet_id,amount,1);
            
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
        finally{
            con.close();
        }
    }
    
}
