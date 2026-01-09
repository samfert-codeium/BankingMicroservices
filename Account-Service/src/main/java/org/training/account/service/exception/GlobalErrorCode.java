package org.training.account.service.exception;

/**
 * Constants class containing standard HTTP error codes used throughout the application.
 * 
 * <p>This utility class provides centralized error code constants that are used
 * when creating exception responses. Using constants ensures consistency across
 * all error handling in the service.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
public class GlobalErrorCode {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GlobalErrorCode() {}

    /** HTTP 404 Not Found - Resource does not exist. */
    public static final String NOT_FOUND = "404";
    
    /** HTTP 409 Conflict - Resource already exists or state conflict. */
    public static final String CONFLICT = "409";

    /** HTTP 400 Bad Request - Invalid request parameters or state. */
    public static final String BAD_REQUEST = "400";
}
