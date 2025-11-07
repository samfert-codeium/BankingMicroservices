package org.training.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@Component
public class RefreshTokenFilter extends AbstractGatewayFilterFactory<RefreshTokenFilter.Config> {

    private final WebClient webClient;

    public RefreshTokenFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (isTokenExpiringSoon(token)) {
                    log.info("Token expiring soon, refresh recommended");
                }
            }
            
            return chain.filter(exchange);
        };
    }

    private boolean isTokenExpiringSoon(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return false;
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return payload.contains("exp");
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return false;
        }
    }

    public static class Config {
    }
}
