package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private static final Map<Long, Film> films = new HashMap<>();

    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.debug("getFilms");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film postFilms(@RequestBody @Valid Film film) {
        log.info("postFilm-start: {}", film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film putFilms(@RequestBody @Valid Film film) {
        log.info("postFilm-start: {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("FilmId: " + film.getId());
        }
    }
}
