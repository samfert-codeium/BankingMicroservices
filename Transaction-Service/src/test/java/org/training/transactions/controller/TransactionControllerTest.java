package org.training.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.training.transactions.model.dto.TransactionDto;
import org.training.transactions.model.response.Response;
import org.training.transactions.model.response.TransactionRequest;
import org.training.transactions.service.TransactionService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void addTransactions_validRequest_returnsCreated() throws Exception {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId("ACC0001234")
                .transactionType("DEPOSIT")
                .amount(BigDecimal.valueOf(500))
                .description("Deposit funds")
                .build();

        Response response = Response.builder()
                .responseCode("200")
                .message("Transaction completed successfully")
                .build();

        when(transactionService.addTransaction(any(TransactionDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.message").value("Transaction completed successfully"));
    }

    @Test
    void makeInternalTransaction_validRequest_returnsCreated() throws Exception {
        List<TransactionDto> transactionDtos = Arrays.asList(
                TransactionDto.builder()
                        .accountId("ACC0001234")
                        .amount(BigDecimal.valueOf(500).negate())
                        .build(),
                TransactionDto.builder()
                        .accountId("ACC0005678")
                        .amount(BigDecimal.valueOf(500))
                        .build()
        );

        String transactionReference = "ref123";

        Response response = Response.builder()
                .responseCode("200")
                .message("Transaction completed successfully")
                .build();

        when(transactionService.internalTransaction(anyList(), eq(transactionReference)))
                .thenReturn(response);

        mockMvc.perform(post("/transactions/internal")
                        .param("transactionReference", transactionReference)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDtos)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.message").value("Transaction completed successfully"));
    }

    @Test
    void getTransactions_transactionsExist_returnsOk() throws Exception {
        String accountId = "ACC0001234";
        List<TransactionRequest> transactions = Arrays.asList(
                new TransactionRequest(),
                new TransactionRequest()
        );

        when(transactionService.getTransaction(accountId))
                .thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTransactions_noTransactions_returnsEmptyList() throws Exception {
        String accountId = "ACC0001234";

        when(transactionService.getTransaction(accountId))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/transactions")
                        .param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTransactionByTransactionReference_transactionsExist_returnsOk() throws Exception {
        String referenceId = "ref123";
        List<TransactionRequest> transactions = Arrays.asList(
                new TransactionRequest(),
                new TransactionRequest()
        );

        when(transactionService.getTransactionByTransactionReference(referenceId))
                .thenReturn(transactions);

        mockMvc.perform(get("/transactions/{referenceId}", referenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTransactionByTransactionReference_noTransactions_returnsEmptyList() throws Exception {
        String referenceId = "nonexistent";

        when(transactionService.getTransactionByTransactionReference(referenceId))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/transactions/{referenceId}", referenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
