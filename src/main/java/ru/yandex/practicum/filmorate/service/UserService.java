package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendListDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendListDao friendListDao;
    private final JdbcTemplate jdbcTemplate;

    public List<User> getCommonFriends(int userId, int friendId) {
        return friendListDao.getCommonFriends(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        return friendListDao.getAll(userId);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findUser(int id) {
        return userStorage.findUser(id);
    }

    public void createUser(User user) {
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
        userStorage.createUser(user);
    }

    public void addFriend(int userId, int friendId) {
        if (Objects.equals(userId, friendId)) {
            throw new ValidationException("Id пользователей не должны совпадать!");
        } else if (!checkUserId(userId) || !checkUserId(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        friendListDao.addFriend(userId, friendId);
    }

    public void updateUser(User user) {
        checkUserName(user);
        userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void delFriend(int userId, int friendId) {
        if (!checkUserId(userId) || !checkUserId(friendId)) {
            throw new ValidationException("Введен некорректный id");
        }
        friendListDao.deleteFriend(userId, friendId);
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private boolean isEmailUnique(String email) {
        return jdbcTemplate.queryForObject("SELECT EXISTS (SELECT * FROM users WHERE user_email = ?);", Integer.class, email) == 0;
    }

    private boolean isLoginUnique(String login) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_login = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, login);
        return count == 0;
    }

    private boolean checkUserId(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}