package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.LotNumberValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LotNumberValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LotNumber {

    String message() default "Invalid lot number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
