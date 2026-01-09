package org.training.fundtransfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for initiating a fund transfer.
 * 
 * <p>This DTO contains the required information to process a fund transfer
 * between two accounts.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferRequest {

    /** Account number from which funds will be transferred. */
    private String fromAccount;

    /** Account number to which funds will be transferred. */
    private String toAccount;

    /** Amount of money to transfer. */
    private BigDecimal amount;
}
