package com.example.Gestao_Viva.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotOnMondayValidator.class)
public @interface NotOnMonday {
    String message() default "Agendamentos não são permitidos às segundas-feiras.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}