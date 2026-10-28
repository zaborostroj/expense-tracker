package com.zaborostroj.expense.auth.domain;

import com.zaborostroj.expense.auth.exception.InvalidCredentialsException;
import com.zaborostroj.expense.auth.exception.UserAlreadyExistsException;
import com.zaborostroj.expense.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        User user;
        try {
            CharSequence password = CharBuffer.wrap(request.getPassword());
            user = new User(
                    UUID.randomUUID(),
                    request.getName(),
                    request.getEmail(),
                    passwordEncoder.encode(password),
                    Instant.now(),
                    null,
                    null
            );
        } finally {
            request.setPassword(null);
        }

        userService.save(user);
        String token = jwtProvider.generateToken(user.getUuid(), user.getEmail());
        return new TokenResponse(token);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        try {
            CharSequence password = CharBuffer.wrap(request.getPassword());
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                throw new InvalidCredentialsException("Invalid email or password");
            }
        } finally {
            request.setPassword(null);
        }

        String token = jwtProvider.generateToken(user.getUuid(), user.getEmail());
        return new TokenResponse(token);
    }
}
