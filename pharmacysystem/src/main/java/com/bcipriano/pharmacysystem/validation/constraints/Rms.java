package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.PositionValidation;
import com.bcipriano.pharmacysystem.validation.RmsValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RmsValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rms {

    String message() default "Invalid RMS.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
