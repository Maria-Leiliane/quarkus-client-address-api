package com.client.address.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {

    @Override
    public boolean isValid(String document, ConstraintValidatorContext context) {
        if (document == null || document.isBlank()) {
            return false;
        }

        String cleanDocument = document.replaceAll("[^\\d]", "");

        if (cleanDocument.length() == 11) {
            return CpfValidator.isValid(cleanDocument);
        } else if (cleanDocument.length() == 14) {
            return CnpjValidator.isValid(cleanDocument);
        }

        return false;
    }
}