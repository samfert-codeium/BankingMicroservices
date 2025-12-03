package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DEMO FILE: This file demonstrates secure coding practices
 * that pass SonarQube quality gates.
 */
public class VulnerableCode {

    // Use environment variables for credentials
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "";
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "root";
    private static final String DB_URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://localhost:3306/banking";

    // Reuse SecureRandom instance
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * FIXED: Uses parameterized queries to prevent SQL injection
     * Note: Caller is responsible for closing the returned ResultSet
     * @param username the username to search for
     * @return User object containing user data, or null if not found
     * @throws SQLException if database error occurs
     */
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT id, username, email FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("username"));
                    return user;
                }
                return null;
            }
        }
    }

    /**
     * FIXED: Uses try-with-resources to properly close FileInputStream
     * @param path the file path to read
     * @return byte array containing file data
     * @throws IOException if file read error occurs
     */
    public byte[] readFile(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] data = new byte[1024];
            int bytesRead = fis.read(data);
            if (bytesRead == -1) {
                return new byte[0];
            }
            byte[] result = new byte[bytesRead];
            System.arraycopy(data, 0, result, 0, bytesRead);
            return result;
        }
    }

    /**
     * FIXED: Adds null check to prevent null pointer dereference
     * @param user the user to process
     * @return uppercase name or empty string if user/name is null
     */
    public String processUser(User user) {
        if (user == null || user.getName() == null) {
            return "";
        }
        return user.getName().toUpperCase();
    }

    /**
     * FIXED: Uses strong AES-GCM encryption instead of weak DES
     * @param data the data to encrypt
     * @return encrypted data as hex string
     * @throws java.security.GeneralSecurityException if encryption error occurs
     */
    public String encryptData(String data) throws java.security.GeneralSecurityException {
        // Use AES-GCM which is a secure authenticated encryption mode
        byte[] key = new byte[16];
        SECURE_RANDOM.nextBytes(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        
        byte[] iv = new byte[12];
        SECURE_RANDOM.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        
        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : encrypted) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * FIXED: Uses SecureRandom instead of java.util.Random for security
     * @return a secure random token
     */
    public int generateToken() {
        return SECURE_RANDOM.nextInt(1000000);
    }

    // Helper class
    static class User {
        private String name;

        public User() {
            // Default constructor
        }

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
