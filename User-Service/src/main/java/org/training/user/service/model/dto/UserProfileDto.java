package org.training.user.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user profile information.
 * 
 * <p>This DTO contains the user's personal profile information that is stored
 * separately from authentication data. It is used in API responses and
 * nested within UserDto.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {

    /** User's first name. */
    private String firstName;

    /** User's last name. */
    private String lastName;

    /** User's gender. */
    private String gender;

    /** User's residential address. */
    private String address;

    /** User's occupation or profession. */
    private String occupation;

    /** User's marital status. */
    private String martialStatus;

    /** User's nationality. */
    private String nationality;
}
