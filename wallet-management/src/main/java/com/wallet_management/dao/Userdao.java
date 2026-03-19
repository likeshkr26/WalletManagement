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
            throw new Exception("DB Error: "+e.getMessage());
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
            throw new Exception("DB Error "+ e.getMessage());
        }


        return null;
    }

    //get wallet by user id
    public List<Wallet> getWalletByUser(Connection con,int user_id,int page,int size,String sort,String order,Integer active,String column,String value) throws Exception {

        List<String> allowedSort =List.of("wallet_id","name","balance","active");

        List<String> allowedSearch =List.of("wallet_id","name","balance","active");

        if(sort == null || !allowedSort.contains(sort))
            sort = "wallet_id";

        if(order == null ||(!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")))
            order = "asc";

        StringBuilder sql =new StringBuilder("select * from wallet where user_id=?");

        if(active != null) {
            sql.append(" and active=?");
        }

        boolean applySearch =
                column != null && value != null && !value.isBlank();

        if(applySearch) {

            if(!allowedSearch.contains(column)) {
                throw new Exception("Invalid search column");
            }

            sql.append(" and ").append(column).append(" = ?");
        }

        sql.append(" order by ").append(sort).append(" ").append(order);

        sql.append(" limit ? offset ?");

        PreparedStatement ps =con.prepareStatement(sql.toString());

        int index = 1;

        ps.setInt(index++, user_id);

        if(active != null) {
            ps.setInt(index++, active);
        }

        if(applySearch) {

            if(column.equals("wallet_id") || column.equals("active")) {
                ps.setInt(index++, Integer.parseInt(value));
            }
            else if(column.equals("balance")) {
                ps.setDouble(index++, Double.parseDouble(value));
            }
            else {
                ps.setString(index++, value);
            }
        }

        int offset = (page - 1) * size;

        ps.setInt(index++, size);
        ps.setInt(index++, offset);

        ResultSet rs = ps.executeQuery();

        List<Wallet> wallets = new ArrayList<>();

        while(rs.next()) {
            Wallet wallet = new Wallet();
            wallet.setWallet_id(rs.getInt("wallet_id"));
            wallet.setName(rs.getString("name"));
            wallet.setBalance(rs.getDouble("balance"));
            wallet.setUser_id(rs.getInt("user_id"));
            wallet.setActive(rs.getInt("active"));

            wallets.add(wallet);
        }

        return wallets;
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
            throw new Exception("DB Error: "+e.getMessage());
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
            throw new Exception("DB Error: "+e.getMessage());
        }
    }

    public boolean updateUser(Connection con,int id,String name,int primaryWallet) throws Exception {


    PreparedStatement ps = con.prepareStatement("UPDATE user SET name=?, primary_wallet=? WHERE user_id=?");

    ps.setString(1, name);
    ps.setInt(2, primaryWallet);
    ps.setInt(3, id);

    int rows = ps.executeUpdate();

    return rows > 0; 
}

    public int checkWallet(Connection con,int user_id) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("select count(*) as count from wallet where user_id=?");
            ps.setInt(1, user_id);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
            {
                return rs.getInt("count");
            }
        }
        catch(Exception e)
        {
            throw new Exception("DB Error: "+e.getMessage());
        }
        return 0;
    }

    public void deleteUser(Connection con,int user_id) throws Exception
    {

        try{
            PreparedStatement ps=con.prepareStatement("delete from user where user_id=?");
            ps.setInt(1, user_id);

            int rows=ps.executeUpdate();

            if(rows==0)
            {
                throw new Exception("No user deleted");
            }
        }
        catch(Exception e)
        {
            throw new Exception("DB Error: "+e.getMessage());
        }
    }

}
