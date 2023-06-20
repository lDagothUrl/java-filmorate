package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RealiseDateValidator implements ConstraintValidator<RealiseDateConstraint, LocalDate> {
    private static final LocalDate STARTING_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (value.isAfter(STARTING_DATE)) {
            return true;
        }
        System.err.println(STARTING_DATE);
        return false;
    }
}
