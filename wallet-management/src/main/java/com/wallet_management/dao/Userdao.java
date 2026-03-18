package com.wallet_management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.wallet_management.Model.User;
import com.wallet_management.Model.Wallet;
import com.wallet_management.util.DBConnection;

public class Userdao {

    //creating new user method
    public void createUser(Connection con,User user) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("insert into user(name,primary_wallet) values(?,?)");

            ps.setString(1,user.getName());
            ps.setInt(2,user.getPrimary_wallet());

            int rows=ps.executeUpdate();

            if(rows==0)
            {
                throw new Exception("User not created");
            }

        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    } 

    //get user by id
    public User getUserByID(Connection con,int user_id) throws Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select * from user where user_id=?");

            ps.setInt(1,user_id);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                User user=new User();
                user.setUser_id(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setPrimary_wallet(rs.getInt("primary_wallet"));

                return user;

            }
        }
        catch(Exception e)
        {
            throw new Exception("Error "+ e.getMessage());
        }


        return null;
    }

    //get wallet by user id
    public List<Wallet> getWalletByUser(Connection con,int user_id) throws Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select * from wallet where user_id=?");
            ps.setInt(1,user_id);

            ResultSet rs=ps.executeQuery();

            List<Wallet> wallets=new ArrayList<>();

            while(rs.next())
            {
                Wallet wallet=new Wallet();
                wallet.setWallet_id(rs.getInt("wallet_id"));
                wallet.setName(rs.getString("name"));
                wallet.setBalance(rs.getDouble("balance"));
                wallet.setUser_id(rs.getInt("user_id"));
                wallet.setActive(rs.getInt("active"));
                wallets.add(wallet);
            }

            return wallets;
        }
        catch(Exception e)
        {
            throw new Exception("Error "+e.getMessage());
        }

    }

    //get wallet count of a user
    public int getWalletCountByUser(Connection con,int user_id) throws Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select count(*) from wallet where user_id=?");
            ps.setInt(1,user_id);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

        return -1;

    }

    public void updatePrimaryWallet(Connection con,int user_id,int wallet_id) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("update user set primary_wallet=? where user_id=?");
            ps.setInt(1, wallet_id);
            ps.setInt(2, user_id);

            int row=ps.executeUpdate();

            if(row==0)
            {
                throw new Exception("User's primary wallet is not updated");
            }

        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    }

    public boolean updateUser(int id,
                          String name,
                          int primaryWallet) throws Exception {

    Connection con = DBConnection.getConnection();

    String sql =
        "UPDATE user SET name=?, primary_wallet=? WHERE user_id=?";

    PreparedStatement ps = con.prepareStatement(sql);

    ps.setString(1, name);
    ps.setInt(2, primaryWallet);
    ps.setInt(3, id);

    int rows = ps.executeUpdate();

    return rows > 0; 
}



}
