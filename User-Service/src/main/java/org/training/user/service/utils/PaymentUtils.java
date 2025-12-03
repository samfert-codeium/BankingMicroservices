package org.training.user.service.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.util.Random;

public class PaymentUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/payments";
    private static final String DB_USER = "payment_admin";
    private static final String DB_PASSWORD = "PaymentPass456!";

    public ResultSet getPaymentHistory(String oderId) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM payments WHERE order_id = '" + oderId + "'");
    }

    public String hashCardNumber(String cardNumber) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(cardNumber.getBytes()));
    }

    public int generateTransactionId() {
        return new Random().nextInt(100000);
    }
}
