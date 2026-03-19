package com.wallet_management.dao;

import java.rmi.ServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wallet_management.Model.Pair;

public class Reportdao {
    
    //get transaction count by wallet id
    public int getTransactionCountByWallet(Connection con,int wallet_id,String from,String to) throws ServerException,Exception
    {

        try{
            
            PreparedStatement ps=con.prepareStatement("select coalesce(count(t.transaction_id),0) as TransactionCount from Transaction t join Wallet w on t.wallet_id=w.wallet_id where t.wallet_id=? and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day);");
            ps.setInt(1, wallet_id);
            ps.setString(2, from);
            ps.setString(3, to);

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

    //get transaction count by user id
    public int getTransactionCountByUser(Connection con,int user_id,String from,String to) throws ServerException,Exception{

        try{

            PreparedStatement ps=con.prepareStatement("select coalesce(count(t.transaction_id),0) as TransactionCount from Transaction t join Wallet w on t.wallet_id=w.wallet_id where w.user_id=? and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day);");
            ps.setInt(1, user_id);
            ps.setString(2, from);
            ps.setString(3, to);

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

    //get total amount spent from a wallet
    public double totalAmountSpendFromWallet(Connection con,int wallet_id,String from,String to) throws ServerException,Exception{

        try{

            PreparedStatement ps=con.prepareStatement("select coalesce(sum(t.amount),0) as TotalSpent from Transaction t join Wallet w on t.wallet_id=w.wallet_id where t.wallet_id=? and t.type in(2,3) and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day);");
            ps.setInt(1, wallet_id);
            ps.setString(2, from);
            ps.setString(3, to);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getDouble(1);
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

        return 0;
    }

    //total amount spend by a user
    public double totalAmountSpentByUser(Connection con,int user_id,String from ,String to) throws ServerException,Exception
    {
        try{

            PreparedStatement ps=con.prepareStatement("select coalesce(sum(t.amount),0) as TotalSpent from Transaction t join Wallet w on t.wallet_id=w.wallet_id where w.user_id=? and t.type in(2,3) and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day);");
            ps.setInt(1, user_id);
            ps.setString(2, from);
            ps.setString(3, to);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getDouble(1);
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

        return 0;
    }

    //top 10 spending users
    public List<Pair> topTenSpendingUser(Connection con,String from,String to) throws ServerException,Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select u.name,coalesce(sum(t.amount),0) as total_spent from user u left join wallet w on w.user_id=u.user_id left join transaction t on t.wallet_id=w.wallet_id and type in(2,3) and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day) group by u.user_id,u.name order by total_spent desc,u.name asc limit 10;");
            ps.setString(1, from);
            ps.setString(2, to);

            ResultSet rs=ps.executeQuery();
            List<Pair> list=new ArrayList<>();

            while(rs.next())
            {
                list.add(new Pair(rs.getString(1),rs.getDouble(2)));
            }

            return list;
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
    }

    //avg spend of a user
    public double avgSpend(Connection con,int user_id,String from,String to) throws ServerException,Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select coalesce(avg(t.amount),0) as TotalSpent from Transaction t join Wallet w on t.wallet_id=w.wallet_id where w.user_id=? and t.type in(2,3) and t.created_at>=? and t.created_at<Date_ADD(?,interval 1 day);");
            ps.setInt(1, user_id);
            ps.setString(2, from);
            ps.setString(3, to);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getDouble(1);
            }
        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }
        return 0;
    }

    //wallets that are expired
    public List<Integer> inactiveWallet(Connection con) throws ServerException,Exception
    {

        try{

            PreparedStatement ps=con.prepareStatement("select wallet_id from wallet where active=0");

            ResultSet rs=ps.executeQuery();
            List<Integer> list=new ArrayList<>();

            while(rs.next())
            {
                list.add(rs.getInt(1));
            }

            return list;

        }
        catch(Exception e)
        {
            throw new Exception("Error: "+e.getMessage());
        }

    }

}
