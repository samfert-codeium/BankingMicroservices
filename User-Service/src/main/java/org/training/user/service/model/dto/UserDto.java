package org.training.user.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.user.service.model.Status;

/**
 * Data Transfer Object for user information.
 * 
 * <p>This DTO is used to transfer user data between layers and to external services.
 * It contains the essential user information including profile details.</p>
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

    /** User's email address. */
    private String emailId;

    /** User's password (typically not populated in responses). */
    private String password;

    /** Unique identification number (UUID) assigned at registration. */
    private String identificationNumber;

    /** Keycloak authentication ID. */
    private String authId;

    /** Current status of the user account. */
    private Status status;

    /** User's profile information. */
    private UserProfileDto userProfileDto;
}
