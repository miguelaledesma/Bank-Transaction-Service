package com.bankapp.model.events;

import java.math.BigDecimal;

public class FundsDepositedEvent {
    private final String accountId;
    private final BigDecimal amount;

    public FundsDepositedEvent(String accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
