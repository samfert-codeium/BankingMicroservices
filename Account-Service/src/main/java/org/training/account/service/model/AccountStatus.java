package org.training.account.service.model;

/**
 * Enumeration representing the possible statuses of a bank account.
 * 
 * <p>An account transitions through these statuses during its lifecycle:</p>
 * <ul>
 *   <li>PENDING - Initial status when account is created, awaiting activation</li>
 *   <li>ACTIVE - Account is fully operational and can perform transactions</li>
 *   <li>BLOCKED - Account is temporarily suspended, no transactions allowed</li>
 *   <li>CLOSED - Account is permanently closed</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 */
public enum AccountStatus {
    
    /** Account is pending activation, requires minimum balance deposit. */
    PENDING,
    
    /** Account is active and can perform all transactions. */
    ACTIVE,
    
    /** Account is blocked and cannot perform transactions. */
    BLOCKED,
    
    /** Account is permanently closed. */
    CLOSED
}
