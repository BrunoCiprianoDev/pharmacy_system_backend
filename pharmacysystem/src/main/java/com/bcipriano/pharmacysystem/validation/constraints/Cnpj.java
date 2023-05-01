package com.bcipriano.pharmacysystem.validation.constraints;

import com.bcipriano.pharmacysystem.validation.CnpjValidation;
import com.bcipriano.pharmacysystem.validation.CpfValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CnpjValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cnpj {

    String message() default "Invalid CNPJ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
