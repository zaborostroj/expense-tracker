package com.zaborostroj.tracker.expense.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query("SELECT e FROM Expense e WHERE e.userUuid = :userUuid AND e.spentAt >= :start AND e.spentAt < :end")
    List<Expense> findByMonth(
            @Param("userUuid") UUID userUuid,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    default List<Expense> findAllByMonth(UUID userUuid, YearMonth month) {
        Instant start = month
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        Instant end = month
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        return findByMonth(userUuid, start, end);
    }

    Optional<Expense> findByUserUuidAndUuid(UUID userUuid, UUID expenseUuid);
}
