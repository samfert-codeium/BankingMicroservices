package org.training.account.service.model;

/**
 * Enumeration representing the types of bank accounts available.
 * 
 * <p>Each account type has different characteristics and rules:</p>
 * <ul>
 *   <li>SAVINGS_ACCOUNT - Standard savings account for regular banking</li>
 *   <li>FIXED_DEPOSIT - Fixed deposit account with locked funds for a term</li>
 *   <li>LOAN_ACCOUNT - Account for managing loan disbursements and repayments</li>
 * </ul>
 * 
 * @author Training Team
 * @version 1.0
 */
public enum AccountType {
    
    /** Standard savings account for regular banking operations. */
    SAVINGS_ACCOUNT,
    
    /** Fixed deposit account with funds locked for a specific term. */
    FIXED_DEPOSIT,
    
    /** Loan account for managing loan transactions. */
    LOAN_ACCOUNT
}
