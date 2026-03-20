package com.wallet_management.util;

public class ValidationUtil {

    public static String validateName(String name, String field) throws Exception {

        if (name == null || name.trim().isEmpty()) {
            throw new Exception(field + " is required");
        }

        name = name.trim();

        if (name.length() > 20) {
            throw new Exception(field + " must be within 20 characters");
        }

        return name;
    }


    public static int validateId(String idStr, String field) throws Exception {

        if (idStr == null || idStr.trim().isEmpty()) {
            throw new Exception(field + " is required");
        }

        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid " + field + " format");
        }
    }
}