package org.training.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main entry point for the API Gateway microservice.
 * 
 * <p>This class bootstraps the Spring Boot application and registers it as a Eureka client
 * for service discovery. The API Gateway serves as the single entry point for all client
 * requests, routing them to the appropriate microservices.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Centralized routing of API requests to backend microservices</li>
 *   <li>Authentication and authorization via OAuth2/JWT</li>
 *   <li>Service discovery integration with Eureka</li>
 *   <li>Load balancing across service instances</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.api.gateway.config.SecurityConfig
 */
@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    /**
     * Main method that starts the API Gateway application.
     * 
     * <p>This method initializes the Spring application context and starts the embedded
     * web server. The gateway will automatically register with the Eureka service registry
     * upon startup.</p>
     * 
     * @param args command-line arguments passed to the application (typically empty)
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
