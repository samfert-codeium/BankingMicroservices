package org.training.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/api/users/register").permitAll()
                .pathMatchers("/actuator/health").permitAll()
                .pathMatchers("/api/users").hasRole("ADMIN")
                .pathMatchers("/api/users/*/status").hasRole("ADMIN")
                .pathMatchers("/accounts/*/status").hasRole("ADMIN")
                .pathMatchers("/accounts/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/fund-transfers/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/transactions/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/transactions/internal").hasRole("SYSTEM")
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .oauth2Login()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(grantedAuthoritiesExtractor());
        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
