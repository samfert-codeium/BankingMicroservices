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
}
