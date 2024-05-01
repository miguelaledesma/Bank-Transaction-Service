package com.bankapp.model.events;

import java.math.BigDecimal;

public class TransactionDeclinedEvent {
    private final String accountId;
    private final BigDecimal amount;
    private final String reason;

    public TransactionDeclinedEvent(String accountId, BigDecimal amount, String reason) {
        this.accountId = accountId;
        this.amount = amount;
        this.reason = reason;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }
}
