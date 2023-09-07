package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendListDao {
    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<User> getAll(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);

}