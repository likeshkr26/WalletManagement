package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_management.Model.Transaction;
import com.wallet_management.Model.TransactionRequest;
import com.wallet_management.Service.TransactionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/transaction/*")
public class TransactionServlet extends HttpServlet{

    private TransactionService transactionService=new TransactionService();
    private ObjectMapper mapper=new ObjectMapper();
    
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {


        TransactionRequest transactionRequest=mapper.readValue(req.getInputStream(), TransactionRequest.class);

        Integer type;
        Double amount;
        Integer wallet_id;
        Integer receiver_id = null;

        try{
            type=transactionRequest.getType();
        }
        catch(Exception e)
        {
            res.sendError(400, "Enter valid type value");
            return;
        }

        try{
            amount=transactionRequest.getAmount();
        }
        catch(Exception e)
        {
            res.sendError(400, "Enter valid amount value");
            return;
        }

        try{
            wallet_id=transactionRequest.getWallet_id();
        }
        catch(Exception e)
        {
            res.sendError(400, "Enter valid wallet id value");
            return;
        }

        if(type==3)
        {
            try{
                receiver_id=transactionRequest.getReceiver_id();
            }
            catch(Exception e)
            {
                res.sendError(400, "Enter valid receiver id value");
                return;
            }
        }

        if(type==null || amount==null || wallet_id==null || (type==3 && receiver_id==null) )
        {
            res.sendError(400,"Enter wallet_id,receiver_id,type and amount");
            return;
        }

        if(type!=1 && type!=2 && type!=3)
        {
            res.sendError(400,"Enter valid type (1,2,3)");
            return;
        }

        try{

            if(type!=3)
            {
                transactionService.doTransaction(wallet_id, 0, type, amount);

                mapper.writeValue(res.getWriter(), Map.of("message","The transaction has been completed successfully"));
                res.setStatus(201);
            }
            else
            {
                transactionService.doTransaction(wallet_id, receiver_id, type, amount);

                res.setStatus(201);
                mapper.writeValue(res.getWriter(), Map.of("message","The amount transfer has been completed successfuly"));
            }

        }
        catch(Exception e)
        {
            res.sendError(500,"Error:"+e.getMessage());
        }

    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse res)throws IOException {

    res.setContentType("application/json");

    TransactionRequest transactionRequest=mapper.readValue(req.getInputStream(), TransactionRequest.class);
    String transferId = transactionRequest.getTransaction_unique_id();

    if (transferId == null || transferId.isEmpty()) {

        res.sendError(400, "transfer_id is required");
        return;
    }

        try {

            transactionService.deleteTransaction(transferId);
            
            res.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(res.getWriter(), Map.of("message","Transfer deleted successfully"));

        }
        catch (Exception e) {

            mapper.writeValue(res.getWriter(), Map.of("message","Error:"+e.getMessage()));
        }

    }

    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        TransactionRequest transactionRequest;
        
        try{
            transactionRequest=mapper.readValue(req.getInputStream(), TransactionRequest.class);
        }
        catch(Exception e)
        {
            mapper.writeValue(res.getWriter(), Map.of("message","Enter valid transaction id"));
            return;
        }

        if(transactionRequest.getTransaction_id()==null)
        {
            mapper.writeValue(res.getWriter(), Map.of("message","Enter transaction id"));
            return;
        }

        int transaction_id=transactionRequest.getTransaction_id();
        

        try{

            Transaction transaction=transactionService.getTransactionDetail(transaction_id);

            mapper.writeValue(res.getWriter(), transaction);

        }
        catch(Exception e)
        {
            mapper.writeValue(res.getWriter(), Map.of("message","Enter transaction id"));
            return;
        }

    }

    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {
        mapper.writeValue(res.getWriter(), Map.of("message","Transactions cannot be modified"));
    }

    @Override
    protected void service(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {

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
        mapper.writeValue(res.getWriter(), Map.of("message","Transactions cannot be modified"));
    }



}
