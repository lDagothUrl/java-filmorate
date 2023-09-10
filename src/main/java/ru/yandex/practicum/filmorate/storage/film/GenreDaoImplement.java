package ru.yandex.practicum.filmorate.storage.film;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDaoImplement implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", this::makeGenre);
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        Genre genre;
        try {
            genre = jdbcTemplate.
                    queryForObject("SELECT * FROM genres WHERE genre_id = ?", this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " + genreId +
                    " не найден!");
        }
        return genre;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("genre_name"));
    }
}