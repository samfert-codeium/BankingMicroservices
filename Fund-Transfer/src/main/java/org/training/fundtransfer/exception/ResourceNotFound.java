package org.training.fundtransfer.exception;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * <p>This exception is thrown when an account or fund transfer record
 * cannot be found in the system.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class ResourceNotFound extends GlobalException {
    
    /**
     * Constructs a ResourceNotFound exception.
     * 
     * @param message the error message describing what was not found
     * @param errorCode the error code (typically "404")
     */
    public ResourceNotFound(String message, String errorCode) {
        super(errorCode, message);
    }
}
