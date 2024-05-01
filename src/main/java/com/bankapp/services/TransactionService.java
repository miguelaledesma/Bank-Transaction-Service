package com.bankapp.services;

import com.bankapp.commands.DepositFundsCommand;
import com.bankapp.commands.WithdrawFundsCommand;
import com.bankapp.dto.AuthorizationRequest;
import com.bankapp.dto.AuthorizationResponse;
import com.bankapp.dto.LoadRequest;
import com.bankapp.dto.LoadResponse;
import com.bankapp.model.Account;
import com.bankapp.model.events.EventEntity;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TransactionService(AccountRepository accountRepository, EventRepository eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    public AuthorizationResponse authorizeTransaction(AuthorizationRequest request) {
        Account account = accountRepository.findById(request.getUserId()).orElse(null);
        BigDecimal requestedAmount = request.getTransactionAmount().getAmount();
        if (account != null && account.getBalance().compareTo(requestedAmount) >= 0) {
            processWithdrawal(account, new WithdrawFundsCommand(request.getUserId(), requestedAmount));
            return new AuthorizationResponse(request.getUserId(), request.getMessageId(), "APPROVED",
                    account.getBalance());
        } else {
            String reason = account == null ? "Account not found" : "Insufficient funds";
            publishDeclinedEvent(request.getUserId(), requestedAmount, reason);
            return new AuthorizationResponse(request.getUserId(), request.getMessageId(), "DECLINED",
                    (account == null ? BigDecimal.ZERO : account.getBalance()));
        }
    }

    public LoadResponse loadFunds(LoadRequest request) {
        BigDecimal amount = request.getTransactionAmount().getAmount();
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot load negative amounts");
        }

        Account account = accountRepository.findById(request.getUserId())
                .orElse(new Account(request.getUserId(), BigDecimal.ZERO));
        handleDepositCommand(account,
                new DepositFundsCommand(request.getUserId(), request.getTransactionAmount().getAmount()));
        return new LoadResponse(request.getUserId(), request.getMessageId(), account.getBalance());
    }

    @Transactional
    private void handleDepositCommand(Account account, DepositFundsCommand command) {
        BigDecimal initialBalance = account.getBalance();
        account.setBalance(initialBalance.add(command.getAmount()));
        accountRepository.save(account);
        String payload = createBalanceChangePayload(initialBalance, account.getBalance(), command.getAmount(),
                "Deposit");
        publishEvent(account, "FundsDeposited", payload);
    }

    @Transactional
    private void processWithdrawal(Account account, WithdrawFundsCommand command) {
        BigDecimal initialBalance = account.getBalance();
        account.setBalance(initialBalance.subtract(command.getAmount()));
        accountRepository.save(account);
        String payload = createBalanceChangePayload(initialBalance, account.getBalance(), command.getAmount(),
                "Withdrawal");
        publishEvent(account, "FundsWithdrawn", payload);
    }

    private void publishDeclinedEvent(String accountId, BigDecimal amount, String reason) {
        String payload = "{\"amount\": " + amount + ", \"reason\": \"" + reason + "\"}";
        publishEvent(new Account(accountId, BigDecimal.ZERO), "TransactionDeclined", payload);

    }

    private void publishEvent(Account account, String eventType, String payload) {
        EventEntity event = new EventEntity(account.getAccountId(), eventType, payload, Instant.now());
        eventRepository.save(event);
    }

    private String createBalanceChangePayload(BigDecimal initialBalance, BigDecimal finalBalance, BigDecimal amount,
            String type) {
        return String.format(
                "{\"initialBalance\": \"%s\", \"finalBalance\": \"%s\", \"amount\": \"%s\", \"type\": \"%s\"}",
                initialBalance.toPlainString(), finalBalance.toPlainString(), amount.toPlainString(), type);
    }
}
