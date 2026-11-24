package com.zaborostroj.tracker.expense.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateExpenseRequest (
        @NotNull BigDecimal amount,
        @NotBlank String category,
        String description,
        @NotNull Instant spentAt
) {}
