package com.example.serbUber.util;

import com.example.serbUber.request.LongLatRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSizeConstraint, List<LongLatRequest>> {
    @Override
    public boolean isValid(List<LongLatRequest> values, ConstraintValidatorContext context) {
        return values.size() >= 2;
    }
}
