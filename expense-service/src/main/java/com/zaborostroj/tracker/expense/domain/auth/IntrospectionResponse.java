package com.zaborostroj.tracker.expense.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class IntrospectionResponse {
    private boolean active;
    private UUID userId;
}
