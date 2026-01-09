package org.training.user.service.exception;

/**
 * Exception thrown when required fields are empty or incomplete.
 * 
 * <p>This exception is thrown when a user profile has empty fields that must
 * be filled before certain operations (like status approval) can proceed.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.exception.GlobalException
 */
public class EmptyFields extends GlobalException{
    
    /**
     * Constructs an EmptyFields exception with the specified message and error code.
     * 
     * @param message the detailed error message
     * @param errorCode the error code for this exception
     */
    public EmptyFields(String message, String errorCode) {
        super(message, errorCode);
    }
}
