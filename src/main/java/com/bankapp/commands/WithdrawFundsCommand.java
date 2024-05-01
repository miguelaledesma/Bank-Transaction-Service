package com.bankapp.commands;

import java.math.BigDecimal;

public class WithdrawFundsCommand {
    private final String accountId;
    private final BigDecimal amount;

    public WithdrawFundsCommand(String accountId, BigDecimal amount) {
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
