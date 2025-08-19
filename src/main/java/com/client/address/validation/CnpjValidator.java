package com.client.address.validation;

public class CnpjValidator {

    private static final int[] WEIGHT_CNPJ = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_CNPJ_DIGIT = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String cleanCnpj = cnpj.replaceAll("[^\\d]", "");

        if (cleanCnpj.length() != 14 || cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int digit1 = calculateDigit(cleanCnpj.substring(0, 12), WEIGHT_CNPJ);
            int digit2 = calculateDigit(cleanCnpj.substring(0, 13), WEIGHT_CNPJ_DIGIT);

            return cleanCnpj.charAt(12) - '0' == digit1 && cleanCnpj.charAt(13) - '0' == digit2;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateDigit(String str, int[] weights) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += (str.charAt(i) - '0') * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}