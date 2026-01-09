package org.training.transactions.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction history queries.
 * 
 * <p>This DTO is used to return transaction details in API responses
 * when querying transaction history.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    /** Unique reference ID for tracking the transaction. */
    private String referenceId;

    /** Account number associated with this transaction. */
    private String accountId;

    /** Type of transaction (DEPOSIT, WITHDRAWAL, etc.). */
    private String transactionType;

    /** Transaction amount. */
    private BigDecimal amount;

    /** Timestamp when the transaction occurred. */
    private LocalDateTime localDateTime;

    /** Current status of the transaction. */
    private String transactionStatus;

    /** Additional comments or description. */
    private String comments;
}
