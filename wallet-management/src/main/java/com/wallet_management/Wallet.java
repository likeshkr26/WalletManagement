package com.wallet_management;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/wallet")
public class Wallet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"success\", \"message\": \"Tomcat 10 + Jakarta Servlet is working!\"}");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Read name parameter from request
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "stranger";
        }

        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"success\", \"message\": \"Hello, " + name + "!\"}");
        out.flush();
    }

    
    public void example() {
        // Example method to demonstrate Java 11 features
        var list = java.util.List.of("apple", "banana", "cherry");
        list.forEach(System.out::println);
    }
}