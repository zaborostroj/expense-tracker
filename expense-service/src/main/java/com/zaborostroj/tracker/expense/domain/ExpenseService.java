package com.zaborostroj.tracker.expense.domain;

import com.zaborostroj.tracker.expense.exception.ExpenseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense create(UUID userUuid, CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setUserUuid(userUuid);
        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setDescription(request.description());
        expense.setSpentAt(request.spentAt());
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> findAllByMonth(UUID userUuid, YearMonth month) {
        return expenseRepository.findAllByMonth(userUuid, month);
    }

    @Transactional(readOnly = true)
    public Expense findByUuid(UUID userUuid, UUID expenseUuid) {
        return expenseRepository.findByUserUuidAndUuid(userUuid, expenseUuid)
                .orElseThrow(() -> new ExpenseNotFoundException(userUuid, expenseUuid));
    }

    public Expense update(UUID userUuid, UUID expenseUuid, UpdateExpenseRequest request) {
        Expense expense = findByUuid(userUuid, expenseUuid);
        expense.updateFrom(request);
        return expenseRepository.save(expense);
    }

    public void delete(UUID userUuid, UUID expenseUuid) {
        Expense expense = findByUuid(userUuid, expenseUuid);
        expenseRepository.delete(expense);
    }
}
