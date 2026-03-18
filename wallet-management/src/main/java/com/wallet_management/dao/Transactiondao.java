package com.wallet_management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.wallet_management.Model.Transaction;

public class Transactiondao {
    
    //log transaction
    public void logTransaction(Connection con,int wallet_id, double amount,int type) throws Exception{

        PreparedStatement ps=con.prepareStatement("insert into transaction(wallet_id,amount,type) values(?,?,?)");
        ps.setInt(1,wallet_id);
        ps.setDouble(2,amount);
        ps.setInt(3,type);

        ps.executeUpdate();
    }
    
    //perform transaction
    public void doTransaction(Connection con,int wallet_id,double amount) throws Exception
    {
        
        try{

            PreparedStatement ps=con.prepareStatement("update wallet set balance=? where wallet_id=?");
            ps.setDouble(1, amount);
            ps.setInt(2,wallet_id);

            int rows=ps.executeUpdate();

            if(rows==0)
            {
                throw new Exception("No wallet balance is updated. Wrong wallet it");
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    }

    //get transaction by wallet id
    public List<Transaction> getTransaction(Connection con,int wallet_id) throws Exception
    {
        
        try{

            PreparedStatement ps=con.prepareStatement("select * from transaction where wallet_id=?");
            ps.setInt(1, wallet_id);

            ResultSet rs=ps.executeQuery();

            List<Transaction> transaction=new ArrayList<>();

            while(rs.next())
            {
                Transaction t=new Transaction();
                t.setTransaction_id(rs.getInt("transaction_id"));
                t.setWallet_id(rs.getInt("wallet_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setTimestamp(rs.getString("created_at"));
                t.setType(rs.getInt("type"));
                transaction.add(t);
            }

            return transaction;
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    }

}
