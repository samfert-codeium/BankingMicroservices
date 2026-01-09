package org.training.account.service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response DTO for API operations.
 * 
 * <p>This class represents a standard response structure for API operations
 * that don't return specific data. It provides a response code and message
 * to indicate the result of the operation.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    /**
     * The response code indicating success or failure (e.g., "200", "OK").
     */
    private String responseCode;

    /**
     * A human-readable message describing the result of the operation.
     */
    private String message;
}
