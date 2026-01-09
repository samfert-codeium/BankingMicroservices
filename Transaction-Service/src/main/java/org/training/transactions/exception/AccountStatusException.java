package org.training.transactions.exception;

/**
 * Exception thrown when an account status prevents a transaction.
 * 
 * <p>This exception is thrown when a transaction cannot be processed
 * because the account is inactive or closed.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class AccountStatusException extends GlobalException {

    /**
     * Constructs an AccountStatusException.
     * 
     * @param message the error message describing the account status issue
     */
    public AccountStatusException(String message) {
        super(message, GlobalErrorCode.BAD_REQUEST);
    }
}
