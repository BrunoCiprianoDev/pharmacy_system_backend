package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateFormatValidation implements ConstraintValidator<DateFormat, String> {

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
