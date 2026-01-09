package org.training.transactions.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.training.transactions.model.TransactionStatus;
import org.training.transactions.model.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a transaction record in the banking system.
 * 
 * <p>This entity stores information about financial transactions including
 * deposits, withdrawals, and internal transfers. Each transaction is linked
 * to an account and has a unique reference ID for tracking.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    /** Unique identifier for the transaction record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    /** Unique reference ID for tracking the transaction. */
    private String referenceId;

    /** Account number associated with this transaction. */
    private String accountId;

    /** Type of transaction (DEPOSIT, WITHDRAWAL, INTERNAL_TRANSFER, etc.). */
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    /** Transaction amount (negative for debits, positive for credits). */
    private BigDecimal amount;

    /** Timestamp when the transaction was created. */
    @CreationTimestamp
    private LocalDateTime transactionDate;

    /** Current status of the transaction (COMPLETED, PENDING). */
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    /** Additional comments or description for the transaction. */
    private String comments;
}
