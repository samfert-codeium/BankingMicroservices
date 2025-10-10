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
import org.training.account.service.model.dto.external.TransactionResponse;
import org.training.account.service.model.dto.external.UserDto;
import org.training.account.service.model.dto.response.Response;
import org.training.account.service.model.entity.Account;
import org.training.account.service.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
                .firstName("John")
                .lastName("Doe")
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
                .thenReturn(new Account());

        Response response = accountService.createAccount(accountDto);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_userNotFound_throwsResourceNotFound() {
        AccountDto accountDto = AccountDto.builder()
                .userId(999L)
                .accountType("SAVINGS_ACCOUNT")
                .build();

        when(userService.readUserById(999L))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> accountService.createAccount(accountDto));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void createAccount_duplicateAccount_throwsResourceConflict() {
        AccountDto accountDto = AccountDto.builder()
                .userId(1L)
                .accountType("SAVINGS_ACCOUNT")
                .build();

        UserDto userDto = UserDto.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Account existingAccount = new Account();
        existingAccount.setAccountId(1L);
        existingAccount.setUserId(1L);
        existingAccount.setAccountType(AccountType.SAVINGS_ACCOUNT);

        when(userService.readUserById(1L))
                .thenReturn(ResponseEntity.ok(userDto));
        when(accountRepository.findAccountByUserIdAndAccountType(1L, AccountType.SAVINGS_ACCOUNT))
                .thenReturn(Optional.of(existingAccount));

        assertThrows(ResourceConflict.class, () -> accountService.createAccount(accountDto));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateStatus_validUpdate_success() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountStatus(AccountStatus.PENDING);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        Response response = accountService.updateStatus(accountNumber, statusUpdate);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateStatus_accountAlreadyActive_throwsAccountStatusException() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(AccountStatusException.class, 
                () -> accountService.updateStatus(accountNumber, statusUpdate));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateStatus_balanceLessThanMinimum_throwsInSufficientFunds() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountStatus(AccountStatus.PENDING);
        account.setAvailableBalance(BigDecimal.valueOf(999));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(InSufficientFunds.class, 
                () -> accountService.updateStatus(accountNumber, statusUpdate));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateStatus_balanceZero_throwsInSufficientFunds() {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountStatus(AccountStatus.PENDING);
        account.setAvailableBalance(BigDecimal.ZERO);

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(InSufficientFunds.class, 
                () -> accountService.updateStatus(accountNumber, statusUpdate));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateStatus_accountNotFound_throwsResourceNotFound() {
        String accountNumber = "ACC9999999";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.updateStatus(accountNumber, statusUpdate));
    }

    @Test
    void closeAccount_zeroBalance_success() {
        String accountNumber = "ACC0001234";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        Response response = accountService.closeAccount(accountNumber);

        assertNotNull(response);
        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());
    }

    @Test
    void closeAccount_nonZeroBalance_throwsAccountClosingException() {
        String accountNumber = "ACC0001234";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAvailableBalance(BigDecimal.valueOf(100));
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        assertThrows(AccountClosingException.class, 
                () -> accountService.closeAccount(accountNumber));
    }

    @Test
    void closeAccount_accountNotFound_throwsResourceNotFound() {
        String accountNumber = "ACC9999999";

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.closeAccount(accountNumber));
    }

    @Test
    void readAccountByUserId_activeAccount_success() {
        Long userId = 1L;

        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber("ACC0001234");
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountType(AccountType.SAVINGS_ACCOUNT);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findAccountByUserId(userId))
                .thenReturn(Optional.of(account));

        AccountDto result = accountService.readAccountByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("ACTIVE", result.getAccountStatus());
    }

    @Test
    void readAccountByUserId_inactiveAccount_throwsAccountStatusException() {
        Long userId = 1L;

        Account account = new Account();
        account.setUserId(userId);
        account.setAccountStatus(AccountStatus.PENDING);

        when(accountRepository.findAccountByUserId(userId))
                .thenReturn(Optional.of(account));

        assertThrows(AccountStatusException.class, 
                () -> accountService.readAccountByUserId(userId));
    }

    @Test
    void readAccountByUserId_accountNotFound_throwsResourceNotFound() {
        Long userId = 999L;

        when(accountRepository.findAccountByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.readAccountByUserId(userId));
    }

    @Test
    void readAccountByAccountNumber_accountExists_returnsAccountDto() {
        String accountNumber = "ACC0001234";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountType(AccountType.SAVINGS_ACCOUNT);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        AccountDto result = accountService.readAccountByAccountNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals("ACTIVE", result.getAccountStatus());
        assertEquals("SAVINGS_ACCOUNT", result.getAccountType());
    }

    @Test
    void readAccountByAccountNumber_accountNotFound_throwsResourceNotFound() {
        String accountNumber = "ACC9999999";

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.readAccountByAccountNumber(accountNumber));
    }

    @Test
    void updateAccount_validUpdate_success() {
        String accountNumber = "ACC0001234";
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(accountNumber)
                .availableBalance(BigDecimal.valueOf(2000))
                .build();

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        Response response = accountService.updateAccount(accountNumber, accountDto);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_accountNotFound_throwsResourceNotFound() {
        String accountNumber = "ACC9999999";
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(accountNumber)
                .build();

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.updateAccount(accountNumber, accountDto));
    }

    @Test
    void getBalance_accountExists_returnsBalance() {
        String accountNumber = "ACC0001234";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAvailableBalance(BigDecimal.valueOf(1500.50));

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        String balance = accountService.getBalance(accountNumber);

        assertNotNull(balance);
        assertEquals("1500.5", balance);
    }

    @Test
    void getBalance_accountNotFound_throwsResourceNotFound() {
        String accountNumber = "ACC9999999";

        when(accountRepository.findAccountByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> accountService.getBalance(accountNumber));
    }

    @Test
    void getTransactionsFromAccountId_returnsTransactionList() {
        String accountId = "ACC0001234";
        List<TransactionResponse> transactions = Arrays.asList(
                new TransactionResponse(),
                new TransactionResponse()
        );

        when(transactionService.getTransactionsFromAccountId(accountId))
                .thenReturn(transactions);

        List<TransactionResponse> result = accountService.getTransactionsFromAccountId(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionService, times(1)).getTransactionsFromAccountId(accountId);
    }
}
