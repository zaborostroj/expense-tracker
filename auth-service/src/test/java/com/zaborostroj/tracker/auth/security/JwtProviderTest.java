package com.zaborostroj.tracker.auth.security;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtProviderTest {

    private JwtProvider jwtProvider;

    private static final String SECRET = "test-secret-key-for-jwt-must-be-at-least-32-chars-long";
    private static final long EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        this.jwtProvider = new JwtProvider(SECRET, EXPIRATION);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtProvider.generateToken(UUID.randomUUID(), "a@b.com");
        assertNotNull(token);
        assertFalse(Strings.isEmpty(token));
    }

    @Test
    void getUserIdFromToken_shouldReturnCorrectId() {
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        String token = jwtProvider.generateToken(userId, email);

        UUID extractedId = jwtProvider.getUserIdFromToken(token);

        assertEquals(userId, extractedId);
    }

    @Test
    void getEmailFromToken_shouldReturnCorrectEmail() {
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        String token = jwtProvider.generateToken(userId, email);

        String extractedEmail = jwtProvider.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void validateToken_withValidToken_shouldReturnTrue() {
        UUID userId = UUID.randomUUID();
        String token = jwtProvider.generateToken(userId, "test@example.com");

        assertTrue(jwtProvider.validateToken(token));
    }

    @Test
    void validateToken_withInvalidToken_shouldReturnFalse() {
        assertFalse(jwtProvider.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_withExpiredToken_shouldReturnFalse() {
        // Создаем провайдер с отрицательной экспирацией (токен сразу истекает)
        JwtProvider expiredProvider = new JwtProvider(SECRET, -1000);
        String token = expiredProvider.generateToken(UUID.randomUUID(), "test@example.com");

        assertFalse(jwtProvider.validateToken(token));
    }
}
