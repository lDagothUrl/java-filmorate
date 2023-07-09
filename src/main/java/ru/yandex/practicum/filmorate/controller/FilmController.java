package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable @NotNull Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilm(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilm(count != null ? count : 10);
    }

    @PostMapping("/films")
    public Film postFilms(@RequestBody @Valid Film film) {
        return filmService.postFilms(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addFilmsLike(@PathVariable @NotNull Long filmId, @PathVariable @NotNull Long userId) {
        filmService.addFilmsLike(filmId, userId);
    }

    @PutMapping("/films")
    public Film putFilms(@RequestBody @Valid Film film) {
        return filmService.putFilms(film);
    }

    @DeleteMapping("/films")
    public void delFilms() {
        filmService.delFilms();
    }

    @DeleteMapping("/films/{id}")
    public Film delFilm(@PathVariable @NotNull Long id) {
        return filmService.delFilm(id);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void delFilmsLike(@PathVariable @NotNull Long filmId, @PathVariable @NotNull Long userId) {
        filmService.delFilmsLike(filmId, userId);
    }
}
