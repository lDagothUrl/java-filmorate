package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BaseFilmService implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public List<Film> getFilms() {
        log.info("getFilms");
        return filmStorage.getFilms();
    }

    @Override
    public Film getFilm(Long id) {
        log.info("getFilm ID: {}", id);
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        return filmStorage.getPopularFilm(count);
    }

    @Override
    public Film postFilms(Film film) {
        log.info("postFilm: {}", film);
        return filmStorage.postFilms(film);
    }

    @Override
    public Film putFilms(Film film) {
        log.info("putFilm: {}", film);
        return filmStorage.putFilms(film);
    }

    @Override
    public void delFilms() {
        log.info("delFilms");
        filmStorage.delFilms();
    }

    @Override
    public Film delFilm(Long id) {
        log.info("delFilm ID: {}", id);
        return filmStorage.delFilm(id);
    }

    @Override
    public void addFilmsLike(Long filmId, Long userId) {
        log.info("addLikeFilm filmId {}, userId {}", filmId, userId);
        filmStorage.addFilmsLike(filmId, userId);
    }

    @Override
    public void delFilmsLike(Long filmId, Long userId) {
        log.info("delLike filmId {}, userId {}", filmId, userId);
        filmStorage.delFilmsLike(filmId, userId);
    }
}
