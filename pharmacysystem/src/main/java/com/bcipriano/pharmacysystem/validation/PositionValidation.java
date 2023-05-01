package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.model.entity.enums.Position;
import com.bcipriano.pharmacysystem.validation.constraints.PositionValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositionValidation implements ConstraintValidator<PositionValid, String> {

    @Override
    public void initialize(PositionValid constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        for(Position position : Position.values()){
            if(value.equals(position.toString())){
                return true;
            }
        }
        return false;
    }

}
