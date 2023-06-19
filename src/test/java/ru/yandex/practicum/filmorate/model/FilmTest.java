package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.RealiseDateConstraint;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmTest {
    private static Validator validator = null;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        film = new Film();
        film.setName("user");
        film.setDescription("text");
        film.setDuration(1000L);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
    }

    @Test
    void validateName() {
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType(), "NotBlank violation not found");
        assertEquals("name", violation.getPropertyPath().toString(), "Not found violation under property name");
    }

    @Test
    void validateCorrectName() {
        film.setName("Name");
        validateCorrectValue(film);
    }

    @Test
    void validateDescription() {
        String str = "";
        for (int i = 1; i <= 200; i++) {
            str += i;
        }
        film.setDescription(str);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectDescription() {
        String str = "";
        for (int i = 1; i <= 200; i++) {
            str += (i % 10);
        }
        film.setDescription(str);
        validateCorrectValue(film);
        film.setDescription(null);
        validateCorrectValue(film);
    }

    @Test
    void validateDuration() {
        film.setDuration(-1000L);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Positive.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("duration", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectDuration() {
        film.setDuration(1000L);
        validateCorrectValue(film);
    }

    @Test
    void validateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1500, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(RealiseDateConstraint.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("releaseDate", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectReleaseDate() {
        film.setReleaseDate(LocalDate.now());
        validateCorrectValue(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        validateCorrectValue(film);
    }

    void validateCorrectValue(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }
}