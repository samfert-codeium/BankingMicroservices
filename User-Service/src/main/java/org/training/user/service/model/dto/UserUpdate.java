package org.training.user.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user profile update requests.
 * 
 * <p>This DTO contains the fields that can be updated for an existing user's profile.
 * It is used by the PUT /api/users/{id} endpoint.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdate {

    /** User's first name. */
    private String firstName;

    /** User's last name. */
    private String lastName;

    /** User's contact phone number. */
    private String contactNo;

    /** User's residential address. */
    private String address;

    /** User's gender. */
    private String gender;

    /** User's occupation or profession. */
    private String occupation;

    /** User's marital status. */
    private String martialStatus;

    /** User's nationality. */
    private String nationality;
}
