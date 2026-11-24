package com.zaborostroj.tracker.expense.domain;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdateExpenseRequest (
        @Positive BigDecimal amount,
        String category,
        String description,
        Instant spentAt
) {}
