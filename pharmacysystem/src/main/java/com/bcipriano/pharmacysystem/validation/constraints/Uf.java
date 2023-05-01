package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.CpfValidation;
import com.bcipriano.pharmacysystem.validation.UfValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UfValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Uf {

    String message() default "Invalid UF";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
