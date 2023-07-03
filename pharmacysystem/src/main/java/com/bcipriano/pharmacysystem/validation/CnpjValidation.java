package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Cnpj;
import com.bcipriano.pharmacysystem.validation.constraints.Cpf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CnpjValidation implements ConstraintValidator<Cnpj, String> {

    @Override
    public void initialize(Cnpj constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // Remove caracteres não numéricos do CNPJ
        String cnpj = value == null ? "" : value.replaceAll("\\D", "");

        // Verifica se o CNPJ possui 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 00000000000000)
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        int weight = 2;
        for (int i = 11; i >= 0; i--) {
            int digit = Character.getNumericValue(cnpj.charAt(i));
            sum += digit * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }
        int digit1 = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

        // Calcula o segundo dígito verificador
        sum = 0;
        weight = 2;
        for (int i = 12; i >= 0; i--) {
            int digit = Character.getNumericValue(cnpj.charAt(i));
            sum += digit * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }
        int digit2 = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

        // Verifica se os dígitos verificadores estão corretos
        return Character.getNumericValue(cnpj.charAt(12)) == digit1 && Character.getNumericValue(cnpj.charAt(13)) == digit2;
    }


}

/*
*      String cnpj = value == null ? "" : value;
        return cnpj.matches( "^\\d{2}\\.\\d{3}\\/\\d{4}\\-\\d{2}$");
* */

