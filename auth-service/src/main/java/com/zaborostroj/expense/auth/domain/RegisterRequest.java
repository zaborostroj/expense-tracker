package com.zaborostroj.expense.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

//    @NotBlank(message = "Password is required")
//    @Size(min = 9, max = 50, message = "Password must be between 9 and 50 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private char[] password;
}
