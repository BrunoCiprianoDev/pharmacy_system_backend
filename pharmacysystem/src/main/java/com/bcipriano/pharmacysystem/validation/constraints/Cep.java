package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.CepValidation;
import com.bcipriano.pharmacysystem.validation.CpfValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cep {

    String message() default "CEP invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
