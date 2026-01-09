package org.training.transactions.model;

/**
 * Enumeration representing the types of transactions supported by the system.
 * 
 * @author Training Team
 * @version 1.0
 */
public enum TransactionType {

    /** Money deposited into an account. */
    DEPOSIT,
    
    /** Money withdrawn from an account. */
    WITHDRAWAL,
    
    /** Transfer between accounts within the same bank. */
    INTERNAL_TRANSFER,
    
    /** Transfer to accounts in other banks. */
    EXTERNAL_TRANSFER
}
