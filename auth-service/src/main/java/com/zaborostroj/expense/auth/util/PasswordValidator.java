package com.zaborostroj.expense.auth.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraintsValidated, char[]> {

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(PasswordConstraintsValidated constraint) {
        this.minLength = constraint.minLength();
        this.maxLength = constraint.maxLength();
    }

    @Override
    public boolean isValid(char[] value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.length < this.minLength || value.length > this.maxLength) {
            return false;
        }

        boolean whiteSpacesAbsent = true;
        boolean containsUpperCase = false;
        boolean containsLowerCase = false;
        boolean containsDigits = false;

        for (char c : value) {
            if (c == ' ') {
                whiteSpacesAbsent = false;
                break;
            }

            if (!containsUpperCase && Character.isUpperCase(c)) {
                containsUpperCase = true;
            }

            if (!containsLowerCase && Character.isLowerCase(c)) {
                containsLowerCase = true;
            }

            if (!containsDigits && Character.isDigit(c)) {
                containsDigits = true;
            }
        }

        return whiteSpacesAbsent && containsUpperCase && containsLowerCase && containsDigits;
    }
}
