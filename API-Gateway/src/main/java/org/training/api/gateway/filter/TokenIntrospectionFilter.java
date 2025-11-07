package org.training.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class TokenIntrospectionFilter extends AbstractGatewayFilterFactory<TokenIntrospectionFilter.Config> {

    private final WebClient webClient;
    
    @Value("${app.config.keycloak.url}")
    private String keycloakUrl;
    
    @Value("${app.config.keycloak.realm}")
    private String realm;
    
    @Value("${spring.security.oauth2.client.registration.banking-service-client.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.banking-service-client.client-secret}")
    private String clientSecret;

    public TokenIntrospectionFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String token = authHeader.substring(7);
            
            return introspectToken(token)
                    .flatMap(active -> {
                        if (!active) {
                            log.warn("Token introspection failed - token is not active");
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    });
        };
    }

    private Mono<Boolean> introspectToken(String token) {
        String introspectionUrl = String.format("%s/realms/%s/protocol/openid-connect/token/introspect", 
                keycloakUrl, realm);
        
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        return webClient.post()
                .uri(introspectionUrl)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("token=" + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Boolean) response.get("active"))
                .onErrorReturn(false);
    }

    public static class Config {
    }
}
