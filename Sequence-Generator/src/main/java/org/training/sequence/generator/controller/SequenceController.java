package org.training.sequence.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.sequence.generator.model.entity.Sequence;
import org.training.sequence.generator.service.SequenceService;

/**
 * REST controller for sequence generation operations.
 * 
 * <p>This controller exposes endpoints for generating unique account numbers.
 * It serves as the entry point for other microservices (primarily the Account Service)
 * to request new account numbers when creating new bank accounts.</p>
 * 
 * <p>Base URL: /sequence</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.sequence.generator.service.SequenceService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    /**
     * Service for handling sequence generation business logic.
     */
    private final SequenceService sequenceService;

    /**
     * Generates a new unique account number.
     * 
     * <p>This endpoint creates and returns a new sequential account number.
     * Each call to this endpoint increments the sequence counter and returns
     * the new value, ensuring uniqueness across all generated account numbers.</p>
     * 
     * <p>HTTP Method: POST</p>
     * <p>URL: /sequence</p>
     * 
     * @return a {@link Sequence} object containing the newly generated account number
     */
    @PostMapping
    public Sequence generateAccountNumber() {
        return sequenceService.create();
    }
}
