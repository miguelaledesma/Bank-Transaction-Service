package com.bankapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    private String accountId;
    private BigDecimal balance;

    public Account() {}

    public Account(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
