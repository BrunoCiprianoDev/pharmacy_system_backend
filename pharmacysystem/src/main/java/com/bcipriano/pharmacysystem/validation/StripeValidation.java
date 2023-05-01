package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import com.bcipriano.pharmacysystem.validation.constraints.StripeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StripeValidation implements ConstraintValidator<StripeValid, String> {

    @Override
    public void initialize(StripeValid constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        for (Stripe stripe : Stripe.values()) {
            if (value.equals(stripe.toString())) {
                return true;
            }
        }
        return false;
    }
}