package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private static Validator validator = null;
    private User user;

    @BeforeEach
    public void BeforeEach() {
        validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        user = new User();
        user.setName("user");
        user.setLogin("login");
        user.setEmail("kot@ya.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void validateEmail() {
        user.setEmail("gg");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectEmail() {
        user.setEmail("lag@mail.com");
        validateCorrectValue(user);
    }

    @Test
    void validateLogin() {
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("login", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectLogin() {
        user.setLogin("UsEr");
        validateCorrectValue(user);
    }

    @Test
    void validateBirthday() {
        user.setBirthday(LocalDate.of(2050, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(PastOrPresent.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("birthday", violation.getPropertyPath().toString());

        user.setBirthday(null);
        violations = validator.validate(user);
        violation = violations.iterator().next();
        assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("birthday", violation.getPropertyPath().toString());
    }

    @Test
    void validateCorrectBirthday() {
        user.setBirthday(LocalDate.now());
        validateCorrectValue(user);
        user.setBirthday(LocalDate.of(1895, 12, 28));
        validateCorrectValue(user);
    }

    void validateCorrectValue(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }
}