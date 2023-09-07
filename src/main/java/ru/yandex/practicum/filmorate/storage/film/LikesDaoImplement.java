package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LikesDaoImplement implements LikeDao {
    JdbcTemplate jdbcTemplate;

    public LikesDaoImplement(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Integer> getFilmLikes(Integer filmId) {
        String sqlQuery = "SELECT user_id FROM films_likes WHERE film_id = ?";
        List<Integer> filmLikes = jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
        return new HashSet<>(filmLikes);
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO films_likes(film_id, user_id)" +
                "VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);

    }

    @Override
    public void deleteLikeFromFilm(Integer filmId, Integer userId) {
        if (checkFilmId(filmId) == 0) {
            throw new NotFoundException("Film id " + filmId + " not found");
        }
        String sqlQuery = "DELETE FROM films_likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    private int checkFilmId(int id) {
        String sql = "select count(*) from FILMS_LIKES where FILM_ID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);

    }
}