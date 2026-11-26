package com.shop.ecommerce.utils;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
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
