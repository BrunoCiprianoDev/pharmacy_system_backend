package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Rms;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RmsValidation implements ConstraintValidator<Rms, String> {

    @Override
    public void initialize(Rms constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String rms = value;
        if(rms == null ) {
            return true;
        } else {
            return rms.matches("\\d{13}") ? true : false;
        }
    }

}
