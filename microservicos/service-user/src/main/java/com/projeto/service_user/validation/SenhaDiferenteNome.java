package com.projeto.service_user.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE}) 
@Retention (RetentionPolicy.RUNTIME)
@Constraint (validatedBy = SenhaDiferenteNomeValidator.class)
@Documented 
public @interface SenhaDiferenteNome {
    String message() default "A senha não pode conter o seu nome.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
