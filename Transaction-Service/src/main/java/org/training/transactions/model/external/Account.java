package org.training.transactions.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing account information from the Account Service.
 * 
 * <p>This DTO is used to receive account data when communicating with the
 * Account Service via Feign client for transaction operations.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    /** Unique identifier for the account. */
    private Long accountId;

    /** Account number used for transactions. */
    private String accountNumber;

    /** Type of account (e.g., SAVINGS, CHECKING). */
    private String accountType;

    /** Current status of the account (e.g., ACTIVE, INACTIVE). */
    private String accountStatus;

    /** Available balance for transactions. */
    private BigDecimal availableBalance;

    /** ID of the user who owns this account. */
    private Long userId;
}
