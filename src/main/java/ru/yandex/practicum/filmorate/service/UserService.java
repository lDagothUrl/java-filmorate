package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendListDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendListDao friendListDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendListDao friendListDao) {
        this.userStorage = userStorage;
        this.friendListDao = friendListDao;
    }

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
        userStorage.createUser(user);
    }

    public void addFriend(int userId, int friendId) {
        friendListDao.addFriend(userId, friendId);
    }

    public void updateUser(User userUpdate) {
        userStorage.updateUser(userUpdate);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void delFriend(int userId, int friendId) {
        friendListDao.deleteFriend(userId, friendId);
    }
}