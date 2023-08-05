package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable @NotNull Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getFriendsUser(@PathVariable @NotNull Long userId) {
        return userService.getFriendsUser(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{friendId}")
    public List<User> mutualFriends(@PathVariable @NotNull Long userId,
                                    @PathVariable @NotNull Long friendId) {
        return userService.mutualFriends(userId, friendId);
    }

    @PostMapping("/users")
    public User postUsers(@RequestBody @Valid User user) {
        return userService.postUsers(user);
    }

    @PutMapping("/users")
    public User putUsers(@RequestBody @Valid User user) {
        return userService.putUsers(user);
    }

    @DeleteMapping("/users")
    public void delUsers() {
        userService.delUsers();
    }

    @DeleteMapping("/users/{id}")
    public User delUser(@PathVariable @NotNull Long id) {
        return userService.delUser(id);
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @PutMapping("/users/{userId}/blocked/{friendId}")
    public void blockedUser(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long friendId) {
        userService.blockedUser(userId, friendId);
    }

}
