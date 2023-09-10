package ru.yandex.practicum.filmorate.validation.film;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmValidator implements ConstraintValidator<ValidFilm, Film> {
    final LocalDate latestReleaseDate = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ValidFilm constraintAnnotation) {
    }

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext context) {

        if (film == null) {
            return false;
        }

        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            return false;
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            return false;
        }

        if (film.getDuration() < 0) {
            return false;
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(latestReleaseDate)) {
            return false;
        }

        return true;
    }
}