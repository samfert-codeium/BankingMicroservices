package org.training.sequence.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Sequence Generator microservice.
 * 
 * <p>This microservice is responsible for generating unique sequential account numbers
 * for the banking application. It provides a centralized service that ensures account
 * numbers are unique and sequential across all account creation requests.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Generating unique, sequential account numbers</li>
 *   <li>Persisting sequence state to ensure uniqueness across restarts</li>
 *   <li>Providing REST API for account number generation</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.sequence.generator.service.SequenceService
 */
@SpringBootApplication
public class SequenceGeneratorApplication {

    /**
     * Main method that starts the Sequence Generator application.
     * 
     * <p>This method initializes the Spring application context and starts the
     * embedded web server to handle sequence generation requests.</p>
     * 
     * @param args command-line arguments passed to the application (typically empty)
     */
    public static void main(String[] args) {
        SpringApplication.run(SequenceGeneratorApplication.class, args);
    }

}
