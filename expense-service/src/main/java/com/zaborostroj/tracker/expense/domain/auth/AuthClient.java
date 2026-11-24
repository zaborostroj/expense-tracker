package com.zaborostroj.tracker.expense.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestClient restClient;

    public IntrospectionResponse introspect(String token) {
        return restClient.post()
                .uri("/internal/auth/introspect")
                .body(new TokenIntrospectionRequest(token))
                .retrieve()
                .body(IntrospectionResponse.class);
    }

    private record TokenIntrospectionRequest(String token) {}
}
