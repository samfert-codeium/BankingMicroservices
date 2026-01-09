package org.training.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the Transaction Service microservice.
 * 
 * <p>This microservice handles transaction recording and history for bank accounts.
 * It processes deposits, withdrawals, and internal transfers, maintaining a complete
 * audit trail of all financial operations.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Recording individual transactions (deposits, withdrawals)</li>
 *   <li>Processing internal transfer transactions</li>
 *   <li>Providing transaction history by account</li>
 *   <li>Tracking transactions by reference ID</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.transactions.service.TransactionService
 * @see org.training.transactions.controller.TransactionController
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class TransactionServiceApplication {

    /**
     * Main method that starts the Transaction Service application.
     * 
     * <p>Initializes the Spring Boot application context and starts the embedded
     * web server. The service registers itself with Eureka for service discovery.</p>
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

}
