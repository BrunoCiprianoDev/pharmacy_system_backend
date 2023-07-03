package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Cpf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfValidation implements ConstraintValidator<Cpf, String> {

    @Override
    public void initialize(Cpf constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String cpf = value == null ? "" : value;
        cpf = cpf.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos do CPF

        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] digitos = new int[11];
        for (int i = 0; i < 11; i++) {
            digitos[i] = Integer.parseInt(cpf.substring(i, i + 1));
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += digitos[i] * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 == 10 || digito1 == 11) {
            digito1 = 0;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += digitos[i] * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 == 10 || digito2 == 11) {
            digito2 = 0;
        }

        // Verifica se os dígitos verificadores calculados são iguais aos dígitos informados
        return digitos[9] == digito1 && digitos[10] == digito2;
    }

}
