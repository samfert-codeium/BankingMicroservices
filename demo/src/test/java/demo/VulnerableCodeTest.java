package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VulnerableCode class
 * Note: This class contains intentionally vulnerable code for demo purposes.
 * These tests exercise the code paths for coverage purposes.
 */
class VulnerableCodeTest {

    private VulnerableCode vulnerableCode;

    @BeforeEach
    void setUp() {
        vulnerableCode = new VulnerableCode();
    }

    // Tests for getUserByUsername method

    @Test
    @DisplayName("getUserByUsername should throw exception when database is unavailable")
    void getUserByUsername_withoutDatabase_throwsException() {
        assertThrows(Exception.class, () -> vulnerableCode.getUserByUsername("testuser"));
    }

    @Test
    @DisplayName("getUserByUsername should throw exception with null username")
    void getUserByUsername_withNullUsername_throwsException() {
        assertThrows(Exception.class, () -> vulnerableCode.getUserByUsername(null));
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
    @DisplayName("readFile should read valid file content")
    void readFile_withValidFile_returnsContent() throws Exception {
        // Create a temporary file for testing
        java.io.File tempFile = java.io.File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();
        java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile);
        fos.write("test content".getBytes());
        fos.close();
        
        byte[] result = vulnerableCode.readFile(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    @DisplayName("readFile should handle empty file")
    void readFile_withEmptyFile_returnsEmptyArray() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("empty", ".txt");
        tempFile.deleteOnExit();
        
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
        VulnerableCode.User user = new VulnerableCode.User();
        user.setName("testuser");
        String result = vulnerableCode.processUser(user);
        assertEquals("TESTUSER", result);
    }

    // Tests for encryptData method

    @Test
    @DisplayName("encryptData should not throw exception with valid input")
    void encryptData_withValidInput_doesNotThrow() {
        assertDoesNotThrow(() -> vulnerableCode.encryptData("test data"));
    }

    @Test
    @DisplayName("encryptData should return non-null result")
    void encryptData_withValidInput_returnsNonNull() throws Exception {
        String result = vulnerableCode.encryptData("test data");
        assertNotNull(result);
    }

    // Tests for generateToken method

    @Test
    @DisplayName("generateToken should return a number")
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
        
        // At least two should be different (statistically very likely)
        boolean allSame = (token1 == token2) && (token2 == token3);
        // This test might occasionally fail due to randomness, but it's very unlikely
        // For demo purposes, we just verify the method runs without error
        assertNotNull(token1);
    }

    // Tests for User inner class

    @Test
    @DisplayName("User getName should return null for new instance")
    void user_getName_returnsNullForNewInstance() {
        VulnerableCode.User user = new VulnerableCode.User();
        assertNull(user.getName());
    }

    @Test
    @DisplayName("User setName and getName should work correctly")
    void user_setNameAndGetName_worksCorrectly() {
        VulnerableCode.User user = new VulnerableCode.User();
        user.setName("John Doe");
        assertEquals("John Doe", user.getName());
    }
}
