package org.training.user.service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class representing a user's profile information.
 * 
 * <p>This entity stores additional personal information about a user that is not
 * required for authentication. It has a one-to-one relationship with the User entity.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.model.entity.User
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    /** Unique identifier for the user profile (auto-generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

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
