package org.training.fundtransfer.model;

/**
 * Enumeration representing the types of fund transfers supported by the system.
 * 
 * @author Training Team
 * @version 1.0
 */
public enum TransferType {

    /** Cash withdrawal from an account. */
    WITHDRAWAL,
    
    /** Transfer between accounts within the same bank. */
    INTERNAL,
    
    /** Transfer to accounts in other banks. */
    EXTERNAL,
    
    /** Transfer via cheque deposit. */
    CHEQUE
}
