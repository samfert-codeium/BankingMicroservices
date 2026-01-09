package org.training.user.service.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing account information from the Account Service.
 * 
 * <p>This DTO is used to receive account data from the Account Service via Feign client.
 * It contains the essential account information needed by the User Service.</p>
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

    /** The account number (unique identifier for banking operations). */
    private String accountNumber;

    /** The type of account (e.g., SAVINGS, CURRENT). */
    private String accountType;

    /** The current status of the account (e.g., ACTIVE, BLOCKED, CLOSED). */
    private String accountStatus;

    /** The available balance in the account. */
    private BigDecimal availableBalance;

    /** The ID of the user who owns this account. */
    private Long userId;
}
