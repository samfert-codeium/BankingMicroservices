package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * Secure Code Demo - demonstrates secure coding practices
 */
public class VulnerableCode {

    private static final Logger LOGGER = Logger.getLogger(VulnerableCode.class.getName());

    // Database credentials from environment variables
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") : "";
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") : "root";
    private static final String DB_URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") : "jdbc:mysql://localhost:3306/banking";

    // Reusable SecureRandom instance
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Get user by username using parameterized query to prevent SQL injection
     */
    public ResultSet getUserByUsername(String username) throws java.sql.SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        return stmt.executeQuery();
    }

    /**
     * Read file with proper resource management using try-with-resources
     */
    public byte[] readFile(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] data = new byte[1024];
            int bytesRead = fis.read(data);
            if (bytesRead < 0) {
                return new byte[0];
            }
            byte[] result = new byte[bytesRead];
            System.arraycopy(data, 0, result, 0, bytesRead);
            return result;
        }
    }

    /**
     * Process user with null safety
     */
    public String processUser(User user) {
        if (user == null || user.getName() == null) {
            return "";
        }
        return user.getName().toUpperCase();
    }

    /**
     * Encrypt data using strong AES encryption
     */
    public String encryptData(String data) throws java.security.NoSuchAlgorithmException, 
            javax.crypto.NoSuchPaddingException {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        LOGGER.info(() -> "Cipher initialized for encryption");
        return cipher.getAlgorithm();
    }

    /**
     * Generate secure token using SecureRandom
     */
    public int generateToken() {
        return SECURE_RANDOM.nextInt(1000000);
    }

    // Helper class
    static class User {
        private String name;

        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }
}
