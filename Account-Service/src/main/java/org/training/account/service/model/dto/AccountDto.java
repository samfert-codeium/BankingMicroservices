package org.training.account.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for account information.
 * 
 * <p>This DTO is used to transfer account data between the controller layer
 * and service layer, as well as for API request/response payloads. It provides
 * a simplified view of the Account entity suitable for external communication.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.model.entity.Account
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    /**
     * Unique identifier for the account.
     */
    private Long accountId;

    /**
     * The unique account number (e.g., "0600140000001").
     */
    private String accountNumber;

    /**
     * The type of account as a string (SAVINGS_ACCOUNT, FIXED_DEPOSIT, LOAN_ACCOUNT).
     */
    private String accountType;

    /**
     * The current status of the account as a string (PENDING, ACTIVE, BLOCKED, CLOSED).
     */
    private String accountStatus;

    /**
     * The current available balance in the account.
     */
    private BigDecimal availableBalance;

    /**
     * The ID of the user who owns this account.
     */
    private Long userId;
}
