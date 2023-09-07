package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
public class FriendListDaoImpl implements FriendListDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendListDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (Objects.equals(userId, friendId)) {
            throw new ValidationException("Id пользователей не должны совпадать!");
        } else if (!checkUserId(userId) || !checkUserId(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        String sqlQuery = "INSERT INTO friend_list(user_id, friend_id, confirmed)" +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (!checkUserId(userId) || !checkUserId(friendId)) {
            throw new ValidationException("Введен некорректный id");
        }
        String sqlQuery = "DELETE FROM friend_list WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getAll(Integer id) {
        String sql = "SELECT us.user_id AS id,us.user_login,us.user_name,us.user_email,us.user_birthday " +
                "FROM friend_list AS fr " +
                "LEFT JOIN users AS us ON us.user_id = fr.friend_id " +
                "WHERE fr.user_id = ? AND fr.confirmed = TRUE";

        return jdbcTemplate.query(sql, this::rowMapper, id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {

        String sql = "SELECT  us.* " +
                "FROM friend_list AS fl " +
                "JOIN users AS us ON fl.friend_id = us.user_id " +
                "WHERE fl.user_id = ? AND fl.friend_id IN (" +
                "SELECT friend_id FROM friend_list WHERE user_id = ?)";
        return jdbcTemplate.query(sql, this::rowMapper, userId, otherId);

    }

    private User rowMapper(ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getString("user_email"),
                resultSet.getString("user_login"),
                resultSet.getString("user_name"),
                resultSet.getDate("user_birthday").toLocalDate(),
                resultSet.getInt("user_id")
        );
    }

    private boolean checkUserId(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}