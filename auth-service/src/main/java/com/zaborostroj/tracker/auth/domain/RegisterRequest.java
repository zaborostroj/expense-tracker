package com.zaborostroj.tracker.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zaborostroj.tracker.auth.util.PasswordConstraintsValidated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Password is required")
    @PasswordConstraintsValidated
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private char[] password;
}
