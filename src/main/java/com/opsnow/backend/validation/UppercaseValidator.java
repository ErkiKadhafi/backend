package com.opsnow.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UppercaseValidator implements ConstraintValidator<Uppercase, String> {

    @Override
    public void initialize(Uppercase constraintAnnotation) {
        // not needed for this case, but can be used for setup
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are considered valid; use @NotNull if needed
        }

        // Check if all letters are uppercase
        return value.equals(value.toUpperCase());
    }
}
