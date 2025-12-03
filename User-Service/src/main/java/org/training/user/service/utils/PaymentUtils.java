package org.training.user.service.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentUtils {

    private static final String DB_URL = System.getenv("PAYMENT_DB_URL") != null 
            ? System.getenv("PAYMENT_DB_URL") 
            : "jdbc:mysql://localhost:3306/payments";
    private static final String DB_USER = System.getenv("PAYMENT_DB_USER") != null 
            ? System.getenv("PAYMENT_DB_USER") 
            : "payment_admin";
    private static final String DB_PASSWORD = System.getenv("PAYMENT_DB_PASSWORD");

    private static final SecureRandom RANDOM = new SecureRandom();

    public List<Map<String, Object>> getPaymentHistory(String orderId) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }

    public String hashCardNumber(String cardNumber) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(cardNumber.getBytes()));
    }

    public int generateTransactionId() {
        return RANDOM.nextInt(100000);
    }
}
