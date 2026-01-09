package org.training.fundtransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the Fund Transfer microservice.
 * 
 * <p>This microservice handles fund transfer operations between bank accounts.
 * It coordinates with the Account Service for balance updates and the Transaction
 * Service for recording transaction history.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Processing internal fund transfers between accounts</li>
 *   <li>Validating account status and balance before transfers</li>
 *   <li>Recording fund transfer history</li>
 *   <li>Generating unique transaction references</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.fundtransfer.service.FundTransferService
 * @see org.training.fundtransfer.controller.FundTransferController
 */
@SpringBootApplication
@EnableFeignClients
public class FundTransferApplication {

    /**
     * Main method that starts the Fund Transfer application.
     * 
     * <p>Initializes the Spring Boot application context and starts the embedded
     * web server. The service registers itself with Eureka for service discovery.</p>
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FundTransferApplication.class, args);
    }

}
