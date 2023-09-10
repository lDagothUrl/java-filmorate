package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendListDaoImpl implements FriendListDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO friend_list(user_id, friend_id, confirmed)" +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
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
}