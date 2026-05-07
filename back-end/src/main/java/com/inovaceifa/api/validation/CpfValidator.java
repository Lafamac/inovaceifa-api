package com.inovaceifa.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPFValido, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {

        if (cpf == null || cpf.isBlank()) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Bloqueia CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            int peso = 10;

            // Primeiro dígito
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }

            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;

            if (digito1 != (cpf.charAt(9) - '0')) {
                return false;
            }

            // Segundo dígito
            soma = 0;
            peso = 11;

            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }

            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;

            return digito2 == (cpf.charAt(10) - '0');

        } catch (Exception e) {
            return false;
        }
    }
}
