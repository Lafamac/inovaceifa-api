package com.inovaceifa.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJValido, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {

        if (cnpj == null) return true; // @NotBlank já trata

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) return false;

        // Evita CNPJs tipo 00000000000000
        if (cnpj.chars().distinct().count() == 1) return false;

        try {
            int[] peso1 = {5,4,3,2,9,8,7,6,5,4,3,2};
            int[] peso2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso1[i];
            }

            int resto = soma % 11;
            int dig1 = (resto < 2) ? 0 : 11 - resto;

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso2[i];
            }

            resto = soma % 11;
            int dig2 = (resto < 2) ? 0 : 11 - resto;

            return dig1 == (cnpj.charAt(12) - '0')
                    && dig2 == (cnpj.charAt(13) - '0');

        } catch (Exception e) {
            return false;
        }
    }
}
