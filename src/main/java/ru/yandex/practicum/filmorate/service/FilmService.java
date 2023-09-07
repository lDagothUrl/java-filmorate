package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final LikeDao likesDao;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikeDao likesDao) {
        this.filmStorage = filmStorage;
        this.likesDao = likesDao;
    }

    public int getLikes(int filmId) {
        return filmStorage.findFilm(filmId).getLikes().size();
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findPopularFilms(count);
    }

    public Film findFilm(int id) {
        return filmStorage.findFilm(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void createFilm(Film film) {
        validation(film);
        filmStorage.createFilm(film);
    }

    public void addLike(int filmId, Integer userId) {
        likesDao.addLikeToFilm(filmId, userId);
    }

    public Film updateFilm(Film filmToUpdate) {
        validation(filmToUpdate);
        return filmStorage.updateFilm(filmToUpdate);
    }

    public void deleteFilm(int filmToDelite) {
        filmStorage.deleteFilm(filmToDelite);
    }

    public void deleteLike(int filmId, Integer userId) {
        likesDao.deleteLikeFromFilm(filmId, userId);
    }

    private void validation(Film film) {
        final LocalDate latestReleaseDate = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(latestReleaseDate)) {
            throw new ValidationException("Release date before 1895.12.28");
        }
        if (film.getName().isEmpty() && film.getName().isBlank()) {
            throw new ValidationException("name not empty");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("duration negotiv");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("duration length more 200");
        }
    }
}