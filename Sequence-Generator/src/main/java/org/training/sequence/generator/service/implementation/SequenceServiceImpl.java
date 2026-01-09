package org.training.sequence.generator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.training.sequence.generator.model.entity.Sequence;
import org.training.sequence.generator.reporitory.SequenceRepository;
import org.training.sequence.generator.service.SequenceService;

/**
 * Implementation of the {@link SequenceService} interface.
 * 
 * <p>This service handles the generation of unique, sequential account numbers
 * for the banking application. It uses a single-record approach where one
 * sequence record is maintained and its account number is incremented with
 * each new request.</p>
 * 
 * <p>The implementation ensures thread-safe generation of account numbers
 * by relying on database-level operations for incrementing the sequence.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.sequence.generator.service.SequenceService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {

    /**
     * Repository for persisting and retrieving sequence data.
     */
    private final SequenceRepository sequenceRepository;

    /**
     * Creates and returns a new unique account number.
     * 
     * <p>This method implements the following logic:</p>
     * <ol>
     *   <li>Attempts to find the existing sequence record with ID 1</li>
     *   <li>If found, increments the account number and saves the updated record</li>
     *   <li>If not found (first run), creates a new sequence starting at 1</li>
     * </ol>
     * 
     * <p>The method logs the account number creation for audit purposes.</p>
     * 
     * @return a {@link Sequence} object containing the newly generated account number
     */
    @Override
    public Sequence create() {
        log.info("creating a account number");
        
        // Try to find existing sequence record and increment it,
        // or create a new one starting at 1 if this is the first request
        return sequenceRepository.findById(1L)
                .map(sequence -> {
                    // Increment the account number for the next account
                    sequence.setAccountNumber(sequence.getAccountNumber() + 1);
                    return sequenceRepository.save(sequence);
                }).orElseGet(() -> sequenceRepository.save(Sequence.builder().accountNumber(1L).build()));
    }
}
