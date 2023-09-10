package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    Film findFilm(int id);

    List<Film> findPopularFilms(Integer count);

}