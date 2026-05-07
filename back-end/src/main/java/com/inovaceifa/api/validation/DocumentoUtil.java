package com.inovaceifa.api.validation;

public final class DocumentoUtil {

    private DocumentoUtil() {
    }

    /**
     * Remove qualquer caractere não numérico
     */
    public static String somenteNumeros(String valor) {
        if (valor == null) return null;
        return valor.replaceAll("\\D", "");
    }

    /* =========================================================
       CPF
       ========================================================= */

    public static boolean cpfValido(String cpf) {
        cpf = somenteNumeros(cpf);

        if (cpf == null || cpf.length() != 11) return false;

        // Rejeita CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }

            int dig1 = 11 - (soma % 11);
            if (dig1 >= 10) dig1 = 0;

            if (dig1 != (cpf.charAt(9) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }

            int dig2 = 11 - (soma % 11);
            if (dig2 >= 10) dig2 = 0;

            return dig2 == (cpf.charAt(10) - '0');

        } catch (Exception e) {
            return false;
        }
    }

    /* =========================================================
       CNPJ
       ========================================================= */

    public static boolean cnpjValido(String cnpj) {
        cnpj = somenteNumeros(cnpj);

        if (cnpj == null || cnpj.length() != 14) return false;

        // Rejeita CNPJs com todos os dígitos iguais
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso1[i];
            }

            int dig1 = soma % 11;
            dig1 = (dig1 < 2) ? 0 : 11 - dig1;

            if (dig1 != (cnpj.charAt(12) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso2[i];
            }

            int dig2 = soma % 11;
            dig2 = (dig2 < 2) ? 0 : 11 - dig2;

            return dig2 == (cnpj.charAt(13) - '0');

        } catch (Exception e) {
            return false;
        }
    }
}
