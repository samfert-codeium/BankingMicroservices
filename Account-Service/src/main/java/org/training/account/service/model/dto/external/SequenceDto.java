package org.training.account.service.model.dto.external;

import lombok.Data;

/**
 * Data Transfer Object representing sequence information from the Sequence Generator Service.
 * 
 * <p>This DTO is used to receive sequence data from the Sequence Generator Service via Feign client.
 * It contains the generated account number that will be used when creating new bank accounts.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Data
public class SequenceDto {

    /** Unique identifier for the sequence record. */
    private long sequenceId;

    /** The generated account number to be used for a new account. */
    private long accountNumber;
}
