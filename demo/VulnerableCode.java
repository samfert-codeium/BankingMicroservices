package com.banking.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * DEMO FILE: This file contains intentionally vulnerable code
 * that SonarQube will flag for the Devin remediation demo.
 *
 * Issues included:
 * 1. SQL Injection vulnerability
 * 2. Hardcoded credentials
 * 3. Resource leak (unclosed streams)
 * 4. Weak cryptography
 * 5. Null pointer dereference
 */
public class VulnerableCode {

    // VULNERABILITY: Hardcoded credentials (SonarQube rule: java:S2068)
    private static final String DB_PASSWORD = "admin123";
    private static final String DB_USER = "root";
    private static final String SECRET_KEY = "mysecretkey12345";

    /**
     * VULNERABILITY: SQL Injection (SonarQube rule: java:S3649)
     * User input is directly concatenated into SQL query
     */
    public ResultSet getUserByUsername(String username) throws Exception {
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/banking", DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();

        // BAD: SQL Injection vulnerability - user input directly in query
        String query = "SELECT * FROM users WHERE username = '" + username + "'";
        return stmt.executeQuery(query);
    }

    /**
     * VULNERABILITY: Resource leak (SonarQube rule: java:S2095)
     * FileInputStream is never closed
     */
    public byte[] readFile(String path) throws IOException {
        // BAD: Resource leak - stream is never closed
        FileInputStream fis = new FileInputStream(path);
        byte[] data = new byte[1024];
        fis.read(data);
        return data;
    }

    /**
     * VULNERABILITY: Null pointer dereference (SonarQube rule: java:S2259)
     */
    public String processUser(User user) {
        // BAD: Potential null pointer dereference
        return user.getName().toUpperCase();
    }

    /**
     * VULNERABILITY: Weak cryptography (SonarQube rule: java:S5542)
     */
    public String encryptData(String data) throws Exception {
        // BAD: Using weak DES encryption
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
        return cipher.toString();
    }

    /**
     * VULNERABILITY: Insecure random (SonarQube rule: java:S2245)
     */
    public int generateToken() {
        // BAD: Using insecure random for security-sensitive operation
        java.util.Random random = new java.util.Random();
        return random.nextInt(1000000);
    }

    // Helper class
    static class User {
        private String name;

        public String getName() {
            return name;
        }
    }
}
