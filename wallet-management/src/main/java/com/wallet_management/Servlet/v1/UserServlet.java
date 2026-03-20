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
import com.wallet_management.util.ValidationUtil;

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


    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

    try {

        if (!"application/json".equals(req.getContentType())) {
            mapper.writeValue(res.getWriter(),Map.of("message","Content-Type must be application/json"));
            return;
        }

        String path = req.getPathInfo();

        User userRequest =mapper.readValue(req.getInputStream(), User.class);

        String name = userRequest.getName();

        // create user
        if (path==null || path.equals("/")) {

            User user = new User(1, name, -1);

            userService.createUser(user);

            mapper.writeValue(res.getWriter(),Map.of("message", "User Created Successfully"));
            return;
        }

        //create wallet
        String parts[] = path.split("/");

        if (parts.length == 3 && parts[2].equals("wallet")) {

            int userId =ValidationUtil.validateId(parts[1], "user id");

            User user = userService.getUserByID(userId);

            if (user == null)
                throw new Exception("User not found");

            walletService.createWallet(name, userId);

            mapper.writeValue(res.getWriter(),Map.of("message", "Wallet created successfully"));
        }

    } catch (Exception e) {

        mapper.writeValue(res.getWriter(),Map.of("message", e.getMessage()));
    }
    }

    protected void doPatch(HttpServletRequest req,HttpServletResponse res) throws ServletException,Exception
    {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

    try {

        if (!"application/json".equals(req.getContentType())) {
            mapper.writeValue(res.getWriter(),Map.of("message","Content-Type must be application/json"));
            return;
        }

        String path = req.getPathInfo();

        if (path == null || path.equals("/"))
            throw new Exception("User id required in URL");

        String[] parts = path.split("/");

        
        int userId =ValidationUtil.validateId(parts[1], "user id");

        
        PrimaryWallet requestBody =mapper.readValue(req.getInputStream(),PrimaryWallet.class);

        
        userService.updatePrimaryWallet(userId,requestBody.getPrimary_wallet());

        mapper.writeValue(res.getWriter(),Map.of("message","Primary wallet id changed"));

    }
    catch (Exception e) {

        mapper.writeValue(res.getWriter(),Map.of("message", e.getMessage()));
    }

    }


    //Get operations
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {

        if (!"application/json".equals(req.getContentType())) {
            mapper.writeValue(res.getWriter(),Map.of("message","Content-Type must be application/json"));
            return;
        }

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String path=req.getPathInfo();

        UserRequest userRequest=mapper.readValue(req.getInputStream(), UserRequest.class);

        String operation=userRequest.getOperation();

        try{

            String parts[]=path.split("/");

            // ----------------------------------------------
            if (path == null || path.equals("/")) {
                mapper.writeValue(res.getWriter(),Map.of("message","User ID required"));
                return;
            }

            int user_id;

            try{
                user_id=Integer.parseInt(parts[1]);
            }
            catch(NumberFormatException e)
            {
                mapper.writeValue(res.getWriter(),Map.of("message","invalid user ID format"));
                return;
            }

            User user=userService.getUserByID(user_id);

            if(user==null)
            {
                mapper.writeValue(res.getWriter(),Map.of("message","User not found"));
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
                String column=null;
                String value=null;

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

                if(req.getParameter("column") != null)
                    column = req.getParameter("column");

                if(req.getParameter("value") != null)
                    value = req.getParameter("value");


                List<Wallet> wallets =userService.getWalletByUser(user_id, page, size, sort, order, active,column,value);


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
            mapper.writeValue(res.getWriter(),Map.of("message","Internal Server Error: "+e.getMessage()));
        }
    }


    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws IOException {

    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    try {

        if (!"application/json".equals(req.getContentType())) {
            mapper.writeValue(res.getWriter(),Map.of("message","Content-Type must be application/json"));
            return;
        }

        String path = req.getPathInfo();

        if (path == null || path.equals("/"))
            throw new Exception("User id required in URL");

        String[] parts = path.split("/");

        int userId =ValidationUtil.validateId(parts[1], "user id");

        if (parts.length == 2) {

            User requestBody =mapper.readValue(req.getInputStream(), User.class);

            userService.updateUser(userId,requestBody.getName(),requestBody.getPrimary_wallet());

            mapper.writeValue(res.getWriter(),Map.of("message","User updated successfully"));
            return;
        }

        throw new Exception("Invalid URL");

    }
    catch (Exception e) {

        mapper.writeValue(res.getWriter(),Map.of("message", e.getMessage()));
    }
    
}


    protected void doDelete(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
    {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {

            String path = req.getPathInfo();

            if (path == null || path.equals("/"))
                throw new Exception("User id required in URL");

            String[] parts = path.split("/");

            if (parts.length != 2)
                throw new Exception("Invalid URL");

            int userId =ValidationUtil.validateId(parts[1], "user id");

            userService.deleteUser(userId);

            mapper.writeValue(res.getWriter(),Map.of("message","Deleted the user successfully"));

        }
        catch (Exception e) {

            mapper.writeValue(res.getWriter(),Map.of("message", e.getMessage()));
        }
            
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


}
