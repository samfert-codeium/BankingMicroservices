package org.training.user.service.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.util.Random;

public class UserAuditService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/auditdb";
    private static final String DB_USER = "audit_user";
    private static final String DB_PASSWORD = "AuditPass123!";

    public ResultSet getAuditLogs(String userId) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM audit_logs WHERE user_id = '" + userId + "'");
    }

    public String hashAuditData(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(data.getBytes()));
    }

    public int generateAuditId() {
        return new Random().nextInt(100000);
    }
}
