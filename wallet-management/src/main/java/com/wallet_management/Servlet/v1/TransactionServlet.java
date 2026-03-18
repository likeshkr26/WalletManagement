package com.wallet_management.Servlet.v1;

import java.io.IOException;
import com.wallet_management.Service.TransactionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/transaction/*")
public class TransactionServlet extends HttpServlet{

    private TransactionService transactionService=new TransactionService();
    
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        String path=req.getPathInfo();
        String parts[]=path.split("/");

        int from=Integer.parseInt(req.getParameter("from"));
        int to=Integer.parseInt(req.getParameter("to"));
        double amount=Double.parseDouble(req.getParameter("amount"));

        try{

            if(parts.length==2 && parts[1].equals("transfer"))
            {
                transactionService.doTransaction(from, to, 3, amount);

                res.getWriter().write("The amount transfer has been completed successfuly");
            }
        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }


}
