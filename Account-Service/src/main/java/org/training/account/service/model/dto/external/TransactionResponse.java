package org.training.account.service.model.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing transaction information from the Transaction Service.
 * 
 * <p>This DTO is used to receive transaction data from the Transaction Service via Feign client.
 * It contains the details of individual transactions associated with an account.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    /** Unique reference ID for the transaction. */
    private String referenceId;

    /** The account ID associated with this transaction. */
    private String accountId;

    /** The type of transaction (e.g., DEPOSIT, WITHDRAWAL, TRANSFER). */
    private String transactionType;

    /** The amount involved in the transaction. */
    private BigDecimal amount;

    /** The date and time when the transaction occurred. */
    private LocalDateTime localDateTime;

    /** The status of the transaction (e.g., SUCCESS, FAILED, PENDING). */
    private String transactionStatus;

    /** Additional comments or notes about the transaction. */
    private String comments;
}
