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
import org.training.fundtransfer.model.dto.Account;
import org.training.fundtransfer.model.dto.FundTransferDto;
import org.training.fundtransfer.model.dto.Transaction;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;
import org.training.fundtransfer.model.dto.response.Response;
import org.training.fundtransfer.model.entity.FundTransfer;
import org.training.fundtransfer.repository.FundTransferRepository;

import java.math.BigDecimal;
import java.util.Arrays;
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
    private FundTransferRepository fundTransferRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private FundTransferServiceImpl fundTransferService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fundTransferService, "ok", "200");
    }

    @Test
    void fundTransfer_validTransfer_success() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .build();

        Account fromAccount = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0000001")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .userId(1L)
                .build();

        Account toAccount = Account.builder()
                .accountId(2L)
                .accountNumber("ACC0000002")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(500))
                .userId(2L)
                .build();

        when(accountService.readByAccountNumber("ACC0000001"))
                .thenReturn(ResponseEntity.ok(fromAccount));
        when(accountService.readByAccountNumber("ACC0000002"))
                .thenReturn(ResponseEntity.ok(toAccount));
        when(accountService.updateAccount(anyString(), any(Account.class)))
                .thenReturn(ResponseEntity.ok(Response.builder().build()));
        when(transactionService.makeInternalTransactions(anyList(), anyString()))
                .thenReturn(ResponseEntity.ok(Response.builder().build()));
        when(fundTransferRepository.save(any(FundTransfer.class)))
                .thenReturn(new FundTransfer());

        FundTransferResponse response = fundTransferService.fundTransfer(request);

        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        assertEquals("Fund transfer was successful", response.getMessage());
        verify(fundTransferRepository, times(1)).save(any(FundTransfer.class));
        verify(accountService, times(2)).updateAccount(anyString(), any(Account.class));
        verify(transactionService, times(1)).makeInternalTransactions(anyList(), anyString());
    }

    @Test
    void fundTransfer_sourceAccountNotFound_throwsResourceNotFound() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC9999999")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .build();

        when(accountService.readByAccountNumber("ACC9999999"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any(FundTransfer.class));
    }

    @Test
    void fundTransfer_destinationAccountNotFound_throwsResourceNotFound() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC9999999")
                .amount(BigDecimal.valueOf(500))
                .build();

        Account fromAccount = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0000001")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0000001"))
                .thenReturn(ResponseEntity.ok(fromAccount));
        when(accountService.readByAccountNumber("ACC9999999"))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(ResourceNotFound.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any(FundTransfer.class));
    }

    @Test
    void fundTransfer_sourceAccountNotActive_throwsAccountUpdateException() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .build();

        Account fromAccount = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0000001")
                .accountStatus("PENDING")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0000001"))
                .thenReturn(ResponseEntity.ok(fromAccount));

        assertThrows(AccountUpdateException.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any(FundTransfer.class));
    }

    @Test
    void fundTransfer_insufficientBalance_throwsInsufficientBalance() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(1500))
                .build();

        Account fromAccount = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0000001")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0000001"))
                .thenReturn(ResponseEntity.ok(fromAccount));

        assertThrows(InsufficientBalance.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any(FundTransfer.class));
    }

    @Test
    void fundTransfer_balanceEqualToAmount_throwsInsufficientBalance() {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(1000))
                .build();

        Account fromAccount = Account.builder()
                .accountId(1L)
                .accountNumber("ACC0000001")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readByAccountNumber("ACC0000001"))
                .thenReturn(ResponseEntity.ok(fromAccount));

        assertThrows(InsufficientBalance.class, () -> fundTransferService.fundTransfer(request));
        verify(fundTransferRepository, never()).save(any(FundTransfer.class));
    }

    @Test
    void getTransferDetailsFromReferenceId_transferFound_returnsTransferDto() {
        String referenceId = "ref123";
        FundTransfer fundTransfer = FundTransfer.builder()
                .fundTransferId(1L)
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .transactionReference(referenceId)
                .build();

        when(fundTransferRepository.findFundTransferByTransactionReference(referenceId))
                .thenReturn(Optional.of(fundTransfer));

        FundTransferDto result = fundTransferService.getTransferDetailsFromReferenceId(referenceId);

        assertNotNull(result);
        assertEquals("ACC0000001", result.getFromAccount());
        assertEquals("ACC0000002", result.getToAccount());
        verify(fundTransferRepository, times(1)).findFundTransferByTransactionReference(referenceId);
    }

    @Test
    void getTransferDetailsFromReferenceId_transferNotFound_throwsResourceNotFound() {
        String referenceId = "nonexistent";

        when(fundTransferRepository.findFundTransferByTransactionReference(referenceId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, 
                () -> fundTransferService.getTransferDetailsFromReferenceId(referenceId));
    }

    @Test
    void getAllTransfersByAccountId_transfersExist_returnsTransferList() {
        String accountId = "ACC0000001";
        List<FundTransfer> transfers = Arrays.asList(
                FundTransfer.builder()
                        .fundTransferId(1L)
                        .fromAccount(accountId)
                        .toAccount("ACC0000002")
                        .amount(BigDecimal.valueOf(100))
                        .build(),
                FundTransfer.builder()
                        .fundTransferId(2L)
                        .fromAccount(accountId)
                        .toAccount("ACC0000003")
                        .amount(BigDecimal.valueOf(200))
                        .build()
        );

        when(fundTransferRepository.findFundTransferByFromAccount(accountId))
                .thenReturn(transfers);

        List<FundTransferDto> result = fundTransferService.getAllTransfersByAccountId(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fundTransferRepository, times(1)).findFundTransferByFromAccount(accountId);
    }

    @Test
    void getAllTransfersByAccountId_noTransfers_returnsEmptyList() {
        String accountId = "ACC0000001";

        when(fundTransferRepository.findFundTransferByFromAccount(accountId))
                .thenReturn(Arrays.asList());

        List<FundTransferDto> result = fundTransferService.getAllTransfersByAccountId(accountId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fundTransferRepository, times(1)).findFundTransferByFromAccount(accountId);
    }
}
