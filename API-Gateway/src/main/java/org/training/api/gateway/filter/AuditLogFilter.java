package org.training.api.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String ipAddress = getClientIpAddress(exchange);
        LocalDateTime requestTime = LocalDateTime.now();

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    HttpStatus statusCode = exchange.getResponse().getStatusCode();
                    logRequest(path, method, ipAddress, statusCode, requestTime);
                })
                .doOnError(error -> {
                    log.error("Request failed: path={}, method={}, ip={}, error={}", 
                            path, method, ipAddress, error.getMessage());
                });
    }

    private void logRequest(String path, String method, String ipAddress, 
                           HttpStatus statusCode, LocalDateTime requestTime) {
        if (isAuthenticationEndpoint(path)) {
            log.info("AUTH_REQUEST: path={}, method={}, ip={}, status={}, time={}", 
                    path, method, ipAddress, statusCode, requestTime);
        }

        if (isHighValueOperation(path, method)) {
            log.info("HIGH_VALUE_OPERATION: path={}, method={}, ip={}, status={}, time={}", 
                    path, method, ipAddress, statusCode, requestTime);
        }
    }

    private boolean isAuthenticationEndpoint(String path) {
        return path.contains("/login") || path.contains("/register") || 
               path.contains("/token") || path.contains("/auth");
    }

    private boolean isHighValueOperation(String path, String method) {
        return (path.startsWith("/fund-transfers") && "POST".equals(method)) ||
               (path.contains("/closure") && "PUT".equals(method));
    }

    private String getClientIpAddress(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return exchange.getRequest().getRemoteAddress() != null ? 
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
