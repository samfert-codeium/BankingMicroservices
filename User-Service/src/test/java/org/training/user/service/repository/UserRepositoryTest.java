package org.training.user.service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.training.user.service.model.Status;
import org.training.user.service.model.entity.User;
import org.training.user.service.model.entity.UserProfile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByAuthId_existingUser_returnsUser() {
        String authId = "auth-123";
        User user = User.builder()
                .authId(authId)
                .emailId("test@example.com")
                .contactNo("1234567890")
                .status(Status.PENDING)
                .identificationNumber("ID-123")
                .userProfile(UserProfile.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();

        userRepository.save(user);

        Optional<User> result = userRepository.findUserByAuthId(authId);

        assertTrue(result.isPresent());
        assertEquals(authId, result.get().getAuthId());
        assertEquals("test@example.com", result.get().getEmailId());
    }

    @Test
    void findUserByAuthId_nonExistingUser_returnsEmpty() {
        Optional<User> result = userRepository.findUserByAuthId("non-existing");

        assertFalse(result.isPresent());
    }
}
