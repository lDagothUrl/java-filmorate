package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> getGenres();

    Genre getGenreById(Integer genreId);

}