package org.training.account.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.training.account.service.external.TransactionService;
import org.training.account.service.model.dto.external.TransactionResponse;
import org.training.account.service.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CachePerformanceTest {

    @Autowired
    private AccountService accountService;
    
    @MockBean
    private TransactionService transactionService;

    @Test
    public void testTransactionCachePerformance() {
        String testAccountId = "TEST_ACCOUNT_001";
        
        List<TransactionResponse> mockTransactions = Arrays.asList(
            TransactionResponse.builder()
                .referenceId("TXN001")
                .accountId(testAccountId)
                .transactionType("DEPOSIT")
                .amount(BigDecimal.valueOf(1000))
                .localDateTime(LocalDateTime.now())
                .transactionStatus("COMPLETED")
                .comments("Test transaction")
                .build()
        );
        
        when(transactionService.getTransactionsFromAccountId(anyString()))
            .thenAnswer(invocation -> {
                Thread.sleep(100);
                return mockTransactions;
            });
        
        System.out.println("=== Cache Performance Test ===");
        
        long startTime1 = System.currentTimeMillis();
        List<TransactionResponse> transactions1 = accountService.getTransactionsFromAccountId(testAccountId);
        long endTime1 = System.currentTimeMillis();
        long firstCallTime = endTime1 - startTime1;
        
        System.out.println("First call (cache miss): " + firstCallTime + "ms");
        System.out.println("Transactions found: " + (transactions1 != null ? transactions1.size() : 0));
        
        long startTime2 = System.currentTimeMillis();
        List<TransactionResponse> transactions2 = accountService.getTransactionsFromAccountId(testAccountId);
        long endTime2 = System.currentTimeMillis();
        long secondCallTime = endTime2 - startTime2;
        
        System.out.println("Second call (cache hit): " + secondCallTime + "ms");
        System.out.println("Transactions found: " + (transactions2 != null ? transactions2.size() : 0));
        
        if (firstCallTime > 0) {
            double improvement = ((double)(firstCallTime - secondCallTime) / firstCallTime) * 100;
            System.out.println("Performance improvement: " + String.format("%.1f", improvement) + "%");
            
            if (improvement > 90) {
                System.out.println("✅ Cache performance target achieved (>90% improvement)");
            } else if (improvement > 50) {
                System.out.println("⚠️ Cache working but improvement below target (<90%)");
            } else {
                System.out.println("❌ Cache may not be working effectively");
            }
        }
        
        long startTime3 = System.currentTimeMillis();
        List<TransactionResponse> transactions3 = accountService.getTransactionsFromAccountId(testAccountId);
        long endTime3 = System.currentTimeMillis();
        long thirdCallTime = endTime3 - startTime3;
        
        System.out.println("Third call (cache hit): " + thirdCallTime + "ms");
        System.out.println("=== Test Complete ===");
    }
}
