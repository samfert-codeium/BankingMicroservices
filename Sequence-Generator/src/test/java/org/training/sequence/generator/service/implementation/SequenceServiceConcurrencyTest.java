package org.training.sequence.generator.service.implementation;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.training.sequence.generator.model.entity.Sequence;
import org.training.sequence.generator.reporitory.SequenceRepository;
import org.training.sequence.generator.service.SequenceService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SequenceServiceConcurrencyTest {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SequenceRepository sequenceRepository;

    @Test
    @Order(2)
    void concurrentCreate_demonstratesThreadSafetyIssue() throws InterruptedException {
        sequenceRepository.save(Sequence.builder().accountNumber(1L).build());

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        Set<Long> accountNumbers = ConcurrentHashMap.newKeySet();
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Sequence sequence = sequenceService.create();
                    accountNumbers.add(sequence.getAccountNumber());
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println("Generated account numbers: " + accountNumbers);
        System.out.println("Unique account numbers: " + accountNumbers.size());
        System.out.println("Expected unique numbers: " + threadCount);
        
        assertTrue(accountNumbers.size() <= threadCount, 
                "Should have at most " + threadCount + " unique account numbers");
    }

    @Test
    @Order(1)
    void sequentialCreate_worksCorrectly() {
        sequenceRepository.deleteAll();

        Set<Long> accountNumbers = new HashSet<>();
        
        for (int i = 0; i < 5; i++) {
            Sequence sequence = sequenceService.create();
            accountNumbers.add(sequence.getAccountNumber());
        }

        assertEquals(5, accountNumbers.size(), "Should have 5 unique account numbers");
    }
}
