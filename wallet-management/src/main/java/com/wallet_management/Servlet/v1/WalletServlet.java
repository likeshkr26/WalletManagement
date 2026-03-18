package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                List<Transaction> transactions=transactionService.getTransaction(wallet_id);

                PrintWriter out=res.getWriter();

                for(Transaction t:transactions)
                {
                    out.println("Transaction ID: "+t.getTransaction_id()+", Transaction's Wallet ID: "+t.getWallet_id()+", Transaction Amount: "+t.getAmount()+", Transaction Created At: "+t.getTimestamp()+", Transaction Type: "+t.getType());
                }
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
        
        int type=Integer.parseInt(req.getParameter("type"));
        double amount=Double.parseDouble(req.getParameter("amount"));

        String path=req.getPathInfo();
        String parts[]=path.split("/");

        try{

            int wallet_id=Integer.parseInt(parts[1]);

            if(parts.length==3 && parts[2].equals("transaction"))
            {
                transactionService.doTransaction(wallet_id, 0, type, amount);
            }

            res.getWriter().write("The transaction has been completed");
        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }

}
