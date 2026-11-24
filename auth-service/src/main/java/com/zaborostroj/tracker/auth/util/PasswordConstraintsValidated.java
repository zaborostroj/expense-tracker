package com.zaborostroj.tracker.auth.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraintsValidated {
    String message() default "Invalid password format";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

    int minLength() default 9;
    int maxLength() default 50;
}
