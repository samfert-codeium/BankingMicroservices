package org.training.transactions.service.implementation;

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
import org.training.transactions.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

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
    void addTransaction_depositType_success() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType(TransactionType.DEPOSIT.toString())
                .amount(BigDecimal.valueOf(500))
                .description("Deposit test")
                .build();

        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok(null));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = transactionService.addTransaction(transactionDto);

        assertNotNull(response);
        assertEquals("Transaction completed successfully", response.getMessage());
        verify(accountService).readByAccountNumber("ACC0001234");
        verify(accountService).updateAccount(anyString(), any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawalWithSufficientBalance_success() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType(TransactionType.WITHDRAWAL.toString())
                .amount(BigDecimal.valueOf(500))
                .description("Withdrawal test")
                .build();

        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok(null));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = transactionService.addTransaction(transactionDto);

        assertNotNull(response);
        assertEquals("Transaction completed successfully", response.getMessage());
        verify(accountService).updateAccount(anyString(), any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void addTransaction_withdrawalInsufficientBalance_throwsInsufficientBalance() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType(TransactionType.WITHDRAWAL.toString())
                .amount(BigDecimal.valueOf(1500))
                .description("Withdrawal test")
                .build();

        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));

        assertThrows(InsufficientBalance.class, () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void addTransaction_withdrawalInactiveAccount_throwsAccountStatusException() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType(TransactionType.WITHDRAWAL.toString())
                .amount(BigDecimal.valueOf(500))
                .description("Withdrawal test")
                .build();

        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("PENDING")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(account));

        assertThrows(AccountStatusException.class, () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void addTransaction_accountNotFound_throwsResourceNotFound() {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType(TransactionType.DEPOSIT.toString())
                .amount(BigDecimal.valueOf(500))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> transactionService.addTransaction(transactionDto));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void internalTransaction_validTransactions_success() {
        List<TransactionDto> transactionDtos = List.of(
                TransactionDto.builder()
                        .accountId("ACC0001234")
                        .amount(BigDecimal.valueOf(-500))
                        .description("Internal transfer debit")
                        .build(),
                TransactionDto.builder()
                        .accountId("ACC0005678")
                        .amount(BigDecimal.valueOf(500))
                        .description("Internal transfer credit")
                        .build()
        );

        String transactionReference = "REF123456";

        when(transactionRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Response response = transactionService.internalTransaction(transactionDtos, transactionReference);

        assertNotNull(response);
        assertEquals("Transaction completed successfully", response.getMessage());
        verify(transactionRepository).saveAll(anyList());
    }

    @Test
    void getTransaction_validAccountId_returnsTransactions() {
        String accountId = "ACC0001234";
        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .transactionId(1L)
                        .accountId(accountId)
                        .amount(BigDecimal.valueOf(500))
                        .transactionType(TransactionType.DEPOSIT)
                        .status(TransactionStatus.COMPLETED)
                        .build()
        );

        when(transactionRepository.findTransactionByAccountId(accountId))
                .thenReturn(transactions);

        assertDoesNotThrow(() -> transactionService.getTransaction(accountId));
        verify(transactionRepository).findTransactionByAccountId(accountId);
    }

    @Test
    void getTransactionByTransactionReference_validReference_returnsTransactions() {
        String transactionReference = "REF123456";
        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .transactionId(1L)
                        .referenceId(transactionReference)
                        .accountId("ACC0001234")
                        .amount(BigDecimal.valueOf(500))
                        .transactionType(TransactionType.INTERNAL_TRANSFER)
                        .status(TransactionStatus.COMPLETED)
                        .build()
        );

        when(transactionRepository.findTransactionByReferenceId(transactionReference))
                .thenReturn(transactions);

        assertDoesNotThrow(() -> transactionService.getTransactionByTransactionReference(transactionReference));
        verify(transactionRepository).findTransactionByReferenceId(transactionReference);
    }
}
