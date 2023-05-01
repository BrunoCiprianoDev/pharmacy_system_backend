package com.bcipriano.pharmacysystem.validation;

import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.validation.constraints.DepartmentValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DepartmentValidation implements ConstraintValidator<DepartmentValid, String> {

    @Override
    public void initialize(DepartmentValid constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        for(Department department : Department.values()){
            if(value.equals(department.toString())){
                return true;
            }
        }
        return false;
    }
}
