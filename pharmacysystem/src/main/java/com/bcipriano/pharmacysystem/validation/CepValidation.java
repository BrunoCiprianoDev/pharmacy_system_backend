package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Cep;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CepValidation implements ConstraintValidator<Cep, String> {

    @Override
    public void initialize(Cep constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String cep = value == null ? "" : value;
        return cep.matches( "^\\d{2}\\.\\d{3}\\-\\d{3}$");
    }

}
