package org.training.account.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.account.service.model.AccountType;
import org.training.account.service.model.entity.Account;

import java.util.Optional;

/**
 * Repository interface for managing {@link Account} entities.
 * 
 * <p>This repository provides data access operations for bank accounts,
 * extending JpaRepository for standard CRUD operations and adding custom
 * query methods for account-specific lookups.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.training.account.service.model.entity.Account
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by user ID and account type.
     *
     * @param userId The ID of the user.
     * @param accountType The type of the account.
     * @return An optional containing the account if found, or empty if not found.
     */
    Optional<Account> findAccountByUserIdAndAccountType(Long userId, AccountType accountType);

    /**
     * Find an account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return An optional containing the found account, or an empty optional if no account was found.
     */
    Optional<Account> findAccountByAccountNumber(String accountNumber);

    /**
     * Finds an account by user ID.
     *
     * @param userId the ID of the user
     * @return an optional account object
     */
    Optional<Account> findAccountByUserId(Long userId);
}
