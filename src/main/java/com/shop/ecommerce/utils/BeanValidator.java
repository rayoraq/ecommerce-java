package com.shop.ecommerce.utils;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BeanValidator {

    public static <T> T validate(@NonNull final T bean) {
        try (val validatorFactory = Validation.buildDefaultValidatorFactory()) {
            val violations = validatorFactory.getValidator().validate(bean);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return bean;
        }
    }
}
