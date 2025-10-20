package org.training.sequence.generator.reporitory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.training.sequence.generator.model.entity.Sequence;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class SequenceRepositoryTest {

    @Autowired
    private SequenceRepository sequenceRepository;

    @Test
    void save_newSequence_persists() {
        Sequence sequence = Sequence.builder()
                .accountNumber(1L)
                .build();

        Sequence saved = sequenceRepository.save(sequence);

        assertNotNull(saved);
        assertNotNull(saved.getSequenceId());
        assertEquals(1L, saved.getAccountNumber());
    }

    @Test
    void findById_existingSequence_returnsSequence() {
        Sequence sequence = Sequence.builder()
                .accountNumber(100L)
                .build();

        Sequence saved = sequenceRepository.save(sequence);

        Optional<Sequence> result = sequenceRepository.findById(saved.getSequenceId());

        assertTrue(result.isPresent());
        assertEquals(100L, result.get().getAccountNumber());
    }

    @Test
    void findById_nonExistingSequence_returnsEmpty() {
        Optional<Sequence> result = sequenceRepository.findById(999L);

        assertFalse(result.isPresent());
    }
}
