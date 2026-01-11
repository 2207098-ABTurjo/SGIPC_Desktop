package com.example.sgipc;

import java.sql.*;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

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

    public List<Contest> getAllContests() {
        List<Contest> contestList = new ArrayList<>();
        getConnection();
        String sql = "SELECT * FROM contests ORDER BY time ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                contestList.add(new Contest(
                        rs.getString("title"),
                        rs.getString("time"),
                        rs.getInt("duration"),
                        rs.getString("link")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching contests: " + e.getMessage());
        }
        return contestList;
    }

    private void initDatabase() {
        String userTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " roll TEXT,"
                + " email TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " codeforcesHandle TEXT,"
                + " memberType TEXT"
                + ");";

        String contestTable = "CREATE TABLE IF NOT EXISTS contests ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " title TEXT NOT NULL,"
                + " time TEXT NOT NULL,"
                + " duration INTEGER,"
                + " link TEXT"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(contestTable);
            logger.info("All tables initialized successfully.");
        } catch (SQLException e) {
            logger.severe("Table creation error: " + e.getMessage());
        }
    }


    public boolean insertContest(Contest contest) {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO contests(title, time, duration, link) VALUES(?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contest.getTitle());
            pstmt.setString(2, contest.getTime());
            pstmt.setInt(3, contest.getDuration());
            pstmt.setString(4, contest.getLink());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.severe("Error inserting contest: " + e.getMessage());
            return false;
        }
    }
}