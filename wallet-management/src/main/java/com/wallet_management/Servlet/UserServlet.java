package com.wallet_management.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.wallet_management.Model.User;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Service.UserService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    
    private UserService userService=new UserService();
    private WalletService walletService=new WalletService();

    //create user
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
    {

        String path=req.getPathInfo();
        String name=req.getParameter("name");
        int wallet_id=Integer.parseInt(req.getParameter("wallet_id"));

        try{


            if(path==null || path.equals("/"))
            {
                User user=new User(1,name,-1);
                userService.createUser(user);

                res.getWriter().write("User Created Successfully");
                return;
            }

            String parts[]=path.split("/");

            if(parts.length==3 && parts[2].equals("wallets"))
            {
                int user_id=Integer.parseInt(parts[1]);
                walletService.createWallet(name, user_id);

                res.getWriter().write("Wallet create successfully");
            }

            else if(parts.length==3 && parts[2].equals("primary"))
            {
                int user_id=Integer.parseInt(parts[1]);
                userService.updatePrimaryWallet(user_id, wallet_id);

                res.getWriter().write("User's Primary wallet updated");
            }


            
        }
        catch(Exception e)
        {
            res.sendError(500,e.getMessage());
        }
    }

    //Get operations
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {

        String path=req.getPathInfo();

        try{

            String parts[]=path.split("/");

            int user_id=Integer.parseInt(parts[1]);

            if(parts.length==2)
            {
                User user=userService.getUserByID(user_id);

                res.getWriter().write("User ID: "+user.getUser_id()+",User Name: "+user.getName()+"User Primary Wallet: "+user.getPrimary_wallet());
            }
            else if(parts.length==3 && parts[2].equals("wallet"))
            {
                List<Wallet> wallets=userService.getWalletByUser(user_id);

                if(wallets.size()==0)
                {
                    res.getWriter().write("No wallet for this user");
                }
                
                PrintWriter out=res.getWriter();

                for(Wallet w:wallets)
                {
                    out.println("Wallet ID: "+w.getWallet_id()+", Wallet Name: "+w.getName()+", Wallet Balance: "+w.getBalance()+", Wallet's User: "+w.getUser_id()+", Wallet Active: "+w.getActive());
                }
            }
            else if(parts.length==4 && parts[3].equals("count"))
            {
                int count=userService.getWalletCountByUser(user_id);
                res.getWriter().write("No of Wallet owned by user: "+count);

            }
        }
        catch(Exception e)
        {
            res.sendError(500,e.getMessage());
        }
    }

}
