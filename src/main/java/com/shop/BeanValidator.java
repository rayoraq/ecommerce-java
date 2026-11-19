package com.shop;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory()
        .getValidator();

    public static <T> T validated(@NonNull final T bean) {
        val violations = VALIDATOR.validate(bean);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return bean;
    }
}
