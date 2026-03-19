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

    public int isActive(Connection con,int wallet_id) throws Exception{
        
        PreparedStatement ps=con.prepareStatement("select active from wallet where wallet_id=?");
        ps.setInt(1, wallet_id);

        ResultSet rs=ps.executeQuery();

        if(rs.next())
        {
            return rs.getInt(1);
        }

        return 0;
    }

    public boolean updateWallet(Connection con,int wallet_id,String name,int active) throws Exception
    {
        int rows;

        try{
            PreparedStatement ps=con.prepareStatement("update wallet set name=?,active=? where wallet_id=?");
            ps.setString(1, name);
            ps.setInt(2, active);
            ps.setInt(3, wallet_id);

            rows=ps.executeUpdate();
        }
        catch(Exception e)
        {
            throw new Exception("Error"+e.getMessage());
        }

        return rows>0;
    }

    public void updateInactive(Connection con,int wallet_id,int active) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("update wallet set active=? where wallet_id=?");
            ps.setInt(1, active);
            ps.setInt(2, wallet_id);

            int rows=ps.executeUpdate();

            if(rows==0)
            {
                throw new Exception("No wallet is modified");
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    }

    public int checkTransaction(Connection con,int wallet_id) throws Exception
    {
        try{
            PreparedStatement ps=con.prepareStatement("select count(*) as count from transaction where wallet_id=?");
            ps.setInt(1, wallet_id);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getInt("count");
            }
        }
        catch(Exception e)
        {
            throw new Exception();
        }
        return 0;
    }

    public void deleteWallet(Connection con,int wallet_id) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("delete from wallet where wallet_id=?");
            ps.setInt(1, wallet_id);

            int rows=ps.executeUpdate();

            if(rows==0)
            {
                throw new Exception("No wallet deleted");
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error:"+e.getMessage());
        }
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
