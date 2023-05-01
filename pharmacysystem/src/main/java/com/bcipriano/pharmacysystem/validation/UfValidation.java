package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Cnpj;
import com.bcipriano.pharmacysystem.validation.constraints.Uf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UfValidation implements ConstraintValidator<Uf, String> {

    @Override
    public void initialize(Uf constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String uf = value == null ? "" : value;
        return uf.length() == 2 && uf.matches("[a-zA-Z]{2}") ? true : false;
    }
}
