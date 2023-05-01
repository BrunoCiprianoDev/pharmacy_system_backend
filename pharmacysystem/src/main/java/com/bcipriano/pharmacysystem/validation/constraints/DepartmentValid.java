package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.DepartmentValidation;
import com.bcipriano.pharmacysystem.validation.PositionValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DepartmentValid {

    String message() default "Invalid department";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
