package com.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.model.events.EventEntity;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByAccountIdOrderByTimestampAsc(String accountId);
}
