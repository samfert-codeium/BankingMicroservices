package org.training.fundtransfer.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.training.fundtransfer.exception.AccountUpdateException;
import org.training.fundtransfer.exception.InsufficientBalance;
import org.training.fundtransfer.exception.ResourceNotFound;
import org.training.fundtransfer.external.AccountService;
import org.training.fundtransfer.external.TransactionService;
import org.training.fundtransfer.model.TransactionStatus;
import org.training.fundtransfer.model.TransferType;
import org.training.fundtransfer.model.dto.Account;
import org.training.fundtransfer.model.dto.Transaction;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;
import org.training.fundtransfer.model.dto.response.Response;
import org.training.fundtransfer.model.entity.FundTransfer;
import org.training.fundtransfer.repository.FundTransferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundTransferServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private FundTransferRepository fundTransferRepository;

    @InjectMocks
    private FundTransferServiceImpl fundTransferService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fundTransferService, "ok", "200");
    }

    @Test
    void fundTransfer_validAccountsAndSufficientBalance_success() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("ACC0001234");
        request.setToAccount("ACC0005678");
        request.setAmount(BigDecimal.valueOf(500));

        Account fromAccount = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        Account toAccount = Account.builder()
                .accountNumber("ACC0005678")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(500))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(fromAccount));
        when(accountService.readByAccountNumber("ACC0005678"))
                .thenReturn(ResponseEntity.ok(toAccount));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok(Response.builder().build()));
        when(transactionService.makeInternalTransactions(anyList(), anyString()))
                .thenReturn(ResponseEntity.ok(Response.builder().build()));
        when(fundTransferRepository.save(any(FundTransfer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FundTransferResponse response = fundTransferService.fundTransfer(request);

        assertNotNull(response);
        assertEquals("Fund transfer was successful", response.getMessage());
        assertNotNull(response.getTransactionId());
        verify(accountService, times(2)).readByAccountNumber(anyString());
        verify(accountService, times(2)).updateAccount(anyString(), any(Account.class));
        verify(transactionService).makeInternalTransactions(anyList(), anyString());
        verify(fundTransferRepository).save(any(FundTransfer.class));
    }

    @Test
    void fundTransfer_sourceAccountNotFound_throwsResourceNotFound() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("ACC0001234");
        request.setToAccount("ACC0005678");
        request.setAmount(BigDecimal.valueOf(500));

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> fundTransferService.fundTransfer(request));
        verify(accountService).readByAccountNumber("ACC0001234");
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void fundTransfer_destinationAccountNotFound_throwsResourceNotFound() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("ACC0001234");
        request.setToAccount("ACC0005678");
        request.setAmount(BigDecimal.valueOf(500));

        Account fromAccount = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(fromAccount));
        when(accountService.readByAccountNumber("ACC0005678"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void fundTransfer_inactiveAccount_throwsAccountUpdateException() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("ACC0001234");
        request.setToAccount("ACC0005678");
        request.setAmount(BigDecimal.valueOf(500));

        Account fromAccount = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("PENDING")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(fromAccount));

        assertThrows(AccountUpdateException.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void fundTransfer_insufficientBalance_throwsInsufficientBalance() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("ACC0001234");
        request.setToAccount("ACC0005678");
        request.setAmount(BigDecimal.valueOf(1500));

        Account fromAccount = Account.builder()
                .accountNumber("ACC0001234")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0001234"))
                .thenReturn(ResponseEntity.ok(fromAccount));

        assertThrows(InsufficientBalance.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any());
    }

    @Test
    void getTransferDetailsFromReferenceId_validReference_returnsFundTransferDto() {
        String referenceId = "REF123456";
        FundTransfer fundTransfer = FundTransfer.builder()
                .fundTransferId(1L)
                .transactionReference(referenceId)
                .fromAccount("ACC0001234")
                .toAccount("ACC0005678")
                .amount(BigDecimal.valueOf(500))
                .status(TransactionStatus.SUCCESS)
                .transferType(TransferType.INTERNAL)
                .build();

        when(fundTransferRepository.findFundTransferByTransactionReference(referenceId))
                .thenReturn(Optional.of(fundTransfer));

        assertDoesNotThrow(() -> fundTransferService.getTransferDetailsFromReferenceId(referenceId));
        verify(fundTransferRepository).findFundTransferByTransactionReference(referenceId);
    }

    @Test
    void getTransferDetailsFromReferenceId_invalidReference_throwsResourceNotFound() {
        String referenceId = "INVALID_REF";

        when(fundTransferRepository.findFundTransferByTransactionReference(referenceId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> fundTransferService.getTransferDetailsFromReferenceId(referenceId));
        verify(fundTransferRepository).findFundTransferByTransactionReference(referenceId);
    }

    @Test
    void getAllTransfersByAccountId_validAccountId_returnsTransferList() {
        String accountId = "ACC0001234";
        List<FundTransfer> transfers = List.of(
                FundTransfer.builder()
                        .fundTransferId(1L)
                        .fromAccount(accountId)
                        .toAccount("ACC0005678")
                        .amount(BigDecimal.valueOf(500))
                        .build(),
                FundTransfer.builder()
                        .fundTransferId(2L)
                        .fromAccount(accountId)
                        .toAccount("ACC0009999")
                        .amount(BigDecimal.valueOf(300))
                        .build()
        );

        when(fundTransferRepository.findFundTransferByFromAccount(accountId))
                .thenReturn(transfers);

        assertDoesNotThrow(() -> fundTransferService.getAllTransfersByAccountId(accountId));
        verify(fundTransferRepository).findFundTransferByFromAccount(accountId);
    }
}
