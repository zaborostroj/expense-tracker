package com.zaborostroj.tracker.expense.exception;

import java.util.UUID;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(UUID userUuid, UUID expenseUuid) {
        super("Expense not found. User uuid: " + userUuid + ", expense uuid: " + expenseUuid);
    }
}
