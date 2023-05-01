package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.DateFormatValidation;
import com.bcipriano.pharmacysystem.validation.NoteNumberValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoteNumberValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoteNumber {

    String message() default "Invalid not number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
