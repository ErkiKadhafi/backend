package com.opsnow.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UppercaseValidator.class)
public @interface Uppercase {

    String message() default "must be all uppercase";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}