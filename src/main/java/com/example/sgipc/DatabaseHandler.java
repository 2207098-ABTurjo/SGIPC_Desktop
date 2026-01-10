package com.example.sgipc;

import java.sql.*;
import java.util.logging.Logger;

public class DatabaseHandler {
    private Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private static final String URL = "jdbc:sqlite:sgipc.db";

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:sgipc.db");

                initDatabase();
                logger.info("Connected to SGIPC Database");
            }
        } catch (SQLException e) {
            logger.severe("Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " roll TEXT,"
                + " email TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " codeforcesHandle TEXT,"
                + " memberType TEXT"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            logger.info("Users table verified/created.");
        } catch (SQLException e) {
            logger.severe("Table Creation Error: " + e.getMessage());
        }
    }

    public boolean insertUser(String name, String roll, String email, String password, String handle, String type) {
        getConnection();

        String sql = "INSERT INTO users(name, roll, email, password, codeforcesHandle, memberType) VALUES(?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, roll);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, handle);
            pstmt.setString(6, type);

            pstmt.executeUpdate();
            logger.info("User registered: " + email);
            return true;
        } catch (SQLException e) {
            logger.severe("Insert Error: " + e.getMessage());
            return false;
        }
    }

    public String validateLogin(String email, String password) {
        getConnection();
        String sql = "SELECT memberType FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("memberType");
            }
        } catch (SQLException e) {
            logger.severe("Login Query Error: " + e.getMessage());
        }
        return null;
    }
}