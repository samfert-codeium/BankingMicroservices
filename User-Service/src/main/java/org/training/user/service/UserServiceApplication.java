package org.training.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the User Service microservice.
 * 
 * <p>This microservice manages user operations including user registration,
 * profile management, and status updates. It integrates with Keycloak for
 * authentication and authorization, storing user credentials in Keycloak
 * while maintaining application-specific user data in MySQL.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>User registration with Keycloak integration</li>
 *   <li>User profile management (personal information, contact details)</li>
 *   <li>User status management (PENDING, APPROVED, DISABLED, REJECTED)</li>
 *   <li>User lookup by ID, email, or account number</li>
 *   <li>Admin approval workflow for new user registrations</li>
 * </ul>
 * 
 * <p>The service uses a dual storage approach:</p>
 * <ul>
 *   <li>Keycloak: Credentials, email, verification status</li>
 *   <li>MySQL: Application data (userId, contactNo, identificationNumber, status, userProfile)</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.service.UserService
 * @see org.training.user.service.controller.UserController
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class UserServiceApplication {

    /**
     * Main method that starts the User Service application.
     * 
     * <p>Initializes the Spring Boot application context and starts the embedded
     * web server. The service registers itself with Eureka for service discovery.</p>
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
