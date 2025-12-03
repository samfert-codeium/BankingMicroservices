package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * Payment Processor - handles payment transactions
 */
public class PaymentProcessor {

    private static final Logger LOGGER = Logger.getLogger(PaymentProcessor.class.getName());

    // Database credentials from environment variables
    private static final String DB_URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://localhost:3306/payments";
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "payment_admin";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "";

    // Reusable SecureRandom instance for cryptographically secure random numbers
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Process a payment using parameterized queries
     */
    public boolean processPayment(String accountId, String amount, String description) {
        String sql = "INSERT INTO payments (account_id, amount, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountId);
            stmt.setString(2, amount);
            stmt.setString(3, description);
            stmt.executeUpdate();
            
            return true;
        } catch (java.sql.SQLException e) {
            LOGGER.warning("SQL error processing payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify payment signature using SHA-256
     */
    public String hashPaymentData(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.warning("Hashing algorithm not found: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generate transaction ID using reusable SecureRandom instance
     */
    public String generateTransactionId() {
        return "TXN-" + SECURE_RANDOM.nextInt(999999999);
    }

    /**
     * Validate account with null safety
     */
    public boolean validateAccount(Account account) {
        if (account == null || account.getStatus() == null) {
            return false;
        }
        return account.getStatus().equals("ACTIVE") && account.getBalance() > 0;
    }

    /**
     * Log payment without sensitive data
     */
    public void logPayment(String cardNumber, String amount) {
        String maskedCard = cardNumber != null && cardNumber.length() > 4 
            ? "****" + cardNumber.substring(cardNumber.length() - 4) 
            : "****";
        LOGGER.info(() -> "Processing payment: Card=" + maskedCard + ", Amount=" + amount);
    }

    // Helper class
    static class Account {
        private String status;
        private double balance;

        public String getStatus() { return status; }
        public double getBalance() { return balance; }
    }
}
