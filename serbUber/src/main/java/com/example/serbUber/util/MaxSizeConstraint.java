package com.example.serbUber.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = MaxSizeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSizeConstraint {
    String message() default "The input list cannot contain less than 2 locations.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}