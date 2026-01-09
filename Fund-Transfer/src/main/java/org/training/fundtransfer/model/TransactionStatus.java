package org.training.fundtransfer.model;

/**
 * Enumeration representing the possible statuses of a fund transfer transaction.
 * 
 * @author Training Team
 * @version 1.0
 */
public enum TransactionStatus {

    /** Transfer is waiting to be processed. */
    PENDING,
    
    /** Transfer is currently being processed. */
    PROCESSING,
    
    /** Transfer completed successfully. */
    SUCCESS,
    
    /** Transfer failed due to an error. */
    FAILED
}
