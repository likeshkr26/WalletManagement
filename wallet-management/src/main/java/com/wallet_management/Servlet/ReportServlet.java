package com.wallet_management.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wallet_management.Service.ReportService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reports/*")
public class ReportServlet extends HttpServlet{
    
    private ReportService reportService=new ReportService();

    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        try{

            String path=req.getPathInfo();
            String parts[]=path.split("/");

            String from=req.getParameter("from");
            String to=req.getParameter("to");

            if(parts.length==5 && parts[1].equals("wallet") && parts[4].equals("count"))
            {
                int wallet_id=Integer.parseInt(parts[2]);

                int count=reportService.getTransactionCountByWallet(wallet_id, from, to);

                res.getWriter().write("No of Transaction from wallet:"+wallet_id+"  is "+count);
                return;
            }

            else if(parts.length==5 && parts[1].equals("user") && parts[4].equals("count"))
            {
                int user_id=Integer.parseInt(parts[2]);

                int count=reportService.getTransactionCountByUser(user_id, from, to);

                res.getWriter().write("No of Transaction from user:"+user_id+"  is "+count);
                return;
            }

            else if(parts.length==5 && parts[1].equals("wallet") && parts[4].equals("spent"))
            {
                int wallet_id=Integer.parseInt(parts[2]);

                double amount=reportService.totalAmountSpendFromWallet(wallet_id, from, to);

                res.getWriter().write("Total amount spent from wallet:"+wallet_id+"  is "+amount);
                return;
            }

            else if(parts.length==5 && parts[1].equals("user") && parts[4].equals("spent"))
            {
                int user_id=Integer.parseInt(parts[2]);

                double amount=reportService.totalAmountSpentByUser(user_id, from, to);

                res.getWriter().write("Total amount spent by user:"+user_id+"  is "+amount);

                return;
            }

            else if(parts.length==3 && parts[2].equals("top"))
            {
                HashMap<String,Double> list=reportService.topTenSpendingUser(from, to);

                PrintWriter out=res.getWriter();

                for (Map.Entry<String,Double> map : list.entrySet())
                {
                    out.println("Name: "+map.getKey()+" , Amount: "+map.getValue());
                }

                return;

            }

            else if(parts.length==4 && parts[3].equals("avg"))
            {
                int user_id=Integer.parseInt(parts[2]);
                double avg=reportService.avgSpend(user_id, from, to);

                res.getWriter().write("Average spend of user: "+user_id+" is : "+avg);

                return;
            }
            
            else if(parts.length==3 && parts[2].equals("expired"))
            {
                List<Integer> list=reportService.inactiveWallet();

                PrintWriter out=res.getWriter();

                out.println("Inactive User wallet");

                if(list.size()==0)
                {
                    out.println("None");
                }
                for(int id:list)
                {
                    out.println(id);
                }
                return;
            }

        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }

}
