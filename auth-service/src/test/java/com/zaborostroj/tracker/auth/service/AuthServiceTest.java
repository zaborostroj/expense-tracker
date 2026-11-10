package com.zaborostroj.tracker.auth.service;

import com.zaborostroj.tracker.auth.domain.AuthService;
import com.zaborostroj.tracker.auth.domain.LoginRequest;
import com.zaborostroj.tracker.auth.domain.RegisterRequest;
import com.zaborostroj.tracker.auth.domain.TokenResponse;
import com.zaborostroj.tracker.auth.domain.User;
import com.zaborostroj.tracker.auth.domain.UserService;
import com.zaborostroj.tracker.auth.exception.InvalidCredentialsException;
import com.zaborostroj.tracker.auth.exception.UserAlreadyExistsException;
import com.zaborostroj.tracker.auth.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private static final String USER_NAME = "tester";
    private static final String USER_EMAIL = "test@example.com";
    private static final String PASSWORD = "test_password";
    private static final String PASSWORD_HASH = "test_password_hash";
    private static final String JWT_TOKEN = "jwt-token";

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName(USER_NAME);
        registerRequest.setEmail(USER_EMAIL);
        registerRequest.setPassword(PASSWORD.toCharArray());

        loginRequest = new LoginRequest();
        loginRequest.setEmail(USER_EMAIL);
        loginRequest.setPassword(PASSWORD.toCharArray());

        testUser = new User(
                UUID.randomUUID(),
                USER_NAME,
                USER_EMAIL,
                PASSWORD_HASH,
                Instant.now(),
                null,
                null
        );
    }

    @Test
    void register_withNewUser_shouldCreateUserAndReturnToken() {
        // Given
        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD_HASH);
        when(jwtProvider.generateToken(any(UUID.class), anyString())).thenReturn(JWT_TOKEN);

        // When
        TokenResponse response = authService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.getToken());
        assertEquals("Bearer", response.getTokenType());

        verify(userService).save(any(User.class));
        verify(jwtProvider).generateToken(any(UUID.class), eq(USER_EMAIL));
    }

    @Test
    void register_withExistingEmail_shouldThrowException() {
        // Given
        when(userService.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(registerRequest)
        );

        assertTrue(exception.getMessage().contains("email"));
        verify(userService, never()).save(any());
    }

    @Test
    void register_withExistingName_shouldThrowException() {
        // Given
        when(userService.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(registerRequest)
        );

        verify(userService, never()).save(any());
    }

    @Test
    void login_withValidCredentials_shouldReturnToken() {
        // Given
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(CharBuffer.wrap(PASSWORD), PASSWORD_HASH)).thenReturn(true);
        when(jwtProvider.generateToken(any(UUID.class), anyString())).thenReturn(JWT_TOKEN);

        // When
        TokenResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.getToken());
        verify(jwtProvider).generateToken(testUser.getUuid(), testUser.getEmail());
    }

    @Test
    void login_withNonExistentUser_shouldThrowException() {
        // Given
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(jwtProvider, never()).generateToken(any(), anyString());
    }

    @Test
    void login_withWrongPassword_shouldThrowException() {
        // Given
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(CharBuffer.wrap(PASSWORD), testUser.getPasswordHash())).thenReturn(false);

        // When & Then
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        verify(jwtProvider, never()).generateToken(any(), anyString());
    }
}
