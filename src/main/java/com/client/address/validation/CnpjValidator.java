package com.client.address.validation;

public class CnpjValidator {

    private static final int[] WEIGHT_CNPJ_1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_CNPJ_2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String cleanCnpj = cnpj.replaceAll("\\D+", "");

        if (cleanCnpj.length() != 14 || cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Calc first digit
            int digit1 = calculateDigit(cleanCnpj.substring(0, 12), WEIGHT_CNPJ_1);

            // Use the first digit to calc the second digit
            int digit2 = calculateDigit(cleanCnpj.substring(0, 12) + digit1, WEIGHT_CNPJ_2);

            // Compare the digits calculated with the CNPJ.
            return cleanCnpj.equals(cleanCnpj.substring(0, 12) + digit1 + digit2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateDigit(String str, int[] weights) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += Character.getNumericValue(str.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}