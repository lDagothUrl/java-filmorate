package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BaseUserService implements UserService{

    private final UserStorage userStorage;

    @Override
    public List<User> getUsers() {
        log.info("getUsers");
        return userStorage.getUsers();
    }

    @Override
    public User getUser(Long id) {
        log.info("getUser-start ID: {}", id);
        User user = userStorage.getUser(id);
        if (user == null){
            throw new NotFoundException("userId: " + id);
        }
        log.info("getUser-end User: {}", user);
        return user;
    }

    @Override
    public List<User> getFriendsUser(Long userId) {
        log.info("getFriendsUser userId: {}", userId);
        return userStorage.getFriendsUser(userId);
    }

    @Override
    public List<User> mutualFriends(Long userId, Long friendId) {
        log.info("mutualFriends userId {}, friendId {}", userId, friendId);
        return userStorage.mutualFriends(userId, friendId);
    }

    @Override
    public User postUsers(User user) {
        log.info("postUser-start: {}", user);
        if (user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        userStorage.postUsers(user);
        log.info("postUser-end: {}", user);
        return user;
    }

    @Override
    public User putUsers(User user) {
        log.info("putUser-start: {}", user);
        userStorage.putUsers(user);
        log.info("putUser-end: {}", user);
        return user;
    }

    @Override
    public void delUsers() {
        log.info("delUsers");
        userStorage.delUsers();
    }

    @Override
    public User delUser(Long id) {
        log.info("delUser-start ID: {}", id);
        User user = userStorage.delUser(id);
        log.info("delUser-end User: {}", user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("deleteFriend-start UserId {}, FriendId {}", userId, friendId);
        userStorage.addFriend(userId, friendId);
        log.info("deleteFriend-end");
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        log.info("deleteFriend-start UserId {}, FriendId {}", userId, friendId);
        userStorage.deleteFriend(userId, friendId);
        log.info("deleteFriend-end");
    }
}
