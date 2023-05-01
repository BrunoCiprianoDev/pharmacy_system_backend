package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.NoteNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoteNumberValidation implements ConstraintValidator<NoteNumber, String> {

    @Override
    public void initialize(NoteNumber constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String noteNumber = value == null ? "" : value;
        if(!noteNumber.matches("^\\d{3}.\\d{3}.\\d{3}-\\d{2}$")) {
            return false;
        }
        return true;
    }


}
