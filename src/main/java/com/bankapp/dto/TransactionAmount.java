package com.bankapp.dto;

import java.math.BigDecimal;

public class TransactionAmount {
    private BigDecimal amount;
    private String currency;
    private String debitOrCredit; // "DEBIT" or "CREDIT"

    public TransactionAmount() {
    }

    public TransactionAmount(BigDecimal amount, String currency, String debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }
}
