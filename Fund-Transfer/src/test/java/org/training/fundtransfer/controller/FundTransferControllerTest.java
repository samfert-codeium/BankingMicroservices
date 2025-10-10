package org.training.fundtransfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.training.fundtransfer.exception.ResourceNotFound;
import org.training.fundtransfer.model.dto.FundTransferDto;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;
import org.training.fundtransfer.service.FundTransferService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FundTransferController.class)
class FundTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FundTransferService fundTransferService;

    @Test
    void fundTransfer_validRequest_returnsCreated() throws Exception {
        FundTransferRequest request = FundTransferRequest.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .build();

        FundTransferResponse response = FundTransferResponse.builder()
                .transactionId("txn123")
                .message("Fund transfer was successful")
                .build();

        when(fundTransferService.fundTransfer(any(FundTransferRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("txn123"))
                .andExpect(jsonPath("$.message").value("Fund transfer was successful"));
    }

    @Test
    void getTransferDetailsFromReferenceId_transferExists_returnsOk() throws Exception {
        String referenceId = "ref123";
        FundTransferDto transferDto = FundTransferDto.builder()
                .fromAccount("ACC0000001")
                .toAccount("ACC0000002")
                .amount(BigDecimal.valueOf(500))
                .transactionReference(referenceId)
                .build();

        when(fundTransferService.getTransferDetailsFromReferenceId(referenceId))
                .thenReturn(transferDto);

        mockMvc.perform(get("/fund-transfers/{referenceId}", referenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromAccount").value("ACC0000001"))
                .andExpect(jsonPath("$.toAccount").value("ACC0000002"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void getTransferDetailsFromReferenceId_transferNotFound_throwsException() throws Exception {
        String referenceId = "nonexistent";

        when(fundTransferService.getTransferDetailsFromReferenceId(referenceId))
                .thenThrow(new ResourceNotFound("Fund transfer not found", null));

        mockMvc.perform(get("/fund-transfers/{referenceId}", referenceId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTransfersByAccountId_transfersExist_returnsOk() throws Exception {
        String accountId = "ACC0000001";
        List<FundTransferDto> transfers = Arrays.asList(
                FundTransferDto.builder()
                        .fromAccount(accountId)
                        .toAccount("ACC0000002")
                        .amount(BigDecimal.valueOf(100))
                        .build(),
                FundTransferDto.builder()
                        .fromAccount(accountId)
                        .toAccount("ACC0000003")
                        .amount(BigDecimal.valueOf(200))
                        .build()
        );

        when(fundTransferService.getAllTransfersByAccountId(accountId))
                .thenReturn(transfers);

        mockMvc.perform(get("/fund-transfers")
                        .param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fromAccount").value(accountId))
                .andExpect(jsonPath("$[1].fromAccount").value(accountId));
    }

    @Test
    void getAllTransfersByAccountId_noTransfers_returnsEmptyList() throws Exception {
        String accountId = "ACC0000001";

        when(fundTransferService.getAllTransfersByAccountId(accountId))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/fund-transfers")
                        .param("accountId", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
