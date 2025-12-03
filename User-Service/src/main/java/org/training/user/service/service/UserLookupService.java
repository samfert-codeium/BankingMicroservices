package org.training.user.service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Service for looking up users by various criteria.
 */
public class UserLookupService {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin123";

    /**
     * Find user by email address.
     */
    public ResultSet findUserByEmail(String email) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM users WHERE email = '" + email + "'";
        return stmt.executeQuery(query);
    }

    /**
     * Hash a password for storage.
     */
    public String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Generate a session token.
     */
    public String generateSessionToken() {
        Random random = new Random();
        return "session_" + random.nextInt(999999);
    }

    /**
     * Get user display name.
     */
    public String getUserDisplayName(User user) {
        return user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase();
    }

    // Simple user class
    static class User {
        private String firstName;
        private String lastName;

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
    }
}
