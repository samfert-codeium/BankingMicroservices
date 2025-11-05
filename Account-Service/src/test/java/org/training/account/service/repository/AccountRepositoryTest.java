package org.training.account.service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.training.account.service.model.AccountStatus;
import org.training.account.service.model.AccountType;
import org.training.account.service.model.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findAccountByAccountNumber_existingAccount_returnsAccount() {
        String accountNumber = "ACC0001234";
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .userId(1L)
                .build();

        accountRepository.save(account);

        Optional<Account> result = accountRepository.findAccountByAccountNumber(accountNumber);

        assertTrue(result.isPresent());
        assertEquals(accountNumber, result.get().getAccountNumber());
        assertEquals(AccountType.SAVINGS_ACCOUNT, result.get().getAccountType());
    }

    @Test
    void findAccountByAccountNumber_nonExistingAccount_returnsEmpty() {
        Optional<Account> result = accountRepository.findAccountByAccountNumber("NONEXISTENT");

        assertFalse(result.isPresent());
    }

    @Test
    void findAccountByUserIdAndAccountType_existingAccount_returnsAccount() {
        Long userId = 1L;
        AccountType accountType = AccountType.SAVINGS_ACCOUNT;
        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountType(accountType)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .userId(userId)
                .build();

        accountRepository.save(account);

        Optional<Account> result = accountRepository.findAccountByUserIdAndAccountType(userId, accountType);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        assertEquals(accountType, result.get().getAccountType());
    }

    @Test
    void findAccountByUserIdAndAccountType_nonExistingAccount_returnsEmpty() {
        Optional<Account> result = accountRepository
                .findAccountByUserIdAndAccountType(999L, AccountType.SAVINGS_ACCOUNT);

        assertFalse(result.isPresent());
    }

    @Test
    void findAccountByUserId_existingAccount_returnsAccount() {
        Long userId = 1L;
        Account account = Account.builder()
                .accountNumber("ACC0001234")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .accountStatus(AccountStatus.ACTIVE)
                .availableBalance(BigDecimal.valueOf(1000))
                .userId(userId)
                .build();

        accountRepository.save(account);

        Optional<Account> result = accountRepository.findAccountByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void findAccountByUserId_nonExistingAccount_returnsEmpty() {
        Optional<Account> result = accountRepository.findAccountByUserId(999L);

        assertFalse(result.isPresent());
    }
}
