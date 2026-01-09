package org.training.transactions.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for error responses.
 * 
 * <p>This class represents the structure of error responses returned by the API
 * when an exception occurs. It provides a consistent format for error information
 * that clients can parse and handle appropriately.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    /** The error code (e.g., "400", "404"). */
    private String errorCode;

    /** A human-readable error message describing what went wrong. */
    private String message;
}
