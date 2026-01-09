package org.training.fundtransfer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for transaction information sent to the Transaction Service.
 * 
 * <p>This DTO is used when creating transaction records in the Transaction Service
 * as part of a fund transfer operation.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    /** Account number associated with this transaction. */
    private String accountId;

    /** Type of transaction (e.g., INTERNAL_TRANSFER). */
    private String transactionType;

    /** Transaction amount (negative for debits, positive for credits). */
    private BigDecimal amount;

    /** Description of the transaction. */
    private String description;
}
