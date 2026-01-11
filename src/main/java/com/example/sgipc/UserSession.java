package com.example.sgipc;

public class UserSession {
    private static String currentUserEmail;

    public static void setEmail(String email) { currentUserEmail = email; }
    public static String getEmail() { return currentUserEmail; }
}