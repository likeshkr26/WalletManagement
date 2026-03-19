package com.wallet_management.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.wallet_management.Model.User;
import com.wallet_management.Model.Wallet;
import com.wallet_management.dao.Userdao;
import com.wallet_management.util.DBConnection;

public class UserService {

    private Userdao userdao=new Userdao();
    private WalletService walletService=new WalletService();
    
    public void createUser(User user) throws Exception
    {
        Connection con=DBConnection.getConnection();
        
        userdao.createUser(con, user);
    }

    public User getUserByID(int user_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return userdao.getUserByID(con, user_id);
    }

    public List<com.wallet_management.Model.Wallet> getWalletByUser(int user_id,int page,int size,String sort,String order,Integer active,String column,String value) throws Exception
    {
        Connection con= DBConnection.getConnection();

        return userdao.getWalletByUser(con, user_id, page, size, sort, order, active,column,value);
    }

    public int getWalletCountByUser(int user_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        return userdao.getWalletCountByUser(con, user_id);
    }

    public void updatePrimaryWallet(int user_id,int wallet_id) throws Exception
    {
        Connection con=DBConnection.getConnection();

        Wallet wallet=walletService.getWalletByID(wallet_id);

        if(wallet==null)
        {
            throw new Exception("Wallet not found");
        }

        if(wallet.getUser_id()!=user_id)
        {
            throw new Exception("user is not associated with the given wallet id");
        }

        userdao.updatePrimaryWallet(con, user_id, wallet_id);
    }

    public boolean updateUser(int id,String name,int primaryWallet) throws Exception {

        Connection con=DBConnection.getConnection();

        return userdao.updateUser(con,id, name, primaryWallet);
    }

    public void deleteUser(int user_id) throws Exception
    {
        try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false); // start transaction

        User u = getUserByID(user_id);

        if (u == null) {
            throw new Exception("User id not found");
        }

        int count = userdao.checkWallet(con, user_id);

        if (count != 0) {
            throw new Exception("Delete all associated wallet with your user id");
        }

        userdao.deleteUser(con, user_id);

        con.commit(); 
        }
        catch (Exception e) {
            throw e;
        }

    }

}
