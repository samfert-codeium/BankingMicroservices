package org.training.user.service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.training.user.service.model.Status;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity class representing a user in the banking system.
 * 
 * <p>This entity stores application-specific user data in MySQL. User credentials
 * and authentication data are stored separately in Keycloak, linked via the authId field.</p>
 * 
 * <p>The dual storage approach separates concerns:</p>
 * <ul>
 *   <li>Keycloak: Credentials, email verification, enabled status</li>
 *   <li>MySQL (this entity): Contact info, identification, status, profile</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.model.entity.UserProfile
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    /** Unique identifier for the user (auto-generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /** User's email address (also stored in Keycloak). */
    private String emailId;

    /** User's contact phone number. */
    private String contactNo;

    /** Keycloak authentication ID linking to the identity provider. */
    private String authId;

    /** Unique identification number (UUID) assigned at registration. */
    private String identificationNumber;

    /** Date when the user account was created. */
    @CreationTimestamp
    private LocalDate creationOn;

    /** Current status of the user (PENDING, APPROVED, DISABLED, REJECTED). */
    @Enumerated(EnumType.STRING)
    private Status status;

    /** User's profile containing additional personal information. */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "userProfileId")
    private UserProfile userProfile;
}
