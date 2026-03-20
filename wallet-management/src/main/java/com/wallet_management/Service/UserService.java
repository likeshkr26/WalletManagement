package com.wallet_management.Service;

import java.sql.Connection;
import java.util.List;

import com.wallet_management.Model.User;
import com.wallet_management.Model.Wallet;
import com.wallet_management.dao.Userdao;
import com.wallet_management.util.DBConnection;
import com.wallet_management.util.ValidationUtil;

public class UserService {

    private Userdao userdao=new Userdao();
    private WalletService walletService=new WalletService();
    
    // public void createUser(User user) throws Exception
    // {
    //     Connection con=DBConnection.getConnection();
        
    //     userdao.createUser(con, user);
    // }

    public void createUser(User user) throws Exception {

        String validName =ValidationUtil.validateName(user.getName(), "Name");

        user.setName(validName);

        Connection con = DBConnection.getConnection();
        userdao.createUser(con, user);
    }

    // public User getUserByID(int user_id) throws Exception
    // {
    //     Connection con=DBConnection.getConnection();

    //     return userdao.getUserByID(con, user_id);
    // }

    public User getUserByID(int userId) throws Exception {

        if (userId <= 0)
            throw new Exception("Invalid user id");

        Connection con = DBConnection.getConnection();
        return userdao.getUserByID(con, userId);
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

    // public void updatePrimaryWallet(int user_id,int wallet_id) throws Exception
    // {
    //     Connection con=DBConnection.getConnection();

    //     Wallet wallet=walletService.getWalletByID(wallet_id);

    //     if(wallet==null)
    //     {
    //         throw new Exception("Wallet not found");
    //     }

    //     if(wallet.getUser_id()!=user_id)
    //     {
    //         throw new Exception("user is not associated with the given wallet id");
    //     }

    //     userdao.updatePrimaryWallet(con, user_id, wallet_id);
    // }

    public void updatePrimaryWallet(int userId, Integer walletId) throws Exception {

        if (userId <= 0)
            throw new Exception("Invalid user id");

        if (walletId == null || walletId <= 0)
            throw new Exception("Primary wallet id required");

        Connection con = DBConnection.getConnection();

        // check user exists
        User user = userdao.getUserByID(con, userId);
        if (user == null)
            throw new Exception("User not found");

        // check wallet
        Wallet wallet = walletService.getWalletByID(walletId);

        if (wallet == null)
            throw new Exception("Wallet not found");

        if (wallet.getUser_id() != userId)
            throw new Exception("User is not associated with given wallet");

        userdao.updatePrimaryWallet(con, userId, walletId);
    }

    public boolean updateUser(int userId,String name,Integer primaryWalletId) throws Exception {

    if (userId <= 0)
        throw new Exception("Invalid user id");

    name = ValidationUtil.validateName(name, "Name");

    if (primaryWalletId == null || primaryWalletId <= 0)
        throw new Exception("Primary wallet required");

    Connection con = DBConnection.getConnection();

    
    User user = userdao.getUserByID(con, userId);

    if (user == null)
        throw new Exception("User not found");

    
    Wallet wallet = walletService.getWalletByID(primaryWalletId);

    if (wallet == null)
        throw new Exception("Wallet not found");

    if (wallet.getUser_id() != userId)
        throw new Exception("Wallet does not belong to user");

    return userdao.updateUser(con, userId, name, primaryWalletId);
}


    public void deleteUser(int userId) throws Exception {

    if (userId <= 0)
        throw new Exception("Invalid user id");

    Connection con = DBConnection.getConnection();

    try {

        User user = userdao.getUserByID(con, userId);

        if (user == null)
            throw new Exception("User not found");

        int walletCount = userdao.checkWallet(con, userId);

        if (walletCount != 0)
            throw new Exception("Delete all associated wallets before deleting user");

        userdao.deleteUser(con, userId);

    }
    catch (Exception e) {

        throw e;
    }
    
}

}
