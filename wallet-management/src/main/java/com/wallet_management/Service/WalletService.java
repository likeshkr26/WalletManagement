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

    public int isActive(int wallet_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return wallet.isActive(con, wallet_id);
    }

    public boolean updateWallet(int wallet_id,String name,int active) throws Exception
    {
        Connection con=DBConnection.getConnection();

        if(active!=0 && active!=1)
        {
            throw new Exception("The active state should be either 1 or 0");
        }

        if(name.length()>20)
        {
            throw new Exception("Name should be within 20 characters");
        }


        try{
            return wallet.updateWallet(con,wallet_id,name,active);
        }
        catch(Exception e)
        {
            throw new Exception("Error updating wallet");
        }
        
    }

    public void updateInactive(int wallet_id,int user_id,int active) throws Exception
    {
        Connection con=DBConnection.getConnection();
        
        Wallet w;

        try{
            w=getWalletByID(wallet_id);
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

        if(user_id!=w.getUser_id())
        {
            throw new Exception("This user is not associated with the wallet");
        }

        wallet.updateInactive(con,wallet_id,active);

    }

    public void deleteWallet(int wallet_id,int user_id) throws Exception
    {

        Connection con=DBConnection.getConnection();

        Wallet w;

        try{
            w=getWalletByID(wallet_id);
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

        if(w==null)
        {
            throw new Exception("Wallet not found");
        }

        if(user_id!=w.getUser_id())
        {
            throw new Exception("This user is not associated with the wallet");
        }

        int count=wallet.checkTransaction(con, wallet_id);

        if(count!=0)
        {
            throw new Exception("Delete all associated transactions made from this wallet");
        }

        wallet.deleteWallet(con,wallet_id);

    }






    //This function is not used

    public void topup(int wallet_id, double amount) throws Exception{

        Connection con=DBConnection.getConnection();
        
        try{
            double Balance=wallet.getBalance(con,wallet_id);
            double newBalance=Balance+amount;

            wallet.updateBalance(con,wallet_id,newBalance);
            transaction.logTransaction(con,wallet_id,amount,1,"");
            
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
