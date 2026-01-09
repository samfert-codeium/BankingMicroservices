package org.training.account.service.exception;

/**
 * Exception thrown when a resource conflict occurs.
 * 
 * <p>This exception is thrown when an operation would create a duplicate resource
 * or violate a uniqueness constraint. For example, attempting to create an account
 * that already exists for a user. It results in an HTTP 409 Conflict response.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.exception.GlobalException
 */
public class ResourceConflict extends GlobalException{

    /**
     * Constructs a ResourceConflict exception with a default message.
     */
    public ResourceConflict() {
        super("Account already exists", GlobalErrorCode.CONFLICT);
    }

    /**
     * Constructs a ResourceConflict exception with a custom message.
     * 
     * @param message the detailed error message
     */
    public ResourceConflict(String message) {
        super(message, GlobalErrorCode.CONFLICT);
    }
}
