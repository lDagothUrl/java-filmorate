package ru.yandex.practicum.filmorate.storage.film;

import java.util.Set;

public interface LikeDao {
    Set<Integer> getFilmLikes(Integer filmId);

    void addLikeToFilm(Integer filmId, Integer userId);

    void deleteLikeFromFilm(Integer filmId, Integer userId);
}