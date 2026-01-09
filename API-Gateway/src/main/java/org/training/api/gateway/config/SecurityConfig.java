package org.training.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration class for the API Gateway.
 * 
 * <p>This class configures Spring Security for the reactive web application,
 * implementing OAuth2 authentication and JWT token validation. It defines
 * which endpoints are publicly accessible and which require authentication.</p>
 * 
 * <p>Security features configured:</p>
 * <ul>
 *   <li>OAuth2 login support for user authentication</li>
 *   <li>JWT token validation for API requests</li>
 *   <li>Public access to registration endpoint</li>
 *   <li>CSRF protection disabled for stateless API</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.springframework.security.config.web.server.ServerHttpSecurity
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the API Gateway.
     * 
     * <p>This method sets up the security rules for incoming HTTP requests:</p>
     * <ul>
     *   <li>The user registration endpoint (/api/users/register) is publicly accessible
     *       to allow new users to create accounts without authentication</li>
     *   <li>All other endpoints require authentication via OAuth2/JWT</li>
     *   <li>CSRF protection is disabled since this is a stateless REST API</li>
     *   <li>OAuth2 login is enabled for interactive authentication flows</li>
     *   <li>JWT validation is configured for the OAuth2 resource server</li>
     * </ul>
     * 
     * @param http the {@link ServerHttpSecurity} object used to configure security
     * @return the configured {@link SecurityWebFilterChain} bean
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // Configure authorization rules for different endpoints
        http
                .authorizeExchange()
                // Allow unauthenticated access to the registration endpoint
                // so new users can create accounts
                .pathMatchers("/api/users/register").permitAll()
                // Require authentication for all other API endpoints
                .anyExchange().authenticated()
                .and()
                // Disable CSRF protection for stateless REST API
                // (tokens are used instead of session cookies)
                .csrf().disable()
                // Enable OAuth2 login for interactive authentication
                .oauth2Login()
                .and()
                // Configure JWT validation for API requests
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
