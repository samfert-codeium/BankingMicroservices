package org.training.user.service.model;

/**
 * Enumeration representing the possible statuses of a user account.
 * 
 * <p>Users follow an approval workflow where they start as PENDING after registration
 * and must be approved by an administrator to become APPROVED. Users can also be
 * DISABLED or REJECTED.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public enum Status {

    /** User is awaiting admin approval after registration. */
    PENDING,
    
    /** User has been approved and can access the system. */
    APPROVED,
    
    /** User account has been disabled by an administrator. */
    DISABLED,
    
    /** User registration has been rejected by an administrator. */
    REJECTED
}
