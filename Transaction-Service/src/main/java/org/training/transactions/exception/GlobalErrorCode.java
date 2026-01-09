package org.training.transactions.exception;

/**
 * Constants class containing standard HTTP error codes used throughout the Transaction Service.
 * 
 * <p>This utility class provides centralized error code constants that are used
 * when creating exception responses. Using constants ensures consistency across
 * all error handling in the service.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class GlobalErrorCode {

    /** HTTP 404 Not Found - Resource does not exist. */
    public static final String NOT_FOUND = "404";
    
    /** HTTP 400 Bad Request - Invalid request parameters. */
    public static final String BAD_REQUEST = "400";
}
