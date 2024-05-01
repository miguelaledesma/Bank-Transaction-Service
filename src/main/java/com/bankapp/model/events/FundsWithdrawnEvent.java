package com.bankapp.model.events;

import java.math.BigDecimal;

public class FundsWithdrawnEvent {
    private final String accountId;
    private final BigDecimal amount;

    public FundsWithdrawnEvent(String accountId, BigDecimal amount) {
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
