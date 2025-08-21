package com.client.address.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfOrCnpjValidator.class)
@Documented
public @interface CpfOrCnpj {
    String message() default "Invalid document. Must be a CPF (11 digits) or CNPJ (14 digits).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}