package com.client.address.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {

    @Override
    public boolean isValid(String document, ConstraintValidatorContext context) {
        if (document == null || document.isBlank()) {
            return false;
        }
        String cleanDocument = document.replaceAll("\\D+", "");
        if (cleanDocument.length() == 11) {
            return isCpfValid(cleanDocument);
        } else if (cleanDocument.length() == 14) {
            return isCnpjValid(cleanDocument);
        }
        return false;
    }

    private static boolean isCpfValid(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        try {
            int digit1 = calculateDigit(cpf.substring(0, 9), 10);
            int digit2 = calculateDigit(cpf.substring(0, 9) + digit1, 11);
            return cpf.equals(cpf.substring(0, 9) + digit1 + digit2);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isCnpjValid(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        try {
            int digit1 = calculateDigit(cnpj.substring(0, 12), 5);
            int digit2 = calculateDigit(cnpj.substring(0, 12) + digit1, 6);
            return cnpj.equals(cnpj.substring(0, 12) + digit1 + digit2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateDigit(String str, int startWeight) {
        int sum = 0;
        int weight = startWeight;
        for (int i = 0; i < str.length(); i++) {
            sum += Character.getNumericValue(str.charAt(i)) * weight;
            weight--;
            if (weight == 1) {
                weight = 9;
            }
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
