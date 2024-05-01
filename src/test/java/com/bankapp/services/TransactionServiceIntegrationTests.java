package com.bankapp.services;

import com.bankapp.dto.*;
import com.bankapp.model.Account;
import com.bankapp.model.events.EventEntity;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceIntegrationTests {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private EventRepository eventRepository;

    @BeforeEach
    void setup() {
        // Reset mocks before each test, ignoring eslint safety for now.
        reset(accountRepository, eventRepository);
    }

    @Test
    void testLoadFundsIntegration() {
        LoadRequest loadRequest = new LoadRequest("1", "1",
                new TransactionAmount(new BigDecimal("100"), "USD", "CREDIT"));
        Account account = new Account("1", BigDecimal.ZERO);

        when(accountRepository.findById("1")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account savedAccount = invocation.getArgument(0);
            return savedAccount;
        });

        LoadResponse response = transactionService.loadFunds(loadRequest);

        assertEquals(new BigDecimal("100"), response.getBalance(),
                "The balance should be updated to 100 after the load operation");
        verify(accountRepository).save(account);
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void testAuthorizeTransactionIntegration() {
        Account account = new Account("1", new BigDecimal("150"));
        when(accountRepository.findById("1")).thenReturn(java.util.Optional.of(account));

        AuthorizationRequest authRequest = new AuthorizationRequest("1", "1",
                new TransactionAmount(new BigDecimal("50"), "USD", "DEBIT"));
        AuthorizationResponse authResponse = transactionService.authorizeTransaction(authRequest);

        assertEquals("APPROVED", authResponse.getResponseCode());
        assertEquals(new BigDecimal("100"), authResponse.getBalance());
        verify(eventRepository).save(any(EventEntity.class));

        // Test edge case where account has exactly the amount requested
        authRequest = new AuthorizationRequest("1", "2", new TransactionAmount(new BigDecimal("100"), "USD", "DEBIT"));
        authResponse = transactionService.authorizeTransaction(authRequest);

        assertEquals("APPROVED", authResponse.getResponseCode());
        assertEquals(BigDecimal.ZERO, authResponse.getBalance());
        verify(eventRepository, times(2)).save(any(EventEntity.class));
    }

    @Test
    void testLoadFundsInvalidAmount() {
        LoadRequest request = new LoadRequest("1", "10",
                new TransactionAmount(new BigDecimal("-100"), "USD", "CREDIT"));
        Account account = new Account("1", BigDecimal.ZERO);

        when(accountRepository.findById("1")).thenReturn(java.util.Optional.of(account));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.loadFunds(request);
        });

        assertTrue(exception.getMessage().contains("Cannot load negative amounts"));
        verify(accountRepository, never()).save(any(Account.class)); // Ensure no save operation is called
        verify(eventRepository, never()).save(any()); // Ensure no event is recorded
    }

}
