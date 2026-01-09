package org.training.account.service.model.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing user information from the User Service.
 * 
 * <p>This DTO is used to receive user data from the User Service via Feign client.
 * It contains the essential user information needed by the Account Service to
 * validate user existence before creating accounts.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    /** Unique identifier for the user. */
    private Long userId;

    /** User's first name. */
    private String firstName;

    /** User's last name. */
    private String lastName;

    /** User's email address. */
    private String emailId;

    /** User's password (typically not populated in responses). */
    private String password;

    /** Unique identification number assigned to the user (UUID format). */
    private String identificationNumber;

    /** Keycloak authentication ID linking the user to the identity provider. */
    private String authId;
}

