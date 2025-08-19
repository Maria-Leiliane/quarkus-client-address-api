package com.client.address.validation;


public class CpfValidator {

    private static final int[] WEIGHT_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }


        String cleanCpf = cpf.replaceAll("[^\\d]", "");

        if (cleanCpf.length() != 11 || cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int digit1 = calculateDigit(cleanCpf.substring(0, 9), WEIGHT_CPF);

            int digit2 = calculateDigit(cleanCpf.substring(0, 10), WEIGHT_CPF);

            return cleanCpf.charAt(9) - '0' == digit1 && cleanCpf.charAt(10) - '0' == digit2;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateDigit(String str, int... weights) {
        int sum = 0;
        int maxIndex = str.length() - 1;
        for (int i = 0; i <= maxIndex; i++) {
            sum += (str.charAt(i) - '0') * weights[maxIndex - i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
