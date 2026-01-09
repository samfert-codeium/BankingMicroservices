package org.training.transactions.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training.transactions.configuration.FeignClientConfiguration;
import org.training.transactions.model.external.Account;
import org.training.transactions.model.response.Response;

/**
 * Feign client interface for communicating with the Account Service.
 * 
 * <p>This client provides methods to retrieve and update account information
 * as part of transaction operations. It uses Eureka service discovery
 * to locate the Account Service.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@FeignClient(name = "account-service", configuration = FeignClientConfiguration.class)
public interface AccountService {

    /**
     * Retrieves an account by account number.
     *
     * @param accountNumber The account number.
     * @return The account matching the account number.
     */
    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);

    /**
     * Update an account with the given account number.
     *
     * @param accountNumber The account number of the account to be updated.
     * @param account The updated account information.
     * @return The response entity containing the response.
     */
    @PutMapping("/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}
