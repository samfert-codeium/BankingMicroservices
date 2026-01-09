package org.training.account.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the Account Service microservice.
 * 
 * <p>This microservice manages bank account operations including account creation,
 * status updates, balance inquiries, and account closure. It communicates with
 * other microservices (User Service, Transaction Service, Sequence Generator)
 * via Feign clients for inter-service communication.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Creating and managing bank accounts</li>
 *   <li>Updating account status (PENDING, ACTIVE, BLOCKED, CLOSED)</li>
 *   <li>Retrieving account balances and transaction histories</li>
 *   <li>Processing account closure requests</li>
 *   <li>Validating user existence before account creation</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.service.AccountService
 * @see org.training.account.service.controller.AccountController
 */
@SpringBootApplication
@EnableFeignClients
public class AccountServiceApplication {

    /**
     * Main method that starts the Account Service application.
     * 
     * <p>This method initializes the Spring application context, enables Feign
     * clients for inter-service communication, and starts the embedded web server.</p>
     * 
     * @param args command-line arguments passed to the application (typically empty)
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
