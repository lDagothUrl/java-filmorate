package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDao;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }
}