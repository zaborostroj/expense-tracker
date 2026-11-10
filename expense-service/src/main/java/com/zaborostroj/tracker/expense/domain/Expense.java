package com.zaborostroj.tracker.expense.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    UUID uuid;

    @Column(name = "user_uuid", nullable = false, updatable = false)
    UUID userUuid;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    BigDecimal amount;

    @Column(name = "category")
    String category;

    @Column(name = "description")
    String description;

    @Column(name = "spent_at", nullable = false)
    Date spentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    Instant updatedAt;
}
