package org.training.sequence.generator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class representing a sequence record for account number generation.
 * 
 * <p>This entity stores the current state of the account number sequence,
 * allowing the system to generate unique, sequential account numbers.
 * The sequence is persisted to the database to ensure continuity across
 * application restarts.</p>
 * 
 * <p>The entity uses a single record approach where the account number
 * is incremented each time a new account is created.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sequence {

    /**
     * The unique identifier for the sequence record.
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sequenceId;

    /**
     * The current account number in the sequence.
     * This value is incremented each time a new account number is generated.
     */
    private long accountNumber;
}
