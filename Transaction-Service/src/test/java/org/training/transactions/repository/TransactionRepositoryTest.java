package org.training.transactions.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.training.transactions.model.TransactionStatus;
import org.training.transactions.model.TransactionType;
import org.training.transactions.model.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void findTransactionByAccountId_existingAccount_returnsTransactions() {
        String accountId = "ACC0001234";
        Transaction transaction1 = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .comments("Test deposit")
                .build();

        Transaction transaction2 = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(-200))
                .transactionType(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .comments("Test withdrawal")
                .build();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> results = transactionRepository.findTransactionByAccountId(accountId);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getAccountId().equals(accountId)));
    }

    @Test
    void findTransactionByAccountId_nonExistingAccount_returnsEmptyList() {
        List<Transaction> results = transactionRepository.findTransactionByAccountId("NONEXISTENT");

        assertTrue(results.isEmpty());
    }

    @Test
    void findTransactionByReferenceId_existingReference_returnsTransactions() {
        String referenceId = "REF123456";
        Transaction transaction1 = Transaction.builder()
                .referenceId(referenceId)
                .accountId("ACC0001234")
                .amount(BigDecimal.valueOf(-500))
                .transactionType(TransactionType.INTERNAL_TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .comments("Internal transfer debit")
                .build();

        Transaction transaction2 = Transaction.builder()
                .referenceId(referenceId)
                .accountId("ACC0005678")
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.INTERNAL_TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .comments("Internal transfer credit")
                .build();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> results = transactionRepository.findTransactionByReferenceId(referenceId);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(t -> t.getReferenceId().equals(referenceId)));
    }

    @Test
    void findTransactionByReferenceId_nonExistingReference_returnsEmptyList() {
        List<Transaction> results = transactionRepository.findTransactionByReferenceId("NONEXISTENT");

        assertTrue(results.isEmpty());
    }
}
