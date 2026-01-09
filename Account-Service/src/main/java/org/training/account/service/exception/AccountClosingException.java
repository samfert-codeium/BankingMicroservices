package org.training.account.service.exception;

/**
 * Exception thrown when an account cannot be closed.
 * 
 * <p>This exception is thrown when an account closure operation fails due to
 * business rule violations. For example, attempting to close an account that
 * still has a non-zero balance. It results in an HTTP 400 Bad Request response.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.exception.GlobalException
 */
public class AccountClosingException extends GlobalException{

    /**
     * Constructs an AccountClosingException with the specified error message.
     * 
     * @param errorMessage the detailed error message describing why the account cannot be closed
     */
    public AccountClosingException(String errorMessage) {
        super(GlobalErrorCode.BAD_REQUEST, errorMessage);
    }
}
