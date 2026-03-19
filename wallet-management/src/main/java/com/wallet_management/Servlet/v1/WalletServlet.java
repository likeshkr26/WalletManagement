package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_management.Model.DoTransaction;
import com.wallet_management.Model.Transaction;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Model.WalletRequest;
import com.wallet_management.Service.TransactionService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.ServletException;
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


    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws IOException {

    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    String path = req.getPathInfo();

    // /wallet/{id}
    if (path == null || path.equals("/")) {
        res.sendError(400, "Wallet id required in URL");
        return;
    }

    String[] parts = path.split("/");

    if (parts.length < 2) {
        res.sendError(400, "Invalid URL");
        return;
    }

    int wallet_id;

        try {
            wallet_id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            res.sendError(400, "Invalid wallet id format");
            return;
        }


        Wallet wallet=mapper.readValue(req.getInputStream(), Wallet.class);

        try{
            Wallet w=walletService.getWalletByID(wallet_id);

            if(w==null)
            {
                res.sendError(400,"Wallet not found");
                return;
            }
            else if(w.getUser_id()!=wallet.getUser_id())
            {
                res.sendError(400,"Wallet not belong to the user");
                return;
            }
        }
        catch(Exception e)
        {
            res.sendError(500,"Internal server error: "+e.getMessage());
            return;
        }


        try{
            boolean updated=walletService.updateWallet(wallet_id,wallet.getName(),wallet.getActive());
            
            if (!updated) {
                res.sendError(404, "Wallet not found");
                return;
            }
            res.setStatus(200);

            mapper.writeValue(res.getWriter(),Map.of("message", "Wallet updated successfully"));
        }
        catch (Exception e) {
            res.sendError(500, "Internal server error"+e.getMessage());
        }
    
    }

    @Override
    protected void service(HttpServletRequest req,
                        HttpServletResponse resp)
                        throws ServletException,IOException {

        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            try {
                doPatch(req, resp);
            } catch (ServletException e) {
   
                e.printStackTrace();
            } catch (IOException e) {
               
                e.printStackTrace();
            } catch (Exception e) {
             
                e.printStackTrace();
            }
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req,HttpServletResponse res) throws ServletException,Exception
    {
        try {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            String path=req.getPathInfo();
            String parts[]=path.split("/");

            if(parts.length==1)
            {
                res.sendError(400,"Enter wallet in the url");
                return;
            }

            if(parts[1]==null || parts[1].trim().isEmpty())
            {
                res.sendError(400,"Enter wallet in the url");
                return;
            }
            int wallet_id;

            try {
                 wallet_id=Integer.parseInt(parts[1]);
            } catch (Exception e) {
                res.sendError(400,"invalid wallet id format");
                return;
            }

            WalletRequest activeWalletRequest=mapper.readValue(req.getInputStream(), WalletRequest.class);


                Integer activeStatus = activeWalletRequest.getActive();
                Integer user_id=activeWalletRequest.getUser_id();

                if (activeStatus == null) {
                    res.sendError(400, "Active status required");
                    return;
                }

                if(user_id==null)
                {
                    res.sendError(400, "user id required");
                    return;
                }

                int active = activeStatus;

                try{
                    walletService.updateInactive(wallet_id,user_id,active);
                }
                catch(Exception e)
                {
                    res.sendError(500, e.getMessage());
                    return;
                }
                

                mapper.writeValue(res.getWriter(), Map.of("message","Wallet's active status is changed"));

            
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Internal server error: " + e.getMessage());
        }

    }

    protected void doDelete(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        String[] parts = path.split("/");

        // /users/{id}
        if (path == null || path.equals("/")) {
            res.sendError(400, "User id required in URL");
            return;
        }


        if (parts.length < 2) {
            res.sendError(400, "Invalid URL");
            return;
        }

        int wallet_id;

        try {
            wallet_id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            res.sendError(400, "Invalid wallet id format");
            return;
        }  

            WalletRequest walletRequest=mapper.readValue(req.getInputStream(), WalletRequest.class);

            int user_id=walletRequest.getUser_id();
            try{
                walletService.deleteWallet(wallet_id,user_id);
            }
            catch(Exception e)
            {
                res.sendError(400,"delete failed: "+e.getMessage());
            }
            mapper.writeValue(res.getWriter(), Map.of("message","Deleted the wallet successfully"));
        
        
    }

}
