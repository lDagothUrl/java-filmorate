package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilm(Long id);

    List<Film> getPopularFilm(Integer count);

    Film postFilms(Film film);

    Film putFilms(Film film);

    void delFilms();

    Film delFilm(Long id);

    void addFilmsLike(Long filmId, Long userId);

    void delFilmsLike(Long filmId, Long userId);
}
