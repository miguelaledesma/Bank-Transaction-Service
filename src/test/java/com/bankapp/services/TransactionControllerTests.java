package com.bankapp.services;

import com.bankapp.controllers.TransactionController;
import com.bankapp.dto.*;
import com.bankapp.util.DataSeeder;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Integration tests to test the API endpoints 

@WebMvcTest(TransactionController.class)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private DataSeeder dataSeeder;

    @Test
    void testLoadEndpoint() throws Exception {
        LoadRequest loadRequest = new LoadRequest("1", "1",
                new TransactionAmount(new BigDecimal("100"), "USD", "CREDIT"));
        LoadResponse loadResponse = new LoadResponse("1", "1", new BigDecimal("100"));

        when(transactionService.loadFunds(any(LoadRequest.class))).thenReturn(loadResponse);

        mockMvc.perform(put("/api/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"userId\": \"1\", \"messageId\": \"1\", \"transactionAmount\": {\"amount\": \"100\", \"currency\": \"USD\", \"debitOrCredit\": \"CREDIT\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.balance").value(100));

    }

    @Test
    void testAuthorizeTransactionEndpoint() throws Exception {
        AuthorizationRequest authRequest = new AuthorizationRequest("1", "2",
                new TransactionAmount(new BigDecimal("50"), "USD", "DEBIT"));
        AuthorizationResponse authResponse = new AuthorizationResponse("1", "2", "APPROVED", new BigDecimal("50"));

        when(transactionService.authorizeTransaction(any(AuthorizationRequest.class))).thenReturn(authResponse);

        mockMvc.perform(put("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"userId\": \"1\", \"messageId\": \"2\", \"transactionAmount\": {\"amount\": \"50\", \"currency\": \"USD\", \"debitOrCredit\": \"DEBIT\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.responseCode").value("APPROVED"));
    }

    @Test
    void testPingEndpoint() throws Exception {
        mockMvc.perform(get("/api/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Server time")));
    }
}
