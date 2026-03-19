package com.wallet_management.Servlet.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_management.Model.PrimaryWallet;
import com.wallet_management.Model.User;
import com.wallet_management.Model.UserRequest;
import com.wallet_management.Model.Wallet;
import com.wallet_management.Service.UserService;
import com.wallet_management.Service.WalletService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/v1/users/*")
public class UserServlet extends HttpServlet {
    
    private UserService userService=new UserService();
    private WalletService walletService=new WalletService();
    private ObjectMapper mapper=new ObjectMapper();

    //create user
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException
    {

        if (!"application/json".equals(req.getContentType())) {
        res.sendError(415, "Content-Type must be application/json");
        return;
        }

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path=req.getPathInfo();
        User userRequest=mapper.readValue(req.getInputStream(), User.class);
        String name=userRequest.getName();
        

        try{

            if(path==null || path.equals("/"))
            {

                if(name==null || name.trim().isEmpty())
                {
                    res.sendError(400,"Name is requireddd");
                    return;
                }

                if(name.length()>20)
                {
                    res.sendError(400,"Name must be within 20 charactersss");
                    return;
                }


                User user=new User(1,name,-1);
                userService.createUser(user);

                mapper.writeValue(res.getWriter(),Map.of("message","User Created Successfullyyy"));
                res.setStatus(201);
                return;
            }

            String parts[]=path.split("/");

            if(parts.length==3 && parts[2].equals("wallet"))
            {

                if(parts[1]==null || parts[1].trim().isEmpty())
                {
                    res.sendError(400,"Enter userid in the url");
                    return;
                }
                int user_id;

                try {
                    user_id=Integer.parseInt(parts[1]);
                } catch (Exception e) {
                    res.sendError(400,"invalid userid format");
                    return;
                }

                user_id=Integer.parseInt(parts[1]);

                User user=userService.getUserByID(user_id);

                if(user==null)
                {
                    res.sendError(400,"User not foundddd");
                    return;
                }

                if(name==null || name.trim().isEmpty())
                {
                    res.sendError(400,"Wallet name is requireddd");
                    return;
                }

                if(name.length()>20)
                {
                    res.sendError(400,"Wallet name must be within 20 charactersss");
                    return;
                }


                walletService.createWallet(name, user_id);

                mapper.writeValue(res.getWriter(),Map.of("message","Wallet create successfullyyy"));
                res.setStatus(201);
                return;
            }

        }
        catch(Exception e)
        {
            res.sendError(500,e.getMessage());
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

            if(parts[1]==null || parts[1].trim().isEmpty())
            {
                res.sendError(400,"Enter userid in the url");
                return;
            }
            int user_id;

            try {
                 user_id=Integer.parseInt(parts[1]);
            } catch (Exception e) {
                res.sendError(400,"invalid userid format");
                return;
            }

            PrimaryWallet primaryWallet=mapper.readValue(req.getInputStream(), PrimaryWallet.class);

            User u=userService.getUserByID(user_id);

            if(u==null)
            {
                res.sendError(400,"User not found");
                return;
            }

            if(parts.length==2)
            {
                if (primaryWallet.getPrimary_wallet() == null) {
                    res.sendError(400, "Primary wallet id required");
                    return;
                }

                Wallet wallet=walletService.getWalletByID(primaryWallet.getPrimary_wallet());

                if(wallet==null)
                {
                    res.sendError(400,"Wallet not found");
                    return;
                }

                userService.updatePrimaryWallet(user_id, wallet.getWallet_id());

                mapper.writeValue(res.getWriter(), Map.of("message","Primary wallet id changed"));

                res.setStatus(200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Internal server error: " + e.getMessage());
        }

    }

    //Get operations
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path=req.getPathInfo();

        UserRequest userRequest=mapper.readValue(req.getInputStream(), UserRequest.class);

        String operation=userRequest.getOperation();

        try{

            String parts[]=path.split("/");

            // ----------------------------------------------
            if (path == null || path.equals("/")) {
                res.sendError(400, "User ID required");
                return;
            }

            int user_id;

            try{
                user_id=Integer.parseInt(parts[1]);
            }
            catch(Exception e)
            {
                res.sendError(400, "invalid user ID format");
                return;
            }

            User user=userService.getUserByID(user_id);

            if(user==null)
            {
                res.sendError(404, "User not found");
                return;
            }    

            // ----------------------------------------------

            if(parts.length==2 && operation.equals("detail"))
            {
                //return user data
                mapper.writeValue(res.getWriter(), user);
            }

            // ----------------------------------------------

            else if(parts.length==3 && operation.equals("list"))
            {

                int page = 1;
                int size = 5;
                String sort = "wallet_id";
                String order = "asc";
                Integer active = null;

                // pagination
                if(req.getParameter("page") != null)
                    page = Integer.parseInt(req.getParameter("page"));

                if(req.getParameter("size") != null)
                    size = Integer.parseInt(req.getParameter("size"));

                // sorting
                if(req.getParameter("sort") != null)
                    sort = req.getParameter("sort");

                if(req.getParameter("order") != null)
                    order = req.getParameter("order");

                // filtering
                if(req.getParameter("active") != null)
                    active = Integer.parseInt(req.getParameter("active"));

                String column = req.getParameter("column");
                String value  = req.getParameter("value");

                List<Wallet> wallets =
                    userService.getWalletByUser(user_id, page, size, sort, order, active,column,value);


                // can return the wallets
                // return empty list when no wallet present
                mapper.writeValue(res.getWriter(), wallets);

            }

            // ----------------------------------------------

            else if(parts.length==3 && operation.equals("count"))
            {
                int count=userService.getWalletCountByUser(user_id);

                //returns number of wallet
                mapper.writeValue(res.getWriter(), Map.of("wallet_count",count));

            }
        }
        catch(Exception e)
        {
            res.sendError(500,e.getMessage());
        }
    }


    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws IOException {

    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    String path = req.getPathInfo();

    // /users/{id}
    if (path == null || path.equals("/")) {
        res.sendError(400, "User id required in URL");
        return;
    }

    String[] parts = path.split("/");

    if (parts.length < 2) {
        res.sendError(400, "Invalid URL");
        return;
    }

    int userId;

    try {
        userId = Integer.parseInt(parts[1]);
    } catch (NumberFormatException e) {
        res.sendError(400, "Invalid user id format");
        return;
    }

    if(parts.length==2)
    {

        User user = mapper.readValue(req.getInputStream(), User.class);

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            res.sendError(400, "Name cannot be empty");
            return;
        }

        if (user.getPrimary_wallet() == null) {
            res.sendError(400, "primary_wallet required");
            return;
        }

        try{
            Wallet w=walletService.getWalletByID(user.getPrimary_wallet());

            if(w==null)
            {
                res.sendError(400,"Wallet not found");
                return;
            }
            else if(w.getUser_id()!=userId)
            {
                res.sendError(400,"Wallet not belong to the user");
                return;
            }
        }
        catch(Exception e)
        {
            res.sendError(500,"Internal server error");
            return;
        }


        try {
            boolean updated = userService.updateUser(userId,user.getName(),user.getPrimary_wallet());

            if (!updated) {
                res.sendError(404, "User not found");
                return;
            }
            res.setStatus(HttpServletResponse.SC_OK);

            mapper.writeValue(res.getWriter(),Map.of("message", "User updated successfully"));

        } catch (Exception e) {
            res.sendError(500, "Internal server error");
        }
    }
    
}


    protected void doDelete(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();

        // /users/{id}
        if (path == null || path.equals("/")) {
            res.sendError(400, "User id required in URL");
            return;
        }

        String[] parts = path.split("/");

        if (parts.length < 2) {
            res.sendError(400, "Invalid URL");
            return;
        }

        int userId;

        try {
            userId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            res.sendError(400, "Invalid user id format");
            return;
        }  

        if(parts.length==2)
        {
            try {
            userService.deleteUser(userId);
            } catch (Exception e) {
                res.sendError(400,"delete failed: "+e.getMessage());
            }
            mapper.writeValue(res.getWriter(), Map.of("message","Deleted the user successfully"));
        }
        
    }


}
