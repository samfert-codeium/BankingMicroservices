package org.training.fundtransfer.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after a fund transfer operation.
 * 
 * <p>Contains the transaction ID for tracking and a message indicating
 * the result of the transfer operation.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferResponse {

    /** Unique transaction ID for tracking the transfer. */
    private String transactionId;

    /** Message describing the result of the transfer. */
    private String message;
}
