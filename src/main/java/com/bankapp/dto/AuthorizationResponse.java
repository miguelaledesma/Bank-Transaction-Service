package com.bankapp.dto;

import java.math.BigDecimal;

public class AuthorizationResponse {
    private String userId;
    private String messageId;
    private String responseCode;
    private BigDecimal balance;

    public AuthorizationResponse(String userId, String messageId, String responseCode, BigDecimal balance) {
        this.userId = userId;
        this.messageId = messageId;
        this.responseCode = responseCode;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
