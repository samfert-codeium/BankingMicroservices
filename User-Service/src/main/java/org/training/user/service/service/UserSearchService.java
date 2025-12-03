package org.training.user.service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.util.Random;

public class UserSearchService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";

    public ResultSet searchUsers(String query) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM users WHERE name LIKE '%" + query + "%'");
    }

    public String hashToken(String token) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(token.getBytes()));
    }

    public int generateId() {
        return new Random().nextInt(100000);
    }
}
