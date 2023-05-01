package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Cpf;
import com.bcipriano.pharmacysystem.validation.constraints.LotNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LotNumberValidation  implements ConstraintValidator<LotNumber, String> {

    @Override
    public void initialize(LotNumber constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String lotNumber = value == null ? "" : value;
        return lotNumber.matches( "^[A-Z]{3}-\\d{4}$");
    }
}
