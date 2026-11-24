package com.zaborostroj.tracker.expense.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(schema = "expenses", name = "expenses")
@Getter
@Setter
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
    Instant spentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void updateFrom(UpdateExpenseRequest request) {
        if (request.amount() != null) {
            this.amount = request.amount();
        }
        if (request.category() != null) {
            this.category = request.category();
        }
        if (request.description() != null) {
            this.description = request.description();
        }
        if (request.spentAt() != null) {
            this.spentAt = request.spentAt();
        }
    }
}
