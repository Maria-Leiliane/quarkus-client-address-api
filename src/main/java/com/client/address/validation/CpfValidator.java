package com.client.address.validation;

public class CpfValidator {

    private static final int[] WEIGHT_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cleanCpf = cpf.replaceAll("\\D+", "");

        if (cleanCpf.length() != 11 || cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Calc first digit verification
            int digit1 = calculateDigit(cleanCpf.substring(0, 9));

            // The first digit is use for calculation of the second digit.
            int digit2 = calculateDigit(cleanCpf.substring(0, 9) + digit1);

            // Compare the digits calculated with the CPF.
            return cleanCpf.equals(cleanCpf.substring(0, 9) + digit1 + digit2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateDigit(String str) {
        int sum = 0;
        // Loop for use the weight
        for (int i = 0; i < str.length(); i++) {
            // The first weight list (11 or 10) multiply the first dig.
            sum += Character.getNumericValue(str.charAt(i)) * CpfValidator.WEIGHT_CPF[i + 1];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}