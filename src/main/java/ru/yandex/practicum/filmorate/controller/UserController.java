package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("userId {} запрашивает общих друзей у otherId {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        return userService.findUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriend(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User userToUpdate) {
        userService.updateUser(userToUpdate);
        return userToUpdate;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
        log.info("userId {} добавил в друзья friendId {}", userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.delFriend(userId, friendId);
        log.info("userId {} удалил из друзей friendId {}", userId, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        log.info("userId {} удален", id);
    }
}
