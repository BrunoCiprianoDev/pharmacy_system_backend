package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Code;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CodeValidation implements ConstraintValidator<Code, String> {

    @Override
    public void initialize(Code constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String code = value == null ? "" : value;
        return code.matches( "^\\d{6}-\\d{6}-\\d{1}$");
    }

}
