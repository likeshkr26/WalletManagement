package com.wallet_management.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.wallet_management.Model.Wallet;

public class Walletdao {

    //create wallet
    public void createWallet(Connection con,String name,int user_id) throws Exception
    {
        PreparedStatement ps=con.prepareStatement("insert into wallet(name,user_id) values(?,?)");
        ps.setString(1,name);
        ps.setInt(2,user_id);

        int rows=ps.executeUpdate();

        if(rows==0)
        {
            throw new Exception("Wallet not created");
        }

    }

    //get wallet
    public Wallet getWalletByID(Connection con,int wallet_id) throws Exception
    {
        
        PreparedStatement ps=con.prepareStatement("select * from wallet where wallet_id=?");
        ps.setInt(1,wallet_id);

        ResultSet rs=ps.executeQuery();

        if(rs.next())
        {
            Wallet wallet=new Wallet();
            wallet.setWallet_id(rs.getInt("wallet_id"));
            wallet.setName(rs.getString("name"));
            wallet.setBalance(rs.getDouble("balance"));
            wallet.setUser_id(rs.getInt("user_id"));
            wallet.setActive(rs.getInt("active"));

            return wallet;
        }

        return null;   
    }
    
    public double getBalance(Connection con,int wallet_id) throws Exception{

        PreparedStatement ps=con.prepareStatement("select balance from wallet where wallet_id=? and active=1");

        ps.setInt(1,wallet_id);

        ResultSet rs=ps.executeQuery();

        if(rs.next())
        {
            return rs.getDouble("balance");
        }

        throw new Exception("Wallet not found or inactive");
    }






    


    //this function is not used
    public void updateBalance(Connection con,int wallet_id,double newBalance) throws Exception{

        PreparedStatement ps=con.prepareStatement("update wallet set balance=? where wallet_id=? and active=1");

        ps.setDouble(1,newBalance);
        ps.setInt(2,wallet_id);

        int rows=ps.executeUpdate();

        if(rows==0)
        {
            throw new Exception("Wallet not found or inactive");
        }

    }


}
