package com.banking.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Payment Processor - handles payment transactions
 *
 * WARNING: This code contains security vulnerabilities for demo purposes
 */
public class PaymentProcessor {

    // VULNERABILITY: Hardcoded database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payments";
    private static final String DB_USER = "payment_admin";
    private static final String DB_PASSWORD = "Payment123!";

    // VULNERABILITY: Hardcoded API key
    private static final String API_KEY = "sk_live_abc123xyz789secret";

    /**
     * Process a payment - VULNERABLE TO SQL INJECTION
     */
    public boolean processPayment(String accountId, String amount, String description) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            // VULNERABILITY: SQL Injection - user input directly concatenated
            String sql = "INSERT INTO payments (account_id, amount, description) VALUES ('"
                + accountId + "', '" + amount + "', '" + description + "')";
            stmt.executeUpdate(sql);

            // VULNERABILITY: Resource leak - connection never closed
            return true;
        } catch (Exception e) {
            // VULNERABILITY: Generic exception catch, swallowing details
            return false;
        }
    }

    /**
     * Verify payment signature - USES WEAK HASHING
     */
    public String hashPaymentData(String data) {
        try {
            // VULNERABILITY: MD5 is cryptographically weak
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Generate transaction ID - INSECURE RANDOM
     */
    public String generateTransactionId() {
        // VULNERABILITY: Using insecure Random for security-sensitive operation
        Random random = new Random();
        return "TXN-" + random.nextInt(999999999);
    }

    /**
     * Validate account - NULL POINTER RISK
     */
    public boolean validateAccount(Account account) {
        // VULNERABILITY: No null check before dereferencing
        return account.getStatus().equals("ACTIVE") && account.getBalance() > 0;
    }

    /**
     * Log payment - POTENTIAL INFO DISCLOSURE
     */
    public void logPayment(String cardNumber, String amount) {
        // VULNERABILITY: Logging sensitive data (card number)
        System.out.println("Processing payment: Card=" + cardNumber + ", Amount=" + amount);
    }

    // Helper class
    static class Account {
        private String status;
        private double balance;

        public String getStatus() { return status; }
        public double getBalance() { return balance; }
    }
}
// Trigger workflow
// Tue Dec  2 23:56:45 PST 2025
