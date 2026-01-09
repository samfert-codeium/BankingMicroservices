package org.training.fundtransfer.exception;

/**
 * Exception thrown when an account has insufficient balance for a fund transfer.
 * 
 * <p>This exception is thrown when a transfer request cannot be completed
 * because the source account does not have enough available balance.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class InsufficientBalance extends GlobalException{
    
    /**
     * Constructs an InsufficientBalance exception.
     * 
     * @param message the error message describing the insufficient balance
     * @param errorCode the error code (typically "406")
     */
    public InsufficientBalance(String message, String errorCode) {
        super(errorCode, message);
    }
}
