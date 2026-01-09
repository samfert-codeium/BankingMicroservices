package org.training.user.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.training.user.service.model.external.Account;

/**
 * Feign client interface for communicating with the Account Service.
 * 
 * <p>This interface defines the contract for making HTTP requests to the Account Service
 * microservice. It enables the User Service to retrieve account information for users.</p>
 * 
 * <p>The client is configured to use the "account-service" name for service discovery.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@FeignClient(name = "account-service", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface AccountService {

    /**
     * Retrieves an account by its account number.
     *
     * @param  accountNumber  the account number to search for
     * @return                the ResponseEntity containing the account
     */
    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);
}
