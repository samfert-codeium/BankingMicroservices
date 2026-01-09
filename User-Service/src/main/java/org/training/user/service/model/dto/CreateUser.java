package org.training.user.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration requests.
 * 
 * <p>This DTO contains the information required to create a new user account.
 * It is used by the public registration endpoint (/api/users/register).</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUser {

    /** User's first name. */
    private String firstName;

    /** User's last name. */
    private String lastName;

    /** User's contact phone number. */
    private String contactNumber;

    /** User's email address (used as username in Keycloak). */
    private String emailId;

    /** User's password for authentication. */
    private String password;
}
