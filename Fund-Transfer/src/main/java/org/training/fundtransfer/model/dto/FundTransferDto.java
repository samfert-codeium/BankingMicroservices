package org.training.fundtransfer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.fundtransfer.model.TransactionStatus;
import org.training.fundtransfer.model.TransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for fund transfer information.
 * 
 * <p>This DTO is used to transfer fund transfer data between layers
 * and in API responses.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferDto {

    /** Unique reference ID for tracking the transaction. */
    private String transactionReference;

    /** Account number from which funds are transferred. */
    private String fromAccount;

    /** Account number to which funds are transferred. */
    private String toAccount;

    /** Amount of money being transferred. */
    private BigDecimal amount;

    /** Current status of the transfer. */
    private TransactionStatus status;

    /** Type of transfer. */
    private TransferType transferType;

    /** Timestamp when the transfer was initiated. */
    private LocalDateTime transferredOn;
}
