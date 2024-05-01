package com.bankapp.dto;

public class LoadRequest {
    private String userId;
    private String messageId;
    private TransactionAmount transactionAmount;

    // No-argument constructor for Jackson
    public LoadRequest() {
    }

    public LoadRequest(String userId, String messageId, TransactionAmount transactionAmount) {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public TransactionAmount getTransactionAmount() {
        return transactionAmount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setTransactionAmount(TransactionAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
