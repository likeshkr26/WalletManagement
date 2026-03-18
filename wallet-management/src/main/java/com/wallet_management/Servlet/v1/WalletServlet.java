package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_management.Model.DoTransaction;
import com.wallet_management.Model.Transaction;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Service.TransactionService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/wallet/*")
public class WalletServlet extends HttpServlet {

    private WalletService walletService=new WalletService();
    private TransactionService transactionService=new TransactionService();
    private ObjectMapper mapper=new ObjectMapper();

    //get wallet by id
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path=req.getPathInfo();

        if(path==null)
        {
            res.sendError(400,"Wallet id required in urlll");
            return;
        }
        String parts[]=path.split("/");
        
        try{

            if(parts.length==0)
            {
                res.sendError(400,"Wallet id required in urlll");
                return;
            }

            if(parts[1]=="" || parts[1].trim().isEmpty())
            {
                res.sendError(400,"Enter wallet id in the urll");
                return;
            }
            int wallet_id;

            try {
                wallet_id=Integer.parseInt(parts[1]);
            } catch (Exception e) {
                res.sendError(400,"invalid wallet id formattt");
                return;
            }

            wallet_id=Integer.parseInt(parts[1]);

            //to get wallet information
            if(parts.length==2)
            {

                Wallet w=walletService.getWalletByID(wallet_id);

                if(w==null)
                {
                    res.sendError(400, "Wallet not foundd");
                    return;
                }

                mapper.writeValue(res.getWriter(), w);

            }
            

            //to get transactions of a wallet
            else if(parts.length==3 && parts[2].equals("transactions"))
            {

                if(parts[1].trim().isEmpty())
                {
                    res.sendError(400,"Enter wallet id in the urll");
                    return;
                }

                wallet_id=Integer.parseInt(parts[1]);

                Wallet w=walletService.getWalletByID(wallet_id);

                if(w==null)
                {
                    res.sendError(400,"Wallet not found");
                    return;
                }


                List<Transaction> transactions=transactionService.getTransaction(wallet_id);

                mapper.writeValue(res.getWriter(), transactions);
            }
        }
        catch(Exception e)
        {
            res.setStatus(500,e.getMessage());
        }
    }

    //perform topup & purchase
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        DoTransaction doTransaction=mapper.readValue(req.getInputStream(), DoTransaction.class);

        
        Integer type;
        Double amount;

        try{
            type=doTransaction.getType();
        }
        catch(Exception e)
        {
            res.sendError(400, "Enter valid type value");
            return;
        }

        try{
            amount=doTransaction.getAmount();
        }
        catch(Exception e)
        {
            res.sendError(400, "Enter valid amount value");
            return;
        }

        if(type==null || amount==null)
        {
            res.sendError(400,"Enter both type and amount");
            return;
        }

        if(type!=1 && type!=2 && type!=3)
        {
            res.sendError(400,"Enter valid type (1,2,3)");
            return;
        }

        String path=req.getPathInfo();

        if(path == null){
            res.sendError(400,"Invalid path");
            return;
        }

        String parts[]=path.split("/");

        try{


            if(parts.length < 2 || parts[1].trim().isEmpty())
            {
                res.sendError(400,"Enter wallet id in the urll");
                return;
            }
            int wallet_id;

            try {
                wallet_id=Integer.parseInt(parts[1]);
            } catch (Exception e) {
                res.sendError(400,"invalid wallet id formattt");
                return;
            }

            wallet_id=Integer.parseInt(parts[1]);

            Wallet w=walletService.getWalletByID(wallet_id);

            if(w==null)
            {
                res.sendError(400,"Wallet not found");
                return;
            }

            if(w.getActive()==0)
            {
                res.sendError(400,"The wallet is inactive");
                return;
            }

            if(amount<0)
            {
                res.sendError(400, "Amount should be a positive value");
                return;
            }

            if(amount==0)
            {
                res.sendError(400, "Amount to topup/purchase cannot be 0");
                return;
            }

            if(amount>9999999999999.99)
            {
                res.sendError(400, "Amount should be of 15 digits with last 2 digit as decimal");
                return;
            }


            if(parts.length==3 && parts[2].equals("transaction"))
            {
                transactionService.doTransaction(wallet_id, 0, type, amount);
            }

            mapper.writeValue(res.getWriter(), Map.of("message","The transaction has been completed"));
            res.setStatus(201);
            res.getWriter().flush();

        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }

}
