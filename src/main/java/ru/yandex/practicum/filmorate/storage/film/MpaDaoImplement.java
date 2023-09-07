package ru.yandex.practicum.filmorate.storage.film;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImplement implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImplement(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getListMpa() {
        String sqlMpa = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sqlMpa, this::rowMapper);
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String sqlMpa = "SELECT * FROM mpa_ratings WHERE rating_id = ?";
        Mpa mpaRating;
        try {
            mpaRating = jdbcTemplate.queryForObject(sqlMpa, this::rowMapper, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Возрастной рейтинг с идентификатором " +
                    mpaId + " не зарегистрирован!");
        }
        return mpaRating;
    }

    private Mpa rowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"),
                resultSet.getString("rating"), resultSet.getString("description"));
    }
}