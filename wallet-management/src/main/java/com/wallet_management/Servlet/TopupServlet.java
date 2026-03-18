











//The file is not used











package com.wallet_management.Servlet;

import java.io.IOException;

import com.wallet_management.Service.WalletService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/topup")
public class TopupServlet extends HttpServlet {
    
    private WalletService walletService=new WalletService();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{

        res.setContentType("text/plain");

        try{

            int wallet_id=Integer.parseInt(req.getParameter("wallet_id"));
            double amount=Double.parseDouble(req.getParameter("amount"));

            if(amount<=0){
                res.getWriter().write("Enter valid amount");
                return;
            }

            walletService.topup(wallet_id,amount);
            res.getWriter().write("Topup for amount "+amount+" is successful");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            res.setStatus(500);
            res.getWriter().write("Topup failer: "+e.getMessage());
        }

        
    }

}
