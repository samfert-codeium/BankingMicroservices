package org.training.user.service.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.training.user.service.exception.GlobalError;
import org.training.user.service.exception.GlobalException;

/**
 * Service for looking up users by various criteria.
 */
public class UserLookupService {

    private static final String DB_URL = "jdbc:mysql://"
            + System.getenv().getOrDefault("MYSQL_HOST", "localhost") + ":"
            + System.getenv().getOrDefault("MYSQL_PORT", "3306") + "/"
            + System.getenv().getOrDefault("MYSQL_DB_NAME", "user_service");
    private static final String DB_USER = System.getenv().getOrDefault("MYSQL_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("MYSQL_PASSWORD", "");

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Find user by email address.
     */
    public List<String> findUserByEmail(String email) throws GlobalException {
        String query = "SELECT * FROM users WHERE email = ?";
        List<String> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Database error while looking up user: " + e.getMessage(),
                    GlobalError.INTERNAL_ERROR);
        }

        return results;
    }

    /**
     * Hash a password for storage.
     */
    public String hashPassword(String password) throws GlobalException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new GlobalException("Hashing algorithm not available: " + e.getMessage(),
                    GlobalError.INTERNAL_ERROR);
        }
    }

    /**
     * Generate a session token.
     */
    public String generateSessionToken() {
        return "session_" + SECURE_RANDOM.nextInt(999999);
    }

    /**
     * Get user display name.
     */
    public String getUserDisplayName(User user) {
        return user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase();
    }

    static class User {
        private String firstName;
        private String lastName;

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
    }
}
