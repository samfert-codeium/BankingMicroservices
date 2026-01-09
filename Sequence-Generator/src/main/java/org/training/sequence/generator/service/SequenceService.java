package org.training.sequence.generator.service;

import org.training.sequence.generator.model.entity.Sequence;

/**
 * Service interface for sequence generation operations.
 * 
 * <p>This interface defines the contract for generating unique account numbers.
 * Implementations of this interface are responsible for creating and managing
 * the sequence of account numbers used throughout the banking application.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.sequence.generator.service.implementation.SequenceServiceImpl
 */
public interface SequenceService {

    /**
     * Creates and returns a new unique account number.
     * 
     * <p>This method generates the next sequential account number and persists
     * the updated sequence state. Each call to this method should return a
     * unique, incrementing account number.</p>
     * 
     * @return a {@link Sequence} object containing the newly generated account number
     */
    Sequence create();
}
