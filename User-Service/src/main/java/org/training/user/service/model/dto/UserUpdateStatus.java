package org.training.user.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.user.service.model.Status;

/**
 * Data Transfer Object for user status update requests.
 * 
 * <p>This DTO is used by administrators to update a user's status, typically
 * for approving or rejecting user registrations. It is used by the
 * PATCH /api/users/{id} endpoint.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.model.Status
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateStatus {

    /** The new status to set for the user. */
    private Status status;
}
