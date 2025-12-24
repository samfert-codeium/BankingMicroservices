package org.training.api.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ApiGatewayRouteTests {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
        assertNotNull(routeLocator);
    }

    @Test
    void routeConfiguration_hasExpectedRoutes() {
        var routes = routeLocator.getRoutes().collectList().block();
        
        assertNotNull(routes);
        assertTrue(routes.size() >= 5, "Should have at least 5 routes configured");
        
        assertTrue(routes.stream().anyMatch(r -> r.getId().contains("user-service")));
        assertTrue(routes.stream().anyMatch(r -> r.getId().contains("fund-transfer-service")));
        assertTrue(routes.stream().anyMatch(r -> r.getId().contains("account-service")));
        assertTrue(routes.stream().anyMatch(r -> r.getId().contains("sequence-generator")));
        assertTrue(routes.stream().anyMatch(r -> r.getId().contains("transaction-service")));
    }
}
