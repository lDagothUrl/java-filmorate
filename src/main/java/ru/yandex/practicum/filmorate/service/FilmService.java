package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getFilms();

    Film getFilm(Long id);

    List<Film> getPopularFilm(Integer count);

    Film postFilms(Film film);

    void addFilmsLike(Long filmId, Long userId);

    Film putFilms(Film film);

    void delFilms();

    Film delFilm(Long id);

    void delFilmsLike(Long filmId, Long userId);
}
