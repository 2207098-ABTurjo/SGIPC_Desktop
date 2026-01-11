package com.example.sgipc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:sgipc.db";
    private static Connection connection;
    private static final Logger logger = Logger.getLogger(DatabaseHandler.class.getName());

    public DatabaseHandler() {
        getConnection();
        initDatabase();
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            logger.severe("Connection Error: " + e.getMessage());
        }
        return connection;
    }

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT name, memberType FROM users ORDER BY name ASC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(rs.getString("name"), rs.getString("memberType")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void initDatabase() {
        String userTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT,"
                + " roll TEXT,"
                + " email TEXT UNIQUE,"
                + " password TEXT,"
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

        String workshopTable = "CREATE TABLE IF NOT EXISTS workshops ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " title TEXT NOT NULL,"
                + " time TEXT NOT NULL,"
                + " duration INTEGER"
                + ");";

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(userTable);
            stmt.execute(contestTable);
            stmt.execute(workshopTable);
        } catch (SQLException e) {
            logger.severe("Table creation error: " + e.getMessage());
        }
    }


    public boolean insertUser(String name, String roll, String email, String password, String handle, String type) {
        String sql = "INSERT INTO users(name, roll, email, password, codeforcesHandle, memberType) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, roll);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, handle);
            pstmt.setString(6, type);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String validateLogin(String email, String password) {
        String sql = "SELECT memberType FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("memberType");
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insertContest(Contest contest) {
        String sql = "INSERT INTO contests(title, time, duration, link) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, contest.getTitle());
            pstmt.setString(2, contest.getTime());
            pstmt.setInt(3, contest.getDuration());
            pstmt.setString(4, contest.getLink());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<Contest> getAllContests() {
        List<Contest> list = new ArrayList<>();
        String sql = "SELECT * FROM contests ORDER BY time ASC";
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Contest(rs.getString("title"), rs.getString("time"), rs.getInt("duration"), rs.getString("link")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insertWorkshop(Workshop workshop) {
        String sql = "INSERT INTO workshops(title, time, duration) VALUES(?,?,?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, workshop.getTitle());
            pstmt.setString(2, workshop.getTime());
            pstmt.setInt(3, workshop.getDuration());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<Workshop> getAllWorkshops() {
        List<Workshop> list = new ArrayList<>();
        String sql = "SELECT * FROM workshops ORDER BY time ASC";
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Workshop(rs.getString("title"), rs.getString("time"), rs.getInt("duration")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }


    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("name"),
                        rs.getString("roll"),
                        rs.getString("email"),
                        rs.getString("codeforcesHandle"),
                        rs.getString("memberType")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateUser(String name, String roll, String handle, String type, String email) {
        String sql = "UPDATE users SET name = ?, roll = ?, codeforcesHandle = ?, memberType = ? WHERE email = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, roll);
            pstmt.setString(3, handle);
            pstmt.setString(4, type);
            pstmt.setString(5, email);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}