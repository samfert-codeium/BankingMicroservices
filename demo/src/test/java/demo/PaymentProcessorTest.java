package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentProcessor class
 */
class PaymentProcessorTest {

    private PaymentProcessor paymentProcessor;

    @BeforeEach
    void setUp() {
        paymentProcessor = new PaymentProcessor();
    }

    // Tests for hashPaymentData method

    @Test
    @DisplayName("hashPaymentData should return SHA-256 hash for valid input")
    void hashPaymentData_withValidInput_returnsHash() {
        String data = "test payment data";
        String hash = paymentProcessor.hashPaymentData(data);
        
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 produces 64 hex characters
        assertTrue(hash.matches("[a-f0-9]+"));
    }

    @Test
    @DisplayName("hashPaymentData should return consistent hash for same input")
    void hashPaymentData_withSameInput_returnsConsistentHash() {
        String data = "consistent data";
        String hash1 = paymentProcessor.hashPaymentData(data);
        String hash2 = paymentProcessor.hashPaymentData(data);
        
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("hashPaymentData should return different hashes for different inputs")
    void hashPaymentData_withDifferentInputs_returnsDifferentHashes() {
        String hash1 = paymentProcessor.hashPaymentData("data1");
        String hash2 = paymentProcessor.hashPaymentData("data2");
        
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("hashPaymentData should handle empty string")
    void hashPaymentData_withEmptyString_returnsHash() {
        String hash = paymentProcessor.hashPaymentData("");
        
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    // Tests for generateTransactionId method

    @Test
    @DisplayName("generateTransactionId should return ID with TXN- prefix")
    void generateTransactionId_returnsIdWithPrefix() {
        String transactionId = paymentProcessor.generateTransactionId();
        
        assertNotNull(transactionId);
        assertTrue(transactionId.startsWith("TXN-"));
    }

    @Test
    @DisplayName("generateTransactionId should return unique IDs")
    void generateTransactionId_returnsUniqueIds() {
        String id1 = paymentProcessor.generateTransactionId();
        String id2 = paymentProcessor.generateTransactionId();
        String id3 = paymentProcessor.generateTransactionId();
        
        // While not guaranteed to be unique, the probability of collision is very low
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id1, id3);
    }

    @Test
    @DisplayName("generateTransactionId should return ID with numeric suffix")
    void generateTransactionId_returnsIdWithNumericSuffix() {
        String transactionId = paymentProcessor.generateTransactionId();
        String numericPart = transactionId.substring(4); // Remove "TXN-" prefix
        
        assertDoesNotThrow(() -> Integer.parseInt(numericPart));
    }

    // Tests for validateAccount method

    @Test
    @DisplayName("validateAccount should return false for null account")
    void validateAccount_withNullAccount_returnsFalse() {
        boolean result = paymentProcessor.validateAccount(null);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return false for account with null status")
    void validateAccount_withNullStatus_returnsFalse() {
        PaymentProcessor.Account account = new PaymentProcessor.Account();
        // Account has null status by default
        
        boolean result = paymentProcessor.validateAccount(account);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return false for inactive account")
    void validateAccount_withInactiveAccount_returnsFalse() {
        PaymentProcessor.Account account = createAccount("INACTIVE", 100.0);
        
        boolean result = paymentProcessor.validateAccount(account);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return false for account with zero balance")
    void validateAccount_withZeroBalance_returnsFalse() {
        PaymentProcessor.Account account = createAccount("ACTIVE", 0.0);
        
        boolean result = paymentProcessor.validateAccount(account);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return false for account with negative balance")
    void validateAccount_withNegativeBalance_returnsFalse() {
        PaymentProcessor.Account account = createAccount("ACTIVE", -50.0);
        
        boolean result = paymentProcessor.validateAccount(account);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return true for active account with positive balance")
    void validateAccount_withActiveAccountAndPositiveBalance_returnsTrue() {
        PaymentProcessor.Account account = createAccount("ACTIVE", 100.0);
        
        boolean result = paymentProcessor.validateAccount(account);
        
        assertTrue(result);
    }

    // Tests for logPayment method

    @Test
    @DisplayName("logPayment should not throw exception with valid inputs")
    void logPayment_withValidInputs_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("1234567890123456", "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle null card number")
    void logPayment_withNullCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment(null, "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle short card number")
    void logPayment_withShortCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("1234", "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle empty card number")
    void logPayment_withEmptyCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("", "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle null amount")
    void logPayment_withNullAmount_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("1234567890123456", null));
    }

    // Tests for processPayment method (will fail without database, but tests the code path)

    @Test
    @DisplayName("processPayment should return false when database is unavailable")
    void processPayment_withoutDatabase_returnsFalse() {
        // Without a database connection, this should return false
        boolean result = paymentProcessor.processPayment("ACC123", "100.00", "Test payment");
        
        assertFalse(result);
    }

    @Test
    @DisplayName("processPayment should handle null parameters gracefully")
    void processPayment_withNullParameters_returnsFalse() {
        boolean result = paymentProcessor.processPayment(null, null, null);
        
        assertFalse(result);
    }

    // Additional tests for hashPaymentData method

    @Test
    @DisplayName("hashPaymentData should handle special characters")
    void hashPaymentData_withSpecialCharacters_returnsHash() {
        String hash = paymentProcessor.hashPaymentData("!@#$%^&*()_+-=[]{}|;':\",./<>?");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("hashPaymentData should handle unicode characters")
    void hashPaymentData_withUnicodeCharacters_returnsHash() {
        String hash = paymentProcessor.hashPaymentData("日本語テスト");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("hashPaymentData should handle long string")
    void hashPaymentData_withLongString_returnsHash() {
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longString.append("a");
        }
        String hash = paymentProcessor.hashPaymentData(longString.toString());
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    // Additional tests for generateTransactionId method

    @Test
    @DisplayName("generateTransactionId should generate many unique IDs")
    void generateTransactionId_manyCallsReturnUniqueIds() {
        java.util.Set<String> ids = new java.util.HashSet<>();
        for (int i = 0; i < 100; i++) {
            ids.add(paymentProcessor.generateTransactionId());
        }
        // Most IDs should be unique (allowing for some collisions)
        assertTrue(ids.size() > 90, "Most IDs should be unique");
    }

    // Additional tests for validateAccount method

    @Test
    @DisplayName("validateAccount should return false for PENDING status")
    void validateAccount_withPendingStatus_returnsFalse() {
        PaymentProcessor.Account account = createAccount("PENDING", 100.0);
        boolean result = paymentProcessor.validateAccount(account);
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return false for CLOSED status")
    void validateAccount_withClosedStatus_returnsFalse() {
        PaymentProcessor.Account account = createAccount("CLOSED", 100.0);
        boolean result = paymentProcessor.validateAccount(account);
        assertFalse(result);
    }

    @Test
    @DisplayName("validateAccount should return true for ACTIVE status with large balance")
    void validateAccount_withLargeBalance_returnsTrue() {
        PaymentProcessor.Account account = createAccount("ACTIVE", 1000000.0);
        boolean result = paymentProcessor.validateAccount(account);
        assertTrue(result);
    }

    @Test
    @DisplayName("validateAccount should return true for ACTIVE status with small positive balance")
    void validateAccount_withSmallPositiveBalance_returnsTrue() {
        PaymentProcessor.Account account = createAccount("ACTIVE", 0.01);
        boolean result = paymentProcessor.validateAccount(account);
        assertTrue(result);
    }

    // Additional tests for logPayment method

    @Test
    @DisplayName("logPayment should handle very long card number")
    void logPayment_withVeryLongCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("12345678901234567890123456789012345678901234567890", "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle card number with exactly 4 characters")
    void logPayment_withExactly4CharCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("1234", "100.00"));
    }

    @Test
    @DisplayName("logPayment should handle card number with 5 characters")
    void logPayment_with5CharCardNumber_doesNotThrow() {
        assertDoesNotThrow(() -> paymentProcessor.logPayment("12345", "100.00"));
    }

    // Additional tests for processPayment method

    @Test
    @DisplayName("processPayment should handle empty strings")
    void processPayment_withEmptyStrings_returnsFalse() {
        boolean result = paymentProcessor.processPayment("", "", "");
        assertFalse(result);
    }

    @Test
    @DisplayName("processPayment should handle special characters in description")
    void processPayment_withSpecialCharactersInDescription_returnsFalse() {
        boolean result = paymentProcessor.processPayment("ACC123", "100.00", "Test payment with special chars: !@#$%");
        assertFalse(result);
    }

    // Helper method to create Account instances using reflection
    private PaymentProcessor.Account createAccount(String status, double balance) {
        try {
            PaymentProcessor.Account account = new PaymentProcessor.Account();
            
            // Use reflection to set private fields
            java.lang.reflect.Field statusField = PaymentProcessor.Account.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(account, status);
            
            java.lang.reflect.Field balanceField = PaymentProcessor.Account.class.getDeclaredField("balance");
            balanceField.setAccessible(true);
            balanceField.setDouble(account, balance);
            
            return account;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test account", e);
        }
    }
}
