package org.training.transactions.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base exception class for all custom exceptions in the Transaction Service.
 * 
 * <p>This runtime exception serves as the parent class for all application-specific
 * exceptions. It carries an error code and message that can be used to provide
 * meaningful error responses to API clients.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException {

    /** The error code associated with this exception (e.g., "400", "404"). */
    private String errorCode;

    /** A human-readable error message describing the exception. */
    private String message;

    /**
     * Constructs a GlobalException with only a message.
     * 
     * @param message the detailed error message
     */
    public GlobalException(String message) {
        this.message = message;
    }
}
