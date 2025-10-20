package org.training.user.service.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.training.user.service.exception.EmptyFields;
import org.training.user.service.exception.ResourceConflictException;
import org.training.user.service.exception.ResourceNotFound;
import org.training.user.service.external.AccountService;
import org.training.user.service.model.Status;
import org.training.user.service.model.dto.CreateUser;
import org.training.user.service.model.dto.UserDto;
import org.training.user.service.model.dto.UserUpdate;
import org.training.user.service.model.dto.UserUpdateStatus;
import org.training.user.service.model.dto.response.Response;
import org.training.user.service.model.entity.User;
import org.training.user.service.model.entity.UserProfile;
import org.training.user.service.model.external.Account;
import org.training.user.service.repository.UserRepository;
import org.training.user.service.service.KeycloakService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "responseCodeSuccess", "200");
        ReflectionTestUtils.setField(userService, "responseCodeNotFound", "404");
    }

    @Test
    void createUser_validUser_success() {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("auth-123");
        
        when(keycloakService.readUserByEmail("test@example.com"))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(userRep));
        
        when(keycloakService.createUser(any(UserRepresentation.class)))
                .thenReturn(201);
        
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        CreateUser createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .emailId("test@example.com")
                .password("password123")
                .contactNumber("1234567890")
                .build();
        
        Response response = userService.createUser(createUser);
        
        assertNotNull(response);
        assertEquals("User created successfully", response.getResponseMessage());
        verify(keycloakService, times(2)).readUserByEmail("test@example.com");
        verify(keycloakService).createUser(any(UserRepresentation.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_throwsResourceConflictException() {
        UserRepresentation existingUser = new UserRepresentation();
        existingUser.setId("existing-123");
        
        when(keycloakService.readUserByEmail("test@example.com"))
                .thenReturn(Collections.singletonList(existingUser));
        
        CreateUser createUser = CreateUser.builder()
                .emailId("test@example.com")
                .build();
        
        assertThrows(ResourceConflictException.class, () -> userService.createUser(createUser));
        verify(keycloakService, never()).createUser(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_keycloakFailure_throwsRuntimeException() {
        when(keycloakService.readUserByEmail(anyString()))
                .thenReturn(Collections.emptyList());
        when(keycloakService.createUser(any(UserRepresentation.class)))
                .thenReturn(400);
        
        CreateUser createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .emailId("test@example.com")
                .password("password123")
                .contactNumber("1234567890")
                .build();
        
        assertThrows(RuntimeException.class, () -> userService.createUser(createUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void readUser_existingUser_returnsUserDto() {
        String authId = "auth-123";
        User user = User.builder()
                .userId(1L)
                .authId(authId)
                .contactNo("1234567890")
                .status(Status.APPROVED)
                .identificationNumber("ID-123")
                .userProfile(UserProfile.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        
        UserRepresentation userRep = new UserRepresentation();
        userRep.setEmail("test@example.com");
        
        when(userRepository.findUserByAuthId(authId))
                .thenReturn(Optional.of(user));
        when(keycloakService.readUser(authId))
                .thenReturn(userRep);
        
        UserDto result = userService.readUser(authId);
        
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmailId());
        verify(userRepository).findUserByAuthId(authId);
        verify(keycloakService).readUser(authId);
    }

    @Test
    void readUser_nonExistingUser_throwsResourceNotFound() {
        String authId = "non-existing";
        
        when(userRepository.findUserByAuthId(authId))
                .thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFound.class, () -> userService.readUser(authId));
    }

    @Test
    void updateUserStatus_approvedStatus_enablesKeycloakAccount() {
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .authId("auth-123")
                .emailId("test@example.com")
                .contactNo("1234567890")
                .status(Status.PENDING)
                .identificationNumber("ID-123")
                .creationOn(java.time.LocalDate.now())
                .userProfile(UserProfile.builder()
                        .userProfileId(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .gender("Male")
                        .address("123 Main St")
                        .occupation("Engineer")
                        .martialStatus("Single")
                        .nationality("US")
                        .build())
                .build();
        
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("auth-123");
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(keycloakService.readUser("auth-123"))
                .thenReturn(userRep);
        doNothing().when(keycloakService).updateUser(any(UserRepresentation.class));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        UserUpdateStatus statusUpdate = new UserUpdateStatus();
        statusUpdate.setStatus(Status.APPROVED);
        
        Response response = userService.updateUserStatus(userId, statusUpdate);
        
        assertNotNull(response);
        assertEquals("User updated successfully", response.getResponseMessage());
        verify(keycloakService).readUser("auth-123");
        verify(keycloakService).updateUser(any(UserRepresentation.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserStatus_otherStatus_doesNotCallKeycloak() {
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .authId("auth-123")
                .emailId("test@example.com")
                .contactNo("1234567890")
                .status(Status.PENDING)
                .identificationNumber("ID-123")
                .creationOn(java.time.LocalDate.now())
                .userProfile(UserProfile.builder()
                        .userProfileId(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .gender("Male")
                        .address("123 Main St")
                        .occupation("Engineer")
                        .martialStatus("Single")
                        .nationality("US")
                        .build())
                .build();
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        UserUpdateStatus statusUpdate = new UserUpdateStatus();
        statusUpdate.setStatus(Status.REJECTED);
        
        Response response = userService.updateUserStatus(userId, statusUpdate);
        
        assertNotNull(response);
        verify(keycloakService, never()).readUser(anyString());
        verify(keycloakService, never()).updateUser(any());
    }

    @Test
    void updateUserStatus_userNotFound_throwsResourceNotFound() {
        Long userId = 999L;
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        UserUpdateStatus statusUpdate = new UserUpdateStatus();
        statusUpdate.setStatus(Status.APPROVED);
        
        assertThrows(ResourceNotFound.class, () -> userService.updateUserStatus(userId, statusUpdate));
    }

    @Test
    void readUserById_existingUser_returnsUserDto() {
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .authId("auth-123")
                .contactNo("1234567890")
                .status(Status.APPROVED)
                .identificationNumber("ID-123")
                .userProfile(UserProfile.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        
        UserDto result = userService.readUserById(userId);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userRepository).findById(userId);
    }

    @Test
    void readUserById_nonExistingUser_throwsResourceNotFound() {
        Long userId = 999L;
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFound.class, () -> userService.readUserById(userId));
    }

    @Test
    void updateUser_validUpdate_success() {
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .contactNo("1234567890")
                .userProfile(UserProfile.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        UserUpdate userUpdate = UserUpdate.builder()
                .firstName("Jane")
                .lastName("Smith")
                .contactNo("0987654321")
                .build();
        
        Response response = userService.updateUser(userId, userUpdate);
        
        assertNotNull(response);
        assertEquals("user updated successfully", response.getResponseMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_userNotFound_throwsResourceNotFound() {
        Long userId = 999L;
        
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        UserUpdate userUpdate = UserUpdate.builder().build();
        
        assertThrows(ResourceNotFound.class, () -> userService.updateUser(userId, userUpdate));
    }

    @Test
    void readUserByAccountId_validAccount_returnsUserDto() {
        String accountId = "ACC123";
        Long userId = 1L;
        
        Account account = new Account();
        account.setUserId(userId);
        
        User user = User.builder()
                .userId(userId)
                .authId("auth-123")
                .contactNo("1234567890")
                .status(Status.APPROVED)
                .identificationNumber("ID-123")
                .userProfile(UserProfile.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
        
        when(accountService.readByAccountNumber(accountId))
                .thenReturn(ResponseEntity.ok(account));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        
        UserDto result = userService.readUserByAccountId(accountId);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(accountService).readByAccountNumber(accountId);
        verify(userRepository).findById(userId);
    }

    @Test
    void readUserByAccountId_accountNotFound_throwsResourceNotFound() {
        String accountId = "INVALID";
        
        when(accountService.readByAccountNumber(accountId))
                .thenReturn(ResponseEntity.ok(null));
        
        assertThrows(ResourceNotFound.class, () -> userService.readUserByAccountId(accountId));
    }

    @Test
    void readUserByAccountId_userNotFound_throwsResourceNotFound() {
        String accountId = "ACC123";
        Long userId = 999L;
        
        Account account = new Account();
        account.setUserId(userId);
        
        when(accountService.readByAccountNumber(accountId))
                .thenReturn(ResponseEntity.ok(account));
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFound.class, () -> userService.readUserByAccountId(accountId));
    }
}
