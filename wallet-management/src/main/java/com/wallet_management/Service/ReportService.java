package com.wallet_management.Service;

import java.sql.Connection;
import java.util.List;

import com.wallet_management.Model.Pair;
import com.wallet_management.dao.Reportdao;
import com.wallet_management.util.DBConnection;

public class ReportService {
    
    private Reportdao reportdao=new Reportdao();

    public int getTransactionCountByWallet(int wallet_id,String from,String to) throws Exception
    {
        Connection con= DBConnection.getConnection();
        return reportdao.getTransactionCountByWallet(con, wallet_id, from, to);
    }

    public int getTransactionCountByUser(int user_id,String from,String to) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.getTransactionCountByUser(con, user_id, from, to);
    }

    public double totalAmountSpendFromWallet(int wallet_id,String from,String to) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.totalAmountSpendFromWallet(con, wallet_id, from, to);
    }

    public double totalAmountSpentByUser(int user_id,String from,String to) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.totalAmountSpentByUser(con, user_id, from, to);
    }

    public List<Pair> topTenSpendingUser(String from,String to) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.topTenSpendingUser(con, from, to);
    }

    public double avgSpend(int user_id,String from,String to) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.avgSpend(con, user_id, from, to);
    }

    public List<Integer> inactiveWallet() throws Exception
    {
        Connection con=DBConnection.getConnection();

        return reportdao.inactiveWallet(con);
    }

}
