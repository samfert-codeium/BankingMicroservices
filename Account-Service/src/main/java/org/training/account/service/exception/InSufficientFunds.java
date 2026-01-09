package org.training.account.service.exception;

/**
 * Exception thrown when an account has insufficient funds for an operation.
 * 
 * <p>This exception is thrown when a transaction or operation requires more
 * funds than are available in the account. For example, attempting to activate
 * an account without the minimum required balance of Rs.1000.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.exception.GlobalException
 */
public class InSufficientFunds extends GlobalException{
    
    /**
     * Constructs an InSufficientFunds exception with a default message.
     */
    public InSufficientFunds() {
        super("Insufficient funds", GlobalErrorCode.NOT_FOUND);
    }

    /**
     * Constructs an InSufficientFunds exception with a custom message.
     * 
     * @param message the detailed error message
     */
    public InSufficientFunds(String message) {
        super(message, GlobalErrorCode.NOT_FOUND);
    }
}
