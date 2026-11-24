package com.zaborostroj.tracker.auth.domain;

import lombok.Data;

@Data
public class TokenResponse {
    private final String token;
    private final String tokenType = "Bearer";
}
