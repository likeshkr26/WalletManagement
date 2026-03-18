package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_management.Model.Report;
import com.wallet_management.Model.User;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Service.ReportService;
import com.wallet_management.Service.UserService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/reports/*")
public class ReportServlet extends HttpServlet{
    
    private ReportService reportService=new ReportService();
    private WalletService walletService=new WalletService();
    private UserService userService=new UserService();
    private ObjectMapper mapper=new ObjectMapper();

    private LocalDate parseDate(String dateStr) throws IllegalArgumentException {

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try {
        return LocalDate.parse(dateStr, formatter);
    } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
    }
}

    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        try{

            String path=req.getPathInfo();
            String parts[]=path.split("/");

            // -------------------
            Report report=mapper.readValue(req.getInputStream(), Report.class);

            String from=report.getFrom();
            String to=report.getTo();

            LocalDate fromDate;
            LocalDate toDate;

            try {
                fromDate = parseDate(from);
                toDate   = parseDate(to);
            } catch (IllegalArgumentException e) {

                res.setStatus(400);
                mapper.writeValue(res.getWriter(),
                    Map.of("error", e.getMessage()));
                return;
            }

            if (fromDate.isAfter(toDate)) {
                res.setStatus(400);
                mapper.writeValue(res.getWriter(),
                    Map.of("error", "fromDate cannot be after toDate"));
                return;
            }

            if (toDate.isAfter(LocalDate.now())) {
                res.sendError(400, "toDate cannot be in future");
                return;
            }

            // -------------------

            if(parts.length==5 && parts[1].equals("wallet") && parts[4].equals("count"))
            {

                if(parts[2].trim().isEmpty())
                {
                    res.sendError(400,"Enter wallet id in the urll");
                    return;
                }
                int wallet_id;

                try {
                    wallet_id=Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    res.sendError(400,"invalid wallet id formattt");
                    return;
                }

                Wallet w=walletService.getWalletByID(wallet_id);

                if(w==null)
                {
                    res.sendError(400,"Wallet not found");
                    return;
                }

                int count=reportService.getTransactionCountByWallet(wallet_id, from, to);

                mapper.writeValue(res.getWriter(), Map.of("wallet_id",wallet_id,"count",count));
                return;
            }

            else if(parts.length==5 && parts[1].equals("user") && parts[4].equals("count"))
            {

                if(parts[2].trim().isEmpty())
                {
                    res.sendError(400,"Enter user id in the urll");
                    return;
                }
                int user_id;

                try {
                    user_id=Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    res.sendError(400,"invalid user id formattt");
                    return;
                }

                User u=userService.getUserByID(user_id);

                if(u==null)
                {
                    res.sendError(400,"User not found");
                    return;
                }

                int count=reportService.getTransactionCountByUser(user_id, from, to);

                mapper.writeValue(res.getWriter(), Map.of("user_id",user_id,"count",count));
                return;
            }

            else if(parts.length==5 && parts[1].equals("wallet") && parts[4].equals("spent"))
            {
                if(parts[2].trim().isEmpty())
                {
                    res.sendError(400,"Enter wallet id in the urll");
                    return;
                }
                int wallet_id;

                try {
                    wallet_id=Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    res.sendError(400,"invalid wallet id formattt");
                    return;
                }

                Wallet w=walletService.getWalletByID(wallet_id);

                if(w==null)
                {
                    res.sendError(400,"Wallet not found");
                    return;
                }

                double amount=reportService.totalAmountSpendFromWallet(wallet_id, from, to);

                mapper.writeValue(res.getWriter(), Map.of("wallet_id",wallet_id,"amount",amount));
                return;
            }

            else if(parts.length==5 && parts[1].equals("user") && parts[4].equals("spent"))
            {
                if(parts[2].trim().isEmpty())
                {
                    res.sendError(400,"Enter user id in the urll");
                    return;
                }
                int user_id;

                try {
                    user_id=Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    res.sendError(400,"invalid user id formattt");
                    return;
                }

                User u=userService.getUserByID(user_id);

                if(u==null)
                {
                    res.sendError(400,"User not found");
                    return;
                }

                double amount=reportService.totalAmountSpentByUser(user_id, from, to);

                mapper.writeValue(res.getWriter(), Map.of("user_id",user_id,"amount",amount));
                return;
            }

            else if(parts.length==3 && parts[2].equals("top"))
            {
                HashMap<String,Double> list=reportService.topTenSpendingUser(from, to);

                mapper.writeValue(res.getWriter(), list);
                return;

            }

            else if(parts.length==4 && parts[3].equals("avg"))
            {
                if(parts[2].trim().isEmpty())
                {
                    res.sendError(400,"Enter user id in the urll");
                    return;
                }
                int user_id;

                try {
                    user_id=Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    res.sendError(400,"invalid user id formattt");
                    return;
                }

                User u=userService.getUserByID(user_id);

                if(u==null)
                {
                    res.sendError(400,"User not found");
                    return;
                }
                double avg=reportService.avgSpend(user_id, from, to);

                mapper.writeValue(res.getWriter(), Map.of("user_id",user_id,"average",avg));
                return;
            }
            
            else if(parts.length==3 && parts[2].equals("expired"))
            {
                List<Integer> list=reportService.inactiveWallet();

                mapper.writeValue(res.getWriter(), list);
                return;
            }

        }
        catch(Exception e)
        {
            throw new IOException("Error: "+e.getMessage());
        }
    }

}
