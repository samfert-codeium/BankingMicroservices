package org.training.transactions.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.training.transactions.exception.AccountStatusException;
import org.training.transactions.exception.InsufficientBalance;
import org.training.transactions.exception.ResourceNotFound;
import org.training.transactions.external.AccountService;
import org.training.transactions.model.TransactionStatus;
import org.training.transactions.model.TransactionType;
import org.training.transactions.model.dto.TransactionDto;
import org.training.transactions.model.entity.Transaction;
import org.training.transactions.model.external.Account;
import org.training.transactions.model.response.Response;
import org.training.transactions.model.response.TransactionRequest;
import org.training.transactions.repository.TransactionRepository;
import org.training.transactions.service.implementation.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionService, "ok", "200");
    }

    @Test
    void addTransaction_deposit_success() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType("DEPOSIT")
                .amount(BigDecimal.valueOf(500))
                .description("Deposit funds")
                .build();

        Account account = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(new Transaction());

        Response response = transactionService.addTransaction(transactionDto);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        assertEquals("Transaction completed successfully", response.getMessage());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).updateAccount(anyString(), any(Account.class));
    }

    @Test
    void addTransaction_deposit_accountNotFound_throwsResourceNotFound() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC9999999")
                .transactionType("DEPOSIT")
                .amount(BigDecimal.valueOf(500))
                .build();

        when(accountService.readByAccountNumber("ACC9999999"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawal_success() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType("WITHDRAWAL")
                .amount(BigDecimal.valueOf(300))
                .description("Withdraw funds")
                .build();

        Account account = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(new Transaction());

        Response response = transactionService.addTransaction(transactionDto);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawal_accountNotActive_throwsAccountStatusException() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType("WITHDRAWAL")
                .amount(BigDecimal.valueOf(300))
                .build();

        Account account = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0001234")
                .accountStatus("PENDING")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));

        assertThrows(AccountStatusException.class, 
                () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawal_insufficientBalance_throwsInsufficientBalance() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType("WITHDRAWAL")
                .amount(BigDecimal.valueOf(1500))
                .build();

        Account account = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));

        assertThrows(InsufficientBalance.class, 
                () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawal_accountNotFound_throwsResourceNotFound() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC9999999")
                .transactionType("WITHDRAWAL")
                .amount(BigDecimal.valueOf(300))
                .build();

        when(accountService.readByAccountNumber("ACC9999999"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, 
                () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void internalTransaction_validTransactions_success() {
        List<TransactionDto> transactionDtos = Arrays.asList(
                TransactionDto.builder()
                        .accountId("ACC0001234")
                        .amount(BigDecimal.valueOf(500).negate())
                        .description("Debit")
                        .build(),
                TransactionDto.builder()
                        .accountId("ACC0005678")
                        .amount(BigDecimal.valueOf(500))
                        .description("Credit")
                        .build()
        );

        String transactionReference = "ref123";

        when(transactionRepository.saveAll(anyList()))
                .thenReturn(Arrays.asList(new Transaction(), new Transaction()));

        Response response = transactionService.internalTransaction(transactionDtos, transactionReference);

        assertNotNull(response);
        assertEquals("200", response.getResponseCode());
        assertEquals("Transaction completed successfully", response.getMessage());
        verify(transactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getTransaction_transactionsExist_returnsTransactionList() {
        String accountId = "ACC0001234";
        List<Transaction> transactions = Arrays.asList(
                createTransaction(accountId, TransactionType.DEPOSIT, BigDecimal.valueOf(100)),
                createTransaction(accountId, TransactionType.WITHDRAWAL, BigDecimal.valueOf(50))
        );

        when(transactionRepository.findTransactionByAccountId(accountId))
                .thenReturn(transactions);

        List<TransactionRequest> result = transactionService.getTransaction(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findTransactionByAccountId(accountId);
    }

    @Test
    void getTransaction_noTransactions_returnsEmptyList() {
        String accountId = "ACC0001234";

        when(transactionRepository.findTransactionByAccountId(accountId))
                .thenReturn(Arrays.asList());

        List<TransactionRequest> result = transactionService.getTransaction(accountId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findTransactionByAccountId(accountId);
    }

    @Test
    void getTransactionByTransactionReference_transactionsExist_returnsTransactionList() {
        String transactionReference = "ref123";
        List<Transaction> transactions = Arrays.asList(
                createTransaction("ACC0001234", TransactionType.INTERNAL_TRANSFER, BigDecimal.valueOf(500).negate()),
                createTransaction("ACC0005678", TransactionType.INTERNAL_TRANSFER, BigDecimal.valueOf(500))
        );

        when(transactionRepository.findTransactionByReferenceId(transactionReference))
                .thenReturn(transactions);

        List<TransactionRequest> result = transactionService.getTransactionByTransactionReference(transactionReference);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findTransactionByReferenceId(transactionReference);
    }

    @Test
    void getTransactionByTransactionReference_noTransactions_returnsEmptyList() {
        String transactionReference = "nonexistent";

        when(transactionRepository.findTransactionByReferenceId(transactionReference))
                .thenReturn(Arrays.asList());

        List<TransactionRequest> result = transactionService.getTransactionByTransactionReference(transactionReference);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findTransactionByReferenceId(transactionReference);
    }

    private Transaction createTransaction(String accountId, TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceId("ref123");
        return transaction;
    }
}
