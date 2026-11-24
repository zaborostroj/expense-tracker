package com.zaborostroj.tracker.expense.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ExpenseResponse(
        UUID uuid,
        UUID userUuid,
        BigDecimal amount,
        String category,
        String description,
        Instant spentAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
                expense.uuid,
                expense.userUuid,
                expense.amount,
                expense.category,
                expense.description,
                expense.spentAt,
                expense.createdAt,
                expense.updatedAt
        );
    }
}
