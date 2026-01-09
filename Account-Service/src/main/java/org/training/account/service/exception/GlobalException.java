package org.training.account.service.exception;

/**
 * Base exception class for all custom exceptions in the Account Service.
 * 
 * <p>This runtime exception serves as the parent class for all application-specific
 * exceptions. It carries an error code and message that can be used to provide
 * meaningful error responses to API clients.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class GlobalException extends RuntimeException{

    /**
     * The error code associated with this exception (e.g., "400", "404", "409").
     */
    private final String errorCode;

    /**
     * A human-readable error message describing the exception.
     */
    private final String errorMessage;

    /**
     * Constructs a new GlobalException with the specified error code and message.
     * 
     * @param errorCode the error code for this exception
     * @param errorMessage the detailed error message
     */
    public GlobalException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Returns the error code associated with this exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the error message associated with this exception.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
