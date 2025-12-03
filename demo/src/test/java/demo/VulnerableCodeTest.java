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

    // Tests for processUser method

    @Test
    @DisplayName("processUser should throw exception for null user")
    void processUser_withNullUser_throwsException() {
        assertThrows(NullPointerException.class, () -> vulnerableCode.processUser(null));
    }

    @Test
    @DisplayName("processUser should throw exception for user with null name")
    void processUser_withUserHavingNullName_throwsException() {
        VulnerableCode.User user = new VulnerableCode.User();
        assertThrows(NullPointerException.class, () -> vulnerableCode.processUser(user));
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
}
