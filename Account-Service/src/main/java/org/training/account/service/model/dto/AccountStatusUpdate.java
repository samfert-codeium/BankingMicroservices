package org.training.account.service.model.dto;

import lombok.Data;
import org.training.account.service.model.AccountStatus;

/**
 * Data Transfer Object for account status update requests.
 * 
 * <p>This DTO is used when updating the status of an existing account.
 * It contains only the new status value to be applied to the account.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.model.AccountStatus
 */
@Data
public class AccountStatusUpdate {
    
    /**
     * The new status to be applied to the account.
     * Valid values: PENDING, ACTIVE, BLOCKED, CLOSED
     */
    AccountStatus accountStatus;
}
