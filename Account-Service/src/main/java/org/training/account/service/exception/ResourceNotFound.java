package org.training.account.service.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * 
 * <p>This exception is thrown when an operation attempts to access a resource
 * (such as an account or user) that does not exist in the system. It results
 * in an HTTP 404 Not Found response.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.exception.GlobalException
 */
public class ResourceNotFound extends GlobalException{

    /**
     * Constructs a ResourceNotFound exception with a default message.
     */
    public ResourceNotFound() {
        super("Resource not found on the server", GlobalErrorCode.NOT_FOUND);
    }

    /**
     * Constructs a ResourceNotFound exception with a custom message.
     * 
     * @param message the detailed error message
     */
    public ResourceNotFound(String message) {
        super(message, GlobalErrorCode.NOT_FOUND);
    }
}
