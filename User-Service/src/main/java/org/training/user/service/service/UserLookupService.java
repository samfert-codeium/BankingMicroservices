package org.training.user.service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * Service for looking up users by various criteria.
 */
public class UserLookupService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "admin";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Find user by email address.
     */
    public Optional<UserData> findUserByEmail(String email) throws SQLException {
        if (DB_PASSWORD == null) {
            throw new SQLException("Database password not configured. Set DB_PASSWORD environment variable.");
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserData userData = new UserData();
                    userData.setEmail(rs.getString("email"));
                    userData.setFirstName(rs.getString("first_name"));
                    userData.setLastName(rs.getString("last_name"));
                    return Optional.of(userData);
                }
                return Optional.empty();
            }
        }
    }

    /**
     * Hash a password for storage.
     */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
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
        return "session_" + SECURE_RANDOM.nextInt(999999);
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

    // User data class for returning query results
    public static class UserData {
        private String email;
        private String firstName;
        private String lastName;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
    }
}
