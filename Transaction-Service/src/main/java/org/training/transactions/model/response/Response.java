package org.training.transactions.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response DTO for API operations.
 * 
 * <p>This DTO provides a standard response format with a response code
 * and message for various API operations.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    /** Response code indicating the result (e.g., "200", "400"). */
    private String responseCode;

    /** Human-readable message describing the result. */
    private String message;
}
