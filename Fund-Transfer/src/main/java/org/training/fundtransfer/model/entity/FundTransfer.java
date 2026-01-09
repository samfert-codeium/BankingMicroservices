package org.training.fundtransfer.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.training.fundtransfer.model.TransactionStatus;
import org.training.fundtransfer.model.TransferType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a fund transfer record in the banking system.
 * 
 * <p>This entity stores information about fund transfers between accounts,
 * including the source and destination accounts, transfer amount, status,
 * and type of transfer.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FundTransfer {

    /** Unique identifier for the fund transfer record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundTransferId;

    /** Unique reference ID for tracking the transaction. */
    private String transactionReference;

    /** Account number from which funds are transferred. */
    private String fromAccount;

    /** Account number to which funds are transferred. */
    private String toAccount;

    /** Amount of money being transferred. */
    private BigDecimal amount;

    /** Current status of the transfer (PENDING, PROCESSING, SUCCESS, FAILED). */
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    /** Type of transfer (INTERNAL, EXTERNAL, WITHDRAWAL, CHEQUE). */
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    /** Timestamp when the transfer was initiated. */
    @CreationTimestamp
    private LocalDateTime transferredOn;
}
