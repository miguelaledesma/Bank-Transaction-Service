package com.bankapp.model.events;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountId;
    private String type;
    private String payload;
    private Instant timestamp;

    public EventEntity() {
    }

    public EventEntity(String accountId, String type, String payload, Instant timestamp) {
        this.accountId = accountId;
        this.type = type;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
