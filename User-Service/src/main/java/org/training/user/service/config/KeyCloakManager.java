package org.training.user.service.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Component;

/**
 * Manager class for obtaining Keycloak client instances.
 * 
 * <p>This component provides access to the Keycloak Admin Client configured
 * for the banking-service realm. It abstracts the Keycloak connection details
 * and provides a simple interface for services to interact with Keycloak.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.config.KeyCloakProperties
 */
@Component
@RequiredArgsConstructor
public class KeyCloakManager {

    /** Configuration properties for Keycloak connection. */
    private final KeyCloakProperties keyCloakProperties;

    /**
     * Returns the KeyCloak instance for the specified realm.
     *
     * @return  the KeyCloak instance for the specified realm
     */
    public RealmResource getKeyCloakInstanceWithRealm() {

        return keyCloakProperties.getKeycloakInstance().realm(keyCloakProperties.getRealm());
    }
}
