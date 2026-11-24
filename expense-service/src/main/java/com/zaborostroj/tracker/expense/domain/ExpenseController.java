package com.zaborostroj.tracker.expense.domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(
            @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
            @Valid @RequestBody CreateExpenseRequest request
    ) {
        UUID userUuid = principal.getAttribute("userUuid");
        Expense expense = expenseService.create(userUuid, request);
        return ExpenseResponse.from(expense);
    }

    @GetMapping("/month/{year-month}") // /api/expense/month/2026-11
    public List<ExpenseResponse> findAllByMonth(
            @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
            @PathVariable("year-month") YearMonth month
    ) {
        UUID userUuid = principal.getAttribute("userUuid");
        return expenseService.findAllByMonth(userUuid, month).stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    @GetMapping("/{uuid}")
    public ExpenseResponse findByUuid(
            @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
            @PathVariable UUID uuid
    ) {
        UUID userUuid = principal.getAttribute("userUuid");
        Expense expense = expenseService.findByUuid(userUuid, uuid);
        return ExpenseResponse.from(expense);
    }

    @PutMapping("/{uuid}")
    public ExpenseResponse update(
            @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {
        UUID userUuid = principal.getAttribute("userUuid");
        Expense expense = expenseService.update(userUuid, uuid, request);
        return ExpenseResponse.from(expense);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
            @PathVariable UUID uuid
    ) {
        UUID userUuid = principal.getAttribute("userUuid");
        expenseService.delete(userUuid, uuid);
    }
}
