package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.BirthDateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {

    String message() default "Born date invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
