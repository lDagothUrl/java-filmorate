package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUser(Long id);

    List<User> getFriendsUser(Long userId);

    List<User> mutualFriends(Long userId, Long friendId);

    User postUsers(User user);

    User putUsers(User user);

    void delUsers();

    User delUser(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    void blockedUser(Long userId, Long friendId);
}
