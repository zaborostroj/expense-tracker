package com.zaborostroj.tracker.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ErrorResponse {

    private final String error;
    private final String message;
    private final Map<String, String> fields;
    private final LocalDateTime timestamp;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.fields = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String error, String message, Map<String, String> fields) {
        this.error = error;
        this.message = message;
        this.fields = fields;
        this.timestamp = LocalDateTime.now();
    }
}
