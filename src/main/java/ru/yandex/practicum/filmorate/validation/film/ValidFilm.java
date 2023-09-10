package ru.yandex.practicum.filmorate.validation.film;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmValidator.class)
@Documented
public @interface ValidFilm {
    String message() default "Invalid Film date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
