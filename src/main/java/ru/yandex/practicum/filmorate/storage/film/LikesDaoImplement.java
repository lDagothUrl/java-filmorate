package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikesDaoImplement implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Integer> getFilmLikes(Integer filmId) {
        return new HashSet<>(jdbcTemplate.queryForList("SELECT user_id FROM films_likes WHERE film_id = ?", Integer.class, filmId));
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO films_likes(film_id, user_id)" +
                "VALUES(?, ?)", filmId, userId);

    }

    @Override
    public void deleteLikeFromFilm(Integer filmId, Integer userId) {
        if (checkFilmId(filmId) == 0) {
            throw new NotFoundException("Film id " + filmId + " not found");
        }
        jdbcTemplate.update("DELETE FROM films_likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    private int checkFilmId(int id) {
        return jdbcTemplate.queryForObject("select count(*) from FILMS_LIKES where FILM_ID = ?", Integer.class, id);

    }
}