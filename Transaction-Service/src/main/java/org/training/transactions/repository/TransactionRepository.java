package org.training.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.transactions.model.entity.Transaction;

import java.util.List;

/**
 * Repository interface for {@link Transaction} entity persistence operations.
 * 
 * <p>This repository extends JpaRepository to provide standard CRUD operations
 * and adds custom query methods for finding transactions by account ID and
 * reference ID.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.transactions.model.entity.Transaction
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds transactions by account ID.
     *
     * @param accountId the ID of the account
     * @return a list of transactions
     */
    List<Transaction> findTransactionByAccountId(String accountId);

    /**
     * Returns a list of transactions that match the given reference ID.
     *
     * @param referenceId The reference ID to match against.
     * @return The list of transactions that match the reference ID.
     */
    List<Transaction> findTransactionByReferenceId(String referenceId);
}
