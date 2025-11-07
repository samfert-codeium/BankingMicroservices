package org.training.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MFARequiredFilter extends AbstractGatewayFilterFactory<MFARequiredFilter.Config> {

    private final ReactiveJwtDecoder jwtDecoder;

    public MFARequiredFilter(ReactiveJwtDecoder jwtDecoder) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (isHighValueOperation(exchange)) {
                return validateMFA(exchange)
                        .flatMap(mfaValid -> {
                            if (!mfaValid) {
                                log.warn("MFA required but not provided for high-value operation");
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                return exchange.getResponse().setComplete();
                            }
                            return chain.filter(exchange);
                        });
            }
            return chain.filter(exchange);
        };
    }

    private boolean isHighValueOperation(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        
        return (path.startsWith("/fund-transfers") && "POST".equals(method)) ||
               (path.contains("/closure") && "PUT".equals(method));
    }

    private Mono<Boolean> validateMFA(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just(false);
        }
        
        String token = authHeader.substring(7);
        
        return jwtDecoder.decode(token)
                .map(jwt -> {
                    String acr = jwt.getClaimAsString("acr");
                    return acr != null && acr.contains("mfa");
                })
                .onErrorReturn(false);
    }

    public static class Config {
    }
}
