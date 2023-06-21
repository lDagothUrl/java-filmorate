package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RealiseDateValidator.class)
public @interface RealiseDateConstraint {
    String message() default "Film realise must be after 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}