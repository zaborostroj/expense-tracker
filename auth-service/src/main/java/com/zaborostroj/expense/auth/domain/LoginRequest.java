package com.zaborostroj.expense.auth.domain;

import com.zaborostroj.expense.auth.util.PasswordConstraintsValidated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull
    @PasswordConstraintsValidated
    private char[] password;
}
