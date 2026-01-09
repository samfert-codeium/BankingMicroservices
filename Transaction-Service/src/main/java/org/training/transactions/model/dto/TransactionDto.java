package org.training.transactions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.transactions.model.TransactionType;

import java.math.BigDecimal;

/**
 * Data Transfer Object for transaction information.
 * 
 * <p>This DTO is used to receive transaction data from API requests
 * for creating new transactions.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

    /** Account number associated with this transaction. */
    private String accountId;

    /** Type of transaction (DEPOSIT, WITHDRAWAL, etc.). */
    private String transactionType;

    /** Transaction amount. */
    private BigDecimal amount;

    /** Description or comments for the transaction. */
    private String description;
}
