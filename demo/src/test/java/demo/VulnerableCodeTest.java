package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VulnerableCode class
 * Tests exercise all code paths for coverage purposes.
 */
class VulnerableCodeTest {

    private VulnerableCode vulnerableCode;

    @BeforeEach
    void setUp() {
        vulnerableCode = new VulnerableCode();
    }

    // Tests for getUserByUsername method

    @Test
    @DisplayName("getUserByUsername should throw SQLException when database is unavailable")
    void getUserByUsername_withoutDatabase_throwsException() {
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername("testuser"));
    }

    @Test
    @DisplayName("getUserByUsername should throw SQLException with null username")
    void getUserByUsername_withNullUsername_throwsException() {
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername(null));
    }

    // Tests for readFile method

    @Test
    @DisplayName("readFile should throw exception for non-existent file")
    void readFile_withNonExistentFile_throwsException() {
        assertThrows(Exception.class, () -> vulnerableCode.readFile("/non/existent/path.txt"));
    }

    @Test
    @DisplayName("readFile should throw exception for null path")
    void readFile_withNullPath_throwsException() {
        assertThrows(Exception.class, () -> vulnerableCode.readFile(null));
    }

    @Test
    @DisplayName("readFile should read file contents successfully")
    void readFile_withValidFile_returnsContent(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Hello World");
        }
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals("Hello World", new String(result).trim());
    }

    @Test
    @DisplayName("readFile should return empty array for empty file")
    void readFile_withEmptyFile_returnsEmptyArray(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("empty.txt").toFile();
        tempFile.createNewFile();
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // Tests for processUser method

    @Test
    @DisplayName("processUser should return empty string for null user")
    void processUser_withNullUser_returnsEmptyString() {
        String result = vulnerableCode.processUser(null);
        assertEquals("", result);
    }

    @Test
    @DisplayName("processUser should return empty string for user with null name")
    void processUser_withUserHavingNullName_returnsEmptyString() {
        VulnerableCode.User user = new VulnerableCode.User();
        String result = vulnerableCode.processUser(user);
        assertEquals("", result);
    }

    @Test
    @DisplayName("processUser should return uppercase name for valid user")
    void processUser_withValidUser_returnsUppercaseName() {
        VulnerableCode.User user = new VulnerableCode.User("john");
        String result = vulnerableCode.processUser(user);
        assertEquals("JOHN", result);
    }

    // Tests for encryptData method

    @Test
    @DisplayName("encryptData should not throw exception with valid input")
    void encryptData_withValidInput_doesNotThrow() {
        assertDoesNotThrow(() -> vulnerableCode.encryptData("test data"));
    }

    @Test
    @DisplayName("encryptData should return non-null result")
    void encryptData_withValidInput_returnsNonNull() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("test data");
        assertNotNull(result);
    }

    @Test
    @DisplayName("encryptData should return hex string")
    void encryptData_withValidInput_returnsHexString() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("test data");
        assertNotNull(result);
        assertTrue(result.matches("[0-9a-f]+"), "Result should be a hex string");
    }

    @Test
    @DisplayName("encryptData should return different results for same input (due to random IV)")
    void encryptData_withSameInput_returnsDifferentResults() throws GeneralSecurityException {
        String result1 = vulnerableCode.encryptData("test data");
        String result2 = vulnerableCode.encryptData("test data");
        // Due to random IV, results should be different
        assertNotEquals(result1, result2);
    }

    // Tests for generateToken method

    @Test
    @DisplayName("generateToken should return a number in valid range")
    void generateToken_returnsNumber() {
        int token = vulnerableCode.generateToken();
        assertTrue(token >= 0 && token < 1000000);
    }

    @Test
    @DisplayName("generateToken should return different values on multiple calls")
    void generateToken_returnsDifferentValues() {
        int token1 = vulnerableCode.generateToken();
        int token2 = vulnerableCode.generateToken();
        int token3 = vulnerableCode.generateToken();
        
        // At least two should be different (statistically very likely with SecureRandom)
        boolean allSame = (token1 == token2) && (token2 == token3);
        assertFalse(allSame, "Tokens should not all be the same");
    }

    // Tests for User inner class

    @Test
    @DisplayName("User default constructor should create user with null name")
    void user_defaultConstructor_createsUserWithNullName() {
        VulnerableCode.User user = new VulnerableCode.User();
        assertNull(user.getName());
    }

    @Test
    @DisplayName("User constructor with name should set name")
    void user_constructorWithName_setsName() {
        VulnerableCode.User user = new VulnerableCode.User("Alice");
        assertEquals("Alice", user.getName());
    }

    @Test
    @DisplayName("User setName should update name")
    void user_setName_updatesName() {
        VulnerableCode.User user = new VulnerableCode.User();
        user.setName("Bob");
        assertEquals("Bob", user.getName());
    }

    // Additional tests for getUserByUsername method

    @Test
    @DisplayName("getUserByUsername should throw SQLException with empty username")
    void getUserByUsername_withEmptyUsername_throwsException() {
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername(""));
    }

    @Test
    @DisplayName("getUserByUsername should throw SQLException with special characters")
    void getUserByUsername_withSpecialCharacters_throwsException() {
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername("user'; DROP TABLE users;--"));
    }

    // Additional tests for encryptData method

    @Test
    @DisplayName("encryptData should handle empty string")
    void encryptData_withEmptyString_returnsNonNull() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("encryptData should handle long string")
    void encryptData_withLongString_returnsNonNull() throws GeneralSecurityException {
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longString.append("a");
        }
        String result = vulnerableCode.encryptData(longString.toString());
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("encryptData should handle special characters")
    void encryptData_withSpecialCharacters_returnsNonNull() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("!@#$%^&*()_+-=[]{}|;':\",./<>?");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    // Additional tests for generateToken method

    @Test
    @DisplayName("generateToken should return values in range multiple times")
    void generateToken_multipleCallsReturnValidRange() {
        for (int i = 0; i < 100; i++) {
            int token = vulnerableCode.generateToken();
            assertTrue(token >= 0 && token < 1000000, "Token should be in valid range");
        }
    }

    // Additional tests for processUser method

    @Test
    @DisplayName("processUser should handle user with empty name")
    void processUser_withEmptyName_returnsEmptyString() {
        VulnerableCode.User user = new VulnerableCode.User("");
        String result = vulnerableCode.processUser(user);
        assertEquals("", result);
    }

    @Test
    @DisplayName("processUser should handle user with whitespace name")
    void processUser_withWhitespaceName_returnsUppercase() {
        VulnerableCode.User user = new VulnerableCode.User("  ");
        String result = vulnerableCode.processUser(user);
        assertEquals("  ", result);
    }

    @Test
    @DisplayName("processUser should handle user with mixed case name")
    void processUser_withMixedCaseName_returnsUppercase() {
        VulnerableCode.User user = new VulnerableCode.User("JoHn DoE");
        String result = vulnerableCode.processUser(user);
        assertEquals("JOHN DOE", result);
    }

    // Additional tests for readFile method

    @Test
    @DisplayName("readFile should handle file with special characters in content")
    void readFile_withSpecialCharacters_returnsContent(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("special.txt").toFile();
        String specialContent = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(specialContent);
        }
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals(specialContent, new String(result));
    }

    @Test
    @DisplayName("readFile should handle file with newlines")
    void readFile_withNewlines_returnsContent(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("newlines.txt").toFile();
        String content = "line1\nline2\nline3";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals(content, new String(result));
    }

    // Additional tests for encryptData to improve coverage

    @Test
    @DisplayName("encryptData should produce non-empty output for single character")
    void encryptData_withSingleChar_returnsNonEmpty() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("a");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("encryptData should produce valid hex for unicode input")
    void encryptData_withUnicode_returnsValidHex() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("\u00e9\u00e8\u00ea");
        assertNotNull(result);
        assertTrue(result.matches("[0-9a-f]+"));
    }

    @Test
    @DisplayName("encryptData should handle numeric string")
    void encryptData_withNumericString_returnsNonNull() throws GeneralSecurityException {
        String result = vulnerableCode.encryptData("1234567890");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    // Additional tests for User class to improve coverage

    @Test
    @DisplayName("User getName after setName should return updated value")
    void user_getNameAfterSetName_returnsUpdatedValue() {
        VulnerableCode.User user = new VulnerableCode.User("initial");
        assertEquals("initial", user.getName());
        user.setName("updated");
        assertEquals("updated", user.getName());
    }

    @Test
    @DisplayName("User setName with null should set null")
    void user_setNameWithNull_setsNull() {
        VulnerableCode.User user = new VulnerableCode.User("initial");
        user.setName(null);
        assertNull(user.getName());
    }

    @Test
    @DisplayName("User constructor with empty string should set empty name")
    void user_constructorWithEmptyString_setsEmptyName() {
        VulnerableCode.User user = new VulnerableCode.User("");
        assertEquals("", user.getName());
    }

    // Additional tests for processUser to improve coverage

    @Test
    @DisplayName("processUser should handle user with numbers in name")
    void processUser_withNumbersInName_returnsUppercase() {
        VulnerableCode.User user = new VulnerableCode.User("user123");
        String result = vulnerableCode.processUser(user);
        assertEquals("USER123", result);
    }

    @Test
    @DisplayName("processUser should handle user with special characters in name")
    void processUser_withSpecialCharsInName_returnsUppercase() {
        VulnerableCode.User user = new VulnerableCode.User("user-name_test");
        String result = vulnerableCode.processUser(user);
        assertEquals("USER-NAME_TEST", result);
    }

    // Additional tests for generateToken to improve coverage

    @Test
    @DisplayName("generateToken should not return negative values")
    void generateToken_shouldNotReturnNegative() {
        for (int i = 0; i < 50; i++) {
            int token = vulnerableCode.generateToken();
            assertTrue(token >= 0, "Token should not be negative");
        }
    }

    @Test
    @DisplayName("generateToken should return values less than 1000000")
    void generateToken_shouldReturnLessThanMillion() {
        for (int i = 0; i < 50; i++) {
            int token = vulnerableCode.generateToken();
            assertTrue(token < 1000000, "Token should be less than 1000000");
        }
    }

    // Additional tests for readFile to improve coverage

    @Test
    @DisplayName("readFile should handle file with exactly 1024 bytes")
    void readFile_withExactBufferSize_returnsContent(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("exact.txt").toFile();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            content.append("x");
        }
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content.toString());
        }
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals(1024, result.length);
    }

    @Test
    @DisplayName("readFile should handle file with single byte")
    void readFile_withSingleByte_returnsContent(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("single.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("X");
        }
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("X", new String(result));
    }

    // Additional getUserByUsername tests

    @Test
    @DisplayName("getUserByUsername should throw SQLException with whitespace username")
    void getUserByUsername_withWhitespaceUsername_throwsException() {
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername("   "));
    }

    @Test
    @DisplayName("getUserByUsername should throw SQLException with very long username")
    void getUserByUsername_withVeryLongUsername_throwsException() {
        StringBuilder longUsername = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longUsername.append("a");
        }
        assertThrows(java.sql.SQLException.class, () -> vulnerableCode.getUserByUsername(longUsername.toString()));
    }
}
