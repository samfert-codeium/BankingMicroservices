package org.training.account.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.training.account.service.model.dto.external.SequenceDto;

/**
 * Feign client interface for communicating with the Sequence Generator Service.
 * 
 * <p>This interface defines the contract for making HTTP requests to the Sequence Generator
 * microservice. It enables the Account Service to obtain unique, sequential account numbers
 * when creating new bank accounts.</p>
 * 
 * <p>The client is configured to use the "sequence-generator" name for service discovery.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@FeignClient(name = "sequence-generator")
public interface SequenceService {

    /**
     * Generates a new account number.
     *
     * @return the generated account number as a SequenceDto object.
     */
    @PostMapping("/sequence")
    SequenceDto generateAccountNumber();
}
