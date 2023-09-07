package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::rowMapper);
    }

    @Override
    public void deleteUser(int id) {
        String deleteQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(deleteQuery, id);
    }


    @Override
    public User createUser(User user) {
        if (!isEmailUnique(user.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        if (!isLoginUnique(user.getLogin())) {
            throw new ValidationException("Пользователь с таким login уже существует");
        }
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday().isAfter(currentDate)) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым и не должен содержать пробелы");
        }
        checkUserName(user);
        String sqlQuery = "INSERT INTO users (user_name, user_email, user_login, user_birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET user_name = ?, user_email = ?, user_login = ?, user_birthday = ?" + "WHERE user_id = ?";
        checkUserName(user);
        int updatedUsers = jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        if (updatedUsers == 0) {
            throw new NotFoundException("Пользователь с идентификатором " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public User findUser(int userId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::rowMapper, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
    }

    private User rowMapper(ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getString("user_email"), resultSet.getString("user_login"), resultSet.getString("user_name"), resultSet.getDate("user_birthday").toLocalDate(), resultSet.getInt("user_id"));
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private boolean isEmailUnique(String email) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_email = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, email);
        return count == 0;
    }

    private boolean isLoginUnique(String login) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_login = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, login);
        return count == 0;
    }

}