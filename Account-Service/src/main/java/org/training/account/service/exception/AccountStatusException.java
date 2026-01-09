package org.training.account.service.exception;

/**
 * Exception thrown when an account status prevents an operation.
 * 
 * <p>This exception is thrown when an operation cannot be performed due to
 * the current status of an account. For example, attempting to perform
 * transactions on an inactive or closed account. It results in an HTTP 400
 * Bad Request response.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.exception.GlobalException
 */
public class AccountStatusException extends GlobalException {
    
    /**
     * Constructs an AccountStatusException with the specified error message.
     * 
     * @param errorMessage the detailed error message describing the status issue
     */
    public AccountStatusException(String errorMessage) {
        super(errorMessage, GlobalErrorCode.BAD_REQUEST);
    }
}
