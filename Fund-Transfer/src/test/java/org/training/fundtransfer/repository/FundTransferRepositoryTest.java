package org.training.fundtransfer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.training.fundtransfer.model.TransactionStatus;
import org.training.fundtransfer.model.TransferType;
import org.training.fundtransfer.model.entity.FundTransfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class FundTransferRepositoryTest {

    @Autowired
    private FundTransferRepository fundTransferRepository;

    @Test
    void findFundTransferByTransactionReference_existingReference_returnsFundTransfer() {
        String transactionReference = "REF123456";
        FundTransfer fundTransfer = FundTransfer.builder()
                .transactionReference(transactionReference)
                .fromAccount("ACC0001234")
                .toAccount("ACC0005678")
                .amount(BigDecimal.valueOf(500))
                .status(TransactionStatus.SUCCESS)
                .transferType(TransferType.INTERNAL)
                .build();

        fundTransferRepository.save(fundTransfer);

        Optional<FundTransfer> result = fundTransferRepository
                .findFundTransferByTransactionReference(transactionReference);

        assertTrue(result.isPresent());
        assertEquals(transactionReference, result.get().getTransactionReference());
        assertEquals("ACC0001234", result.get().getFromAccount());
        assertEquals("ACC0005678", result.get().getToAccount());
    }

    @Test
    void findFundTransferByTransactionReference_nonExistingReference_returnsEmpty() {
        Optional<FundTransfer> result = fundTransferRepository
                .findFundTransferByTransactionReference("NONEXISTENT");

        assertFalse(result.isPresent());
    }

    @Test
    void findFundTransferByFromAccount_existingAccount_returnsTransfers() {
        String fromAccount = "ACC0001234";
        FundTransfer transfer1 = FundTransfer.builder()
                .transactionReference("REF001")
                .fromAccount(fromAccount)
                .toAccount("ACC0005678")
                .amount(BigDecimal.valueOf(500))
                .status(TransactionStatus.SUCCESS)
                .transferType(TransferType.INTERNAL)
                .build();

        FundTransfer transfer2 = FundTransfer.builder()
                .transactionReference("REF002")
                .fromAccount(fromAccount)
                .toAccount("ACC0009999")
                .amount(BigDecimal.valueOf(300))
                .status(TransactionStatus.SUCCESS)
                .transferType(TransferType.INTERNAL)
                .build();

        fundTransferRepository.save(transfer1);
        fundTransferRepository.save(transfer2);

        List<FundTransfer> results = fundTransferRepository.findFundTransferByFromAccount(fromAccount);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getFromAccount().equals(fromAccount)));
    }

    @Test
    void findFundTransferByFromAccount_nonExistingAccount_returnsEmptyList() {
        List<FundTransfer> results = fundTransferRepository
                .findFundTransferByFromAccount("NONEXISTENT");

        assertTrue(results.isEmpty());
    }
}
