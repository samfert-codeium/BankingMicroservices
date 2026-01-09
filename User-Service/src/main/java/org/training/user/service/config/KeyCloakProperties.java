package org.training.user.service.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Keycloak Admin Client.
 * 
 * <p>This component holds the configuration properties needed to connect to Keycloak
 * and provides a singleton instance of the Keycloak Admin Client. It uses the
 * client_credentials grant type for administrative operations.</p>
 * 
 * <p>Configuration is loaded from application properties:</p>
 * <ul>
 *   <li>app.config.keycloak.server-url - Keycloak server URL</li>
 *   <li>app.config.keycloak.realm - The banking-service realm</li>
 *   <li>app.config.keycloak.client-id - Client ID for admin operations</li>
 *   <li>app.config.keycloak.client-secret - Client secret for authentication</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 */
@Slf4j
@Component
public class KeyCloakProperties {

    /** Keycloak server URL (e.g., http://localhost:8571). */
    @Value("${app.config.keycloak.server-url}")
    private String serverUrl;

    /** The Keycloak realm name (banking-service). */
    @Value("${app.config.keycloak.realm}")
    private String realm;

    /** Client ID for Keycloak admin operations. */
    @Value("${app.config.keycloak.client-id}")
    private String clientId;

    /** Client secret for authentication. */
    @Value("${app.config.keycloak.client-secret}")
    private String clientSecret;

    /** Singleton instance of the Keycloak Admin Client. */
    private static Keycloak keycloakInstance = null;

    /**
     * Returns an instance of Keycloak.
     * If the instance is null, it creates a new instance using the provided configuration.
     *
     * @return The Keycloak instance
     */
    public Keycloak getKeycloakInstance() {

        if (keycloakInstance == null) {
            keycloakInstance = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();
        }

        return keycloakInstance;
    }

    /**
     * Returns the realm.
     *
     * @return the realm
     */
    public String getRealm() {
        return realm;
    }
}
