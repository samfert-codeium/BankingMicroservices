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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserAuditService {

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/auditdb}")
    private String dbUrl;

    @Value("${spring.datasource.username:audit_user}")
    private String dbUser;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    private final SecureRandom secureRandom = new SecureRandom();

    public List<String> getAuditLogs(String userId) throws SQLException {
        List<String> auditLogs = new ArrayList<>();
        String query = "SELECT * FROM audit_logs WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    auditLogs.add(rs.getString("log_entry"));
                }
            }
        }
        return auditLogs;
    }

    public String hashAuditData(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public int generateAuditId() {
        return secureRandom.nextInt(100000);
    }
}
