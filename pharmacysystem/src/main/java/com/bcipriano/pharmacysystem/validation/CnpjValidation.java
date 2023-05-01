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
        String cnpj = value == null ? "" : value;
        return cnpj.matches( "^\\d{2}\\.\\d{3}\\/\\d{4}\\-\\d{2}$");
    }

}
