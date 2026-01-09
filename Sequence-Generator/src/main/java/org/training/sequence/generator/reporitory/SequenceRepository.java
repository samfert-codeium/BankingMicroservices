package org.training.sequence.generator.reporitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.training.sequence.generator.model.entity.Sequence;

/**
 * Repository interface for managing {@link Sequence} entities.
 * 
 * <p>This repository provides data access operations for the sequence table,
 * which is used to generate unique account numbers. It extends JpaRepository
 * to inherit standard CRUD operations and adds custom query methods.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.training.sequence.generator.model.entity.Sequence
 */
public interface SequenceRepository extends JpaRepository<Sequence, Long> {

    /**
     * Counts the total number of sequence records in the database.
     * 
     * <p>This method uses a custom JPQL query to count all sequence entries.
     * It is useful for determining if the sequence has been initialized.</p>
     * 
     * @return the total count of sequence records
     */
    @Query("SELECT COUNT(s) from Sequence s")
    int countAll();

    /**
     * Retrieves the most recent sequence record based on sequence ID.
     * 
     * <p>This method finds the sequence record with the highest sequence ID,
     * which represents the latest generated account number.</p>
     * 
     * @return the most recent {@link Sequence} entity, or null if none exists
     */
    Sequence findFirstByOrderBySequenceIdDesc();

}
