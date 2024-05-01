package com.bankapp.dto;

import java.math.BigDecimal;

public class LoadResponse {
    private String userId;
    private String messageId;
    private BigDecimal balance;

    public LoadResponse(String userId, String messageId, BigDecimal balance) {
        this.userId = userId;
        this.messageId = messageId;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
