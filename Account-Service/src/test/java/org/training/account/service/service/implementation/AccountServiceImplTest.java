package org.training.account.service.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.training.account.service.exception.*;
import org.training.account.service.external.SequenceService;
import org.training.account.service.external.TransactionService;
import org.training.account.service.external.UserService;
import org.training.account.service.model.AccountStatus;
import org.training.account.service.model.AccountType;
import org.training.account.service.model.dto.AccountDto;
import org.training.account.service.model.dto.AccountStatusUpdate;
import org.training.account.service.model.dto.external.SequenceDto;
import org.training.account.service.model.dto.external.UserDto;
import org.training.account.service.model.dto.response.Response;
import org.training.account.service.model.entity.Account;
import org.training.account.service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SequenceService sequenceService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(accountService, "success", "200");
    }

    @Test
    void createAccount_validUser_success() {
        AccountDto accountDto = AccountDto.builder()
                .userId(1L)
                .accountType("SAVINGS_ACCOUNT")
                .build();

        UserDto userDto = UserDto.builder()
                .userId(1L)
                .firstName("testuser")
                .build();

        SequenceDto sequenceDto = new SequenceDto();
        sequenceDto.setAccountNumber(1234567L);

        when(userService.readUserById(1L))
                .thenReturn(ResponseEntity.ok(userDto));
        when(accountRepository.findAccountByUserIdAndAccountType(1L, AccountType.SAVINGS_ACCOUNT))
                .thenReturn(Optional.empty());
        when(sequenceService.generateAccountNumber())
                .thenReturn(sequenceDto);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = accountService.createAccount(accountDto);

        assertNotNull(response);
        assertEquals(" Account created successfully", response.getMessage());
        verify(userService).readUserById(1L);
        verify(accountRepository).findAccountByUserIdAndAccountType(1L, AccountType.SAVINGS_ACCOUNT);
        verify(sequenceService).generateAccountNumber();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_userNotFound_throwsResourceNotFound() {
        AccountDto accountDto = AccountDto.builder()
                .userId(1L)
                .accountType("SAVINGS_ACCOUNT")
                .build();

        when(userService.readUserById(1L))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> accountService.createAccount(accountDto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_duplicateAccount_throwsResourceConflict() {
        AccountDto accountDto = AccountDto.builder()
                .userId(1L)
                .accountType("SAVINGS_ACCOUNT")
                .build();

        UserDto userDto = UserDto.builder()
                .userId(1L)
                .firstName("testuser")
                .build();

        Account existingAccount = Account.builder()
                .userId(1L)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .build();

        when(userService.readUserById(1L))
                .thenReturn(ResponseEntity.ok(userDto));
        when(accountRepository.findAccountByUserIdAndAccountType(1L, AccountType.SAVINGS_ACCOUNT))
                .thenReturn(Optional.of(existingAccount));

        assertThrows(ResourceConflict.class, () -> accountService.createAccount(accountDto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateStatus_validAccount_success() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountStatus(AccountStatus.PENDING)
                .availableBalance(BigDecimal.valueOf(1500))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = accountService.updateStatus(accountNumber, statusUpdate);

        assertNotNull(response);
        assertEquals("Account updated successfully", response.getMessage());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateStatus_insufficientFunds_throwsInSufficientFunds() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountStatus(AccountStatus.PENDING)
                .availableBalance(BigDecimal.valueOf(500))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(InSufficientFunds.class, () -> accountService.updateStatus(accountNumber, statusUpdate));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void readAccountByAccountNumber_existingAccount_returnsAccount() {
        String accountNumber = "ACC0001234";
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        AccountDto result = accountService.readAccountByAccountNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        verify(accountRepository).findAccountByAccountNumber(accountNumber);
    }

    @Test
    void readAccountByAccountNumber_nonExistingAccount_throwsResourceNotFound() {
        String accountNumber = "NONEXISTENT";

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> accountService.readAccountByAccountNumber(accountNumber));
    }

    @Test
    void updateAccount_validAccount_success() {
        String accountNumber = "ACC0001234";
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(accountNumber)
                .availableBalance(BigDecimal.valueOf(2000))
                .build();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = accountService.updateAccount(accountNumber, accountDto);

        assertNotNull(response);
        assertEquals("Account updated successfully", response.getMessage());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getBalance_existingAccount_returnsBalance() {
        String accountNumber = "ACC0001234";
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .availableBalance(BigDecimal.valueOf(1500))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        String balance = accountService.getBalance(accountNumber);

        assertEquals("1500", balance);
        verify(accountRepository).findAccountByAccountNumber(accountNumber);
    }

    @Test
    void closeAccount_zeroBalance_success() {
        String accountNumber = "ACC0001234";
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.ZERO)
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        Response response = accountService.closeAccount(accountNumber);

        assertNotNull(response);
        verify(accountRepository, times(2)).findAccountByAccountNumber(accountNumber);
    }

    @Test
    void closeAccount_nonZeroBalance_throwsAccountClosingException() {
        String accountNumber = "ACC0001234";
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(AccountClosingException.class, () -> accountService.closeAccount(accountNumber));
    }

    @Test
    void readAccountByUserId_activeAccount_returnsAccount() {
        Long userId = 1L;
        Account account = Account.builder()
                .userId(userId)
                .accountNumber("ACC0001234")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountRepository.findAccountByUserId(userId))
                .thenReturn(Optional.of(account));

        AccountDto result = accountService.readAccountByUserId(userId);

        assertNotNull(result);
        assertEquals("ACC0001234", result.getAccountNumber());
        verify(accountRepository).findAccountByUserId(userId);
    }

    @Test
    void readAccountByUserId_inactiveAccount_throwsAccountStatusException() {
        Long userId = 1L;
        Account account = Account.builder()
                .userId(userId)
                .accountNumber("ACC0001234")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.PENDING)
                .build();

        when(accountRepository.findAccountByUserId(userId))
                .thenReturn(Optional.of(account));

        assertThrows(AccountStatusException.class, () -> accountService.readAccountByUserId(userId));
    }
}
