package org.training.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main entry point for the Service Registry (Eureka Server) microservice.
 * 
 * <p>This class bootstraps the Eureka Server application, which acts as the central
 * service registry for the banking microservices architecture. All microservices
 * register themselves with this server and use it to discover other services.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Maintaining a registry of all available microservice instances</li>
 *   <li>Providing service discovery capabilities to client microservices</li>
 *   <li>Health monitoring of registered services</li>
 *   <li>Load balancing support through service instance information</li>
 * </ul>
 * 
 * <p>The Eureka Server dashboard is typically accessible at the configured port
 * (default: 8761) and provides a web interface to view registered services.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

    /**
     * Main method that starts the Service Registry application.
     * 
     * <p>This method initializes the Spring application context and starts the
     * Eureka Server. Once started, other microservices can register with this
     * server and discover each other's locations.</p>
     * 
     * @param args command-line arguments passed to the application (typically empty)
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }

}
