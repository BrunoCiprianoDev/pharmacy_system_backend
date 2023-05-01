package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidation implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String phone = value == null ? "" : value;
        if(!phone.matches("^\\(\\d{2}\\)\\d{5}\\-\\d{4}$") && !phone.matches("^\\(\\d{2}\\)\\d{4}\\-\\d{4}$")) {
            return false;
        }
        return true;
    }
}
