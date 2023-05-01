package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.validation.constraints.BirthDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BirthDateValidation implements ConstraintValidator<BirthDate, String> {

    @Override
    public void initialize(BirthDate constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        try {
            LocalDate now = LocalDate.now();
            LocalDate minBirthDate = now.minusYears(120);
            LocalDate maxBirthDate = now.minusYears(18);

            LocalDate localDate = LocalDate.parse(value);
            if (localDate.isBefore(minBirthDate) || localDate.isAfter(maxBirthDate)) {
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}