package org.training.account.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.training.account.service.exception.ResourceNotFound;
import org.training.account.service.model.AccountStatus;
import org.training.account.service.model.dto.AccountDto;
import org.training.account.service.model.dto.AccountStatusUpdate;
import org.training.account.service.model.dto.external.TransactionResponse;
import org.training.account.service.model.dto.response.Response;
import org.training.account.service.service.AccountService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void createAccount_validRequest_returnsCreated() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .build();

        Response response = Response.builder()
                .responseCode("200")
                .message("Account created successfully")
                .build();

        when(accountService.createAccount(any(AccountDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.message").value("Account created successfully"));
    }

    @Test
    void updateAccountStatus_validRequest_returnsOk() throws Exception {
        String accountNumber = "ACC0001234";
        AccountStatusUpdate statusUpdate = new AccountStatusUpdate();
        statusUpdate.setAccountStatus(AccountStatus.ACTIVE);

        Response response = Response.builder()
                .responseCode("200")
                .message("Account updated successfully")
                .build();

        when(accountService.updateStatus(eq(accountNumber), any(AccountStatusUpdate.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/accounts")
                        .param("accountNumber", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"))
                .andExpect(jsonPath("$.message").value("Account updated successfully"));
    }

    @Test
    void readByAccountNumber_accountExists_returnsOk() throws Exception {
        String accountNumber = "ACC0001234";
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(accountNumber)
                .accountType("SAVINGS")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .userId(1L)
                .build();

        when(accountService.readAccountByAccountNumber(accountNumber))
                .thenReturn(accountDto);

        mockMvc.perform(get("/accounts")
                        .param("accountNumber", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    void readByAccountNumber_accountNotFound_throwsException() throws Exception {
        String accountNumber = "ACC9999999";

        when(accountService.readAccountByAccountNumber(accountNumber))
                .thenThrow(new ResourceNotFound());

        mockMvc.perform(get("/accounts")
                        .param("accountNumber", accountNumber))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_validRequest_returnsOk() throws Exception {
        String accountNumber = "ACC0001234";
        AccountDto accountDto = AccountDto.builder()
                .accountNumber(accountNumber)
                .availableBalance(BigDecimal.valueOf(2000))
                .build();

        Response response = Response.builder()
                .responseCode("200")
                .message("Account updated successfully")
                .build();

        when(accountService.updateAccount(eq(accountNumber), any(AccountDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/accounts")
                        .param("accountNumber", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("200"));
    }

    @Test
    void accountBalance_accountExists_returnsOk() throws Exception {
        String accountNumber = "ACC0001234";
        String balance = "1500.50";

        when(accountService.getBalance(accountNumber))
                .thenReturn(balance);

        mockMvc.perform(get("/accounts/balance")
                        .param("accountNumber", accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(balance));
    }

    @Test
    void getTransactionsFromAccountId_returnsOk() throws Exception {
        String accountId = "ACC0001234";
        List<TransactionResponse> transactions = Arrays.asList(
                new TransactionResponse(),
                new TransactionResponse()
        );

        when(accountService.getTransactionsFromAccountId(accountId))
                .thenReturn(transactions);

        mockMvc.perform(get("/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void closeAccount_validRequest_returnsOk() throws Exception {
        String accountNumber = "ACC0001234";
        Response response = Response.builder()
                .responseCode("200")
                .message("Account closed successfully")
                .build();

        when(accountService.closeAccount(accountNumber))
                .thenReturn(response);

        mockMvc.perform(put("/accounts/closure")
                        .param("accountNumber", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Account closed successfully"));
    }

    @Test
    void readAccountByUserId_accountExists_returnsOk() throws Exception {
        Long userId = 1L;
        AccountDto accountDto = AccountDto.builder()
                .userId(userId)
                .accountNumber("ACC0001234")
                .accountType("SAVINGS")
                .accountStatus("ACTIVE")
                .availableBalance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.readAccountByUserId(userId))
                .thenReturn(accountDto);

        mockMvc.perform(get("/accounts/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.accountNumber").value("ACC0001234"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }
}
