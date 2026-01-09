package org.training.user.service.exception;

/**
 * Exception thrown when a resource conflict occurs.
 * 
 * <p>This exception is thrown when an operation would create a duplicate resource
 * or violate a uniqueness constraint. For example, attempting to register a user
 * with an email that is already registered. It results in an HTTP 409 Conflict response.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.exception.GlobalException
 */
public class ResourceConflictException extends GlobalException{

    /**
     * Constructs a ResourceConflictException with a default message.
     */
    public ResourceConflictException() {
        super("Resource already present on the server!!!");
    }

    /**
     * Constructs a ResourceConflictException with a custom message.
     *
     * @param message the detailed error message
     */
    public ResourceConflictException(String message) {
        super(message);
    }
}
