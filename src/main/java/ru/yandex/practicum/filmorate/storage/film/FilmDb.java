package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Component
@Primary
public class FilmDb implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikeDao likesDao;


    public FilmDb(JdbcTemplate jdbcTemplate, LikeDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.likesDao = likesDao;
    }

    @Override
    public void deleteFilm(int id) {
        String deleteLikesQuery = "DELETE FROM films_likes WHERE film_id = ?";
        jdbcTemplate.update(deleteLikesQuery, id);

        String deleteGenresQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresQuery, id);

        String deleteFilmQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(deleteFilmQuery, id);
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT f.*, " +
                "m.rating as mpa_name, " +
                "m.description as mpa_description, " +
                "m.rating_id as mpa_id " +
                "FROM films as f " +
                "JOIN mpa_ratings as m ON f.mpa_id = m.rating_id ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        getFilmGenres(films);
        getFilmLikes(films);
        return films;
    }

    private void getFilmGenres(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        final String sqlQuery = "SELECT * " +
                "FROM genres AS g " +
                "INNER JOIN films_genres AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id IN(" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.getGenres().add(makeGenre(rs, films.size()));
        }, films.stream().map(Film::getId).toArray());
    }

    private void getFilmLikes(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        final String sqlQuery = "SELECT film_id, user_id FROM films_likes WHERE film_id IN(" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs) -> {
            final int filmId = rs.getInt("film_id");
            final int userId = rs.getInt("user_id");
            final Film film = filmById.get(filmId);
            if (film != null) {
                film.getLikes().add(userId);
            }
        }, films.stream().map(Film::getId).toArray());
    }

    @Override
    public Film createFilm(Film film) {

        try {
            String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id)"
                    + "values (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);

            film.setId(keyHolder.getKey().intValue());

            if (film.getGenres() != null) {
                addGenresToFilm(film);
            }

            return film;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create film: " + e.getMessage(), e);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (!checkFilmId(film.getId())) {
            throw new NotFoundException("Фильм с идентификатором " + film.getId() + " не найден!");
        }
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            deleteGenresFromFilm(film);
        } else {
            film.setGenres(new LinkedHashSet<>(film.getGenres()));
            updateGenresOfFilm(film);
        }
        return film;
    }


    @Override
    public Film findFilm(int id) {
        if (!checkFilmId(id)) {
            throw new NotFoundException("Фильм с идентификатором " + id + " не найден!");
        }
        String sqlQuery = "SELECT f.*, " +
                "m.rating as mpa_name, " +
                "m.description as mpa_description, " +
                "m.rating_id as mpa_id, " +
                "FROM films as f " +
                "JOIN mpa_ratings as m ON f.mpa_id = m.rating_id " +
                "WHERE film_id = ?";

        Film film = jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
        film.setGenres(getGenresOfFilm(id));
        film.setLikes(likesDao.getFilmLikes(id));
        return film;
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, " +
                "m.rating as mpa_name, " +
                "m.rating_id as mpa_id, " +
                "m.description as mpa_description, " +
                "FROM films as f " +
                "JOIN mpa_ratings as m ON f.mpa_id = m.rating_id " +
                "LEFT JOIN films_likes as l ON l.film_id = f.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, count);
        getFilmGenres(films);
        getFilmLikes(films);
        return films;
    }

    public void deleteGenresFromFilm(Film film) {
        String sqlQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public void addGenresToFilm(Film film) {
        for (Genre genre : film.getGenres()) {
            String setNewGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(setNewGenres, film.getId(), genre.getId());
        }
    }

    public void updateGenresOfFilm(Film film) {
        String sqlQuery = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        addGenresToFilm(film);
    }

    public Set<Genre> getGenresOfFilm(int filmId) {
        String sqlQuery = "SELECT * FROM genres " +
                "INNER JOIN films_genres AS fg ON genres.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::makeGenre, filmId));
    }

    private Film makeFilm(ResultSet resultSet, int i) throws SQLException {
        return new Film(
                resultSet.getInt("film_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                new HashSet<Integer>(),
                new HashSet<Genre>(),
                new Mpa(resultSet.getInt("mpa_id"),
                        resultSet.getString("mpa_name"),
                        resultSet.getString("mpa_description"))
        );

    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("genre_name"));
    }

    private boolean checkFilmId(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM films WHERE film_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
