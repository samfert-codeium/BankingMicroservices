package org.training.sequence.generator.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.sequence.generator.model.entity.Sequence;
import org.training.sequence.generator.reporitory.SequenceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SequenceServiceImplTest {

    @Mock
    private SequenceRepository sequenceRepository;

    @InjectMocks
    private SequenceServiceImpl sequenceService;

    @Test
    void create_firstTime_createsNewSequence() {
        when(sequenceRepository.findById(1L))
                .thenReturn(Optional.empty());
        when(sequenceRepository.save(any(Sequence.class)))
                .thenAnswer(invocation -> {
                    Sequence seq = invocation.getArgument(0);
                    seq.setSequenceId(1L);
                    return seq;
                });

        Sequence result = sequenceService.create();

        assertNotNull(result);
        assertEquals(1L, result.getAccountNumber());
        verify(sequenceRepository).findById(1L);
        verify(sequenceRepository).save(any(Sequence.class));
    }

    @Test
    void create_subsequentCalls_incrementsAccountNumber() {
        Sequence existingSequence = Sequence.builder()
                .sequenceId(1L)
                .accountNumber(100L)
                .build();

        when(sequenceRepository.findById(1L))
                .thenReturn(Optional.of(existingSequence));
        when(sequenceRepository.save(any(Sequence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sequence result = sequenceService.create();

        assertNotNull(result);
        assertEquals(101L, result.getAccountNumber());
        verify(sequenceRepository).findById(1L);
        verify(sequenceRepository).save(any(Sequence.class));
    }

    @Test
    void create_multipleSequentialCalls_incrementsCorrectly() {
        Sequence sequence = Sequence.builder()
                .sequenceId(1L)
                .accountNumber(1L)
                .build();

        when(sequenceRepository.findById(1L))
                .thenReturn(Optional.of(sequence));
        when(sequenceRepository.save(any(Sequence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sequence result1 = sequenceService.create();
        assertEquals(2L, result1.getAccountNumber());

        sequence.setAccountNumber(2L);
        Sequence result2 = sequenceService.create();
        assertEquals(3L, result2.getAccountNumber());

        sequence.setAccountNumber(3L);
        Sequence result3 = sequenceService.create();
        assertEquals(4L, result3.getAccountNumber());
    }
}
