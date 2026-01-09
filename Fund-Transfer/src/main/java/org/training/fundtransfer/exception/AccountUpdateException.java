package org.training.fundtransfer.exception;

/**
 * Exception thrown when an account cannot be updated for a fund transfer.
 * 
 * <p>This exception is thrown when a transfer cannot proceed due to account
 * status issues, such as the account being pending or inactive.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class AccountUpdateException extends GlobalException{
    
    /**
     * Constructs an AccountUpdateException.
     * 
     * @param message the error message describing the account update issue
     * @param errorCode the error code (typically "406")
     */
    public AccountUpdateException(String message, String errorCode) {
        super(errorCode, message);
    }
}
