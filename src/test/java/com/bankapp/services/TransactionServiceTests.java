package com.bankapp.services;

import com.bankapp.dto.*;
import com.bankapp.model.Account;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(accountRepository, eventRepository);
    }

    @Test
    void testLoadCreditApproved() {
        LoadRequest loadRequest = new LoadRequest("1", "1",
                new TransactionAmount(new BigDecimal("100.00"), "USD", "CREDIT"));
        Account account = new Account("1", BigDecimal.ZERO);

        when(accountRepository.findById("1")).thenReturn(java.util.Optional.of(account));

        LoadResponse response = transactionService.loadFunds(loadRequest);

        assertEquals(new BigDecimal("100.00"), response.getBalance());
        verify(accountRepository).save(account);

        verify(eventRepository).save(argThat(event -> event.getAccountId().equals("1") &&
                event.getType().equals("FundsDeposited")));

    }

    @Test
    void testAuthorizationDebitApprovedAndDenied() {
        Account account = new Account("1", new BigDecimal("103.23"));
        when(accountRepository.findById("1")).thenReturn(java.util.Optional.of(account));

        AuthorizationRequest authRequestApproved = new AuthorizationRequest("1", "3",
                new TransactionAmount(new BigDecimal("100.00"), "USD", "DEBIT"));
        AuthorizationResponse authResponse = transactionService.authorizeTransaction(authRequestApproved);

        assertEquals("APPROVED", authResponse.getResponseCode());
        assertEquals(new BigDecimal("3.23"), authResponse.getBalance());
        verify(eventRepository).save(argThat(event -> event.getAccountId().equals("1") &&
                event.getType().equals("FundsWithdrawn")

        ));

        AuthorizationRequest authRequestDenied = new AuthorizationRequest("1", "4",
                new TransactionAmount(new BigDecimal("10"), "USD", "DEBIT"));
        AuthorizationResponse authResponseDenied = transactionService.authorizeTransaction(authRequestDenied);

        assertEquals("DECLINED", authResponseDenied.getResponseCode());
        assertEquals(new BigDecimal("3.23"), authResponseDenied.getBalance());
        verify(eventRepository).save(argThat(event -> event.getType().equals("TransactionDeclined")));
    }

    @Test
    void testAuthorizationOnEmptyAccount() {
        Account account = new Account("2", new BigDecimal("0.00"));
        when(accountRepository.findById("2")).thenReturn(java.util.Optional.of(account));

        AuthorizationRequest authRequest = new AuthorizationRequest("2", "5",
                new TransactionAmount(new BigDecimal("50.01"), "USD", "DEBIT"));
        AuthorizationResponse authResponse = transactionService.authorizeTransaction(authRequest);

        assertEquals("DECLINED", authResponse.getResponseCode());
        assertEquals(new BigDecimal("0.00"), authResponse.getBalance());
    }

    @Test
    void testEventPayloadOnLoadFunds() {
        String userId = "1";
        BigDecimal loadAmount = new BigDecimal("150.00");
        LoadRequest loadRequest = new LoadRequest(userId, "1", new TransactionAmount(loadAmount, "USD", "CREDIT"));
        Account account = new Account(userId, BigDecimal.ZERO);

        when(accountRepository.findById(userId)).thenReturn(java.util.Optional.of(account));

        transactionService.loadFunds(loadRequest);

        verify(eventRepository).save(argThat(event -> event.getAccountId().equals(userId) &&
                event.getType().equals("FundsDeposited") &&
                event.getPayload().contains(loadAmount.toString())));
    }

    @Test
    void testEventPayloadOnAuthorization() {
        String userId = "2";
        BigDecimal withdrawAmount = new BigDecimal("50.00");
        Account account = new Account(userId, new BigDecimal("100.00"));
        AuthorizationRequest authRequest = new AuthorizationRequest(userId, "2",
                new TransactionAmount(withdrawAmount, "USD", "DEBIT"));

        when(accountRepository.findById(userId)).thenReturn(java.util.Optional.of(account));

        AuthorizationResponse response = transactionService.authorizeTransaction(authRequest);
        assertEquals("APPROVED", response.getResponseCode());

        verify(eventRepository).save(argThat(event -> event.getAccountId().equals(userId) &&
                event.getType().equals("FundsWithdrawn") &&
                event.getPayload().contains(withdrawAmount.toString())));
    }

}
