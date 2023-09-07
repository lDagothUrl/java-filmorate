package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping("/{id}/likes")
    public int getAllLikes(@PathVariable int id) {
        return filmService.getLikes(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("запрос на {} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable int id) {
        return filmService.findFilm(id);

    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        filmService.createFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film filmToUpdate) {
        filmService.updateFilm(filmToUpdate);
        return filmToUpdate;

    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
        log.info("userId {} like filmId {}", userId, filmId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(filmId, userId);
        log.info("userId {} del like filmId {}", userId, filmId);
    }

}
