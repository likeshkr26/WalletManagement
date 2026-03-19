package com.wallet_management.Servlet.v1;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.wallet_management.Model.Transfer;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Service.TransactionService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/transaction/*")
public class TransactionServlet extends HttpServlet{

    private TransactionService transactionService=new TransactionService();
    private WalletService walletService=new WalletService();
    private ObjectMapper mapper=new ObjectMapper();
    
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        String path=req.getPathInfo();
        String parts[]=path.split("/");

        Transfer transfer=mapper.readValue(req.getInputStream(), Transfer.class);
        
        if(transfer.getFrom()==null || transfer.getTo()==null || transfer.getAmount()==null)
        {
            res.sendError(400,"Enter from,to wallet id and amount as well");
            return;
        }

        int from=transfer.getFrom();
        int to=transfer.getTo();
        double amount=transfer.getAmount();

        if(from==to)
        {
            res.sendError(400,"Amount cannot be transfered to same wallet");
            return;
        }

        Wallet w1;
        Wallet w2;
        try {
            w1=walletService.getWalletByID(from);
            w2=walletService.getWalletByID(to);
        } catch (Exception e) {
            res.sendError(500,"Error occured while fetching walled details");
            return;
        }

        if(w1==null)
        {
            res.sendError(400,"Enter valid wallet id for sender");
            return;
        }

        if(w2==null)
        {
            res.sendError(400,"Enter valid wallet id for receiver");
            return;
        }

        if(w1.getActive()==0)
        {
            res.sendError(400,"The sender wallet is inactive");
            return;
        }

        if(w2.getActive()==0)
        {
            res.sendError(400,"The receiver wallet is inactive");
            return;
        }

        if(amount<0)
        {
            res.sendError(400, "Amount should be a positive value");
            return;
        }

        if(amount==0)
        {
            res.sendError(400, "Amount to transfer cannot be 0");
            return;
        }

        if(amount>9999999999999.99)
        {
            res.sendError(400, "Amount should be of 15 digits with last 2 digit as decimal");
            return;
        }



        try{

            if(parts.length==2 && parts[1].equals("transfer"))
            {
                transactionService.doTransaction(from, to, 3, amount);

                res.setStatus(201);
                res.getWriter().write("The amount transfer has been completed successfuly");
            }
        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }


    protected void doDelete(HttpServletRequest req, HttpServletResponse res)throws IOException {

    res.setContentType("application/json");

    try {

        String pathInfo = req.getPathInfo();
        String parts[]=pathInfo.split("/");
        String transferId = parts[1];

        if (transferId == null || transferId.isEmpty()) {

            res.sendError(400, "transfer_id is required");
            return;
        }


        transactionService.deleteTransaction(transferId);

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\"message\":\"Transfer deleted successfully\"}");

    }
    catch (Exception e) {

        res.sendError(500, "Error: " + e.getMessage());
    }
}


}
