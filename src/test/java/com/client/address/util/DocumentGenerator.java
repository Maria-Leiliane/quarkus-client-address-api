package com.client.address.util;

import java.util.Random;

public final class DocumentGenerator {

    private static final Random random = new Random();

    private DocumentGenerator() {}

    public static String generateCPF() {
        int[] cpf = new int[11];
        // Generate the first 9 digits randomly
        for (int i = 0; i < 9; i++) {
            cpf[i] = random.nextInt(10);
        }

        // Calculate the first check digit
        cpf[9] = calculateCpfDigit(cpf, 10);

        // Calculate the second check digit
        cpf[10] = calculateCpfDigit(cpf, 11);

        StringBuilder sb = new StringBuilder();
        for (int digit : cpf) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String generateCNPJ() {
        int[] cnpj = new int[14];
        // Generate the first 12 digits randomly (first 8 are company, last 4 are branch)
        for (int i = 0; i < 12; i++) {
            cnpj[i] = random.nextInt(10);
        }

        // Calculate the first check digit
        cnpj[12] = calculateCnpjDigit(cnpj, 12);

        // Calculate the second check digit
        cnpj[13] = calculateCnpjDigit(cnpj, 13);

        StringBuilder sb = new StringBuilder();
        for (int digit : cnpj) {
            sb.append(digit);
        }
        return sb.toString();
    }

    private static int calculateCpfDigit(int[] cpf, int length) {
        int sum = 0;
        for (int i = 0; i < length - 1; i++) {
            sum += cpf[i] * (length - i);
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }

    private static int calculateCnpjDigit(int[] cnpj, int length) {
        int[] weights = (length == 12)
                ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += cnpj[i] * weights[i];
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}